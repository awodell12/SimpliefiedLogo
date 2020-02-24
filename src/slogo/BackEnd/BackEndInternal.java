package slogo.BackEnd;
import java.util.Collection;
import java.util.List;

/**
 * Classes inside the view with access to the model may assume that it has these functions, which allow
 * the model to store state information that affects language processing.
 */
public interface BackEndInternal {

  /**
   * Creates or overrides a variable (:[a-zA-Z_]+) to store a double value so that
   * the next time it is referenced and used by a command that value is substituted in for it.
   * @param name The name of the variable, excluding the colon.
   * @param value The value to be stored in the variable.
   */
  void setVariable(String name, double value);

  /**
   * Returns the value of a variable if it has been set, otherwise throws an error
   * that is caught by the parser so that it can report that it doesn't know about
   * the variable.
   * @param name The name of the variable (excluding the colon) to look for and return
   *             the value of.
   */
  double getVariable(String name) throws ParseException;

  /**
   * Removes all (variable-name,value) pairings previously set in the model. Should be hidden from
   * overzealous Compsci 201 students.
   */
  void clearVariables();

  /**
   * Adds a user-defined command (Which was defined using TO-END) to the list of user-defined
   * commands so that the command can be recognized and parsed. Throws an exception when the user
   * attempts to overwrite a command that cannot be modified, like Forward.
   * Assumes that the user-defined command is able to run when passed in.
   * @param name The name to give the user-defined command to be used in the future.
   * @param parameters The local variables that are set by calls to the command and are used within
   *                   the command to execute it.
   * @param commands The contents of the command, which are parsed and executed whenever the
   *               command is called.
   */
  void setUserCommand(String name, List<String> parameters, String[] commands) throws ParseException;

  /**
   * Returns a list of named arguments whose size is equal to the number of
   * arguments for the user defined command.
   * @param name The name of the command to find arguments for.
   * @return The argument list of the command.
   */
  Collection<String> getUserCommandArgs(String name);

  /**
   * Returns the String contents of a user-defined command that were used to define the command
   * in the first place.
   * @param name The name of the String to return the script for.
   * @return The code that makes up the command's definition, as a String, referencing its
   * named constants.
   */
  Collection<String> getUserCommandScript(String name);

  /**
   * @return A list of all Turtles in this instance of the model.
   */
  List<Turtle> getTurtles();

  /**
   * Set a new list of turtles.
   * @param t A list of Turtles that will be tied to this model.
   */
  void setTurtles(List<Turtle> t);

  /**
   * Remove all turtles from the model.
   */
  void clearTurtles();
}
