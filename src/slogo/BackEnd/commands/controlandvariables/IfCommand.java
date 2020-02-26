package slogo.BackEnd.commands.controlandvariables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import slogo.BackEnd.Command;
import slogo.BackEnd.BackEndInternal;
import slogo.BackEnd.ParseException;
import slogo.CommandResult;

public class IfCommand implements Command {

  private static final int NUM_ARGS = 1;
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
    double returnVal = 0;
    List<CommandResult> results = new ArrayList<>();
    int listLength = backEnd.distanceToEndBracket(Arrays.copyOfRange(tokens,0,tokens.length));
    if (arguments.get(0) != 0) {
      System.out.println("IF evaluated to TRUE");
      results.addAll(backEnd.parseCommandsList(Arrays.copyOfRange(tokens,1,listLength-1)));
      returnVal = results.get(results.size()-1).getReturnVal();
    }
    else {
      System.out.println("IF evaluated to FALSE");
    }
    results.add(backEnd.makeCommandResult(returnVal,listLength));
    return results;
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }
}
