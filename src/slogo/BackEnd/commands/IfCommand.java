package slogo.BackEnd.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import slogo.BackEnd.AltCommand;
import slogo.BackEnd.ParseException;
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
  public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
      SLogoBackEnd backEnd) throws ParseException {
    double returnVal = 0;
    List<CommandResult> results = new ArrayList<>();
    int listLength = backEnd.distanceToEndBracket(Arrays.copyOfRange(tokens,0,tokens.length));
    if (arguments.get(0) != 0) {
      System.out.println("IF evaluated to TRUE");
      results.addAll(backEnd.parseCommandsList(Arrays.copyOfRange(tokens,1,listLength-1)));
      returnVal = results.get(results.size()-1).getReturnVal();
    }
    else {
      System.out.println("IF evaluated to FALSE");
    }
    results.add(new CommandResult(returnVal,listLength));
    return results;
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }
}
