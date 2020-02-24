package slogo.BackEnd.commands;

import java.util.Arrays;
import java.util.List;
import slogo.BackEnd.AltCommand;
import slogo.BackEnd.ParseException;
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
  public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
      SLogoBackEnd backEnd) throws ParseException {
    double returnVal;
    List<CommandResult> results;
    int firstListLength = backEnd.distanceToEndBracket(Arrays.copyOfRange(tokens,1,tokens.length));
    int secondListLength = backEnd.distanceToEndBracket(Arrays.copyOfRange(tokens,firstListLength+2,tokens.length));
    System.out.println("firstListLength = " + firstListLength);
    System.out.println("secondListLength = " + secondListLength);
    if (arguments.get(0) != 0) {
      System.out.println("IF evaluated to TRUE");
      results = backEnd.parseCommandsList(Arrays.copyOfRange(tokens,2,firstListLength));
      returnVal = results.get(results.size()-1).getReturnVal();
    }
    else {
      System.out.println("IF evaluated to FALSE");
      results = backEnd.parseCommandsList(Arrays.copyOfRange(tokens,2+firstListLength,firstListLength+secondListLength+1));
      returnVal = results.get(results.size()-1).getReturnVal();
    }
    results.add(new CommandResult(returnVal,firstListLength+secondListLength+2));
    return results;
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }
}
