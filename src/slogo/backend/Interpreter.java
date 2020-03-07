package slogo.backend;

import java.util.List;
import slogo.CommandResult;

public interface Interpreter {
  List<CommandResult> parseCommandsList(String[] tokenList);

  List<CommandResult> parseForRetVal(String[] tokenList) throws ParseException;

  void setLanguage(String language);

  boolean hasPrimitiveCommand(String command);
}
