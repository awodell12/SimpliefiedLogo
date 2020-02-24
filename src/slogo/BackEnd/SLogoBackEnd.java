package slogo.BackEnd;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.regex.Pattern;
import slogo.BackEnd.commands.UserCommand;
import slogo.CommandResult;

/**
 *
 */
public class SLogoBackEnd implements BackEndExternal, BackEndInternal {


  private static final String RESOURCES_PACKAGE = "resources/languages/";
  public static final String COMMENT_LINE = "(^#(?s).*|\\s+)";
  public static final String NEWLINE = "\\n+";
  private List<Entry<String, Pattern>> mySymbols;
  private Map<String, Double> myVariables;
  private Map<String, AltCommand> myUserCommands;
  public static final String WHITESPACE = "\\s+";

  public SLogoBackEnd() {
    mySymbols = new ArrayList<>();
    myVariables = new HashMap<>();
    myUserCommands = new HashMap<>();
  }

  /**
   * Adds the given resource file to this language's recognized types
   * Stolen from spike_parser
   */
  public void addPatterns (String syntax) {
    ResourceBundle resources = ResourceBundle.getBundle(RESOURCES_PACKAGE + syntax);
    for (String key : Collections.list(resources.getKeys())) {
      String regex = resources.getString(key);
      mySymbols.add(new SimpleEntry<>(key,

      Pattern.compile(regex, Pattern.CASE_INSENSITIVE)));
    }
  }

  /**
   * Returns language's type associated with the given text if one exists
   */
  public String getSymbol (String text) {
    final String ERROR = "NO MATCH";
    for (Entry<String, Pattern> e : mySymbols) {
      if (match(text, e.getValue())) {
        return e.getKey();
      }
    }
    // FIXME: perhaps throw an exception instead
    return ERROR;
  }


  // Returns true if the given text matches the given regular expression pattern
  private boolean match (String text, Pattern regex) {
    // THIS IS THE IMPORTANT LINE
    return regex.matcher(text).matches();
  }

  @Override
  public List<CommandResult> parseScript(String script) {
    String[] scriptTokens = getTokenList(script).toArray(new String[0]);
    parseCommandsList(scriptTokens);
    return null;
  }

  private List<String> getTokenList(String script) {
    String[] scriptLines = script.split(NEWLINE);
    List<String> scriptTokenList = new ArrayList<>();
    for (String line : scriptLines) {
      System.out.println(line);
      if (!line.matches(COMMENT_LINE)) {
        scriptTokenList.addAll(Arrays.asList(line.strip().split(WHITESPACE)));
      }
    }
    return scriptTokenList;
  }

  public CommandResult parseCommandsList(String[] tokenList) {
    int numTokens = tokenList.length;
    int programCounter = 0;
    CommandResult result = null;
    while (programCounter < tokenList.length) {
//      System.out.println("continuing parse command list on the outermost side at PC = " + programCounter);
//      printRemainingTokens(tokenList,programCounter);
      try {
        AltCommand command = CommandFactory.makeCommand(getSymbol(tokenList[programCounter]));
        result = parseCommand(command,Arrays.copyOfRange(tokenList,programCounter+1,numTokens));
        programCounter += result.getTokensParsed() + 1;
      } catch (ParseException e) {
        if (myUserCommands.containsKey(tokenList[programCounter])) {
          AltCommand command = myUserCommands.get(tokenList[programCounter]);
          result = parseCommand(command,Arrays.copyOfRange(tokenList,programCounter+1,numTokens));
          programCounter += result.getTokensParsed() + 1;
        }
        else {
          System.out.println("Globally read a non-command (" + tokenList[programCounter] + ") when expecting a command at PC = " + programCounter);
          break;
        }
      }

    }
    return new CommandResult(result.getReturnVal(),programCounter);
  }

  private CommandResult parseCommand(AltCommand command, String[] tokenList) {
//    System.out.println("Beginning parse of " + command);
//    printRemainingTokens(tokenList,0);
    //'fd 50' expects to start at PC = 1, where '50' is.
    Stack<Double> commandValues = new Stack<>();
    List<String> variableNames = new ArrayList<>();
    int startPos = 0;
    if (command.getNumVars() > 0) {
      variableNames.addAll(command.findVars(tokenList));
      startPos = variableNames.size();
    }
    for (int programCounter = startPos; programCounter <= tokenList.length; programCounter ++) {
      //Check this first because a command can have 0 arguments
      if (commandValues.size() >= command.getNumArgs()) {
        List<Double> argList = getArgsFromStack(commandValues,command.getNumArgs());
        CommandResult result = command.execute(argList,variableNames,
            Arrays.copyOfRange(tokenList,programCounter,tokenList.length),
            this);
        return new CommandResult(result.getReturnVal(),result.getTokensParsed()+programCounter);
      }

      String currentTokenRaw = tokenList[programCounter];
      String currentTokenType = getSymbol(tokenList[programCounter]);

      if (isValue(currentTokenType)) {
        commandValues.add(parseValue(currentTokenType,currentTokenRaw));
      }
      //if IS_COMMAND @ PC
      else {
        if (currentTokenType.equals("MakeUserInstruction")) {
          try {
            programCounter += handleUserCommandCreation(Arrays.copyOfRange(tokenList,programCounter+1,tokenList.length));
            commandValues.add(1.0);
          } catch (ParseException e) {
            System.out.printf("Can't redefine primitive %s.", currentTokenRaw);
            commandValues.add(0.0);
          }
        }
        else {
          try {
            AltCommand insideCommand = CommandFactory.makeCommand(currentTokenType);
            CommandResult insideResult = parseCommand(insideCommand,Arrays.copyOfRange(tokenList,programCounter+1,tokenList.length));
            commandValues.add(insideResult.getReturnVal());
            programCounter += insideResult.getTokensParsed();
          } catch (ParseException e) {
            if (myUserCommands.containsKey(currentTokenRaw)) {
              AltCommand insideCommand = myUserCommands.get(currentTokenRaw);
              CommandResult insideResult = parseCommand(insideCommand,Arrays.copyOfRange(tokenList,programCounter+1,tokenList.length));
              commandValues.add(insideResult.getReturnVal());
              programCounter += insideResult.getTokensParsed();
            }
            else {
              //TODO: Realize that this is duplicated code.
              System.out.println("Read a non-command (" + currentTokenRaw + ") when expecting a command at PC = " + programCounter);
            }
          }
        }
      }
    }
    System.out.println("Unexpected end of instructions");
    return null;
  }

  private List<Double> getArgsFromStack(Stack<Double> values, int numArgs) {
    List<Double> argList = new ArrayList<>();
    for (int arg = 0; arg < numArgs; arg ++) {
      argList.add(values.pop());
    }
    Collections.reverse(argList);
    return argList;
  }

  private double parseValue(String type, String token) {
    double value;
    if (isVariable(type)) {
      try {
        value = getVariable(token.substring(1));
      }
      catch (ParseException e) {
        System.out.printf("Don't know about variable %s\n", token);
        value = 0.0;
      }
    }
    else {
      value = Double.parseDouble(token);
    }
    return value;
  }

  public int handleUserCommandCreation(String[] tokenList) throws ParseException{
    int programCounter = 0;
    String cmdName = tokenList[programCounter];
    int numVars = distanceToEndBracket(Arrays.copyOfRange(tokenList,programCounter+2,tokenList.length)) - 1;
    List<String> toVars = new ArrayList<>();
    for (programCounter = 2; programCounter < 2 + numVars; programCounter ++) {
      toVars.add(tokenList[programCounter].substring(1));
    }
    programCounter += 2;
    int numCommands = distanceToEndBracket(Arrays.copyOfRange(tokenList,programCounter,tokenList.length)) - 1;
    System.out.println();
    try {
      setUserCommand(cmdName,toVars,Arrays.copyOfRange(tokenList,programCounter,programCounter + numCommands));
    } catch (ParseException e) {
      throw new ParseException();
    }
//    System.out.println("Advanced PC by " + (programCounter + numCommands));
    return programCounter + numCommands + 1;
  }

  private void printRemainingTokens(String[] scriptTokens, int i) {
    String[] remaining = Arrays.copyOfRange(scriptTokens,i,scriptTokens.length);
    for (String string : remaining) {
      System.out.printf("(%s) ", string);
    }
    System.out.println();
  }

  private boolean isCommand(String identity) {
    return (true);
  }

  private boolean isValue(String identity) {
    return identity.equals("Constant") || identity.equals("Variable");
  }

  private boolean isVariable(String identity) {
    return identity.equals("Variable");
  }

  private boolean isOpenBracket(String identity) {
    return identity.equals("ListStart");
  }

  private boolean isClosedBracket(String identity) {
    return identity.equals("ListEnd");
  }

  private boolean isControl(String identity) {
    return (identity.equals("Repeat") || identity.equals("For") || identity.equals("MakeVariable")
            || identity.equals("If") || identity.equals("IfElse"));
  }

  private boolean isForLoop(String identity) {
    return (identity.equals("For"));
  }

  public static int distanceToEndBracketStatic(String[] tokenList) {
    return new SLogoBackEnd().distanceToEndBracket(tokenList);
  }

  public int distanceToEndBracket(String[] tokenList) {
    Stack<String> openBrackets = new Stack<>();
    for (int i = 0; i < tokenList.length; i ++) {
      String tokenSymbol = getSymbol(tokenList[i]);
      if (isOpenBracket(tokenSymbol)) {
        openBrackets.add(tokenList[i]); //THERE IS NOT A POINT TO DOING THIS WITH A STACK.
      }
      else if (isClosedBracket(tokenSymbol)) {
        if (openBrackets.isEmpty()) {
          return i + 1;
        }
        else {
          openBrackets.pop();
        }
      }
    }
    return tokenList.length;
  }

  @Override
  public void setVariable(String name, double value) {
    myVariables.put(name, value);
  }

  @Override
  public double getVariable(String name) throws ParseException {
    if (myVariables.containsKey(name)) {
       return myVariables.get(name);
    }
    throw new ParseException();
  }

  @Override
  public void clearVariables() {
    myVariables.clear();
  }

  @Override
  public void setUserCommand(String name, List<String> parameters, String[] commands)
      throws ParseException {
    if (CommandFactory.hasCommand(getSymbol(name))) {
      throw new ParseException();
    }
    myUserCommands.put(name, new UserCommand(parameters,Arrays.asList(commands)));
  }
  @Override
  public Collection<String> getUserCommandArgs(String name) {
    return null;
  }

  @Override
  //TODO: Figure out a way to report the contents of user-generated commands.
  public Collection<String> getUserCommandScript(String name) {
    return null;
  }
}