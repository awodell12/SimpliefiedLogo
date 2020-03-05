package slogo.backend;

import java.util.List;
import slogo.CommandResult;

public interface Interpreter {
  public List<CommandResult> parseCommandsList(String[] tokenList);

  public List<CommandResult> parseForRetVal(String[] tokenList) throws ParseException;

  void setLanguage(String language);
}
