package slogo.BackEnd.commands;

import java.util.List;
import slogo.BackEnd.AltCommand;
import slogo.BackEnd.SLogoBackEnd;
import slogo.CommandResult;

public class SumCommand implements AltCommand {

  @Override
  public int getNumArgs() {
    return 2;
  }

  @Override
  public int getNumVars() {
    return 0;
  }

  @Override
  public CommandResult execute(List<Double> arguments, List<String> vars, String[] tokens,
      SLogoBackEnd backEnd) {
    System.out.println("Executed sum of " + arguments.get(0) + " and " + arguments.get(1));
    return new CommandResult(arguments.get(0)+arguments.get(1),0);
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }
}
