package slogo.backend.commands.multiplecommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import slogo.CommandResult;
import slogo.backend.BackEndInternal;
import slogo.backend.BackEndUtil;
import slogo.backend.Command;
import slogo.backend.Interpreter;
import slogo.backend.ParseException;

public class AskWithCommand extends Command {

  private static final int NUM_ARGS = 0;
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
      BackEndInternal backEnd, Interpreter interpreter) throws ParseException {
    List<Integer> originalActives = backEnd.getActiveTurtleNumbers();
    double returnVal;
    List<CommandResult> results = new ArrayList<>();
    int firstListLength = BackEndUtil.distanceToEndBracket(Arrays.copyOfRange(tokens,1,tokens.length));
    int secondListLength = BackEndUtil.distanceToEndBracket(Arrays.copyOfRange(tokens,firstListLength+2,tokens.length));
    backEnd.setActiveTurtles(List.of(backEnd.getActiveTurtleID()));
      results.addAll(interpreter.parseForRetVal(Arrays.copyOfRange(tokens,1,firstListLength)));
      System.out.println("Evaluation: " + results.get(results.size()-1).getReturnVal());
      System.out.println("backEnd.getActiveTurtleNumbers().toString() = " + backEnd.getActiveTurtleNumbers().toString());
      if (results.get(results.size()-1).getReturnVal() == 1) { //condition evaluates to true
        results.addAll(interpreter.parseCommandsList(Arrays.copyOfRange(tokens,2+firstListLength,firstListLength+secondListLength+1)));
      }
    backEnd.setActiveTurtles(originalActives);
    returnVal = results.get(results.size()-1).getReturnVal();
    results.add(backEnd.makeCommandResult(returnVal,firstListLength+secondListLength+2));
    return results;
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }

  @Override
  public boolean runsPerTurtle() {
    return true;
  }
}
