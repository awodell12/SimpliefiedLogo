package slogo.BackEnd.commands;

import java.util.ArrayList;
import java.util.List;
import slogo.BackEnd.AltCommand;
import slogo.CommandResult;
import slogo.BackEnd.SLogoBackEnd;

public class SetPosCommand implements AltCommand {

    public static final int NUM_ARGS = 2;
    public static final String COMMAND_NAME = "SetPosition";

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
        backEnd.getTurtles().get(0).setPos(arguments.get(0), arguments.get(1));
        //System.out.println("Turtle now at x=" + arguments.get(0) ", y=" + arguments.get(1));
        return List.of(new CommandResult(arguments.get(0),0));
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }
}