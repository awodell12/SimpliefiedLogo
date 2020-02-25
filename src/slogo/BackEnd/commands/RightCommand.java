package slogo.BackEnd.commands;

import java.util.List;
import slogo.BackEnd.AltCommand;
import slogo.BackEnd.BackEndInternal;
import slogo.BackEnd.ParseException;
import slogo.CommandResult;

public class RightCommand implements AltCommand {

  public static final int NUM_ARGS = 1;
  public static final String COMMAND_NAME = "Right";

  @Override
  public int getNumArgs() {
    return 1;
  }

  @Override
  public int getNumVars() {
    return 0;
  }

  @Override
  public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens, BackEndInternal backEnd)
      throws ParseException {
    System.out.println("Turning right by " + arguments.get(0) + " degrees.");
    return List.of(new CommandResult(arguments.get(0),0));
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }
}
