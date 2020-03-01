package slogo.backend.commands.turtlecommands;

import java.util.ArrayList;
import java.util.List;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.backend.CommandResultBuilder;
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
      turtle.moveForward(arguments.get(0));
      System.out.println("Turtle " + turtle.getId() + " moved forward by " + arguments.get(0) + " and is now at x=" +  turtle.getX() + " y=" + turtle.getY());
      CommandResultBuilder builder = new CommandResultBuilder(
          turtle.getId(),
          turtle.getHeading(),
          turtle.getPosition(),
          turtle.getPenUp(),
          backEnd.getActiveTurtleNumbers()
      );
      builder.retVal(arguments.get(0));
      builder.setPathStart(prevPos);
      builder.setPathColor(0); //TODO: Make this based on some data stored in the back end or passed to us.
      results.add(builder.buildCommandResult());
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
