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
    List<CommandResult> results = parseCommandsList(scriptTokens);
    CommandResult result = results.get(results.size()-1);
    if (!result.getErrorMessage().equals("")) {
      System.out.println(result.getErrorMessage());
    }
    System.out.println("--------------------------------");
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

  public List<CommandResult> parseCommandsList(String[] tokenList) {
    List<CommandResult> results = new ArrayList<>();
    int numTokens = tokenList.length;
    int programCounter = 0;
    CommandResult result = null;
    while (programCounter < tokenList.length) {
      try {
        AltCommand command = identifyCommand(tokenList[programCounter]);
        result = parseCommand(command,Arrays.copyOfRange(tokenList,programCounter+1,numTokens));
        results.add(result);
        programCounter += result.getTokensParsed() + 1;
      } catch (ParseException e) {
        result = new CommandResult(0.0,0);
        result.setErrorMessage(e.getMessage());
        results.add(result);
        return results;
      }
    }
    System.out.println("results.size() = " + results.size());
//    return new CommandResult(result.getReturnVal(),programCounter);
    results.remove(result);
    results.add(new CommandResult(result.getReturnVal(),programCounter));
    return results;
  }

  private AltCommand identifyCommand(String rawToken) throws ParseException {
    if (isValue(getSymbol(rawToken))) {
      throw new ParseException("Don't know what to do with " + rawToken);
    }
    try {
      return CommandFactory.makeCommand(getSymbol(rawToken));
    } catch (ParseException e) {
      if (myUserCommands.containsKey(rawToken)) {
        return myUserCommands.get(rawToken);
      }
      throw e;
    }
  }

  private CommandResult parseCommand(AltCommand command, String[] tokenList) throws ParseException {
    //'fd 50' expects to start at PC = 1, where '50' is.
    Stack<Double> commandValues = new Stack<>();
    List<String> variableNames = getCommandVars(command,tokenList);
    int programCounter;
    for (programCounter = variableNames.size(); programCounter <= tokenList.length; programCounter ++) {
      if (commandValues.size() >= command.getNumArgs()) {
        return executeCurrentCommand(command, tokenList, commandValues, variableNames,
            programCounter);
      }
      programCounter += parseNextToken(tokenList,programCounter,commandValues);
    }
    throw new ParseException("Unexpected end of instructions.");
  }

  private CommandResult executeCurrentCommand(AltCommand command, String[] tokenList,
      Stack<Double> commandValues, List<String> variableNames, int programCounter) {
    List<Double> argList = getArgsFromStack(commandValues,command.getNumArgs());
    CommandResult result = command.execute(argList,variableNames,
        Arrays.copyOfRange(tokenList,programCounter,tokenList.length),
        this);
    return new CommandResult(result.getReturnVal(),result.getTokensParsed()+programCounter);
  }

  //TODO: This method might be better replaced by changing the default behavior
  //TODO: of AltCommand.findVars().
  private List<String> getCommandVars(AltCommand command, String[] tokenList) {
    if (command.getNumVars() > 0) {
      return (command.findVars(tokenList));
    }
    return new ArrayList<>();
  }
  private int parseNextToken(String[] tokenList, int programCounter, Stack<Double> commandValues) throws ParseException{
    if (tokenList.length == 0) {
      throw new ParseException("Unexpected end of instructions.");
    }
    String currentTokenRaw = tokenList[programCounter];
    String currentTokenType = getSymbol(tokenList[programCounter]);
    if (isValue(currentTokenType)) {
      commandValues.add(parseValue(currentTokenType,currentTokenRaw));
    }
    else { //It's a command. TODO: actually check if it's a command or if it's gibberish.
        CommandResult insideResult = parseCommand(identifyCommand(currentTokenRaw),Arrays.copyOfRange(tokenList,programCounter+1,tokenList.length));
        commandValues.add(insideResult.getReturnVal());
        return insideResult.getTokensParsed();
    }
    return 0;
  }

  private List<Double> getArgsFromStack(Stack<Double> values, int numArgs) {
    List<Double> argList = new ArrayList<>();
    for (int arg = 0; arg < numArgs; arg ++) {
      argList.add(values.pop());
    }
    Collections.reverse(argList);
    return argList;
  }

  private double parseValue(String type, String token) throws ParseException {
    if (isVariable(type)) {
        return getVariable(token.substring(1));
    }
    else {
      return Double.parseDouble(token);
    }
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
//    try {
      setUserCommand(cmdName,toVars,Arrays.copyOfRange(tokenList,programCounter,programCounter + numCommands));
//    } catch (ParseException e) {
//      throw e;
//    }
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
    int extraBrackets = 0;
    for (int i = 0; i < tokenList.length; i ++) {
      String tokenSymbol = getSymbol(tokenList[i]);
      if (isOpenBracket(tokenSymbol)) {
        extraBrackets ++;
      }
      else if (isClosedBracket(tokenSymbol)) {
        if (extraBrackets == 0) {
          return i + 1;
        }
        else {
          extraBrackets --;
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
    throw new ParseException("Don't know about variable " + name);
  }

  @Override
  public void clearVariables() {
    myVariables.clear();
  }

  @Override
  public void setUserCommand(String name, List<String> parameters, String[] commands)
      throws ParseException {
    if (CommandFactory.hasCommand(getSymbol(name))) {
      throw new ParseException("Can't redefine primitive " + name);
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