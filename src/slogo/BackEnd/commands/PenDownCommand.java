package slogo.BackEnd.commands;

import java.util.ArrayList;
import java.util.List;
import slogo.BackEnd.AltCommand;
import slogo.CommandResult;
import slogo.BackEnd.SLogoBackEnd;

public class PenDownCommand implements AltCommand {

    public static final int NUM_ARGS = 0;
    public static final String COMMAND_NAME = "PenDown";

    @Override
    public int getNumArgs() {
        return NUM_ARGS;
    }

    @Override
    public int getNumVars() {
        return 0;
    }

    @Override
    public List<CommandResult> execute(List<Double> arguments,  List<String> vars, String[] tokens, SLogoBackEnd backEnd) {
        int retVal = backEnd.getTurtles().get(0).setPenUp(false);
        //System.out.println("Pen is down");
        return List.of(new CommandResult(retVal,0));
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }
}
