package slogo.BackEnd.commands.turtlecommands;

import java.util.List;
import slogo.BackEnd.Command;
import slogo.BackEnd.BackEndInternal;
import slogo.CommandResult;

public class GoHomeCommand implements Command {

    public static final int NUM_ARGS = 0;
    public static final String COMMAND_NAME = "Home";

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
                "000000",
                null,
                0,
                null,
                null,
                false,
                backEnd.getTurtles().get(0).getPenUp(),
                backEnd.getTurtles().get(0).getVisible(),
                true
        );
        return List.of(clearResult);
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }
}
