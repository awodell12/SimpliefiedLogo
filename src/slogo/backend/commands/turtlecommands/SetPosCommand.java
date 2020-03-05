package slogo.backend.commands.turtlecommands;

import java.util.List;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.CommandResult;
import slogo.backend.CommandResultBuilder;
import slogo.backend.Interpreter;

public class SetPosCommand implements Command {

    private static final int NUM_ARGS = 2;
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
    public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
        BackEndInternal backEnd, Interpreter interpreter) {
        List<Double> prevPos = backEnd.getTurtles().get(0).getPosition();
        double retVal = backEnd.getTurtles().get(0).setPos(arguments.get(0), arguments.get(1));
        //System.out.println("Turtle now at x=" + arguments.get(0) ", y=" + arguments.get(1));
        CommandResultBuilder builder = backEnd.startCommandResult(retVal);
        builder.setPathStart(prevPos);
        return List.of(builder.buildCommandResult());
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }
}
