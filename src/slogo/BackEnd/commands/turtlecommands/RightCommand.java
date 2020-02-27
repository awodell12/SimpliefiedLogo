package slogo.BackEnd.commands.turtlecommands;

import java.util.List;
import slogo.BackEnd.Command;
import slogo.BackEnd.BackEndInternal;
import slogo.BackEnd.ParseException;
import slogo.CommandResult;

public class RightCommand implements Command {

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
  public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens, BackEndInternal backEnd)
      throws ParseException {
    backEnd.getTurtles().get(0).turn(arguments.get(0));
    System.out.println("Turning right by " + arguments.get(0) + " degrees.");
    System.out.println("Heading is now " + backEnd.getTurtles().get(0).getHeading() + " degrees.");
    return List.of(backEnd.makeCommandResult(arguments.get(0),0));
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }
}
