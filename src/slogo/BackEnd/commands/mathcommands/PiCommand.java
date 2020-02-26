package slogo.BackEnd.commands.mathcommands;

import java.util.List;
import slogo.BackEnd.AltCommand;
import slogo.BackEnd.BackEndInternal;
import slogo.BackEnd.ParseException;
import slogo.CommandResult;

public class PiCommand implements AltCommand {

    @Override
    public int getNumArgs() {
        return 2;
    }

    @Override
    public int getNumVars() {
        return 0;
    }

    @Override
    public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
                                       BackEndInternal backEnd) throws ParseException {
        return List.of(backEnd.makeCommandResult(Math.PI,0));
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }
}
