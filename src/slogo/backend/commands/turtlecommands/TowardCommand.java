package slogo.backend.commands.turtlecommands;

import java.util.List;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.CommandResult;
import slogo.backend.CommandResultBuilder;
import slogo.backend.Turtle;

public class TowardCommand extends TurtleCommand {

    public TowardCommand(){
        NUM_ARGS = 2;
        NUM_VARS = 0;
    }
    
    @Override
    protected void applyToTurtle(slogo.backend.Turtle turtle, List<Double> args) {
        myRetVal = turtle.moveTowards(args.get(0), args.get(1));
    }

    @Override
    protected CommandResult createCommandResult(Turtle turtle, List<Double> arguments,
                                                List<Double> prevPos, BackEndInternal backEnd) {
        CommandResultBuilder builder = backEnd.startCommandResult(turtle.getId(), myRetVal);
        builder.setPathStart(prevPos);
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

