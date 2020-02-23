package slogo.BackEnd.commands;

import java.util.List;
import slogo.BackEnd.AltCommand;
import slogo.BackEnd.SLogoBackEnd;
import slogo.CommandResult;

public class GreaterCommand implements AltCommand {

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
    double retValue = 0;
    if (arguments.get(0) > arguments.get(1)) { retValue = 1; }
    return new CommandResult(retValue,0);
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }
}
