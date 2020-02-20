package slogo;

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

public class SLogoBackEnd implements BackEndExternal, BackEndInternal {

  public static final String WHITESPACE = "\\s+";

  private static final String RESOURCES_PACKAGE = "resources/languages/";
  private List<Entry<String, Pattern>> mySymbols;
  private Map<String, Double> myVariables;
  private int myProgramCounter;

  public SLogoBackEnd() {
    mySymbols = new ArrayList<>();
    myVariables = new HashMap<>();
  }

  public int getProgramCounter() {
    return myProgramCounter;
  }

  public void setProgramCounter(int newVal) {
    myProgramCounter = newVal;
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
    String[] scriptTokens = script.split(WHITESPACE);
    parseTokens(scriptTokens);
    return null;
  }

  public int parseTokens(String[] scriptTokens) {
    System.out.println("Start of parse");
    Stack<AltCommand> commands = new Stack<>();
    Stack<Double> commandValues = new Stack<>();
    Stack<String> controlVars = new Stack<>();

    int programCounter;
    for (programCounter = 0; programCounter < scriptTokens.length; programCounter++) {
      System.out.println("Currently on token " + programCounter);
      printRemainingTokens(scriptTokens,programCounter);
      String currentToken = getSymbol(scriptTokens[programCounter]);
      System.out.println("scriptTokens[programCounter] = " + scriptTokens[programCounter]);
      System.out.println("currentToken = " + currentToken);
      if (isClosedBracket(currentToken)) {
        System.out.println("end bracket, DONE WITH PARSE, at token " + programCounter);
        System.out.println();
        return programCounter+1; //end the current call if an end bracket happens.
      }
      if (isOpenBracket(currentToken)) {
        programCounter += parseTokens(Arrays.copyOfRange(scriptTokens,programCounter+1,scriptTokens.length));
      }
      programCounter += recordToken(commands, commandValues, controlVars,
          scriptTokens, programCounter);

      System.out.println("commands = " + commands);
      System.out.println("controlVars = " + controlVars);
      System.out.println("commandValues = " + commandValues);
      System.out.println();

      System.out.println("commands = " + commands);
      if (!commands.isEmpty()) {
        System.out.println("commands.peek() = " + commands.peek());
        if (commandValues.size() >= commands.peek().getNumArgs()) {
          List<Double> argList = new ArrayList<>();
          for (int arg = 0; arg < commands.peek().getNumArgs(); arg ++) {
            argList.add(commandValues.pop());
          }
          List<String> varList = new ArrayList<>();
          for (int arg = 0; arg < commands.peek().getNumVars(); arg ++) {
            varList.add(controlVars.pop());
          }
          //call it "run a command
          CommandResult result = commands.pop().execute( argList, varList,
                                                    Arrays.copyOfRange(scriptTokens,programCounter,scriptTokens.length),
                                                  this);
          double retValue = result.getReturnVal();
          programCounter += result.getTokensParsed();
          //refactor into 'handle add value'
          if (!commands.isEmpty()) {
            commandValues.add(retValue);
          }
        }
      }
    }
    System.out.println("DONE WITH PARSE");
    System.out.println("programCounter = " + programCounter);
    return programCounter;
  }

  private int recordToken(Stack<AltCommand> commands,
      Stack<Double> commandValues, Stack<String> vars,
      String[] tokenList, int programCounter) {
    String tokenType = getSymbol(tokenList[programCounter]);
    if (isControl(tokenType) || isCommand(tokenType)) {
      commands.add(CommandFactory.makeCommand(tokenType));
      if (isForLoop(tokenType)) {
        int offset = 2;
        vars.add(tokenList[programCounter+offset].substring(1));
        return offset;
      }
      if (tokenType.equals("MakeVariable")) {
        int offset = 1;
        vars.add(tokenList[programCounter+offset].substring(1));
        return offset;
      }
    }
    if (isValue(tokenType)) {
      Double value;
      if (isVariable(tokenType)) {
        try {
          value = getVariable(tokenList[programCounter].substring(1));
        }
        catch (ParseException e) {
          value = 0.0;
        }
      }
      else {
        System.out.println("currentToken = " + tokenList);
        value = Double.parseDouble(tokenList[programCounter]);
      }
      if (!commands.isEmpty()) {
        commandValues.add(value);
      }
    }
    return 0;
  }

  private void printRemainingTokens(String[] scriptTokens, int i) {
    String[] remaining = Arrays.copyOfRange(scriptTokens,i,scriptTokens.length);
    for (String string : remaining) {
      System.out.print(string + " ");
    }
    System.out.println();
  }

  /**
   * @param tokenList
   * @param startVal
   * @param endVal
   * @param incrementVal
   * @param variable
   * @return The number of commands that were parsed by this call.
   */
  private int parseForLoop(String[] tokenList, Double startVal, Double endVal,
      Double incrementVal, String variable) {
    System.out.println("Beginning FOR loop.");
    printRemainingTokens(tokenList, 0);
    String[] commandList = Arrays.copyOfRange(tokenList, 2,tokenList.length);
    int numParsed = distanceToEndBracket(commandList);
    for (Double counter = startVal; counter < endVal; counter += incrementVal) {
      setVariable(variable,counter);
      //TODO: Make a function that finds the length of a list of commands,
      //TODO: so that we don't make mistakes when the for loop runs 0 times.
      parseTokens(commandList);
    }
    System.out.println("Ending FOR loop after parsing " + numParsed + " tokens.");
    return numParsed + 1;
  }

  private int parseRepeat(String[] tokenList) {
    System.out.println("Beginning REPEAT loop.");
    printRemainingTokens(tokenList,0);
    Double repeatAmount = Double.parseDouble(tokenList[0]);
    int numParsed = 0;
    for (Double counter = 0.0; counter < repeatAmount; counter++) {
      numParsed = parseTokens(Arrays.copyOfRange(tokenList,2,tokenList.length));
    }
    System.out.println("Ending REPEAT loop.");
    return numParsed;
  }

  private boolean isCommand(String identity) {
    return
        identity.equals("Right") || identity.equals("Backward") || identity.equals("Left") || identity.equals("Forward");
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
    System.out.println("Beginning to parse for distance to end bracket.");
    printRemainingTokens(tokenList,0);
    Stack<String> openBrackets = new Stack<>();
    for (int i = 0; i < tokenList.length; i ++) {
      String tokenSymbol = getSymbol(tokenList[i]);
      if (isOpenBracket(tokenSymbol)) {
        openBrackets.add(tokenList[i]); //THERE IS NOT A POINT TO DOING THIS WITH A STACK.
      }
      else if (isClosedBracket(tokenSymbol)) {
        if (openBrackets.isEmpty()) {
          System.out.println("Found an end bracket at distance " + (i +1) + ".\n");
          return i + 1;
        }
        else {
          openBrackets.pop();
        }
      }
    }
    System.out.println("No end bracket found.\n");
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
    //TODO: Make this throw an exception.
    System.out.println("VARIABLE " + name + " DOES NOT EXIST.");
    return 0;
  }

  @Override
  public void clearVariables() {
    myVariables.clear();
  }

  @Override
  public void setUserCommand(String name, Collection<String> parameters, String script)
      throws ParseException {

  }

  @Override
  public Collection<String> getUserCommandArgs(String name) {
    return null;
  }

  @Override
  public String getUserCommandScript(String name) {
    return null;
  }
}