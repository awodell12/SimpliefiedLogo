package slogo;

public class CommandResult {
  private double returnVal;
  private int myTokensParsed;

  public CommandResult(double retVal, int tokensParsed) {
    returnVal = retVal;
    myTokensParsed = tokensParsed;
  }

  public double getReturnVal() {
    return returnVal;
  }

  public int getTokensParsed() {
    return myTokensParsed;
  }
}
