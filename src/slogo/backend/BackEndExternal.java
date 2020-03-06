package slogo.backend;

import slogo.CommandResult;

import java.util.List;

/**
 * The public side of the model of the SLogo parser. View and controller classes that interact
 * with the model can only assume that it has the public methods listed.
 */
public interface BackEndExternal {

  /**
   * Takes in a script (a block of SLogo for this project) as a String, interprets the script,
   * updates the back end state accordingly, and produces a List of CommandResults that describes
   * what affect each command had on the turtle, the paths, the variables, and the user defined
   * commands.
   * @param script The SLogo script to be parsed. Other implementations could use other languages
   *               and return other CommandResult types.
   * @return A List of CommandResults describing the effect of each command run, in the order they
   * were run.
   */
  List<CommandResult> parseScript(String script);

  List<CommandResult> redo();

  List<CommandResult> undo();

  List<CommandResult> loadLibraryFile(String filePath);

  void writeLibraryFile(String filePath);

  void applyChanger(Changer changer);
}
