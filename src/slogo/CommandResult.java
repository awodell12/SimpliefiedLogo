package slogo;

import java.util.ArrayList;
import java.util.List;


public class CommandResult {
  private double returnVal;
  private int myTokensParsed;
  private String myErrorMessage;

  private double myRotation;
  private List<Double> myPosition;
  private String myVariableName;
  private double myVariableValue;
  private String myUDCName;
  private String myUDCText;
  private boolean myScreenClear;
  private boolean myPenUp;
  private boolean myTurtleVisible;
  private boolean myTurtleReset;
  //TODO: how to pass in path(s)?

  public CommandResult(double retVal, int tokensParsed, int turtleID, double heading, List<Double> pos, List<Double> pathStart, String pathColor,
                       String variableName, double variableVal, String udcName, String udcText,
                       boolean clearScreen, boolean penUp, boolean turtleVisible, boolean turtleReset) {
    returnVal = retVal;
    myTokensParsed = tokensParsed;
    myErrorMessage = "";

    myRotation = heading;
    myPosition = pos;
    myVariableName = variableName;
    myVariableValue = variableVal;
    myUDCName = udcName;
    myUDCText = udcText;
    myScreenClear = clearScreen;
    myPenUp = penUp;
    myTurtleVisible = turtleVisible;
    myTurtleReset = turtleReset;
  }

  public CommandResult(double retVal, int tokensParsed){
    returnVal = retVal;
    myTokensParsed = tokensParsed;

    myRotation = 0;
    myPosition = new ArrayList<>();
    myPosition.add(0.0); myPosition.add(0.0);
    myVariableName = "varName";
    myVariableValue = 0;
    myUDCName = "udcName";
    myUDCText = "udcText";
    myScreenClear = false;
    myPenUp = true;
    myTurtleVisible = true;
    myTurtleReset = false;
  }


  public void setErrorMessage(String msg) {
    myErrorMessage = msg;
  }
  public String getErrorMessage() {
    return myErrorMessage;
  }

  public double getReturnVal() {
    return returnVal;
  }

  public int getTokensParsed() {
    return myTokensParsed;
  }
}
