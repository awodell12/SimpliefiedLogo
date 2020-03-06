package slogo.backend;

public class FileBuilderException extends Exception {
  private String myMessage;

  public FileBuilderException(String message) {
    myMessage = message;
  }

  public String getMessage() {
    return myMessage;
  }
}
