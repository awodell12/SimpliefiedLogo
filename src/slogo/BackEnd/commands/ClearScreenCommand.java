package slogo.BackEnd.commands;

import java.util.ArrayList;
import java.util.List;
import slogo.BackEnd.AltCommand;
import slogo.CommandResult;
import slogo.BackEnd.SLogoBackEnd;

public class ClearScreenCommand implements AltCommand {

    public static final int NUM_ARGS = 0;
    public static final String COMMAND_NAME = "ClearScreen";

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
        double retVal = backEnd.getTurtles().get(0).setPos(0, 0);
        //TODO un-comment this when paths are implemented
        //backend.getPaths().clear();
        //System.out.println("Screen cleared");
        return List.of(new CommandResult(retVal,0));
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }
}
