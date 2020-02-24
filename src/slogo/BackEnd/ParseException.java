package slogo.BackEnd;

public class ParseException extends Exception {
  private String myMessage;

  public ParseException(String msg) {
    myMessage = msg;
  }

  public String getMessage() {
    return myMessage;
  }

  public ParseException() {
    myMessage = "Error in parse.";
  }
}
