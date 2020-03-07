package slogo.backend;

import java.util.List;
import slogo.CommandResult;

/**
 * Carries out the functionality of individual instructions
 * in the programming language used (SLogo in this case).
 * Implemented/Extended by concrete commands like "Forward".
 */
public abstract class Command {

  protected int NUM_ARGS = 0;
  protected int NUM_VARS = 0;

  /*
  public Command(){
    NUM_ARGS = 0;
    NUM_ARGS = 0;
  } */

  /**
   * Carries out the command, changing the relevant data in the model according to the
   * effects of the command. Currently, commands can effect the turtle, the paths list,
   * the variables, and the user defined commands. The effects are documented and returned.
   * @return The effects on the model of this individual command, bundled into a CommandResult
   * instance.
   */
  public abstract List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
      BackEndInternal backEnd, Interpreter interpreter)
      throws ParseException;

  public abstract List<String> findVars(String[] tokenList);

  public int getNumArgs(){ return NUM_ARGS;}

  public int getNumVars(){ return NUM_VARS; }

  public boolean runsPerTurtle() {
    return false;
  }

  public int getTokensParsed(String[] tokens) {
    return 0;
  }
}
