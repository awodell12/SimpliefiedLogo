package slogo.backend.commands.turtlecommands;

import java.util.ArrayList;
import java.util.List;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.backend.ParseException;
import slogo.CommandResult;
import slogo.backend.Turtle;

public class ForwardCommand implements Command {

  private static final int NUM_ARGS = 1;
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
  public List<CommandResult> execute(List<Double> arguments,  List<String> vars, String[] tokens, BackEndInternal backEnd)
      throws ParseException {
    List<CommandResult> results = new ArrayList<>();
    for (Turtle turtle : backEnd.getActiveTurtles()) {
      List<Double> prevPos = backEnd.getTurtles().get(0).getPosition();
      backEnd.getTurtles().get(0).moveForward(arguments.get(0));
      System.out.println("Moved forward by " + arguments.get(0));
      System.out.println("Turtle is now at x=" +  backEnd.getTurtles().get(0).getX() + " y=" + backEnd.getTurtles().get(0).getY());
      results.add(backEnd.makeCommandResult(arguments.get(0),0,prevPos,0));
    }
    if (results.isEmpty()) {
      results.add(backEnd.makeCommandResult(arguments.get(0),0));
    }
    return results;
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }

}
