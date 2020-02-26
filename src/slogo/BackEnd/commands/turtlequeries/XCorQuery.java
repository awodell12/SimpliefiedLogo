package slogo.BackEnd.commands.turtlequeries;

import java.util.List;
import slogo.BackEnd.Command;
import slogo.BackEnd.BackEndInternal;
import slogo.CommandResult;

public class XCorQuery implements Command {

    public static final int NUM_ARGS = 0;
    public static final int NUM_VARS = 0;
    public static final String COMMAND_NAME = "XCoordinate";

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
        return List.of(backEnd.makeCommandResult(backEnd.getTurtles().get(0).getX(),0));
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
