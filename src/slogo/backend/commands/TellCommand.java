package slogo.backend.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import slogo.CommandResult;
import slogo.backend.BackEndInternal;
import slogo.backend.BackEndUtil;
import slogo.backend.Command;
import slogo.backend.ParseException;

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
      BackEndInternal backEnd) throws ParseException {
    int programCounter = 1;
    int numTokens = new BackEndUtil().distanceToEndBracket(Arrays.copyOfRange(tokens,programCounter,tokens.length)) - 1;
    List<Integer> activeTurtleNums = new ArrayList<>();
    for (programCounter = 1; programCounter <= numTokens; programCounter ++) {
      activeTurtleNums.add(Integer.parseInt(tokens[programCounter]));
    }
    backEnd.setActiveTurtles(activeTurtleNums);
    return List.of(backEnd.makeCommandResult(0.0,programCounter));
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }
}
