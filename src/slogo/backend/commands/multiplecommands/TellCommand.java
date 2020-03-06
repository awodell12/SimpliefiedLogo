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

public class TellCommand implements Command {

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
      BackEndInternal backEnd, Interpreter interpreter) throws ParseException {
    double lastTurtleNum = 0;
    int programCounter = 1;
    int numTokens = BackEndUtil.distanceToEndBracket(Arrays.copyOfRange(tokens,programCounter,tokens.length)) - 1;
    List<Integer> activeTurtleNums = new ArrayList<>();
    for (programCounter = 1; programCounter <= numTokens; programCounter ++) {
      activeTurtleNums.add(Integer.parseInt(tokens[programCounter]));
      lastTurtleNum = Double.parseDouble(tokens[programCounter]);
    }
    backEnd.setActiveTurtles(activeTurtleNums);
    List<CommandResult> results = new ArrayList<>();
    for (Turtle newlyActive : backEnd.getActiveTurtles()) {
      CommandResultBuilder builder = backEnd.startCommandResult(
          newlyActive.getHeading(),
          newlyActive.getPosition(),
              newlyActive.getVisible());
      builder.setRetVal(lastTurtleNum);
      builder.setTokensParsed(programCounter+1);
      builder.activeTurtleIDs(activeTurtleNums);
      builder.setTurtleID(newlyActive.getId());
      results.add(builder.buildCommandResult());
    }
    if (results.isEmpty()) {
      CommandResultBuilder builder = backEnd.startCommandResult(
      backEnd.getTurtles().get(0).getHeading(),
      backEnd.getTurtles().get(0).getPosition(),
              backEnd.getTurtles().get(0).getVisible());
      builder.setRetVal(lastTurtleNum);
      builder.setTokensParsed(programCounter+1);
      results.add(builder.buildCommandResult());
    }
    return results;
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }
}
