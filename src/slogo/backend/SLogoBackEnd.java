package slogo.backend;

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
import slogo.backend.commands.controlandvariables.UserCommand;
import slogo.CommandResult;

/**
 *
 */
public class SLogoBackEnd implements BackEndExternal, BackEndInternal {

  private static final String RESOURCES_PACKAGE = "resources/languages/";
  public static final String COMMENT_LINE = "(^#(?s).*|\\s+)";
  public static final String NEWLINE = "\\n+";
  private List<Entry<String, Pattern>> myLanguage;
  private List<Entry<String, Pattern>> mySyntax;
  private Map<String, Double> myVariables;
  // TODO: make VariableMap
  private Map<String, Command> myUserCommands;
  // TODO: make UserCommand Map
  public static final String WHITESPACE = "\\s+";
  private List<Turtle> myTurtles;
  private List<Turtle> myActiveTurtles;
  private Map<Integer, List<Integer>> myPalette;

  public SLogoBackEnd() {
    myLanguage = new ArrayList<>();
    myVariables = new HashMap<>();
    myUserCommands = new HashMap<>();
    myTurtles = new ArrayList<>();
    myTurtles.add(new SLogoTurtle());
    myLanguage = interpretPatterns("English");
    mySyntax = interpretPatterns("Syntax");
  }

  public List<Entry<String, Pattern>> interpretPatterns(String syntax) {
    List<Entry<String, Pattern>> patterns = new ArrayList<>();
    ResourceBundle resources = ResourceBundle.getBundle(RESOURCES_PACKAGE + syntax);
    for (String key : Collections.list(resources.getKeys())) {
      String regex = resources.getString(key);
      patterns.add(new SimpleEntry<>(key,

          Pattern.compile(regex, Pattern.CASE_INSENSITIVE)));
    }
    return patterns;
  }

  /**
   * Returns language's type associated with the given text if one exists
   */
  public String getSymbol(String text) {
    for (Entry<String, Pattern> e : myLanguage) {
      if (match(text, e.getValue())) {
        return e.getKey();
      }
    }
    for (Entry<String, Pattern> e : mySyntax) {
      if (match(text, e.getValue())) {
        return e.getKey();
      }
    }
    // FIXME: perhaps throw an exception instead
    return "NO MATCH";
  }

  // Returns true if the given text matches the given regular expression pattern
  private boolean match(String text, Pattern regex) {
    // THIS IS THE IMPORTANT LINE
    return regex.matcher(text).matches();
  }

  @Override
  public List<CommandResult> parseScript(String script) {
    String[] scriptTokens = getTokenList(script).toArray(new String[0]);
    return parseCommandsList(scriptTokens);
  }

  @Override
  public void applyChanger(Changer changer) {
    changer.doChanges(this);
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
    int programCounter = 0;
    while (programCounter < tokenList.length) {
      try {
        Command command = identifyCommand(tokenList[programCounter]);
        List<CommandResult> listResult = parseCommand(command,
            Arrays.copyOfRange(tokenList, programCounter + 1, tokenList.length));
        results.addAll(listResult);
        programCounter += results.get(results.size() - 1).getTokensParsed() + 1;
      } catch (ParseException e) {
        results.add(makeErrorCommandResult(0.0,0,e.getMessage()));
        return results;
      }
    }
    results.add(makeCommandResult(findRetVal(results), programCounter));
    return results;
  }

  private double findRetVal(List<CommandResult> results) {
    double retVal = 0;
    if (!results.isEmpty()) {
      retVal = results.get(results.size() - 1).getReturnVal();
    }
    return retVal;
  }

  private Command identifyCommand(String rawToken) throws ParseException {
    if (isValue(getSymbol(rawToken))) {
      throw new ParseException("Don't know what to do with " + rawToken);
    }
    try {
      return CommandFactory.makeCommand(getSymbol(rawToken));
    } catch (ParseException e) {
      if (myUserCommands.containsKey(rawToken)) {
        return myUserCommands.get(rawToken);
      }
      throw new ParseException("Don't know how to " + rawToken.toUpperCase());
    }
  }

  private List<CommandResult> parseCommand(Command command, String[] tokenList)
      throws ParseException {
    //'fd 50' expects to start at PC = 1, where '50' is.
    Stack<Double> commandValues = new Stack<>();
    List<String> variableNames = getCommandVars(command, tokenList);
    int programCounter;
    for (programCounter = variableNames.size(); programCounter <= tokenList.length;
        programCounter++) {
      if (commandValues.size() >= command.getNumArgs()) {
        return executeCurrentCommand(command, tokenList, commandValues, variableNames,
            programCounter);
      }
      programCounter += parseNextToken(tokenList, programCounter, commandValues);
    }
    throw new ParseException("Unexpected end of instructions.");
  }

  private List<CommandResult> executeCurrentCommand(Command command, String[] tokenList,
                                                    Stack<Double> commandValues, List<String> variableNames, int programCounter)
      throws ParseException {
    List<Double> argList = getArgsFromStack(commandValues, command.getNumArgs());
    List<CommandResult> results = null;
    results = new ArrayList<>(command.execute(argList, variableNames,
        Arrays.copyOfRange(tokenList, programCounter, tokenList.length),
        this));
    CommandResult lastResult = results.get(results.size() - 1);
    results.add(makeCommandResult(lastResult.getReturnVal(),
        lastResult.getTokensParsed() + programCounter));
    return results;
  }

  //TODO: This method might be better replaced by changing the default behavior
  //TODO: of Command.findVars().
  private List<String> getCommandVars(Command command, String[] tokenList) {
    if (command.getNumVars() > 0) {
      return (command.findVars(tokenList));
    }
    return new ArrayList<>();
  }

  private int parseNextToken(String[] tokenList, int programCounter, Stack<Double> commandValues)
      throws ParseException {
    if (programCounter >= tokenList.length) {
      throw new ParseException("Unexpected end of instructions.");
    }
    String currentTokenRaw = tokenList[programCounter];
    String currentTokenType = getSymbol(tokenList[programCounter]);
    if (isValue(currentTokenType)) {
      commandValues.add(parseValue(currentTokenType, currentTokenRaw));
    } else { //It's a command. TODO: actually check if it's a command or if it's gibberish.
      List<CommandResult> insideResult = parseCommand(identifyCommand(currentTokenRaw),
          Arrays.copyOfRange(tokenList, programCounter + 1, tokenList.length));
      commandValues.add(insideResult.get(insideResult.size() - 1).getReturnVal());
      return insideResult.get(insideResult.size() - 1).getTokensParsed();
    }
    return 0;
  }

  private List<Double> getArgsFromStack(Stack<Double> values, int numArgs) {
    List<Double> argList = new ArrayList<>();
    for (int arg = 0; arg < numArgs; arg++) {
      argList.add(values.pop());
    }
    Collections.reverse(argList);
    return argList;
  }

  private double parseValue(String type, String token) throws ParseException {
    if (isVariable(type)) {
      return getVariable(token.substring(1));
    } else {
      return Double.parseDouble(token);
    }
  }

  private void printRemainingTokens(String[] scriptTokens, int i) {
    String[] remaining = Arrays.copyOfRange(scriptTokens, i, scriptTokens.length);
    for (String string : remaining) {
      System.out.printf("(%s) ", string);
    }
    System.out.println();
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

  public static int distanceToEndBracketStatic(String[] tokenList) {
    return new SLogoBackEnd().distanceToEndBracket(tokenList);
  }

  public int distanceToEndBracket(String[] tokenList) {
    int extraBrackets = 0;
    for (int i = 0; i < tokenList.length; i++) {
      String tokenSymbol = getSymbol(tokenList[i]);
      if (isOpenBracket(tokenSymbol)) {
        extraBrackets++;
      } else if (isClosedBracket(tokenSymbol)) {
        if (extraBrackets == 0) {
          return i + 1;
        } else {
          extraBrackets--;
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
    myUserCommands.put(name, new UserCommand(parameters, Arrays.asList(commands)));
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

  @Override
  public List<Turtle> getTurtles() {
    return new ArrayList<>(myTurtles);
  }

  @Override
  public void setTurtles(List<Turtle> t) {
    myTurtles = new ArrayList<>(t);
  }

  @Override
  public void clearTurtles() {
    myTurtles.clear();
  }

  public void setLanguage(String language) {
    ResourceBundle resources = ResourceBundle.getBundle(RESOURCES_PACKAGE + language);
    myLanguage = interpretPatterns(language);
  }

  public CommandResult makeCommandResult(double retVal, int tokensParsed, List<Double> pathStart, int pathColor) {
    return new CommandResult(
        retVal,
        tokensParsed,
        0,
        myTurtles.get(0).getHeading(),
        List.of(myTurtles.get(0).getX(),
            myTurtles.get(0).getY()),
        pathStart,
        pathColor,
        null,
        0,
        null,
        null,
        false,
        myTurtles.get(0).getPenUp(),
        myTurtles.get(0).getVisible(),
        false,
        0,
        List.of(0,0,0),
        0.0,
        List.of(0),
        0,
        0, //TODO: change these to be not zeros so stuff doesn't get reset
        ""
    );
  }

  public CommandResult makeCommandResult(double retVal, int tokensParsed, String varName,
      double varValue) {
    CommandResultBuilder builder = new CommandResultBuilder(myTurtles.get(0).getHeading(),myTurtles.get(0).getPosition());
    builder.retVal(retVal);
    builder.tokensParsed(tokensParsed);
    builder.variableName(varName);
    builder.varValue(varValue);
    return builder.buildCommandResult();
  }

  public CommandResult makeCommandResult(double retVal, int tokensParsed, String udcName, String udcScript) {
    CommandResultBuilder builder = new CommandResultBuilder(myTurtles.get(0).getHeading(),myTurtles.get(0).getPosition());
    builder.retVal(retVal);
    builder.tokensParsed(tokensParsed);
    builder.userDefinedCommandName(udcName);
    builder.userDefinedCommandScript(udcScript);
    return builder.buildCommandResult();
  }
  public CommandResult makeErrorCommandResult(double retVal, int tokensParsed, String errorMessage) {
    CommandResultBuilder builder = new CommandResultBuilder(myTurtles.get(0).getHeading(),myTurtles.get(0).getPosition());
    builder.retVal(retVal);
    builder.tokensParsed(tokensParsed);
    builder.setErrorMessage(errorMessage);
    return builder.buildCommandResult();
  }
  public CommandResult makeCommandResult(double retVal, int tokensParsed) {
    CommandResultBuilder builder = new CommandResultBuilder(myTurtles.get(0).getHeading(),myTurtles.get(0).getPosition());
    builder.retVal(retVal);
    builder.tokensParsed(tokensParsed);
    return builder.buildCommandResult();
  }

  @Override
  public void setActiveTurtles(List<Integer> turtleIDs) {
    ArrayList<Turtle> active = new ArrayList<>();
    for (Integer num : turtleIDs) {
      for (Turtle turtle : myTurtles) {
        if (turtle.getId() == num) {
          active.add(turtle);
        }
      }
    }
    myActiveTurtles = active;
    System.out.println("Active turtles: " + active.toString());
  }

  @Override
  public void addPaletteColor(int index, List<Integer> rgbColor) {

  }

  @Override
  public List<Turtle> getTurtles(List<Integer> ids) {
    return null;
  }

  @Override
  public List<Turtle> getActiveTurtles() {
    return new ArrayList<>(myActiveTurtles);
  }
}