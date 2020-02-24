package slogo.BackEnd.commands;

import java.util.Arrays;
import java.util.List;
import slogo.BackEnd.AltCommand;
import slogo.BackEnd.Command;
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
      List<CommandResult> results = backEnd.parseCommandsList(Arrays.copyOfRange(tokens,2,listLength+1));
      returnVal = results.get(results.size()-1).getReturnVal();
    }
    System.out.println("Ending DOTIMES Loop.");
    return new CommandResult(returnVal, backEnd.distanceToEndBracket(Arrays.copyOfRange(tokens,2,tokens.length))+2);
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return List.of(tokenList[0],tokenList[1].substring(1));
  }
}
