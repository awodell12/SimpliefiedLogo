package slogo.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.regex.Pattern;
import slogo.CommandResult;

public class SLogoParser implements BackEndExternal, Interpreter{
  private BackEndInternal myBackEnd;
  private List<Entry<String, Pattern>> myLanguage;
  private List<Entry<String, Pattern>> mySyntax;

  private List<SLogoMemento> myPrevStates;
  private int myTimelineLocation;

  public SLogoParser(BackEndInternal backEnd) {
    myBackEnd = backEnd;
    myPrevStates = new ArrayList<>();
    myTimelineLocation = -1;
  }

  public SLogoParser() {
    this(new SLogoBackEnd());
    mySyntax = BackEndUtil.interpretPatterns("Syntax");
    setLanguage("English");
  }

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

  public List<CommandResult> parseScript(String script) {
    String[] scriptTokens = BackEndUtil.getTokenList(script).toArray(new String[0]);
    List<CommandResult> retList = parseCommandsList(scriptTokens);
    if (myTimelineLocation < myPrevStates.size()-1) {
      System.out.println("Deleting previous state timeline");
      myPrevStates = new ArrayList<>(myPrevStates.subList(0,myTimelineLocation+1));
    }
    myTimelineLocation += 1;
    System.out.println("myTimelineLocation = " + myTimelineLocation);
    myPrevStates.add(myBackEnd.saveStateToMemento());
    return retList;
  }

  @Override
  public List<CommandResult> redo() {
    System.out.println("REDOING");
    List<CommandResult> results = new ArrayList<>();
    if (myTimelineLocation < myPrevStates.size()-1) {
      myTimelineLocation += 1;
      results = myBackEnd.loadStateFromMemento(myPrevStates.get(myTimelineLocation), false, true);
    }
    return results;
//    return myBackEnd.redo();
  }

  @Override
  public List<CommandResult> undo() {
    System.out.println("UNDOING");
    List<CommandResult> results = new ArrayList<>();
    System.out.println("myTimelineLocation = " + myTimelineLocation);
    if (myTimelineLocation > 0) {
      myTimelineLocation -= 1;
      results = myBackEnd.loadStateFromMemento(myPrevStates.get(myTimelineLocation),true,false);
    }
    System.out.println("results.get(0).getTurtleID() = " + results.get(0).getTurtleID());
    System.out.println("results.get(0).getTurtlePosition() = " + results.get(0).getTurtlePosition());
    return results;
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
        CommandResultBuilder builder = myBackEnd.startCommandResult(0);
        builder.setErrorMessage(e.getMessage());
        results.add(builder.buildCommandResult());
        return results;
      }
    }
//    CommandResultBuilder builder = myBackEnd.startCommandResult(findRetVal(results));
//    builder.setTokensParsed(programCounter);
//    results.add(builder.buildCommandResult());
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

    String currentTokenType = getSymbol(tokenList[0]);
    if (isValue(currentTokenType)) {
      CommandResultBuilder builder = myBackEnd.startCommandResult(parseValue(currentTokenType,tokenList[0]));
      return List.of(builder.buildCommandResult());
    }

    Command command = identifyCommand(tokenList[0]);
    return parseSingleCommand(command,
        Arrays.copyOfRange(tokenList, 1, tokenList.length));
  }

  private Command identifyCommand(String rawToken) throws ParseException {
    if (isValue(getSymbol(rawToken))) {
      throw new ParseException("Don't know what to do with " + rawToken);
    }
    try {
      return CommandFactory.makeCommand(getSymbol(rawToken));
    } catch (ParseException e) {
      Command command = myBackEnd.getUserCommand(rawToken);
      if (command != null) {
        return command;
      }
      throw new ParseException("Don't know how to " + rawToken.toUpperCase());
    }
  }

  private List<CommandResult> parseCommandPerTurtle(Command command, String[] tokenList) throws ParseException {
    System.out.println("Running PER TURTLE in SLogoParser");
    List<CommandResult> results = new ArrayList<>();
//    myBackEnd.doActionPerTurtle( () -> (results.addAll(parseCommand(command,tokenList))) );
    System.out.println("myBackEnd.getActiveTurtles().size() = " + myBackEnd.getActiveTurtles().size());
    for (Turtle activeTurtle : myBackEnd.getActiveTurtles()) {
      myBackEnd.setActiveTurtleID(activeTurtle.getId());
      System.out.println("myActiveTurtleID = " + activeTurtle.getId());
      results.addAll(parseCommand(command,tokenList));
    }
    myBackEnd.setActiveTurtleID(null);
    if (results.isEmpty()) {
      List<CommandResult> nonActionResult = (parseCommand(command,tokenList));
      results.addAll(nonActionResult);
    }
    return results;
  }

  private List<CommandResult> parseCommand(Command command, String[] tokenList)
      throws ParseException {
    //'fd 50' expects to start at PC = 1, where '50' is.
    Stack<Double> commandValues = new Stack<>();
    List<String> variableNames = getCommandVars(command, tokenList);
    for (int programCounter = variableNames.size(); programCounter <= tokenList.length;
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
    List<Double> argList = BackEndUtil.getArgsFromStack(commandValues, command.getNumArgs());
    List<CommandResult> results = new ArrayList<>((command.execute(argList, variableNames,
        Arrays.copyOfRange(tokenList, programCounter, tokenList.length),
        myBackEnd, this)));
    CommandResult lastResult = results.get(results.size() - 1);
    //TODO: FIX BECAUSE THIS BREAKS IMMUTABILITY
    lastResult.setTokensParsed(lastResult.getTokensParsed()+programCounter);
    return results;
  }

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
      return myBackEnd.getVariable(token.substring(1));
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
  public void setLanguage(String language) {
    myLanguage = BackEndUtil.interpretPatterns(language);
  }
}
