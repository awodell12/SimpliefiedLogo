package slogo;

import javafx.scene.paint.Color;
import javafx.scene.shape.Path;

/**
 * The TurtleView class encapsulated the view of the turtle and allows for the current state of the
 * Turtle and its taken paths to be displayed to the user.
 */
public class TurtleView {

  /**
   * Updates the position of the turtle in the Display to the desired set of coordinates.
   * @param x the new x coordinate for the turtle
   * @param y the new y coordinate for the turtle
   */
  void setTurtlePosition(double x, double y){};

  /**
   *
   * @param angle
   */
  void setTurtleHeading(double angle){};

  /**
   * Change the background color of the turtleView section of the display to a desired color.
   * Will be controlled through a lambda from a drop-down menu
   * @param color the desired color for the background
   */
  void setBackGroundColor(Color color){};

  /**
   * This method tells the TurtleView it must add a new path to the display by drawing it, in the color
   * specified by the Path object
   * @param path the path the turtle took from its previous location to its current
   */
  void addPath(Path path){};

  /**
   * Removes all of the taken paths displayed on the screen by clearing the display and returns the turtle
   * back to its starting position
   */
  void clearPaths(){};
}
