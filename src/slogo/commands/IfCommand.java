package slogo.commands;

import java.util.Arrays;
import java.util.List;
import slogo.AltCommand;
import slogo.CommandResult;
import slogo.SLogoBackEnd;

public class IfCommand implements AltCommand {

  @Override
  public int getNumArgs() {
    return 1;
  }

  @Override
  public int getNumVars() {
    return 0;
  }

  @Override
  public CommandResult execute(List<Double> arguments, List<String> vars, String[] tokens,
      SLogoBackEnd backEnd) {
    if (arguments.get(0) != 0) {
      System.out.println("IF evaluated to TRUE");
      backEnd.parseTokens(Arrays.copyOfRange(tokens,2,tokens.length));
    }
    else {
      System.out.println("IF evaluated to FALSE");
    }
    return new CommandResult(0, backEnd.distanceToEndBracket(Arrays.copyOfRange(tokens,2,tokens.length))+1);
  }
}
