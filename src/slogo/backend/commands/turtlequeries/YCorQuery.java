package slogo.backend.commands.turtlequeries;

import java.util.List;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.CommandResult;

public class YCorQuery implements Command {

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
        return List.of(backEnd.makeCommandResult(backEnd.getTurtles().get(0).getY(),0));
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }

}
