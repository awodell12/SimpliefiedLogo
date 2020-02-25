package slogo.FrontEnd;


import java.net.URL;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;

/**
 * The TurtleView class encapsulated the view of the turtle and allows for the current state of the
 * Turtle and its taken paths to be displayed to the user.
 */
public class TurtleView {
    private ImageView myTurtle;
    private Group myGroup;
    private Color myPenColor = Color.BLACK;
    private Rectangle myBackground;
    private boolean isPenUp = false;
    private boolean myTurtleVisibility = true;


    public TurtleView(Pane layout, double width, double height){
        String myTurtleImage = "slogo/FrontEnd/Resources/turtle.png";
        myTurtle = new ImageView(myTurtleImage);
        myTurtle.setPreserveRatio(true);
        myTurtle.setFitWidth(50);
        myTurtle.setCache(true);
        myTurtle.setFitWidth(width);
        myTurtle.setFitHeight(height);
        myBackground = new Rectangle(width, height);
        myBackground.setFill(Color.WHITE);
        myBackground.setStyle("-fx-border-color: black");
        myGroup = new Group();
        myGroup.getChildren().add(myBackground);
        myGroup.getChildren().add(myTurtle);
        layout.getChildren().add(myGroup);
        // add some listener/binding to canvas to update whenever the turtle moves?
        //  myTurtle.imageProperty().addListener((observable, oldValue, newValue) -> myGroup.getGraphicsContext2D().drawImage(newValue));
    }

    /**
     * set the pen color (color of paths)
     * @param color color to set to
     */
    public void setPenColor(Color color){
        myPenColor = color;
    }

    /**
     * Updates the position of the turtle in the Display to the desired set of coordinates.
     * @param x the new x coordinate for the turtle
     * @param y the new y coordinate for the turtle
     */
    void setTurtlePosition(double x, double y){
        myTurtle.setX(x);
        myTurtle.setY(y);
    };

    /**
     *
     * @param angle
     */
    void setTurtleHeading(double angle){
        myTurtle.setRotate(myTurtle.getRotate() + angle);
    };

    /**
     * Change the background color of the turtleView section of the display to a desired color.
     * Will be controlled through a lambda from a drop-down menu
     * @param color the desired color for the background
     */
    public void setBackGroundColor(Color color){
        myBackground.setFill(color);
    };

    /**
     * moves the turtle back to home position
     */
    public void resetTurtle(){
        myTurtle.setY(0);
        myTurtle.setX(0);
        myTurtle.setRotate(0);
    }

    /**
     * This method tells the TurtleView it must add a new path to the display by drawing it, in the color
     * specified by the Path object
     * @param path the path the turtle took from its previous location to its current
     */
    public void addPath(Path path){
        if(!isPenUp) {
            path.setFill(myPenColor);
            myGroup.getChildren().add(path);
        }
    };

    /**
     * Removes all of the taken paths displayed on the screen by clearing the display and returns the turtle
     * back to its starting position
     */
    public void clearPaths(){
        int numChildren = myGroup.getChildren().size();
        if(numChildren >= 2) myGroup.getChildren().remove(1, numChildren);
        if(myTurtleVisibility) myGroup.getChildren().add(myTurtle);
        resetTurtle();
    };

    /**
     * change the turtle image. Image is determined by the file chooser
     * @param newTurtleImage image object to set the turtle image to
     */
    public void setTurtleImage(Image newTurtleImage){
        myTurtle.setImage(newTurtleImage);
    }

    public void setTurtleVisibility(boolean turtleVisibility) {
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
    public void setIsPenUp(boolean up){
        isPenUp = up;
    }
}
