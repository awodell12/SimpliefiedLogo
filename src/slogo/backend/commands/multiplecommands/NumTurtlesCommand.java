package slogo.backend.commands.multiplecommands;

import slogo.CommandResult;
import slogo.backend.BackEndInternal;
import slogo.backend.Command;
import slogo.backend.Interpreter;
import slogo.backend.ParseException;

import java.util.List;

public class NumTurtlesCommand extends Command {
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
    public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
                                       BackEndInternal backEnd, Interpreter interpreter) throws ParseException {
        return List.of(backEnd.makeCommandResult(backEnd.getTurtles().size(),0));
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }
}
