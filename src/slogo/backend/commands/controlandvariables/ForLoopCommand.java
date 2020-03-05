package slogo.backend.commands.controlandvariables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import slogo.backend.BackEndUtil;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.backend.Interpreter;
import slogo.backend.ParseException;
import slogo.CommandResult;

public class ForLoopCommand implements Command {

  public static final int NUM_ARGS = 3;
  public static final int NUM_VARS = 1;

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
      BackEndInternal backEnd, Interpreter interpreter)
      throws ParseException {
    System.out.println("Beginning FOR Loop");
    double start = arguments.get(0);
    double end = arguments.get(1);
    double increment = arguments.get(2);
    System.out.println("Start is " + start);
    System.out.println("End is " + end);
    System.out.println("Increment is " + increment);
    //This is because the first 'var' was actually the opening bracket.
    String var = vars.get(1);
    double returnVal = 0;
    List<CommandResult> results = new ArrayList<>();
    int listLength = BackEndUtil.distanceToEndBracket(Arrays.copyOfRange(tokens,2,tokens.length));
    for (double i = start; i <= end; i += increment) {
      backEnd.setVariable(var,i);
      results.addAll(interpreter.parseCommandsList(Arrays.copyOfRange(tokens,2,listLength+1)));
      returnVal = results.get(results.size()-1).getReturnVal();    }
    System.out.println("Ending FOR Loop.");
    results.add(backEnd.makeCommandResult(returnVal,listLength+2));
    return results;
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return List.of(tokenList[0],tokenList[1].substring(1));
  }
}
