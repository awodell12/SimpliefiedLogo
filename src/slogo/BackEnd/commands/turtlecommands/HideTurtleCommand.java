package slogo.BackEnd.commands.turtlecommands;

import java.util.List;
import slogo.BackEnd.AltCommand;
import slogo.BackEnd.BackEndInternal;
import slogo.BackEnd.Command;
import slogo.CommandResult;

public class HideTurtleCommand implements AltCommand {

    public static final int NUM_ARGS = 0;
    public static final String COMMAND_NAME = "HideTurtle";

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
        int retVal = backEnd.getTurtles().get(0).setVisible(false);
        System.out.println("Turtle is hidden");
        return List.of(backEnd.makeCommandResult(retVal, 0));
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }
}
