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
    String var = vars.get(0);
    double returnVal = 0;
    for (double i = 1; i <= limit; i ++) {
      backEnd.setVariable(var,i);
      returnVal = backEnd.parseTokens(Arrays.copyOfRange(tokens,3,tokens.length)).getReturnVal();
    }
    System.out.println("Ending DOTIMES Loop.");
    return new CommandResult(returnVal, backEnd.distanceToEndBracket(Arrays.copyOfRange(tokens,3,tokens.length))+2);
  }
}
