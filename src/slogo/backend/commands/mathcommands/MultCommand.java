package slogo.backend.commands.mathcommands;

import java.util.List;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.backend.Interpreter;
import slogo.backend.ParseException;
import slogo.CommandResult;

public class MultCommand extends Command {

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
      BackEndInternal backEnd, Interpreter interpreter) throws ParseException {
    return List.of(backEnd.makeCommandResult(arguments.get(0) * arguments.get(1),0));
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }
}
