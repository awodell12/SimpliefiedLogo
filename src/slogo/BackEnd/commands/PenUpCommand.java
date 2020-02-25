package slogo.BackEnd.commands;

import java.util.ArrayList;
import java.util.List;
import slogo.BackEnd.AltCommand;
import slogo.CommandResult;
import slogo.BackEnd.SLogoBackEnd;

public class PenUpCommand implements AltCommand {

    public static final int NUM_ARGS = 0;
    public static final String COMMAND_NAME = "PenUp";

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
        int retVal = backEnd.getTurtles().get(0).setPenUp(true);
        //System.out.println("Pen is up");
        return List.of(new CommandResult(retVal,0));
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }
}
