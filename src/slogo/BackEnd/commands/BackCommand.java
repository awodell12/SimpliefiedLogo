package slogo.BackEnd.commands;

import java.util.ArrayList;
import java.util.List;
import slogo.BackEnd.AltCommand;
import slogo.CommandResult;
import slogo.BackEnd.SLogoBackEnd;

public class BackCommand implements AltCommand {

    public static final int NUM_ARGS = 1;
    public static final String COMMAND_NAME = "Forward";

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
        backEnd.getTurtles().get(0).moveBack(arguments.get(0));
        //System.out.println("Moved forward by " + arguments.get(0));
        return List.of(new CommandResult(arguments.get(0),0));
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }

    @Override
    public String toString() {
        return COMMAND_NAME;
    }
}
