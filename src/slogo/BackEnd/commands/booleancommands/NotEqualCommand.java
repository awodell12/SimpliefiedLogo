package slogo.BackEnd.commands.booleancommands;

import java.util.List;
import slogo.BackEnd.Command;
import slogo.BackEnd.BackEndInternal;
import slogo.BackEnd.ParseException;
import slogo.CommandResult;

public class NotEqualCommand implements Command {

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
        System.out.println("Checking equality of " + arguments.get(0) + " and " + arguments.get(1));
        double retVal = 0;
        if(arguments.get(0) != arguments.get(1)){ retVal = 1;}
        return List.of(backEnd.makeCommandResult(retVal,0));
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }
}
