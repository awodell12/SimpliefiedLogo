package slogo.backend;

import java.util.ArrayList;
import java.util.List;
import slogo.CommandResult;

public class CommandResultBuilder {

  private double myRetVal = 0;
  private int myTokensParsed;
  private int turtleID;
  private double turtleHeading;
  private List<Double> turtlePos;
  private List<Double> startPos;
  private int pathColorIndex;
  private String varName;
  private double varValue;
  private String udcName;
  private String udcScript;
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


  public CommandResultBuilder(double turtleFacing, List<Double> turtlePosition, List<Integer> activeTurtleNumbers,
                              int pathColor, int bgColor, int shape, double size, boolean isUp) {
    myRetVal = 0;
    myTokensParsed = 0;
    turtleID = 0;
    turtlePos = turtlePosition;
    turtleHeading = turtleFacing;
    startPos = null;
    pathColorIndex = pathColor;
    varName = null;
    varValue = 0;
    udcName = null;
    udcScript = null;
    clear = false;
    penUp = isUp;
    turtleVis = true;
    turtleReset = false;
    bgColorIndex = bgColor;
    newColor = null;
    penSize = size;
    shapeIndex = shape;
    errorMessage = "";
    //TODO: change these so that stuff doesn't automatically get set to zero
    newPaletteIndex = 0;
    activeTurtles = new ArrayList<>(activeTurtleNumbers);
    actualCommand = true;
  }

  public CommandResultBuilder(int turtleNumber, double turtleFacing, List<Double> turtlePosition, boolean turtlePenUp, List<Integer> activeTurtles,
                              int pathColor, int bgColor, int shape, double size, boolean isUp) {
    this(turtleFacing,turtlePosition, activeTurtles, pathColor, bgColor, shape, size, isUp);
    turtleID = turtleNumber;
    penUp = turtlePenUp;
  }


  public void retVal(double val) {
    myRetVal = val;
  }

  public void tokensParsed(int val) {
    myTokensParsed = val;
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

  public void variableName(String name) {
    varName = name;
  }

  public void varValue(double val) {
    varValue = val;
  }

  public void userDefinedCommandName(String name) {
    udcName = name;
  }

  public void userDefinedCommandScript(String script) {
    udcScript = script;
  }

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

  public void setBackgroundColor(int index){ bgColorIndex = index; }

  public void setColor(List<Integer> color){ newColor = color; }

  public void setShapeIndex(int index){ shapeIndex = index; }

  public void setPaletteIndex(int index){ newPaletteIndex = index; }

  public void setPenSize(double size){ penSize = size; }

  public CommandResult buildCommandResult() {
    return new CommandResult(myRetVal, myTokensParsed, turtleID, turtleHeading, turtlePos,
        startPos, pathColorIndex, varName, varValue, udcName, udcScript,
        clear, penUp, turtleVis, turtleReset, bgColorIndex, newColor, penSize, activeTurtles, shapeIndex,
            newPaletteIndex, errorMessage, actualCommand);
  }
}