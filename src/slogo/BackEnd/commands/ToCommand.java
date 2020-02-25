package slogo.BackEnd.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import slogo.BackEnd.AltCommand;
import slogo.BackEnd.BackEndInternal;
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
      BackEndInternal backEnd) throws ParseException {
    int programCounter = 0;
    String cmdName = tokens[programCounter];
    int numVars = backEnd.distanceToEndBracket(Arrays.copyOfRange(tokens,programCounter+2,tokens.length)) - 1;
    List<String> toVars = new ArrayList<>();
    for (programCounter = 2; programCounter < 2 + numVars; programCounter ++) {
      toVars.add(tokens[programCounter].substring(1));
    }
    programCounter += 2;
    int numCommands = backEnd.distanceToEndBracket(Arrays.copyOfRange(tokens,programCounter,tokens.length)) - 1;
    backEnd.setUserCommand(cmdName,toVars,Arrays.copyOfRange(tokens,programCounter,programCounter + numCommands));
    return List.of(new CommandResult(1.0,programCounter + numCommands + 1));
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    List<String> vars = new ArrayList<>();
    int numVars = SLogoBackEnd.distanceToEndBracketStatic(Arrays.copyOfRange(tokenList,1,tokenList.length));
    Collections.addAll(vars,Arrays.copyOfRange(tokenList,0,numVars));
    return vars;
  }
}
