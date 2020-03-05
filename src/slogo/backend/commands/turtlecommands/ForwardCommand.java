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
//    List<CommandResult> results = new ArrayList<>();
//    for (Turtle turtle : backEnd.getActiveTurtles()) {
//      List<Double> prevPos = backEnd.getTurtles().get(0).getPosition();
//      turtle.moveForward(arguments.get(0));
//      System.out.println("Turtle " + turtle.getId() + " moved forward by " + arguments.get(0) + " and is now at x=" +  turtle.getX() + " y=" + turtle.getY());
//      CommandResultBuilder builder = backEnd.startCommandResult(turtle.getHeading(),turtle.getPosition());
//      builder.setTurtleID(turtle.getId());
//      builder.setRetVal(arguments.get(0));
//      builder.setPathStart(prevPos);
//      builder.setPathColor(backEnd.getPathColor());
//      results.add(builder.buildCommandResult());
//    }
//    if (results.isEmpty()) {
//      results.add(backEnd.makeCommandResult(arguments.get(0),0));
//    }
//    return results;

    Integer id = backEnd.getActiveTurtleID();
    System.out.println("id = " + id);

    if (id == null) {
      System.out.println("ID was null");
      return List.of(backEnd.startCommandResult(0).buildCommandResult());
    }
    else {
      Turtle turtle = backEnd.getTurtles(List.of(id)).get(0);
      List<Double> prevPos = turtle.getPosition();
      turtle.moveForward(arguments.get(0));
      System.out.println("Moved FORWARD by " + arguments.get(0));
      System.out.println("Turtle " + turtle.getId() + " is now at x=" +  turtle.getX() + " y=" + turtle.getY());
      CommandResultBuilder builder = backEnd.startCommandResult(turtle.getId(),arguments.get(0));
      builder.setPathStart(prevPos);
      return List.of(builder.buildCommandResult());
    }
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }

  @Override
  public boolean runsPerTurtle() {
    return true;
  }
}
