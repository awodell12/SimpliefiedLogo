package slogo.BackEnd.commands.turtlecommands;

import java.util.List;
import slogo.BackEnd.Command;
import slogo.BackEnd.BackEndInternal;
import slogo.CommandResult;

public class PenUpCommand implements Command {

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
        int retVal = backEnd.getTurtles().get(0).setPenUp(true);
        //System.out.println("Pen is up");
        return List.of(backEnd.makeCommandResult(retVal,0));
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }
}
