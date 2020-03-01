package slogo.backend.commands.turtlecommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.CommandResult;
import slogo.backend.CommandResultBuilder;

public class ClearScreenCommand implements Command {

    private static final int NUM_ARGS = 0;
    private static final int NUM_VARS = 0;

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
        double retVal = backEnd.getTurtles().get(0).setPos(0, 0);
        backEnd.getTurtles().get(0).setHeading(0);
        System.out.println("Went home, cleared paths \n " +
                "Turtle is now at x=" +  backEnd.getTurtles().get(0).getX() + " y=" + backEnd.getTurtles().get(0).getY());
        CommandResultBuilder clearResult = new CommandResultBuilder(backEnd.getTurtles().get(0).getHeading(),
            List.of(backEnd.getTurtles().get(0).getX(),
                backEnd.getTurtles().get(0).getY()));
        return List.of(clearResult.buildCommandResult());
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }
}