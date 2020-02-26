package slogo.BackEnd.commands.turtlecommands;

import java.util.List;
import slogo.BackEnd.Command;
import slogo.BackEnd.BackEndInternal;
import slogo.CommandResult;

public class SetPosCommand implements Command {

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
    public List<CommandResult> execute(List<Double> arguments,  List<String> vars, String[] tokens, BackEndInternal backEnd) {
        List<Double> prevPos = backEnd.getTurtles().get(0).getPosition();
        double retVal = backEnd.getTurtles().get(0).setPos(arguments.get(0), arguments.get(1));
        //System.out.println("Turtle now at x=" + arguments.get(0) ", y=" + arguments.get(1));
        return List.of(backEnd.makeCommandResult(retVal,0, prevPos, "000000"));
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }
}
