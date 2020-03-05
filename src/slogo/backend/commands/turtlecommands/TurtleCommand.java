package slogo.backend.commands.turtlecommands;

import java.util.List;
import slogo.CommandResult;
import slogo.backend.BackEndInternal;
import slogo.backend.Command;
import slogo.backend.CommandResultBuilder;
import slogo.backend.ParseException;
import slogo.backend.Turtle;

public abstract class TurtleCommand implements Command {
  @Override
  public List<CommandResult> execute(List<Double> arguments,  List<String> vars, String[] tokens, BackEndInternal backEnd)
  throws ParseException {
    Integer id = backEnd.getActiveTurtleID();
    System.out.println("id = " + id);

    if (id == null) {
      System.out.println("ID was null");
      return List.of(backEnd.startCommandResult(0).buildCommandResult());
    }
    else {
      Turtle turtle = backEnd.getTurtles(List.of(id)).get(0);
      List<Double> prevPos = turtle.getPosition();
      applyToTurtle(turtle,arguments);
      return List.of(createCommandResult(turtle,arguments,prevPos,backEnd));
    }
  }

  protected abstract void applyToTurtle(Turtle turtle, List<Double> args);

  protected abstract CommandResult createCommandResult(Turtle turtle, List<Double> arguments,
      List<Double> prevPos, BackEndInternal backEnd);
}
