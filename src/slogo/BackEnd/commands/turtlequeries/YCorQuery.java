package slogo.BackEnd.commands.turtlequeries;

import java.util.List;
import slogo.BackEnd.AltCommand;
import slogo.BackEnd.BackEndInternal;
import slogo.CommandResult;

public class YCorQuery implements AltCommand {

    public static final int NUM_ARGS = 0;
    public static final int NUM_VARS = 0;
    public static final String COMMAND_NAME = "YCoordinate";

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
        return List.of(backEnd.makeCommandResult(backEnd.getTurtles().get(0).getY(),0));
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
