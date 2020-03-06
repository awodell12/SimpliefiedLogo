package slogo.backend.commands.controlandvariables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import slogo.backend.BackEndUtil;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.backend.CommandFactory;
import slogo.backend.CommandResultBuilder;
import slogo.backend.Interpreter;
import slogo.backend.ParseException;
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
      BackEndInternal backEnd, Interpreter interpreter) throws ParseException {
    //TODO: Find out why adding an empty instruction doesn't parse the correct number of tokens.
    int programCounter = 0;
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
    backEnd.setUserCommand(tokens[0],toVars,commandTokens);
    CommandResultBuilder builder = backEnd.startCommandResult(1.0);
    builder.setTokensParsed(programCounter+numCommands+1);
    return List.of(builder.buildCommandResult());
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    List<String> vars = new ArrayList<>();
    int numVars = BackEndUtil.distanceToEndBracket(Arrays.copyOfRange(tokenList,1,tokenList.length));
    Collections.addAll(vars,Arrays.copyOfRange(tokenList,0,numVars));
    return vars;
  }
}
