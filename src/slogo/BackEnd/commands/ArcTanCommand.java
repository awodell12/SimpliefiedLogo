package slogo.BackEnd.commands;

import java.util.List;
import slogo.BackEnd.AltCommand;
import slogo.BackEnd.BackEndInternal;
import slogo.BackEnd.ParseException;
import slogo.CommandResult;

public class ArcTanCommand implements AltCommand {

    @Override
    public int getNumArgs() {
        return 1;
    }

    @Override
    public int getNumVars() {
        return 0;
    }

    @Override
    public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
                                       BackEndInternal backEnd) throws ParseException {
        System.out.println("Found arctan of " + arguments.get(0));
        return List.of(backEnd.makeCommandResult(Math.atan(arguments.get(0) * Math.PI/180),0));
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }
}