package slogo.FrontEnd;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;

/**
 * The TurtleView class encapsulated the view of the turtle and allows for the current state of the
 * Turtle and its taken paths to be displayed to the user.
 */
public class TurtleView {
    private final ImageView myTurtle;
    private final Group myGroup;
    private Color myPenColor = Color.BLACK;
    private final Rectangle myBackground;
    private boolean isPenUp = false;
    private boolean myTurtleVisibility = true;
    private static final double TURTLE_SIZE = 50;
    private final double myWidth;
    private final double myHeight;

    public TurtleView(Pane layout, double width, double height){
        myWidth = width;
        myHeight = height;
        String myTurtleImage = "slogo/FrontEnd/Resources/turtle.png";
        myTurtle = new ImageView(myTurtleImage);
        myTurtle.setPreserveRatio(true);
        myTurtle.setCache(true);
        myTurtle.setFitWidth(TURTLE_SIZE);
        myTurtle.setFitHeight(TURTLE_SIZE);
        resetTurtle();
        myBackground = new Rectangle(width, height);
        myBackground.setFill(Color.WHITE);
        myBackground.setStyle("-fx-border-color: black");
        myGroup = new Group();
        myGroup.getChildren().add(myBackground);
        myGroup.getChildren().add(myTurtle);
        layout.getChildren().add(myGroup);
    }

    /**
     * set the pen color (color of paths)
     * @param color color to set to
     */
    protected void setPenColor(Color color){
        myPenColor = color;
    }

    /**
     * Updates the position of the turtle in the Display to the desired set of coordinates. Offsets so that 0, 0 is center of screen
     * @param x the new x coordinate for the turtle
     * @param y the new y coordinate for the turtle
     */
    protected void setTurtlePosition(double x, double y){
        myTurtle.setX(x + myWidth/2 - TURTLE_SIZE/2);
        myTurtle.setY(y + myHeight/2 - TURTLE_SIZE/2);
        while(myTurtle.getX() > myWidth) myTurtle.setX(myTurtle.getX()-myWidth);
        while(myTurtle.getY() > myHeight) myTurtle.setY(myTurtle.getY() - myHeight);
    }

    /**
     * Rotate the turtle
     * @param angle angle to rotate by
     */
    protected void setTurtleHeading(double angle){
        myTurtle.setRotate(angle);
    }

    /**
     * Change the background color of the turtleView section of the display to a desired color.
     * Will be controlled through a lambda from a drop-down menu
     * @param color the desired color for the background
     */
    protected void setBackGroundColor(Color color){
        myBackground.setFill(color);
    }

    /**
     * moves the turtle back to home position
     */
    protected void resetTurtle(){
        myTurtle.setY(myHeight/2 - TURTLE_SIZE/2);
        myTurtle.setX(myWidth/2 - TURTLE_SIZE/2);
        myTurtle.setRotate(0);
    }

    /**
     * This method tells the TurtleView it must add a new path to the display by drawing it, in the color
     * specified by the Path object
     * @param path the path the turtle took from its previous location to its current
     */
    protected void addPath(Path path){
        if(!isPenUp) {
            path.setFill(myPenColor);
            myGroup.getChildren().add(path);
        }
    }

    /**
     * Removes all of the taken paths displayed on the screen by clearing the display and returns the turtle
     * back to its starting position
     */
    protected void clearPaths(){
        int numChildren = myGroup.getChildren().size();
        if(numChildren >= 2) myGroup.getChildren().remove(1, numChildren);
        if(myTurtleVisibility) myGroup.getChildren().add(myTurtle);
        resetTurtle();
    }

    /**
     * change the turtle image. Image is determined by the file chooser
     * @param newTurtleImage image object to set the turtle image to
     */
    protected void setTurtleImage(Image newTurtleImage){
        myTurtle.setImage(newTurtleImage);
    }

    protected void setTurtleVisibility(boolean turtleVisibility) {
        if(turtleVisibility && !myTurtleVisibility){
            myGroup.getChildren().add(myTurtle);
        }
        else if(!turtleVisibility && myTurtleVisibility){
            myGroup.getChildren().remove(myTurtle);
        }
        myTurtleVisibility = turtleVisibility;
    }

    /**
     * set the pen status (up/down)
     * @param up true if the pen is up
     */
    protected void setIsPenUp(boolean up){
        isPenUp = up;
    }
}
