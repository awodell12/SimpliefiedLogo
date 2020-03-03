package slogo.backend.commands.displaycommands;

import java.util.ArrayList;
import java.util.List;
import slogo.CommandResult;
import slogo.backend.BackEndInternal;
import slogo.backend.Command;
import slogo.backend.CommandResultBuilder;
import slogo.backend.ParseException;

public class SetPenColorCommand implements Command {

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
        int index = (int) Math.round(arguments.get(0));
        backEnd.setPathColor(index);
        System.out.println("Changed pen color to index " + index);
        CommandResultBuilder builder = backEnd.startCommandResult(backEnd.getTurtles().get(0).getHeading(), backEnd.getTurtles().get(0).getPosition());
        builder.retVal(index);
        //TODO error handling if this is not a valid index or not an integer
        builder.setPathColor(backEnd.getPathColor());
        return List.of(builder.buildCommandResult());
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }
}
