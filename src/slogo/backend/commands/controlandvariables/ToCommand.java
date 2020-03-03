package slogo.backend.commands.controlandvariables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import slogo.backend.BackEndUtil;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.backend.ParseException;
import slogo.backend.SLogoBackEnd;
import slogo.CommandResult;

public class ToCommand implements Command {

  private static final int NUM_ARGS = 0;
  private static final int NUM_VARS = 0;

  @Override
  public int getNumArgs() {
    return NUM_ARGS;
  }

  @Override
  public int getNumVars() {
    return NUM_VARS;
  }

  @Override
  public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
      BackEndInternal backEnd) throws ParseException {
    int programCounter = 0;
    String cmdName = tokens[programCounter];
    int numVars = BackEndUtil.distanceToEndBracket(Arrays.copyOfRange(tokens,programCounter+2,tokens.length)) - 1;
    List<String> toVars = new ArrayList<>();
    for (programCounter = 2; programCounter < 2 + numVars; programCounter ++) {
      toVars.add(tokens[programCounter].substring(1));
    }
    programCounter += 2;
    if (programCounter >= tokens.length) {
      throw new ParseException("Expected instructions in brackets ([ ... ])");
    }
    int numCommands = BackEndUtil.distanceToEndBracket(Arrays.copyOfRange(tokens,programCounter,tokens.length)) - 1;
    String[] commandTokens = Arrays.copyOfRange(tokens,programCounter,programCounter + numCommands);
    backEnd.setUserCommand(cmdName,toVars,commandTokens);
    return List.of(backEnd.makeCommandResult(1.0,programCounter+numCommands+1,cmdName,concatStringArray(commandTokens)));
  }

  private String concatStringArray(String[] tokens) {
    StringBuffer sb = new StringBuffer();
    for (String string : tokens) {
      sb.append(string + " ");
    }
    return sb.toString();
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    List<String> vars = new ArrayList<>();
    int numVars = new BackEndUtil().distanceToEndBracket(Arrays.copyOfRange(tokenList,1,tokenList.length));
    Collections.addAll(vars,Arrays.copyOfRange(tokenList,0,numVars));
    return vars;
  }
}
