package slogo.BackEnd.commands;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import slogo.BackEnd.AltCommand;
import slogo.BackEnd.SLogoBackEnd;
import slogo.CommandResult;

public class UserCommand implements AltCommand {

  private List<String> myArguments;
  private Collection<String> myInstructions;

  public UserCommand(List<String> arguments, Collection<String> commands) {
    myArguments = arguments;
    myInstructions = commands;
  }

  @Override
  public int getNumArgs() {
    return myArguments.size();
  }

  @Override
  public int getNumVars() {
    return 0;
  }

  @Override
  public CommandResult execute(List<Double> arguments, List<String> vars, String[] tokens,
      SLogoBackEnd backEnd) {
    double returnVal;
    for (int i = 0; i < arguments.size(); i++) {
      backEnd.setVariable(myArguments.get(i),arguments.get(i));
    }
    returnVal = backEnd.parseTokens(myInstructions.toArray(new String[0])).getReturnVal();
    return new CommandResult(returnVal, 0);
  }
}
