package slogo;

import java.util.ArrayList;
import java.util.List;
import slogo.BackEnd.Path;


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
  private List<Double> myPathStart;
  private int myPathColor;
  private int myBackgroundColor;
  private List<Integer> myNewPaletteColor;
  private double myPenSize;
  private List<Integer> myActiveTurtleIDs;
  private int myShapeIndex;

  public CommandResult(double retVal, int tokensParsed, int turtleID, double heading, List<Double> pos, List<Double> pathStart, int pathColor,
                       String variableName, double variableVal, String udcName, String udcText,
                       boolean clearScreen, boolean penUp, boolean turtleVisible, boolean turtleReset,
                       int backGroundColor, List<Integer> newPaletteColor, double penSize, List<Integer> activeTurtles, int shapeIndex) {
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
    myPathStart = pathStart;
    myPathColor = pathColor;
    myBackgroundColor = backGroundColor;
    myNewPaletteColor = newPaletteColor;
    myPenSize = penSize;
    myActiveTurtleIDs = activeTurtles;
    myShapeIndex = shapeIndex;
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
    myPenUp = false;
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

  public double getMyRotation() {
    return myRotation;
  }

  public List<Double> getMyPosition() {
    return myPosition;
  }

  public List<Double> getPathStart(){
      return myPathStart;
  }

  public String getMyVariableName() {
    return myVariableName;
  }

  public double getMyVariableValue() {
    return myVariableValue;
  }

  public String getMyUDCName() {
    return myUDCName;
  }

  public String getMyUDCText() {
    return myUDCText;
  }

  public boolean isMyScreenClear() {
    return myScreenClear;
  }

  public boolean isMyPenUp() {
    return myPenUp;
  }

  public boolean isMyTurtleVisible() {
    return myTurtleVisible;
  }

  public boolean isMyTurtleReset() {
    return myTurtleReset;
  }

  public int getPathColor(){
    return myPathColor;
  }

  public int getBackgroundColor(){
    return myBackgroundColor;
  }

  public List<Integer> getNewPaletteColor(){
    return myNewPaletteColor;
  }

  public double getPenSize(){
    return myPenSize;
  }

  public List<Integer> getActiveTurtleIDs(){
    return myActiveTurtleIDs;
  }

  public int getShapeIndex(){
    return myShapeIndex;
  }
}
