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
import slogo.CommandResult;

/**
 *
 */
public class SLogoBackEnd implements BackEndExternal, BackEndInternal {

  private static final String RESOURCES_PACKAGE = "resources/languages/";
  private List<Entry<String, Pattern>> myLanguage;
  private List<Entry<String, Pattern>> mySyntax;
  private Map<String, Double> myVariables;
  private UserCommandManager myUserCommandManager;
  private List<Turtle> myTurtles;
  private List<Turtle> myActiveTurtles;
  private Map<Integer, List<Integer>> myPalette;
  private int myPathColor = 0;
  private int myBackgroundColor = 0;
  private int myShapeIndex = 0;
  private double myPenSize = 1;
  private Integer myActiveTurtleID;
  SLogoCareTaker careTaker = new SLogoCareTaker();


  public SLogoBackEnd() {
    myLanguage = new ArrayList<>();
    myVariables = new HashMap<>();
    myUserCommandManager = new UserCommandManager();
    myTurtles = new ArrayList<>();
    myTurtles.add(new SLogoTurtle(0));
    myActiveTurtles = List.of(myTurtles.get(0));
    myLanguage = interpretPatterns("English");
    mySyntax = interpretPatterns("Syntax");
    myActiveTurtleID = null;
    myPalette = new HashMap<>();
  }

  public List<Entry<String, Pattern>> interpretPatterns(String syntax) {
    List<Entry<String, Pattern>> patterns = new ArrayList<>();
    ResourceBundle resources = ResourceBundle.getBundle(RESOURCES_PACKAGE + syntax);
    for (String key : Collections.list(resources.getKeys())) {
      String regex = resources.getString(key);
      patterns.add(new SimpleEntry<>(key, Pattern.compile(regex, Pattern.CASE_INSENSITIVE)));
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

  //from the parser spike on the cs308 repo
  private boolean match(String text, Pattern regex) {
    return regex.matcher(text).matches();
  }

  @Override
  public List<CommandResult> parseScript(String script) {
    String[] scriptTokens = BackEndUtil.getTokenList(script).toArray(new String[0]);
    return parseCommandsList(scriptTokens);
  }

  @Override
  public void applyChanger(Changer changer) {
    changer.doChanges(this);
  }

  public List<CommandResult> parseCommandsList(String[] tokenList) {
    List<CommandResult> results = new ArrayList<>();
    int programCounter = 0;
    while (programCounter < tokenList.length) {
      try {
        Command command = identifyCommand(tokenList[programCounter]);
        String[] tokensToParse = Arrays.copyOfRange(tokenList, programCounter + 1, tokenList.length);
        results.addAll(parseSingleCommand(command, tokensToParse));
        //in 'fd sum 30 40 bk 10', we advance by 4 then by 2.
        programCounter += results.get(results.size() - 1).getTokensParsed() + 1;
      } catch (ParseException e) {
        CommandResultBuilder builder = startCommandResult(0);
        builder.setErrorMessage(e.getMessage());
        results.add(builder.buildCommandResult());
        return results;
      }
    }
    CommandResultBuilder builder = startCommandResult(findRetVal(results));
    builder.setTokensParsed(programCounter);
    results.add(builder.buildCommandResult());
    return results;
  }

  private List<CommandResult> parseSingleCommand(Command command, String[] tokensToParse)
      throws ParseException {
    List<CommandResult> listResult;
    if (command.runsPerTurtle()) {
      listResult = parseCommandPerTurtle(command, tokensToParse);
    } else {
      listResult = parseCommand(command, tokensToParse);
    }
    return listResult;
  }

  private double findRetVal(List<CommandResult> results) {
    double retVal = 0;
    if (!results.isEmpty()) {
      retVal = results.get(results.size() - 1).getReturnVal();
    }
    return retVal;
  }

  public List<CommandResult> parseForRetVal(String[] tokenList) throws ParseException {
    int programCounter = 0;
    List<CommandResult> results = new ArrayList<>();
    String currentTokenType = getSymbol(tokenList[0]);
    if (isValue(currentTokenType)) {
      CommandResultBuilder builder = startCommandResult(myTurtles.get(0).getHeading(),myTurtles.get(0).getPosition());
      builder.setRetVal(parseValue(currentTokenType,tokenList[0]));
      return List.of(builder.buildCommandResult());
    }
    Command command = identifyCommand(tokenList[0]);
    List<CommandResult> listResult = parseSingleCommand(command,
        Arrays.copyOfRange(tokenList, programCounter + 1, tokenList.length));
    results.addAll(listResult);
    return results;
  }

  private Command identifyCommand(String rawToken) throws ParseException {
    if (isValue(getSymbol(rawToken))) {
      throw new ParseException("Don't know what to do with " + rawToken);
    }
    try {
      return CommandFactory.makeCommand(getSymbol(rawToken));
    } catch (ParseException e) {
      if (myUserCommandManager.containsCommand(rawToken)) {
        return myUserCommandManager.getCommand(rawToken);
      }
      throw new ParseException("Don't know how to " + rawToken.toUpperCase());
    }
  }

  private List<CommandResult> parseCommandPerTurtle(Command command, String[] tokenList) throws ParseException {
    System.out.println("Running PER TURTLE");
    List<CommandResult> results = new ArrayList<>();
    for (Turtle activeTurtle : myActiveTurtles) {
      myActiveTurtleID = activeTurtle.getId();
      System.out.println("myActiveTurtleID = " + myActiveTurtleID);
      results.addAll(parseCommand(command,tokenList));
    }
    myActiveTurtleID = null;
    if (results.isEmpty()) {
      List<CommandResult> nonActionResult = (parseCommand(command,tokenList));
      results.addAll(nonActionResult);
    }
    return results;
  }

  public void doActionPerTurtle(Runnable action) throws ParseException {
    System.out.println("Running PER TURTLE");
    List<CommandResult> results = new ArrayList<>();
    for (Turtle activeTurtle : myActiveTurtles) {
      myActiveTurtleID = activeTurtle.getId();
      action.run();
    }
    myActiveTurtleID = null;
  }

  private List<CommandResult> parseCommand(Command command, String[] tokenList)
      throws ParseException {
    //'fd 50' expects to start at PC = 1, where '50' is.
    Stack<Double> commandValues = new Stack<>();
    List<String> variableNames = getCommandVars(command, tokenList);
    for (int programCounter = variableNames.size(); programCounter <= tokenList.length;
        programCounter++) {
      if (commandValues.size() >= command.getNumArgs()) {
        //TODO: Figure this one out actually
        int tokensParsed = programCounter + command.getTokensParsed(tokenList);
        System.out.println("tokensParsed = " + tokensParsed);
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
    List<Double> argList = BackEndUtil.getArgsFromStack(commandValues, command.getNumArgs());
    List<CommandResult> results = new ArrayList<>((command.execute(argList, variableNames,
        Arrays.copyOfRange(tokenList, programCounter, tokenList.length),
        this)));
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
    } else {
      List<CommandResult> insideResult = parseCommand(identifyCommand(currentTokenRaw),
          Arrays.copyOfRange(tokenList, programCounter + 1, tokenList.length));
      commandValues.add(insideResult.get(insideResult.size() - 1).getReturnVal());
      return insideResult.get(insideResult.size() - 1).getTokensParsed();
    }
    return 0;
  }

  private double parseValue(String type, String token) throws ParseException {
    if (isVariable(type)) {
      return getVariable(token.substring(1));
    } else {
      return Double.parseDouble(token);
    }
  }

  private boolean isValue(String identity) {
    return identity.equals("Constant") || identity.equals("Variable");
  }

  private boolean isVariable(String identity) {
    return identity.equals("Variable");
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
    myUserCommandManager.addUserCommand(name, parameters, Arrays.asList(commands));
  }

  @Override
  public Collection<String> getUserCommandArgs(String name) {
    if (myUserCommandManager.containsCommand(name)) {
      return myUserCommandManager.getArguments(name);
    }
    return null;
  }

  @Override
  //TODO: Figure out a way to report the contents of user-generated commands.
  public Collection<String> getUserCommandScript(String name) {
    return null;
  }

  @Override
  public Command getUserCommand(String name) {
    if (myUserCommandManager.containsCommand(name)) {
      return myUserCommandManager.getCommand(name);
    }
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
    myLanguage = interpretPatterns(language);
  }

  public CommandResult makeCommandResult(double retVal, int tokensParsed) {
    CommandResultBuilder builder = startCommandResult(retVal);
    builder.setTokensParsed(tokensParsed);
    return builder.buildCommandResult();
  }

  @Override
  public void setActiveTurtles(List<Integer> turtleIDs) {
    ArrayList<Turtle> active = new ArrayList<>();
    for (Integer num : turtleIDs) {
      active.add(getTurtleWithID(num));
    }
    myActiveTurtles = active;
  }

  private Turtle getTurtleWithID(Integer num) {
    for (Turtle turtle : myTurtles) {
      if (turtle.getId() == num) {
        return turtle;
      }
    }
    Turtle newTurtle = new SLogoTurtle(num);
    myTurtles.add(newTurtle);
    return newTurtle;
  }

  @Override
  public void addPaletteColor(int index, List<Integer> rgbColor) {

  }

  @Override
  public List<Turtle> getTurtles(List<Integer> ids) {
    List<Turtle> ret = new ArrayList<>();
    for (int num : ids) {
      ret.add(getTurtleWithID(num));
    }
    return ret;
  }

  @Override
  public List<Turtle> getActiveTurtles() {
    return new ArrayList<>(myActiveTurtles);
  }

  @Override
  public List<Integer> getActiveTurtleNumbers() {
    List<Integer> ret = new ArrayList<>();
    for (Turtle active : myActiveTurtles) {
      ret.add(active.getId());
    }
    return ret;
  }

  public CommandResultBuilder startCommandResult(double turtleFacing, List<Double> turtlePosition) {
    CommandResultBuilder builder = new CommandResultBuilder(turtleFacing,turtlePosition,getActiveTurtleNumbers(),
        myPathColor, myBackgroundColor, myShapeIndex);
    builder.setIsActualCommand(true);
    builder.setPenSize(-1);
    return builder;
  }

  @Override
  public CommandResultBuilder startCommandResult(double retVal) {
    Turtle activeTurtle;
    if (myActiveTurtleID != null) {
      activeTurtle = getTurtleWithID(myActiveTurtleID);
    }
    else {
      activeTurtle = myTurtles.get(0);
    }

    CommandResultBuilder builder = new CommandResultBuilder(activeTurtle.getHeading(),activeTurtle.getPosition(),getActiveTurtleNumbers(), myPathColor, myBackgroundColor, myShapeIndex);
    builder.setTurtleID(activeTurtle.getId());
    builder.setRetVal(retVal);
    builder.setPenSize(-1);
    return builder;
  }

  @Override
  public int getPathColor(){ return myPathColor; }

  @Override
  public void setPathColor(int index){ myPathColor = index; }

  @Override
  public int getBackgroundColor(){ return myBackgroundColor; }

  @Override
  public void setBackgroundColor(int index){ myBackgroundColor = index; }

  @Override
  public int getShapeIndex(){ return myShapeIndex; }

  @Override
  public void setShapeIndex(int index){ myShapeIndex = index; }

  public Integer getActiveTurtleID() {
    return myActiveTurtleID;
  }

  public SLogoMemento saveStateToMemento() {
    ArrayList<Turtle> turtleCopy = new ArrayList<>();
    for (Turtle turtle : myTurtles) {
      turtleCopy.add(turtle.getClone());
    }
    return new SLogoMemento(turtleCopy,getActiveTurtleNumbers(),new HashMap<>(myPalette),
                            myBackgroundColor,myPathColor,
                            myShapeIndex, myPenSize,new ArrayList<>(myLanguage),
                            new HashMap<>(myVariables),new UserCommandManager(myUserCommandManager));
  }

  public List<CommandResult> loadStateFromMemento(SLogoMemento memento) {
    myTurtles = memento.getTurtles();
    setActiveTurtles(memento.getActiveTurtles());
    myPalette = memento.getPalette();
    myBackgroundColor = memento.getBackgroundIndex();
    myPathColor = memento.getPenColorIndex();
    myPenSize = memento.getPenSize();
    myShapeIndex = memento.getShapeIndex();
    myLanguage = memento.getLanguage();
    myVariables = memento.getVariables();
    myUserCommandManager = memento.getUserCommandManager();

    ArrayList<CommandResult> results = new ArrayList<>();
    for (Turtle turtle : myTurtles) {
      CommandResultBuilder builder = startCommandResult(turtle.getHeading(),turtle.getPosition());
      builder.setTurtleID(turtle.getId());
      builder.setPenUp(turtle.getPenUp());
      results.add(builder.buildCommandResult());
    }

    return results;
  }

}