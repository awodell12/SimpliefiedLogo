package slogo.backend.commands;

import java.util.List;
import slogo.CommandResult;
import slogo.backend.BackEndInternal;
import slogo.backend.Command;
import slogo.backend.ParseException;

public class IdCommand implements Command {

  @Override
  public int getNumArgs() {
    return 0;
  }

  @Override
  public int getNumVars() {
    return 0;
  }

  @Override
  public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
      BackEndInternal backEnd) throws ParseException {
    return List.of(backEnd.makeCommandResult(backEnd.getActiveTurtleID(),0));
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }
}
