package slogo.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import slogo.CommandResult;

/**
 *
 */
public class SLogoBackEnd implements BackEndInternal {

  public static final int INITIAL_BG_COLOR = 5;
  private Map<String, Double> myVariables;
  private UserCommandManager myUserCommandManager;
  private List<Turtle> myTurtles;
  private List<Turtle> myActiveTurtles;
  private Map<Integer, List<Integer>> myPalette;
  private int myPathColor = 0;
  private int myBackgroundColor;
  private int myShapeIndex = 0;
  private double myPenSize = 1;
  private boolean penUp = false;
  private Integer myActiveTurtleID;


  public SLogoBackEnd() {
    myVariables = new HashMap<>();
    myUserCommandManager = new UserCommandManager();
    myTurtles = new ArrayList<>();
    myTurtles.add(new SLogoTurtle(0));
    myActiveTurtles = List.of(myTurtles.get(0));
    myActiveTurtleID = null;
    myPalette = new HashMap<>();
    myBackgroundColor = INITIAL_BG_COLOR;
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
    //TODO: Remove this last artifact of parsing from the internal interface
    if (CommandFactory.hasCommand(BackEndUtil.getSymbol(name))) {
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
    return BackEndUtil.getTokenList(myUserCommandManager.getCommandScript(name));
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

  @Override
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
    myPalette.put(index, rgbColor);
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


  @Override
  public CommandResultBuilder startCommandResult(double turtleFacing, List<Double> turtlePosition, boolean turtleVisible) {
    return new CommandResultBuilder(0, turtleFacing,turtlePosition,getActiveTurtleNumbers(), turtleVisible,  myPathColor, myBackgroundColor, myShapeIndex, myPenSize, penUp, myVariables,myUserCommandManager.getScriptMap());

  }

  @Override
  public CommandResultBuilder startCommandResult(int turtleID, double retVal) {
    Turtle turtle = getTurtleWithID(turtleID);
    CommandResultBuilder ret =  new CommandResultBuilder(turtleID, turtle.getHeading(),turtle.getPosition(),getActiveTurtleNumbers(), turtle.getVisible(), myPathColor, myBackgroundColor, myShapeIndex, myPenSize, penUp, myVariables,myUserCommandManager.getScriptMap());
    ret.setRetVal(retVal);
    return ret;
  }

  @Override
  public CommandResultBuilder startCommandResult(double retVal) {
    CommandResultBuilder ret = new CommandResultBuilder(0,myTurtles.get(0).getHeading(),myTurtles.get(0).getPosition(),
                                    getActiveTurtleNumbers(), myTurtles.get(0).getVisible() ,myPathColor,myBackgroundColor,myShapeIndex,myPenSize,penUp, myVariables, myUserCommandManager.getScriptMap());
    ret.setRetVal(retVal);
    return ret;
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

  @Override
  public double getPenSize(){ return myPenSize; }

  @Override
  public void setPenSize(double size){ myPenSize = size; }

  @Override
  public boolean getPenUp(){ return penUp; }

  @Override
  public void setPenUp(boolean isUp){ penUp = isUp; }

  @Override
  public Integer getActiveTurtleID() {
    return myActiveTurtleID;
  }

  @Override
  public SLogoMemento saveStateToMemento() {
    ArrayList<Turtle> turtleCopy = new ArrayList<>();
    for (Turtle turtle : myTurtles) {
      turtleCopy.add(turtle.getClone());
    }
    return new SLogoMemento(turtleCopy,getActiveTurtleNumbers(),new HashMap<>(myPalette),
                            myBackgroundColor,myPathColor,
                            myShapeIndex, myPenSize,
                            new HashMap<>(myVariables),new UserCommandManager(myUserCommandManager));
  }

  @Override
  public List<CommandResult> loadStateFromMemento(SLogoMemento memento, boolean isUndo, boolean isRedo) {
    myTurtles = memento.getTurtles();
    setActiveTurtles(memento.getActiveTurtles());
    myPalette = memento.getPalette();
    myBackgroundColor = memento.getBackgroundIndex();
    myPathColor = memento.getPenColorIndex();
    myPenSize = memento.getPenSize();
    myShapeIndex = memento.getShapeIndex();
    myVariables = memento.getVariables();
    myUserCommandManager = memento.getUserCommandManager();

    ArrayList<CommandResult> results = new ArrayList<>();
    for (Turtle turtle : myTurtles) {
      CommandResultBuilder builder = startCommandResult(turtle.getHeading(),turtle.getPosition(), turtle.getVisible());
      builder.setTurtleID(turtle.getId());
      builder.setPenUp(turtle.getPenUp());
      builder.setIsUndo(isUndo);
      builder.setIsRedo(isRedo);
      results.add(builder.buildCommandResult());
    }
    return results;
  }

  @Override
  public void setActiveTurtleID(Integer id) {
    myActiveTurtleID = id;
  }
}