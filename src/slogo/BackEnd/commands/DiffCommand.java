package slogo.BackEnd.commands;

import slogo.BackEnd.BackEndInternal;
import slogo.BackEnd.ParseException;
import slogo.CommandResult;

import java.util.List;

public class DiffCommand extends SumCommand {
    @Override
    public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
                                       BackEndInternal backEnd) throws ParseException {
        System.out.println("Executed difference of " + arguments.get(0) + " and " + arguments.get(1));
        return List.of(backEnd.makeCommandResult(arguments.get(0)-arguments.get(1),0));
    }
}