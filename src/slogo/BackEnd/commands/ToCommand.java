package slogo.BackEnd.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import slogo.BackEnd.AltCommand;
import slogo.BackEnd.ParseException;
import slogo.BackEnd.SLogoBackEnd;
import slogo.CommandResult;

public class ToCommand implements AltCommand {

  @Override
  public int getNumArgs() {
    return 0;
  }

  @Override
  public int getNumVars() {
    return 0;
  }

  @Override
  public CommandResult execute(List<Double> arguments, List<String> vars, String[] tokens,
      SLogoBackEnd backEnd) {
    try {
      int tokensParsed = backEnd.handleUserCommandCreation(tokens);
      return new CommandResult(1.0,tokensParsed);
    } catch (ParseException e) {
      System.out.println("Tried to redefine primitive.");
      return new CommandResult(0.0, 0);
    }
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    List<String> vars = new ArrayList<>();
    int numVars = SLogoBackEnd.distanceToEndBracketStatic(Arrays.copyOfRange(tokenList,1,tokenList.length));
    //We can compress this down, but that destroys the logical difference between the 'name' var and
    // the 'variables' vars.
    vars.add(tokenList[0]);
    for (int i = 0; i < numVars; i ++) {
      vars.add(tokenList[i]);
    }
    return vars;
  }
}
