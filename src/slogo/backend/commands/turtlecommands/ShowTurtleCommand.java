package slogo.backend.commands.turtlecommands;

import java.util.List;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.CommandResult;
import slogo.backend.CommandResultBuilder;
import slogo.backend.Turtle;

public class ShowTurtleCommand extends TurtleCommand {

    private static final int NUM_ARGS = 0;
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
        System.out.println("Turtle is unhidden");
        myRetVal = 1;
        turtle.setVisible(true);
    }

    @Override
    protected CommandResult createCommandResult(Turtle turtle, List<Double> arguments,
        List<Double> prevPos, BackEndInternal backEnd) {
        CommandResultBuilder builder = backEnd.startCommandResult(turtle.getId(),myRetVal);
        builder.setVisible(turtle.getVisible());
        return builder.buildCommandResult();
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }
}
