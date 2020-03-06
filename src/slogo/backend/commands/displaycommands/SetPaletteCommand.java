package slogo.backend.commands.displaycommands;

import java.util.ArrayList;
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
    private static final int COLOR_COMPONENTS = 3;


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
        List<Integer> paletteColor = new ArrayList<>();
        List<Double> color = new ArrayList<>();
        for(int i = 1; i <= COLOR_COMPONENTS; i++){
            color.add(arguments.get(i));
            paletteColor.add((int) Math.round((arguments.get(i))));
        }
        CommandResultBuilder builder = backEnd.startCommandResult(backEnd.getTurtles().get(0).getHeading(), backEnd.getTurtles().get(0).getPosition(), backEnd.getTurtles().get(0).getVisible());
        builder.setRetVal(idx);
        if(isValidColor(color)){
            builder.setColor(paletteColor);
            builder.setPaletteIndex(idx);
            backEnd.addPaletteColor(idx, paletteColor);
        }
        else
            builder.setErrorMessage(INVALID_COLOR_MESSAGE);
        return List.of(builder.buildCommandResult());
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }

    private boolean isValidColor(List<Double> color){
        return isRGB(color) && areWholeNumbers(color);
    }

    private boolean isRGB(List<Double> color){
        for(double component : color){
            if(component < 0 || component > 256){
                return false;
            }
        }
        return true;
    }

    private boolean areWholeNumbers(List<Double> color){
        for(double component : color){
            if(Math.round(component) != component){
                return false;
            }
        }
        return true;
    }
}
