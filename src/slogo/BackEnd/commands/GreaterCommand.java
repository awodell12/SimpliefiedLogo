package slogo.BackEnd.commands;

import java.util.List;
import slogo.BackEnd.AltCommand;
import slogo.BackEnd.BackEndInternal;
import slogo.BackEnd.ParseException;
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
  public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
      BackEndInternal backEnd) throws ParseException {
    double retValue = 0;
    if (arguments.get(0) > arguments.get(1)) { retValue = 1; }
    return List.of(new CommandResult(retValue,0));
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }
}
