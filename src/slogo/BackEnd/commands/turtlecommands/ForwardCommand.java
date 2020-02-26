package slogo.BackEnd.commands.turtlecommands;

import java.util.List;
import slogo.BackEnd.Command;
import slogo.BackEnd.BackEndInternal;
import slogo.BackEnd.ParseException;
import slogo.CommandResult;

public class ForwardCommand implements Command {

  public static final int NUM_ARGS = 1;
  public static final String COMMAND_NAME = "Forward";

  @Override
  public int getNumArgs() {
    return 1;
  }

  @Override
  public int getNumVars() {
    return 0;
  }

  @Override
  public List<CommandResult> execute(List<Double> arguments,  List<String> vars, String[] tokens, BackEndInternal backEnd)
      throws ParseException {
    List<Double> prevPos = backEnd.getTurtles().get(0).getPosition();
    backEnd.getTurtles().get(0).moveForward(arguments.get(0));
    System.out.println("Moved forward by " + arguments.get(0));
    System.out.println("Turtle is now at x=" +  backEnd.getTurtles().get(0).getX() + " y=" + backEnd.getTurtles().get(0).getY());
    return List.of(backEnd.makeCommandResult(arguments.get(0),0,prevPos,"000000"));
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }

  @Override
  public String toString() {
    return COMMAND_NAME;
  }
}
