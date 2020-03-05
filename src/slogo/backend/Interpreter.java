package slogo.backend;

import java.util.List;
import slogo.CommandResult;

public interface Interpreter {
  public List<CommandResult> parseCommandsList(String[] tokenList);

}
