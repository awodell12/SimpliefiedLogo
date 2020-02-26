package slogo.BackEnd.commands.controlandvariables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import slogo.BackEnd.AltCommand;
import slogo.BackEnd.BackEndInternal;
import slogo.BackEnd.ParseException;
import slogo.CommandResult;

public class RepeatCommand implements AltCommand {

  @Override
  public int getNumArgs() {
    return 1;
  }

  @Override
  public int getNumVars() {
    return 0;
  }

  @Override
  public List<CommandResult> execute(List<Double> arguments,  List<String> vars, String[] tokens, BackEndInternal backEnd)
      throws ParseException {
    double numLoops = arguments.get(0);
    double returnVal = 0;
    List<CommandResult> results = new ArrayList<>();
    int listLength = backEnd.distanceToEndBracket(Arrays.copyOfRange(tokens,1,tokens.length));
    for (double i = 1; i <= numLoops; i ++) {
      backEnd.setVariable("repcount",numLoops);
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
