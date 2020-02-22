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
import javafx.util.Pair;
import slogo.BackEnd.commands.UserCommand;
import slogo.CommandResult;

/**
 *
 */
public class SLogoBackEnd implements BackEndExternal, BackEndInternal {

  public static final String WHITESPACE = "\\s+";

  private static final String RESOURCES_PACKAGE = "resources/languages/";
  private List<Entry<String, Pattern>> mySymbols;
  private Map<String, Double> myVariables;
  private Map<String, Pair<Collection<String>,Collection<String>>> myUserInstructions;
  private Map<String, AltCommand> myUserCommands;
//  private Map<String, Collection<String>> myUserInstructions;
  private int myProgramCounter;

  public SLogoBackEnd() {
    mySymbols = new ArrayList<>();
    myVariables = new HashMap<>();
    myUserInstructions = new HashMap<>();
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
    String[] scriptLines = script.split("\\n+");
    List<String> scriptTokenList = new ArrayList<>();
    for (String line : scriptLines) {
      System.out.println(line);
      if (!line.matches("(^#(?s).*|\\s+)")) {
        scriptTokenList.addAll(Arrays.asList(line.strip().split(WHITESPACE)));
      }
    }
    String[] scriptTokens = scriptTokenList.toArray(new String[0]);
    parseTokens(scriptTokens);
    return null;
  }

  public CommandResult parseTokens(String[] scriptTokens) {
    Stack<AltCommand> commands = new Stack<>();
    Stack<Double> commandValues = new Stack<>();
    Stack<String> controlVars = new Stack<>();
    double lastCommandRet = 0;

    int programCounter;
    for (programCounter = 0; programCounter < scriptTokens.length; programCounter++) {
      String currentToken = getSymbol(scriptTokens[programCounter]);
      if (isClosedBracket(currentToken)) {
        return new CommandResult(lastCommandRet,programCounter+1); //end the current call if an end bracket happens.
      }
      else if (isOpenBracket(currentToken)) {
        programCounter += parseTokens(Arrays.copyOfRange(scriptTokens,programCounter+1,scriptTokens.length)).getTokensParsed();
      }
      else if (currentToken.equals("MakeUserInstruction")) {
        System.out.println("Making user defined command");
        printRemainingTokens(scriptTokens,programCounter);
        programCounter += 1;
        String cmdName = scriptTokens[programCounter];
        System.out.printf("Command has name %s\n", cmdName);
        programCounter += 2;
        System.out.println("Finding variables.");
        printRemainingTokens(scriptTokens,programCounter);
        int numVars = distanceToEndBracket(Arrays.copyOfRange(scriptTokens,programCounter,scriptTokens.length)) - 1;
        List<String> toVars = new ArrayList<>();
        for (int i = programCounter; i < programCounter + numVars; i ++) {
          toVars.add(scriptTokens[i].substring(1));
          System.out.printf("Added variable %s\n", scriptTokens[i].substring(1));
        }
        programCounter += numVars + 2;
        System.out.println("Finding instructions.");
        printRemainingTokens(scriptTokens,programCounter);
        int numCommands = distanceToEndBracket(Arrays.copyOfRange(scriptTokens,programCounter,scriptTokens.length)) - 1;
        System.out.printf("There are %d tokens and an end bracket.\n", numCommands);
        try {
          setUserCommand(cmdName,toVars,Arrays.copyOfRange(scriptTokens,programCounter,programCounter + numCommands));
        } catch (ParseException e) {
          System.out.printf("Couldn't make command %s", cmdName);
        }
        programCounter += numCommands;
        continue;
      }
      else {
        programCounter += recordToken(commands, commandValues, controlVars,
            scriptTokens, programCounter);
      }

      while (!commands.isEmpty() && commandValues.size() >= commands.peek().getNumArgs()) {
        List<Double> argList = new ArrayList<>();
        for (int arg = 0; arg < commands.peek().getNumArgs(); arg ++) {
          argList.add(commandValues.pop());
        }
        List<Double> argListReversed = new ArrayList<>(argList);
        for (int arg = 0; arg < argList.size(); arg++) {
          argListReversed.set(argList.size()-1-arg,argList.get(arg));
        }
        List<String> varList = new ArrayList<>();
        for (int arg = 0; arg < commands.peek().getNumVars(); arg ++) {
          varList.add(controlVars.pop());
        }
        //call it "run a command
        CommandResult result = commands.pop().execute( argListReversed, varList,
                                                  Arrays.copyOfRange(scriptTokens,programCounter,scriptTokens.length),
                                                this);
        lastCommandRet = result.getReturnVal();
        double retValue = result.getReturnVal();
        programCounter += result.getTokensParsed();
        //refactor into 'handle add value'
        if (!commands.isEmpty()) {
          commandValues.add(retValue);
        }
      }
    }
    if (!commands.isEmpty()) {
      System.out.println("Unexpected end of instructions");
    }
    return new CommandResult(lastCommandRet,programCounter);
  }

  private int recordToken(Stack<AltCommand> commands,
      Stack<Double> commandValues, Stack<String> vars,
      String[] tokenList, int programCounter) {
    String tokenType = getSymbol(tokenList[programCounter]);
    if (isValue(tokenType)) {
      Double value;
      if (isVariable(tokenType)) {
        try {
          value = getVariable(tokenList[programCounter].substring(1));
        }
        catch (ParseException e) {
          System.out.printf("Don't know about variable %s", tokenList[programCounter].substring(1));
          value = 0.0;
        }
      }
      else {
        value = Double.parseDouble(tokenList[programCounter]);
      }
      if (!commands.isEmpty()) {
        commandValues.add(value);
      }
      else {
        System.out.printf("Don't know what to do with %f",value);
      }
    }
    else { //if it's not a bracket or a value, it's a command.
      try {
        commands.add(CommandFactory.makeCommand(tokenType));
      } catch (ParseException e) {
        if (myUserCommands.containsKey(tokenList[programCounter])) {
          //TODO: Handle a situation where the user uses the return value of
          // TO as the argument for a previous version of the command they
          // are setting.
          // (ex.) TO foo [ :distance ] [ fd :distance ] foo TO foo [ ] [ rt 90 ]
          commands.add(myUserCommands.get(tokenList[programCounter]));
        }
        else {
          System.out.printf("Don't know how to %s", tokenList[programCounter]);
        }
      }
      //TODO: This is a fantastic spot to stop repeating myself.
      if (isForLoop(tokenType) || tokenType.equals("DoTimes")) {
        int offset = 2;
        vars.add(tokenList[programCounter+offset].substring(1));
        return offset;
      }
      else if (tokenType.equals("MakeVariable")) {
        int offset = 1;
        vars.add(tokenList[programCounter+offset].substring(1));
        return offset;
      }
    }
    return 0;
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
//    myUserInstructions.put(name,new Pair<>((parameters),Arrays.asList(commands)));
    myUserCommands.put(name, new UserCommand(parameters,Arrays.asList(commands)));
  }
  @Override
  public Collection<String> getUserCommandArgs(String name) {
    return null;
  }

  @Override
  public Collection<String> getUserCommandScript(String name) {
    if (myUserInstructions.containsKey(name)) {
      return myUserInstructions.get(name).getValue();
    }
    //TODO: Put an error here.
    return null;
  }

  private CommandResult runUserCommand(String name) throws ParseException {
    if (myUserInstructions.containsKey(name)) {
      return parseTokens(getUserCommandScript(name).toArray(new String[0]));
    }
    throw new ParseException();
  }
}