package slogo.backend.commands.turtlecommands;

import java.util.List;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.CommandResult;

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
        List<Double> prevPos = backEnd.getTurtles().get(0).getPosition();
        backEnd.getTurtles().get(0).moveBack(arguments.get(0));
        System.out.println("Moved BACK by " + arguments.get(0));
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
