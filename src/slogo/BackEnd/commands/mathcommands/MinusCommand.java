package slogo.BackEnd.commands.mathcommands;

import java.util.List;
import slogo.BackEnd.Command;
import slogo.BackEnd.BackEndInternal;
import slogo.BackEnd.ParseException;
import slogo.CommandResult;

public class MinusCommand implements Command {

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
        System.out.println("Negated " + arguments.get(0));
        return List.of(backEnd.makeCommandResult(-1*arguments.get(0),0));
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }
}

