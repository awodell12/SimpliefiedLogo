package slogo.BackEnd.commands;

import java.util.Arrays;
import java.util.List;
import slogo.BackEnd.AltCommand;
import slogo.BackEnd.SLogoBackEnd;
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
  public CommandResult execute(List<Double> arguments,  List<String> vars, String[] tokens, SLogoBackEnd backEnd) {
    System.out.println("Beginning REPEAT Loop.");
    double numLoops = arguments.get(0);
    double returnVal = 0;
    for (double i = 0; i < numLoops; i ++) {
      returnVal = backEnd.parseTokens(Arrays.copyOfRange(tokens,2,tokens.length)).getReturnVal();
    }
    System.out.println("Ending REPEAT Loop.");
    return new CommandResult(returnVal, backEnd.distanceToEndBracket(Arrays.copyOfRange(tokens,3,tokens.length))+2);
  }
}
