package slogo.backend.commands.turtlecommands;

import java.util.List;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.CommandResult;
import slogo.backend.CommandResultBuilder;
import slogo.backend.Turtle;

public class BackCommand implements Command {

    private static final int NUM_ARGS = 1;
    private static final String COMMAND_NAME = "Forward";

    @Override
    public int getNumArgs() {
        return NUM_ARGS;
    }

    @Override
    public int getNumVars() {
        return 0;
    }

    @Override
    public List<CommandResult> execute(List<Double> arguments,  List<String> vars, String[] tokens, BackEndInternal backEnd) {
        int id = backEnd.getActiveTurtleID();
        System.out.println("id = " + id);
        System.out.println(backEnd.getTurtles(List.of(0)));
        System.out.println("Got your turtles");
        Turtle turtle = backEnd.getTurtles(List.of(id)).get(0);
        List<Double> prevPos = turtle.getPosition();
        turtle.moveBack(arguments.get(0));
        System.out.println("Moved BACK by " + arguments.get(0));
        System.out.println("Turtle is now at x=" +  turtle.getX() + " y=" + turtle.getY());
        CommandResultBuilder builder = backEnd.startCommandResult(turtle.getHeading(),turtle.getPosition());
            builder.setTurtleID(id);
            builder.retVal(arguments.get(0));
            builder.tokensParsed(0);
            builder.setPathColor(0);
            builder.setPathStart(prevPos);
        return List.of(builder.buildCommandResult());
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }

    @Override
    public String toString() {
        return COMMAND_NAME;
    }

    @Override
    public boolean runsPerTurtle() {
        return true;
    }
}
