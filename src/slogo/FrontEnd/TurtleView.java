package slogo.FrontEnd;

import java.util.ResourceBundle;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.List;

/**
 * The TurtleView class encapsulated the view of the turtle and allows for the current state of the
 * Turtle and its taken paths to be displayed to the user.
 */
public class TurtleView extends Group{
    private static final String RESOURCE_LOCATION = "slogo/FrontEnd/Resources.config";
    private static final ResourceBundle myResources = ResourceBundle.getBundle(RESOURCE_LOCATION);
    private final ImageView myTurtle;
    private Color myPenColor = Color.BLACK;
    private final Rectangle myBackground;
    private boolean isPenUp = false;
    private boolean myTurtleVisibility = true;
    private static final double TURTLE_SIZE = 50;
    private static final double SIGNIFICANT_DIFFERENCE = 0.001;
    private final double myWidth;
    private final double myHeight;
    private final double xOffset;
    private final double yOffset;

    public TurtleView(double width, double height){
        myWidth = width - TURTLE_SIZE;
        myHeight = height - TURTLE_SIZE;
        xOffset = myWidth/2 - TURTLE_SIZE/2;
        yOffset = myHeight/2 - TURTLE_SIZE/2;
        String myTurtleImage = "slogo/FrontEnd/Resources/turtle.png";
        myTurtle = new ImageView(myTurtleImage);
        myTurtle.setPreserveRatio(true);
        myTurtle.setCache(true);
        myTurtle.setFitWidth(TURTLE_SIZE);
        myTurtle.setFitHeight(TURTLE_SIZE);
        resetTurtle();
        myBackground = new Rectangle(width, height);
        myBackground.setFill(Color.WHITE);
        myBackground.setStroke(Color.BLACK);
        myBackground.setStrokeType(StrokeType.OUTSIDE);
        myBackground.setStyle("-fx-border-color: black");
        this.getChildren().add(myBackground);
        this.getChildren().add(myTurtle);
    }

    /**
     * set the pen color (color of paths)
     * @param color color to set to
     */
    protected void setPenColor(Color color){ myPenColor = color; }

    /**
     * Updates the position of the turtle in the Display to the desired set of coordinates. Offsets so that 0, 0 is center of screen
     * @param x the new x coordinate for the turtle
     * @param y the new y coordinate for the turtle
     */
    protected void setTurtlePosition(double x, double y){
        myTurtle.setX(boundX(x));
        myTurtle.setY(boundY(y));
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
     * This method tells the TurtleView it must add a new path to the display by drawing it, in the color
     * specified by the Path object
     * @param startPos the turtle previous location
     * @param turtlePos the turtle current position
     */
    protected void addPath(Point2D startPos, Point2D turtlePos){
        if(!isPenUp) {
            Path path = new Path();
            double turtleX = boundX(turtlePos.getX()) + TURTLE_SIZE/2;
            double turtleY = boundY(turtlePos.getY()) + TURTLE_SIZE/2;
            double startX = boundX(startPos.getX())+TURTLE_SIZE/2;
            double startY = boundY(startPos.getY())+TURTLE_SIZE/2;
            if(!almostEqual(turtleX-startX, turtlePos.getX()-startPos.getX()) || !almostEqual(turtleY-startY, turtlePos.getY()-startPos.getY())){
                return; // don't draw the path if the turtle is wrapping around in this step
            }
            MoveTo moveTo = new MoveTo(turtleX , turtleY);
            LineTo line = new LineTo(startX, startY);
            path.getElements().add(moveTo);
            path.getElements().add(line);
            path.setStroke(myPenColor);
            this.getChildren().add(path);
        }
    }

    /**
     * Removes all of the taken paths displayed on the screen by clearing the display and returns the turtle
     * back to its starting position
     */
    protected void clearPaths(){
        int numChildren = this.getChildren().size();
        if(numChildren >= 2) this.getChildren().remove(1, numChildren);
        if(myTurtleVisibility) this.getChildren().add(myTurtle);
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
            this.getChildren().add(myTurtle);
        }
        else if(!turtleVisibility && myTurtleVisibility){
            this.getChildren().remove(myTurtle);
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

    /**
     * moves the turtle back to home position
     */
    private void resetTurtle(){
        myTurtle.setY(yOffset);
        myTurtle.setX(xOffset);
        myTurtle.setRotate(0);
    }

    private double boundX(double x){
        x = x + xOffset;
        while(x > myWidth) x -= myWidth;
        while(x < 0) x += myWidth;
        return x;
    }

    private double boundY(double y){
        y = y + yOffset;
        while(y > myHeight) y -= myHeight;
        while(y < 0) y += myHeight;
        return y;
    }

    private boolean almostEqual(double a, double b){
        return Math.abs(a-b) <= SIGNIFICANT_DIFFERENCE;
    }
}
