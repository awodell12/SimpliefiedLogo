package slogo.backend.commands.mathcommands;

import slogo.backend.BackEndInternal;
import slogo.backend.Interpreter;
import slogo.backend.ParseException;
import slogo.CommandResult;

import java.util.List;

public class QuotientCommand extends SumCommand {
    @Override
    public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
        BackEndInternal backEnd, Interpreter interpreter) throws ParseException {
        System.out.println("Executed quotient of " + arguments.get(0) + " and " + arguments.get(1));
        return List.of(backEnd.makeCommandResult(arguments.get(0)/arguments.get(1),0));
    }
}
