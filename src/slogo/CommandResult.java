package slogo;

public class CommandResult {
  private double returnVal;
  private int myTokensParsed;
  private String myErrorMessage;

  public CommandResult(double retVal, int tokensParsed) {
    returnVal = retVal;
    myTokensParsed = tokensParsed;
    myErrorMessage = "";
  }

  public void setErrorMessage(String msg) {
    myErrorMessage = msg;
  }

  public double getReturnVal() {
    return returnVal;
  }

  public int getTokensParsed() {
    return myTokensParsed;
  }
}
