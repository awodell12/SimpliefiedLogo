package slogo.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import slogo.CommandResult;

public class CommandResultBuilder {

  private double myRetVal = 0;
  private int myTokensParsed;
  private int turtleID;
  private double turtleHeading;
  private List<Double> turtlePos;
  private List<Double> startPos;
  private int pathColorIndex;
  private Map<String, Double> variables;
  private Map<String, String> userCommands;
  private boolean clear;
  private boolean penUp;
  private boolean turtleVis;
  private boolean turtleReset;
  private int bgColorIndex;
  private List<Integer> newColor;
  private double penSize;
  private List<Integer> activeTurtles;
  private int shapeIndex;
  private String errorMessage;
  private int newPaletteIndex;
  private boolean actualCommand;
  private boolean isUndo;
  private boolean isRedo;


  public CommandResultBuilder(int turtleNumber, double turtleFacing, List<Double> turtlePosition,
      List<Integer> activeTurtleNumbers,
      int pathColor, int bgColor, int shape, double size, boolean isUp,
      Map<String, Double> varMap, Map<String, String> commandMap) {
    myRetVal = 0;
    myTokensParsed = 0;
    turtleID = turtleNumber;
    turtlePos = turtlePosition;
    turtleHeading = turtleFacing;
    startPos = null;
    pathColorIndex = pathColor;
    variables = varMap;
    userCommands = commandMap;
    clear = false;
    turtleVis = true;
    turtleReset = false;
    bgColorIndex = bgColor;
    newColor = null;
    shapeIndex = shape;
    penSize = size;
    errorMessage = "";
    //TODO: change these so that stuff doesn't automatically get set to zero
    newPaletteIndex = 0;
    activeTurtles = new ArrayList<>(activeTurtleNumbers);
    actualCommand = true;
    penUp = isUp;
    isUndo = false;
    isRedo = false;
  }

  public void setTokensParsed(int val) {
    myTokensParsed = val;
  }

  public void setRetVal(double val) {
    myRetVal = val;
  }

  public void setTurtleID (int val) {
    turtleID = val;
  }

  public void setTurtlePos(List<Double> pos) {
    turtlePos = pos;
  }

  public void setPathStart(List<Double> pos) {
    startPos = pos;
  }

  public void setPathColor(int index) {
    pathColorIndex = index;
  }

  public void setPenSize(double size) { penSize = size; }

  public void activeTurtleIDs(List<Integer> turtles) {
    activeTurtles = new ArrayList<>(turtles);
  }

  public void setErrorMessage(String message) {
    errorMessage = message;
  }

  public void setIsActualCommand(boolean isCommand) {
    actualCommand = isCommand;
  }

  public void setTurtleReset(boolean isReset){ turtleReset = isReset; }

  public void setMyScreenClear(boolean value){ clear = value; }

  public void setPenUp(boolean penIsUp) {
    penUp = penIsUp;
  }

  public void setBackgroundColor(int index){ bgColorIndex = index; }

  public void setColor(List<Integer> color){ newColor = color; }

  public void setShapeIndex(int index){ shapeIndex = index; }

  public void setPaletteIndex(int index){ newPaletteIndex = index; }

  public void setIsUndo(boolean undo) {
    isUndo = undo;
  }

  public void setIsRedo(boolean redo) {
    isRedo = redo;
  }

  public void setVariables(Map<String, Double> vars) {
    variables = vars;
  }

  public void setUserCommands(Map<String,String> commands) {
    userCommands = commands;
  }


  public CommandResult buildCommandResult() {
    return new CommandResult(myRetVal, myTokensParsed, turtleID, turtleHeading, turtlePos,
        startPos, pathColorIndex, variables, userCommands,
        clear, penUp, turtleVis, turtleReset, bgColorIndex, newColor, penSize, activeTurtles, shapeIndex,
            newPaletteIndex, errorMessage, actualCommand, isUndo, isRedo);
  }
}