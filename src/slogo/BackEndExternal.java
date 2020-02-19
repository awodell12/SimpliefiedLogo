package slogo;

import java.util.List;

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
}
