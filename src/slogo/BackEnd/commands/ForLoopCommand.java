package slogo.BackEnd.commands;

import java.util.Arrays;
import java.util.List;
import slogo.BackEnd.AltCommand;
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
  public CommandResult execute(List<Double> arguments,  List<String> vars, String[] tokens, SLogoBackEnd backEnd) {
    System.out.println("Beginning FOR Loop");
    double start = arguments.get(0);
    double end = arguments.get(1);
    double increment = arguments.get(2);
    System.out.println("Start is " + start);
    System.out.println("End is " + end);
    System.out.println("Increment is " + increment);
    String var = vars.get(0);
    double returnVal = 0;
    for (double i = start; i < end; i += increment) {
      backEnd.setVariable(var,i);
      returnVal = backEnd.parseTokens(Arrays.copyOfRange(tokens,3,tokens.length)).getReturnVal();
    }
    System.out.println("Ending FOR Loop.");
    return new CommandResult(returnVal, backEnd.distanceToEndBracket(Arrays.copyOfRange(tokens,3,tokens.length))+2);
  }
}
