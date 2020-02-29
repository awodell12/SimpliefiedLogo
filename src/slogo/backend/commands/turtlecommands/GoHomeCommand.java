package slogo.backend.commands.turtlecommands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.CommandResult;

public class GoHomeCommand implements Command {

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
    public List<CommandResult> execute(List<Double> arguments,  List<String> vars, String[] tokens, BackEndInternal backEnd) {
        List<Double> prevPos = backEnd.getTurtles().get(0).getPosition();
        double retVal = backEnd.getTurtles().get(0).setPos(0, 0);
        System.out.println("Went home \n " +
                "Turtle is now at x=" +  backEnd.getTurtles().get(0).getX() + " y=" + backEnd.getTurtles().get(0).getY());

        CommandResult clearResult = new CommandResult(
                retVal,
                0,
                0,
                backEnd.getTurtles().get(0).getHeading(),
                List.of(backEnd.getTurtles().get(0).getX(),
                        backEnd.getTurtles().get(0).getY()),
                prevPos,
                0,
                null,
                0,
                null,
                null,
                false,
                backEnd.getTurtles().get(0).getPenUp(),
                backEnd.getTurtles().get(0).getVisible(),
                true,
                0,
                Arrays.asList(0, 0, 0),
                1.0,
                Collections.singletonList(0),
                0,
                0,
                ""
        );
        return List.of(clearResult);
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }
}
