package slogo.backend.commands.controlandvariables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.backend.ParseException;
import slogo.CommandResult;

public class DoTimesCommand implements Command {

  private static final int NUM_ARGS = 1;
  private static final int NUM_VARS = 1;

  @Override
  public int getNumArgs() {
    return NUM_ARGS;
  }

  @Override
  public int getNumVars() {
    return NUM_VARS;
  }

  @Override
  public List<CommandResult> execute(List<Double> arguments,  List<String> vars, String[] tokens, BackEndInternal backEnd)
      throws ParseException {
    System.out.println("Beginning DOTIMES Loop.");
    double limit = arguments.get(0);
    String var = vars.get(1);
    double returnVal = 0;
    List<CommandResult> results = new ArrayList<>();
    int listLength = backEnd.distanceToEndBracket(Arrays.copyOfRange(tokens,2,tokens.length));
    for (double i = 1; i <= limit; i ++) {
      backEnd.setVariable(var,i);
      results.addAll(backEnd.parseCommandsList(Arrays.copyOfRange(tokens,2,listLength+1)));
      returnVal = results.get(results.size()-1).getReturnVal();
    }
    System.out.println("Ending DOTIMES Loop.");
    results.add(backEnd.makeCommandResult(returnVal, listLength+2, var, limit));
    //results.add(make)
    return results;
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return List.of(tokenList[0],tokenList[1].substring(1));
  }
}
