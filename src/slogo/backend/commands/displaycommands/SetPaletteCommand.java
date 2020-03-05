package slogo.backend.commands.displaycommands;

import java.util.List;
import slogo.CommandResult;
import slogo.backend.BackEndInternal;
import slogo.backend.Command;
import slogo.backend.CommandResultBuilder;
import slogo.backend.Interpreter;
import slogo.backend.ParseException;

public class SetPaletteCommand implements Command {

    private static final int NUM_ARGS = 4;
    private static final int NUM_VARS = 0;
    private static final String INVALID_COLOR_MESSAGE = "Color components must be non-negative integers between 0 and 256";


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
        BackEndInternal backEnd, Interpreter interpreter) throws ParseException {
        int idx = (int) Math.round(arguments.get(0));
        int r = (int) Math.round(arguments.get(1)); int g = (int) Math.round(arguments.get(2)); int b = (int) Math.round(arguments.get(3));
        System.out.println("Palette color index " + idx + " set to: R:" + r + " G:" + g + " B:" + b);
        List<Integer> paletteColor = List.of(r, g, b);
        CommandResultBuilder builder = backEnd.startCommandResult(backEnd.getTurtles().get(0).getHeading(), backEnd.getTurtles().get(0).getPosition());
        builder.setRetVal(idx);
        if(isValidColor(arguments.get(1), arguments.get(2), arguments.get(3))){
            builder.setColor(paletteColor);
            builder.setPaletteIndex(idx);
        }
        else
            builder.setErrorMessage(INVALID_COLOR_MESSAGE);
        return List.of(builder.buildCommandResult());
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }

    private boolean isValidColor(double r, double g, double b){
        return r == Math.round(r) && g == Math.round(g) && b == Math.round(b) &&
                r >= 0 && r <= 256 && g >= 0 && g <= 256 && b >= 0 && b <= 256;
    }
}
