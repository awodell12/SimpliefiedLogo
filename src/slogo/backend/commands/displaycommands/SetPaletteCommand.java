package slogo.backend.commands.displaycommands;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import slogo.CommandResult;
import slogo.backend.BackEndInternal;
import slogo.backend.Command;
import slogo.backend.CommandResultBuilder;
import slogo.backend.ParseException;

public class SetPaletteCommand implements Command {

    public static final int NUM_ARGS = 4;
    public static final int NUM_VARS = 0;

    @Override
    public int getNumArgs() {
        return NUM_ARGS;
    }

    @Override
    public int getNumVars() {
        return NUM_VARS;
    }

    @Override
    public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
                                       BackEndInternal backEnd) throws ParseException {
        int idx = (int) Math.round(arguments.get(0));
        int r = (int) Math.round(arguments.get(1)); int g = (int) Math.round(arguments.get(2)); int b = (int) Math.round(arguments.get(3));
        System.out.println("Palette color index " + idx + " set to: R:" + r + " G:" + g + " B:" + b);
        List<Integer> paletteColor = List.of(idx, r, g, b);
        CommandResultBuilder builder = new CommandResultBuilder(backEnd.getTurtles().get(0).getHeading(), backEnd.getTurtles().get(0).getPosition(), backEnd.getActiveTurtleNumbers());
        builder.retVal(idx);
        builder.setColor(paletteColor);
        //FIXME Throw exception if this is not a valid color (as specified in command description), perhaps use a helper method to determine if color valid
        //TODO add to palette in back end?
        return List.of(builder.buildCommandResult());
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }
}
