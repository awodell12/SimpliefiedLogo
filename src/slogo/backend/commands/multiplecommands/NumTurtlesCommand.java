package slogo.backend.commands.multiplecommands;

import slogo.CommandResult;
import slogo.backend.BackEndInternal;
import slogo.backend.Command;
import slogo.backend.Interpreter;
import slogo.backend.ParseException;

import java.util.List;

public class NumTurtlesCommand implements Command {
    @Override
    public int getNumArgs() {
        return 0;
    }

    @Override
    public int getNumVars() {
        return 0;
    }

    @Override
    public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
                                       BackEndInternal backEnd, Interpreter interpreter) throws ParseException {
        return List.of(backEnd.makeCommandResult(backEnd.getTurtles().size(),0));
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }
}
