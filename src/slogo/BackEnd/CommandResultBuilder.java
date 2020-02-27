package slogo.BackEnd;

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
  private String pathColor;
  private String varName;
  private double varValue;
  private String udcName;
  private String udcScript;
  private boolean clear;
  private boolean penUp;
  private boolean turtleVis;
  private boolean turtleReset;
  private String errorMessage;

  public CommandResultBuilder(double turtleFacing, List<Double> turtlePosition) {
    myRetVal = 0;
    myTokensParsed = 0;
    turtleID = 0;
    turtlePos = turtlePosition;
    turtleHeading = turtleFacing;
    startPos = null;
    pathColor = null;
    varName = null;
    varValue = 0;
    udcName = null;
    udcScript = null;
    clear = false;
    penUp = true;
    turtleVis = true;
    turtleReset = false;
    errorMessage = "";
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
        clear, penUp, turtleVis, turtleReset, errorMessage);
  }
}