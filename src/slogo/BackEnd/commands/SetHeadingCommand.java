package slogo.BackEnd.commands;

import java.util.List;
import slogo.BackEnd.AltCommand;
import slogo.BackEnd.BackEndInternal;
import slogo.CommandResult;

public class SetHeadingCommand implements AltCommand {

    public static final int NUM_ARGS = 1;
    public static final String COMMAND_NAME = "SetHeading";

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
        double retVal = backEnd.getTurtles().get(0).setHeading(arguments.get(0));
        System.out.println("Heading is now " + backEnd.getTurtles().get(0).getHeading() + " degrees.");
        return List.of(backEnd.makeCommandResult(retVal,0));
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }
}
