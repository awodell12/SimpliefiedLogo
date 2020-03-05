package slogo.backend.commands.controlandvariables;

import java.util.Arrays;
import java.util.List;
import slogo.backend.BackEndUtil;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.backend.ParseException;
import slogo.CommandResult;

public class IfElseCommand implements Command {

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
    double returnVal;
    List<CommandResult> results;
    int firstListLength = BackEndUtil.distanceToEndBracket(Arrays.copyOfRange(tokens,1,tokens.length));
    int secondListLength = BackEndUtil.distanceToEndBracket(Arrays.copyOfRange(tokens,firstListLength+2,tokens.length));
    System.out.println("firstListLength = " + firstListLength);
    System.out.println("secondListLength = " + secondListLength);
    if (arguments.get(0) != 0) {
      System.out.println("IF evaluated to TRUE");
      results = backEnd.parseCommandsList(Arrays.copyOfRange(tokens,2,firstListLength));
    }
    else {
      System.out.println("IF evaluated to FALSE");
      results = backEnd.parseCommandsList(Arrays.copyOfRange(tokens,2+firstListLength,firstListLength+secondListLength+1));
    }
    returnVal = results.get(results.size()-1).getReturnVal();
    results.add(backEnd.makeCommandResult(returnVal,firstListLength+secondListLength+2));
    return results;
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }
}
