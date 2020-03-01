package slogo.frontEnd;

import java.util.*;
import java.util.function.Consumer;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

/**
 * The TurtleView class encapsulated the view of the turtle and allows for the current state of the
 * Turtle and its taken paths to be displayed to the user.
 */
public class TurtleView extends Group{
    private static final String RESOURCE_LOCATION = "slogo/frontEnd/Resources.config";
    private static final ResourceBundle myResources = ResourceBundle.getBundle(RESOURCE_LOCATION);
    private static final Image myActiveTurtleImage = new Image("slogo/frontEnd/Resources/activeTurtle.png");
    private static final Image myInactiveTurtleImage = new Image("slogo/frontEnd/Resources/turtle.png");
    private static final double TURTLE_SIZE = 50;
    private static final double SIGNIFICANT_DIFFERENCE = 0.001;
    private static final double UNHIGHLIGHTED_OPACITY = 0.1;
    private static final double HIGHLIGHTED_OPACITY = 1.0;

    private final Map<Integer, Turtle> myTurtles = new HashMap<>();
    private Map<Integer, Point2D> unalteredTurtlePositions = new HashMap<>();
    private List<Integer> existingTurtleIDs = new ArrayList<>();
    private Color myPenColor = Color.BLACK;
    private double myPenThickness = 1;
    private final Rectangle myBackground;
    private boolean isPenUp = false;
    private final double myWidth;
    private final double myHeight;
    private final double xOffset;
    private final double yOffset;

    public TurtleView(double width, double height){
        myWidth = width - TURTLE_SIZE;
        myHeight = height - TURTLE_SIZE;
        xOffset = myWidth/2 - TURTLE_SIZE/2;
        yOffset = myHeight/2 - TURTLE_SIZE/2;
        myBackground = new Rectangle(width, height);
        myBackground.setFill(Color.WHITE);
        myBackground.setStroke(Color.BLACK);
        myBackground.setStrokeType(StrokeType.OUTSIDE);
        myBackground.setStyle("-fx-border-color: black");
        this.getChildren().add(myBackground);
    }

    protected void makeTurtle(int id, Consumer<Boolean> onClicked){
        Turtle myTurtle = new Turtle(myActiveTurtleImage);
        myTurtle.setPreserveRatio(true);
        myTurtle.setCache(true);
        myTurtle.setFitWidth(TURTLE_SIZE);
        myTurtle.setFitHeight(TURTLE_SIZE);
        myTurtles.put(id, myTurtle);
        resetTurtle(id);
        myTurtle.setOnMouseClicked(event -> toggleActive(id, onClicked));
        this.getChildren().add(myTurtle);
    }

    /**
     * get the list of actual turtle coordinates that have not been altered to fit the screen
     * @return map of turtle ids to positions
     */
    protected Map<Integer, Point2D> getUnalteredTurtlePositions(){
        return unalteredTurtlePositions;
    }

    /**
     * give the visualizer a list of existing turtle ids
     * @return list of ids
     */
    protected List<Integer> getExistingTurtleIDs(){
        return existingTurtleIDs;
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
    protected void setTurtlePosition(double x, double y, int id){
        myTurtles.get(id).setX(boundX(x));
        myTurtles.get(id).setY(boundY(y));
    }

    /**
     * Rotate the turtle
     * @param angle angle to rotate by
     */
    protected void setTurtleHeading(double angle, int id){
        myTurtles.get(id).setRotate(angle);
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
            if(notAlmostEqual(turtleX - startX, turtlePos.getX() - startPos.getX()) || notAlmostEqual(turtleY - startY, turtlePos.getY() - startPos.getY())){
                return; // don't draw the path if the turtle is wrapping around in this step
            }
            MoveTo moveTo = new MoveTo(turtleX , turtleY);
            LineTo line = new LineTo(startX, startY);
            path.getElements().add(moveTo);
            path.getElements().add(line);
            path.setStroke(myPenColor);
            path.setStrokeWidth(myPenThickness);
            this.getChildren().add(path);
        }
    }

    /**
     * Removes all of the taken paths displayed on the screen by clearing the display and returns all turtles
     * back to the starting position
     */
    protected void clearPaths(){
        int numChildren = this.getChildren().size();
        if(numChildren >= 2) this.getChildren().remove(1, numChildren);
        for(Map.Entry<Integer, Turtle> turtle : myTurtles.entrySet()){
            if(turtle.getValue().getVisibility()) this.getChildren().add(turtle.getValue());
            resetTurtle(turtle.getKey());
        }
        //TODO: decide if this should only clear paths/reset a specific turtle
    }

    /**
     * change the turtle image. Image is determined by the file chooser
     * @param newTurtleImage image object to set the turtle image to
     */
    protected void setTurtleImage(Image newTurtleImage){
        for(Turtle turtle : myTurtles.values()) {
            turtle.setImage(newTurtleImage);
        }
    }

    protected void setTurtleVisibility(boolean turtleVisibility, int id) {
        Turtle myTurtle = myTurtles.get(id);
        if(turtleVisibility && !myTurtle.getVisibility()){
            this.getChildren().add(myTurtle);
        }
        else if(!turtleVisibility && myTurtle.getVisibility()){
            this.getChildren().remove(myTurtle);
        }
        myTurtle.setVisibility(turtleVisibility);
    }

    /**
     * set the pen status (up/down)
     * @param up true if the pen is up
     */
    protected void setIsPenUp(boolean up){
        isPenUp = up;
    }

    protected void setPenThickness(double value) {
        myPenThickness = value;
    }

    /**
     * get a list of ids for the active turtles
     * @return list of ids for the active turtles
     */
    protected List<Integer> getActiveTurtles(){
        List<Integer> activeTurtles = new ArrayList<>();
        for(Map.Entry<Integer, Turtle> turtleEntry : myTurtles.entrySet()){
            if(turtleEntry.getValue().isActive()) activeTurtles.add(turtleEntry.getKey());
        }
        return activeTurtles;
    }

    protected void activateTurtles(List<Integer> activeTurtles) {
        for(Turtle turtle : myTurtles.values()){
            turtle.setActive(false);
            turtle.setImage(myInactiveTurtleImage);
        }
        for(int id : activeTurtles){
            if(myTurtles.containsKey(id)) {
                myTurtles.get(id).setActive(true);
                myTurtles.get(id).setImage(myActiveTurtleImage);
            }
        }
    }

    protected String[] getPenState(){
        //TODO: replace 0 with myPenColor.toString() or pen color index
        return new String[]{Boolean.toString(isPenUp), "0", Double.toString(myPenThickness)};
    }

    protected String[] getTurtleInfo(int turtleID) {
        Turtle turtle = myTurtles.get(turtleID);
        return new String[]{Boolean.toString(turtle.isActive()), Integer.toString((int)turtle.getRotate())};
    }

    private void toggleActive(int id, Consumer<Boolean> onClicked) {
        if(myTurtles.get(id).isActive()){
            myTurtles.get(id).setActive(false);
            myTurtles.get(id).setImage(myInactiveTurtleImage); //TODO: maybe just use opacity
            onClicked.accept(false);
        }else{
            myTurtles.get(id).setActive(true);
            myTurtles.get(id).setImage(myActiveTurtleImage);
            onClicked.accept(true);
        }
    }

    /**
     * moves the turtle back to home position
     */
    private void resetTurtle(int id){
        myTurtles.get(id).setY(yOffset);
        myTurtles.get(id).setX(xOffset);
        myTurtles.get(id).setRotate(0);
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

    private boolean notAlmostEqual(double a, double b){
        return Math.abs(a - b) > SIGNIFICANT_DIFFERENCE;
    }
}
