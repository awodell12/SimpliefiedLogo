package slogo.BackEnd.commands.mathcommands;

import java.util.List;
import slogo.BackEnd.AltCommand;
import slogo.BackEnd.BackEndInternal;
import slogo.BackEnd.ParseException;
import slogo.CommandResult;

public class NatLogCommand implements AltCommand {

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
        System.out.println("Found natural log of " + arguments.get(0));
        return List.of(backEnd.makeCommandResult(Math.log(arguments.get(0)),0));
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }
}
