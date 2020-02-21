package slogo.BackEnd;

import java.util.List;
import slogo.BackEnd.SLogoBackEnd;
import slogo.CommandResult;

/**
 * Carries out the functionality of individual instructions
 * in the programming language used (SLogo in this case).
 * Implemented/Extended by concrete commands like "Forward".
 */
public interface AltCommand {

  public int getNumArgs();

  public int getNumVars();

  /**
   * Carries out the command, changing the relevant data in the model according to the
   * effects of the command. Currently, commands can effect the turtle, the paths list,
   * the variables, and the user defined commands. The effects are documented and returned.
   * @return The effects on the model of this individual command, bundled into a CommandResult
   * instance.
   */
  public CommandResult execute(List<Double> arguments, List<String> vars, String[] tokens, SLogoBackEnd backEnd);
}
