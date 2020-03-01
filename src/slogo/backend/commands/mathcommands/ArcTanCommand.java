package slogo.backend.commands.mathcommands;

import java.util.List;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.backend.ParseException;
import slogo.CommandResult;

public class ArcTanCommand implements Command {

    private static final int NUM_ARGS = 1;
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
                                       BackEndInternal backEnd) throws ParseException {
        System.out.println("Found arctan of " + arguments.get(0));
        return List.of(backEnd.makeCommandResult(Math.atan(arguments.get(0) * Math.PI/180),0));
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }
}