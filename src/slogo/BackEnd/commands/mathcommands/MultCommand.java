package slogo.BackEnd.commands.mathcommands;

import java.util.List;
import slogo.BackEnd.Command;
import slogo.BackEnd.BackEndInternal;
import slogo.BackEnd.ParseException;
import slogo.CommandResult;

public class MultCommand implements Command {

  private static final int NUM_ARGS = 2;
  private static final int NUM_VARS = 0;
  @Override
  public int getNumArgs() {
    return NUM_ARGS;
  }

  @Override
  public int getNumVars() {
    return NUM_VARS;
  }

  @Override
  public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
      BackEndInternal backEnd) throws ParseException {
    return List.of(backEnd.makeCommandResult(arguments.get(0) * arguments.get(1),0));
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }
}
