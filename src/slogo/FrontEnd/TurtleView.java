package slogo.FrontEnd;


import java.net.URL;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;

/**
 * The TurtleView class encapsulated the view of the turtle and allows for the current state of the
 * Turtle and its taken paths to be displayed to the user.
 */
public class TurtleView {
    private ImageView myTurtle;
    private Canvas myCanvas; //don't think canvas is the right thing for this

    public TurtleView(Pane layout, double width, double height){
        String myTurtleImage = "slogo/FrontEnd/Resources/turtle.jpg";
        myTurtle = new ImageView(myTurtleImage);
        myTurtle.setPreserveRatio(true);
        myTurtle.setFitWidth(50);
        myTurtle.setCache(true);
        myCanvas = new Canvas(width, height);
        myCanvas.getGraphicsContext2D().drawImage(myTurtle.getImage(), myTurtle.getX(), myTurtle.getY());
        layout.getChildren().add(myCanvas);
        // add some listener/binding to canvas to update whenever the turtle moves?
        //  myTurtle.imageProperty().addListener((observable, oldValue, newValue) -> myCanvas.getGraphicsContext2D().drawImage(newValue));
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

    void setTurtleImage(Image newTurtleImage){
        myTurtle.setImage(newTurtleImage);
        myCanvas.getGraphicsContext2D().drawImage(myTurtle.getImage(), myTurtle.getX(), myTurtle.getY());
        // TODO: delete the previous turtle image
    }
}
