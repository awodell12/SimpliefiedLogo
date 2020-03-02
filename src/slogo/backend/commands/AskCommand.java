package slogo.backend.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import slogo.CommandResult;
import slogo.backend.BackEndInternal;
import slogo.backend.BackEndUtil;
import slogo.backend.Command;
import slogo.backend.CommandResultBuilder;
import slogo.backend.ParseException;
import slogo.backend.Turtle;

public class AskCommand implements Command {

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
    int programCounter = 1;
    int numTokens = new BackEndUtil().distanceToEndBracket(
        Arrays.copyOfRange(tokens,programCounter,tokens.length)) - 1;
    List<Integer> activeTurtleNums = new ArrayList<>();
    for (programCounter = 1; programCounter <= numTokens; programCounter ++) {
      activeTurtleNums.add(Integer.parseInt(tokens[programCounter]));
    }
    for (Integer num : activeTurtleNums) {
      System.out.println(num);
    }
    List<Integer> originalActives = backEnd.getActiveTurtleNumbers();
    backEnd.setActiveTurtles(activeTurtleNums);
    List<CommandResult> results = new ArrayList<>();
    int totalParsed = new BackEndUtil().distanceToEndBracket(Arrays.copyOfRange(tokens,numTokens+3,tokens.length))-1;
    for (String token : Arrays.copyOfRange(tokens,numTokens+3,tokens.length)) {
      System.out.println(token);
    }
    results.addAll(backEnd.parseCommandsList(Arrays.copyOfRange(tokens,numTokens+3,tokens.length)));
    CommandResultBuilder builder = backEnd.startCommandResult(backEnd.getTurtles().get(0).getHeading(),backEnd.getTurtles().get(0).getPosition());
    backEnd.setActiveTurtles(originalActives);
    builder.retVal(0);
    builder.tokensParsed(numTokens + totalParsed +4);
    results.add(builder.buildCommandResult());
    return results;
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }
}
