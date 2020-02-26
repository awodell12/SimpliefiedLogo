package slogo.BackEnd.commands.controlandvariables;

import java.util.List;
import slogo.BackEnd.Command;
import slogo.BackEnd.BackEndInternal;
import slogo.BackEnd.ParseException;
import slogo.CommandResult;

public class MakeCommand implements Command {

  @Override
  public int getNumArgs() {
    return 1;
  }

  @Override
  public int getNumVars() {
    return 1;
  }

  @Override
  public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
      BackEndInternal backEnd) throws ParseException {
    backEnd.setVariable(vars.get(0),arguments.get(0));
    return List.of(backEnd.makeCommandResult(arguments.get(0),0,vars.get(0),arguments.get(0)));
  }

  @Override
  public List<String> findVars(String[] tokenList) {return List.of(tokenList[0].substring(1));}
}
