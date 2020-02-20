package slogo.commands;

import java.util.List;
import slogo.AltCommand;
import slogo.BackEndExternal;
import slogo.Command;
import slogo.CommandResult;
import slogo.SLogoBackEnd;

public class ForwardCommand implements AltCommand {

  public static final int NUM_ARGS = 1;
  public static final String COMMAND_NAME = "Forward";

  @Override
  public int getNumArgs() {
    return 1;
  }

  @Override
  public int getNumVars() {
    return 0;
  }

  @Override
  public CommandResult execute(List<Double> arguments,  List<String> vars, String[] tokens, SLogoBackEnd backEnd) {
    System.out.println("Moved forward by " + arguments.get(0));
    return new CommandResult(arguments.get(0),0);
  }

  @Override
  public String toString() {
    return COMMAND_NAME;
  }
}
