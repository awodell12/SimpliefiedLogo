package slogo.BackEnd.commands;

import java.util.Arrays;
import java.util.List;
import slogo.BackEnd.AltCommand;
import slogo.CommandResult;
import slogo.BackEnd.SLogoBackEnd;

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
    double returnVal = 0;
    int listLength = backEnd.distanceToEndBracket(Arrays.copyOfRange(tokens,0,tokens.length));
    if (arguments.get(0) != 0) {
      System.out.println("IF evaluated to TRUE");
      returnVal = backEnd.parseCommandsList(Arrays.copyOfRange(tokens,1,listLength-1)).getReturnVal();
    }
    else {
      System.out.println("IF evaluated to FALSE");
    }
    return new CommandResult(returnVal, listLength-2);
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }
}
