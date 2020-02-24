package slogo.BackEnd.commands;

import java.util.Arrays;
import java.util.List;
import slogo.BackEnd.AltCommand;
import slogo.CommandResult;
import slogo.BackEnd.SLogoBackEnd;

public class IfElseCommand implements AltCommand {

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
    double returnVal;
    int firstListLength = backEnd.distanceToEndBracket(Arrays.copyOfRange(tokens,1,tokens.length));
    int secondListLength = backEnd.distanceToEndBracket(Arrays.copyOfRange(tokens,firstListLength+2,tokens.length));
    System.out.println("firstListLength = " + firstListLength);
    System.out.println("secondListLength = " + secondListLength);
    if (arguments.get(0) != 0) {
      System.out.println("IF evaluated to TRUE");
      returnVal = backEnd.parseCommandsList(Arrays.copyOfRange(tokens,1,firstListLength)).getReturnVal();
    }
    else {
      System.out.println("IF evaluated to FALSE");
      returnVal = backEnd.parseCommandsList(Arrays.copyOfRange(tokens,2+firstListLength,firstListLength+secondListLength+1)).getReturnVal();
    }
    return new CommandResult(returnVal,firstListLength+secondListLength+2);
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }
}
