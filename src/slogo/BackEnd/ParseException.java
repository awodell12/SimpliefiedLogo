package slogo.BackEnd;

public class ParseException extends Exception {
  private final String myMessage;

  public ParseException(String msg) {
    myMessage = msg;
  }

  public ParseException() {
    myMessage = "Error in parse.";
  }

  public String getMessage() {
    return myMessage;
  }
}
