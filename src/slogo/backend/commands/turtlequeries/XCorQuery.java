package slogo.backend.commands.turtlequeries;

import java.util.List;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.CommandResult;
import slogo.backend.CommandResultBuilder;
import slogo.backend.Turtle;
import slogo.backend.commands.turtlecommands.TurtleCommand;

public class XCorQuery extends TurtleCommand{


    @Override
    protected void applyToTurtle(Turtle turtle, List<Double> args) {
        myRetVal = turtle.getX();
    }

    @Override
    protected CommandResult createCommandResult(Turtle turtle, List<Double> arguments,
                                                List<Double> prevPos, BackEndInternal backEnd) {
        CommandResultBuilder builder = backEnd.startCommandResult(turtle.getId(), myRetVal);
        return builder.buildCommandResult();
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
