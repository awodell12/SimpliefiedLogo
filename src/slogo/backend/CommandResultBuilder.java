package slogo.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import slogo.CommandResult;

public class CommandResultBuilder {

  private double myRetVal = 0;
  private int myTokensParsed;
  private int turtleID;
  private double turtleHeading;
  private List<Double> turtlePos;
  private List<Double> startPos;
  private int pathColor;
  private String varName;
  private double varValue;
  private String udcName;
  private String udcScript;
  private boolean clear;
  private boolean penUp;
  private boolean turtleVis;
  private boolean turtleReset;
  private String errorMessage;
  private int backgroundColor;
  private int newPaletteIndex;
  private double penSize;
  private List<Integer> newPaletteColor;
  private List<Integer> activeTurtles;
  private int shapeIndex;

  public CommandResultBuilder(double turtleFacing, List<Double> turtlePosition) {
    myRetVal = 0;
    myTokensParsed = 0;
    turtleID = 0;
    turtlePos = turtlePosition;
    turtleHeading = turtleFacing;
    startPos = null;
    pathColor = 0;
    varName = null;
    varValue = 0;
    udcName = null;
    udcScript = null;
    clear = false;
    penUp = true;
    turtleVis = true;
    turtleReset = false;
    errorMessage = "";
    backgroundColor = 0; //TODO: change these so that stuff doesn't automatically get set to zero
    newPaletteIndex = 0;
    activeTurtles = new ArrayList<>();
    activeTurtles.add(0);
    penSize = 1.0;
    newPaletteColor = new ArrayList<>();
    newPaletteColor.add(0);
    newPaletteColor.add(0);
    newPaletteColor.add(0);
    shapeIndex = 0;
  }


  public void retVal(double val) {
    myRetVal = val;
  }

  public void tokensParsed(int val) {
    myTokensParsed = val;
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

  public CommandResult buildCommandResult() {
    return new CommandResult(myRetVal, myTokensParsed, turtleID, turtleHeading, turtlePos,
        startPos, pathColor, varName, varValue, udcName, udcScript,
        clear, penUp, turtleVis, turtleReset, backgroundColor, newPaletteColor, penSize, activeTurtles,
            shapeIndex, newPaletteIndex, errorMessage);
  }
}