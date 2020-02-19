package slogo;

/**
 * The methods that allow interacting with the turtle and causing it
 * to execute its behavior. Allows multiple turtles.
 */
public interface Turtle {

  /**
   * @return The X coordinate of the turtle (0 is the middle)
   */
  public double getX();

  /**
   * @return The Y coordinate of the turtle (1 is the middle)
   */
  public double getY();

  /**
   * Set the X and Y position of the turtle. (0,0) is the center (depending on how the view draws
   * things)
   * @param x The new X position of the turtle
   * @param y The new Y position of the turtle
   */
  public void setPos(double x, double y);

  /**
   * Moves the turtle forward in the direction of its heading.
   * @param distance The distance to move in the heading direction
   */
  public void moveForward(double distance);

  /**
   * Moves the turtle backward in the direction of its heading.
   * @param distance The distance to move in the opposite of the heading direction
   */
  public void moveBack(double distance);

  /**
   * Turns heading clockwise by the specified number of degrees
   * @param degrees The number of degrees to turn clockwise. Enter a negative number to turn
   *                counter-clockwise.
   */
  public void turn(double degrees);

  /**
   * Sets the turtle to point in the given direction.
   * @param direction The direction to face, in degrees clockwise from facing upward.
   */
  public void setHeading(double direction);

  /**
   * @return The direction the turtle is facing, in degrees clockwise from directly up/north.
   */
  public double getHeading();

  /**
   * @return Whether the pen is up (inactive). When the pen is up, no paths are drawn.
   */
  public boolean getPenUp();

  /**
   * @param up True for setting the pen to up/inactive, False to set the pen to down/active.
   */
  public void setPenUp(boolean up);

}
