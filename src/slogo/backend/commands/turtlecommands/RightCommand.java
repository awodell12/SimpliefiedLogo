package slogo.backend.commands.turtlecommands;

import java.util.List;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.backend.CommandResultBuilder;
import slogo.backend.ParseException;
import slogo.CommandResult;
import slogo.backend.Turtle;

public class RightCommand extends TurtleCommand implements Command {

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
  protected void applyToTurtle(Turtle turtle, List<Double> args) {
    turtle.turn(args.get(0));
    System.out.println("Turning right by " + args.get(0) + " degrees.");
    System.out.println("Heading is now " + turtle.getHeading() + " degrees.");
  }

  @Override
  protected CommandResult createCommandResult(Turtle turtle, List<Double> arguments,
      List<Double> prevPos, BackEndInternal backEnd) {
    CommandResultBuilder builder = backEnd.startCommandResult(turtle.getId(),arguments.get(0));
    return builder.buildCommandResult();
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }
}
