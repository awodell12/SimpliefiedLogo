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
    double numLoops = arguments.get(0);
    double returnVal = 0;
    int listLength = backEnd.distanceToEndBracket(Arrays.copyOfRange(tokens,1,tokens.length));
    for (double i = 1; i <= numLoops; i ++) {
      backEnd.setVariable("repcount",numLoops);
      returnVal = backEnd.parseCommandsList(Arrays.copyOfRange(tokens,1,listLength)).getReturnVal();
    }
    return new CommandResult(returnVal, listLength+1);
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }
}
