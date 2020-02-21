package slogo.BackEnd.commands;

import java.util.List;
import slogo.BackEnd.AltCommand;
import slogo.CommandResult;
import slogo.BackEnd.SLogoBackEnd;

public class MakeCommand implements AltCommand {

  @Override
  public int getNumArgs() {
    return 1;
  }

  @Override
  public int getNumVars() {
    return 1;
  }

  @Override
  public CommandResult execute(List<Double> arguments, List<String> vars, String[] tokens,
      SLogoBackEnd backEnd) {
    backEnd.setVariable(vars.get(0),arguments.get(0));
    return new CommandResult(arguments.get(0),0);
  }
}
