package slogo.BackEnd.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
  public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
      SLogoBackEnd backEnd) throws ParseException {
    int tokensParsed = backEnd.handleUserCommandCreation(tokens);
    return List.of(new CommandResult(1.0,tokensParsed));
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    List<String> vars = new ArrayList<>();
    int numVars = SLogoBackEnd.distanceToEndBracketStatic(Arrays.copyOfRange(tokenList,1,tokenList.length));
    Collections.addAll(vars,Arrays.copyOfRange(tokenList,0,numVars));
    return vars;
  }
}
