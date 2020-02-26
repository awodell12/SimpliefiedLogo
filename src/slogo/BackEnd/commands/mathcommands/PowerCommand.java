package slogo.BackEnd.commands.mathcommands;

import java.util.List;
import slogo.BackEnd.Command;
import slogo.BackEnd.BackEndInternal;
import slogo.BackEnd.ParseException;
import slogo.CommandResult;

public class PowerCommand implements Command {

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
        System.out.println("Executed " + arguments.get(0) + " to the power of " + arguments.get(1));
        return List.of(backEnd.makeCommandResult(Math.pow(arguments.get(0), arguments.get(1)),0));
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }
}
