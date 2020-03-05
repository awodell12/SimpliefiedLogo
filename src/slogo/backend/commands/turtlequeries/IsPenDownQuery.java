package slogo.backend.commands.turtlequeries;

import java.util.List;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.CommandResult;
import slogo.backend.CommandResultBuilder;

public class IsPenDownQuery implements Command {

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
        int retVal = (backEnd.getPenUp()) ? 0 : 1;
        CommandResultBuilder builder = backEnd.startCommandResult(backEnd.getTurtles().get(0).getHeading(), backEnd.getTurtles().get(0).getPosition());
        builder.setRetVal(retVal);
        return List.of(builder.buildCommandResult());
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }

}
