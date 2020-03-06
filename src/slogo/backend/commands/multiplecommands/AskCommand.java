package slogo.backend.commands.multiplecommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import slogo.CommandResult;
import slogo.backend.BackEndInternal;
import slogo.backend.BackEndUtil;
import slogo.backend.Command;
import slogo.backend.CommandResultBuilder;
import slogo.backend.Interpreter;
import slogo.backend.ParseException;
import slogo.backend.Turtle;

public class AskCommand extends Command {

  private static final int START_BUFFER = 3;
  private static final int NUM_TOKENS = 4;
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
    int programCounter = 1;
    int numTokens = BackEndUtil.distanceToEndBracket(
        Arrays.copyOfRange(tokens,programCounter,tokens.length)) - 1;
    List<Integer> activeTurtleNums = new ArrayList<>();
    for (programCounter = 1; programCounter <= numTokens; programCounter ++) {
      activeTurtleNums.add(Integer.parseInt(tokens[programCounter]));
    }
    List<Integer> originalActives = backEnd.getActiveTurtleNumbers();
    backEnd.setActiveTurtles(activeTurtleNums);

    List<CommandResult> results = new ArrayList<>();
    String[] tokensToParse = Arrays.copyOfRange(tokens,numTokens+ START_BUFFER,tokens.length);
    int totalParsed = BackEndUtil.distanceToEndBracket(tokensToParse)-1;
    for (Turtle newlyActive : backEnd.getActiveTurtles()) {
      CommandResultBuilder builder = backEnd.startCommandResult(
          newlyActive.getHeading(),
          newlyActive.getPosition(),
              newlyActive.getVisible());
      builder.setRetVal(0);
      builder.setTokensParsed(programCounter+1);
      builder.activeTurtleIDs(activeTurtleNums);
      builder.setTurtleID(newlyActive.getId());
      results.add(builder.buildCommandResult());
    }
    results.addAll(interpreter.parseCommandsList(tokensToParse));
    backEnd.setActiveTurtles(originalActives);
    CommandResultBuilder builder = backEnd.startCommandResult(backEnd.getTurtles().get(0).getHeading(),backEnd.getTurtles().get(0).getPosition(), backEnd.getTurtles().get(0).getVisible());
    builder.setRetVal(0);
    builder.setTokensParsed(numTokens + totalParsed +NUM_TOKENS);
    results.add(builder.buildCommandResult());
    return results;
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }
}
