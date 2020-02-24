package slogo.BackEnd.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import slogo.BackEnd.AltCommand;
import slogo.BackEnd.ParseException;
import slogo.CommandResult;
import slogo.BackEnd.SLogoBackEnd;

public class ForLoopCommand implements AltCommand {

  @Override
  public int getNumArgs() {
    return 3;
  }

  @Override
  public int getNumVars() {
    return 1;
  }

  @Override
  public List<CommandResult> execute(List<Double> arguments,  List<String> vars, String[] tokens, SLogoBackEnd backEnd)
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
    int listLength = backEnd.distanceToEndBracket(Arrays.copyOfRange(tokens,2,tokens.length));
    for (double i = start; i <= end; i += increment) {
      backEnd.setVariable(var,i);
      results.addAll(backEnd.parseCommandsList(Arrays.copyOfRange(tokens,2,listLength+1)));
      returnVal = results.get(results.size()-1).getReturnVal();    }
    System.out.println("Ending FOR Loop.");
    results.add(new CommandResult(returnVal,listLength+2));
    return results;
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return List.of(tokenList[0],tokenList[1].substring(1));
  }
}
