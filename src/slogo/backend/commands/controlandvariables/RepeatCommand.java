package slogo.backend.commands.controlandvariables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.backend.ParseException;
import slogo.CommandResult;

public class RepeatCommand implements Command {

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
  public List<CommandResult> execute(List<Double> arguments,  List<String> vars, String[] tokens, BackEndInternal backEnd)
      throws ParseException {
    double numLoops = arguments.get(0);
    double returnVal = 0;
    List<CommandResult> results = new ArrayList<>();
    int listLength = backEnd.distanceToEndBracket(Arrays.copyOfRange(tokens,1,tokens.length));
    for (double i = 1; i <= numLoops; i ++) {
      backEnd.setVariable("repcount",i);
      results.addAll(backEnd.parseCommandsList(Arrays.copyOfRange(tokens,1,listLength)));
      returnVal = results.get(results.size()-1).getReturnVal();
    }
    results.add(backEnd.makeCommandResult(returnVal,listLength+1));
    return results;
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }
}
