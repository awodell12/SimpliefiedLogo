package slogo.BackEnd.commands;

import java.util.Arrays;
import java.util.List;
import slogo.BackEnd.AltCommand;
import slogo.BackEnd.SLogoBackEnd;
import slogo.CommandResult;

public class DoTimesCommand implements AltCommand {

  @Override
  public int getNumArgs() {
    return 1;
  }

  @Override
  public int getNumVars() {
    return 1;
  }

  @Override
  public CommandResult execute(List<Double> arguments,  List<String> vars, String[] tokens, SLogoBackEnd backEnd) {
    System.out.println("Beginning DOTIMES Loop.");
    double limit = arguments.get(0);
    String var = vars.get(1);
    double returnVal = 0;
    int listLength = backEnd.distanceToEndBracket(Arrays.copyOfRange(tokens,2,tokens.length));
    for (double i = 1; i <= limit; i ++) {
      backEnd.setVariable(var,i);
      returnVal = backEnd.parseCommandsList(Arrays.copyOfRange(tokens,2,listLength+1)).getReturnVal();
    }
    System.out.println("Ending DOTIMES Loop.");
    return new CommandResult(returnVal, backEnd.distanceToEndBracket(Arrays.copyOfRange(tokens,2,tokens.length))+2);
  }
//
//  public CommandResult execute(List<Double> arguments,  List<String> vars, String[] tokens, SLogoBackEnd backEnd) {
//    System.out.println("Beginning FOR Loop");
//    double start = arguments.get(0);
//    double end = arguments.get(1);
//    double increment = arguments.get(2);
//    System.out.println("Start is " + start);
//    System.out.println("End is " + end);
//    System.out.println("Increment is " + increment);
//    //This is because the first 'var' was actually the opening bracket.
//    String var = vars.get(1);
//    double returnVal = 0;
//    int listLength = backEnd.distanceToEndBracket(Arrays.copyOfRange(tokens,2,tokens.length));
//    for (double i = start; i <= end; i += increment) {
//      backEnd.setVariable(var,i);
//      returnVal = backEnd.parseCommandsList(Arrays.copyOfRange(tokens,2,listLength+1)).getReturnVal();
//    }
//    System.out.println("Ending FOR Loop.");
//    return new CommandResult(returnVal, backEnd.distanceToEndBracket(Arrays.copyOfRange(tokens,3,tokens.length))+2);
//  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return List.of(tokenList[0],tokenList[1].substring(1));
  }
}
