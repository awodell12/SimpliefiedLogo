package slogo.FrontEnd;

import javafx.collections.ListChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.shape.Path;
import slogo.CommandResult;
import slogo.FrontEnd.Visualizer;
import java.util.List;

import java.awt.*;

public interface FrontEndExternal {

     /**
     * Constructor for the visualizer class, which manages the display components and state
     */
     //public Visualizer(ListChangeListener<String> instructionQueueListener);

    /**
     * Pops the first element of the instruction queue, which contains strings that are either scripts taken directly
     *      from the command box, or special instructions generated by buttons which need to interact with model
     * Relevant Features:
     * React to the text and update the model
     * Choose a language in which slogo commands are understood (with a button/menu)
     * @return the instruction string, uninterpreted
     */
    public String popInstructionQueue();

    /**
     * Interpret result of CommandResults object, update everything that is updateable
     * Relevant Features:
     * React to the text and update the model
     * See the results of the turtle executing commands displayed visually
     * See resulting errors in user friendly way
     * see user defined commands currently available
     * @param turtleRotate new angle to set turtle to
     * @param turtlePos new coordinates for turtle
     * @param variableName string name for variable to be created/overwritten
     * @param variableValue value for new variable
     * @param startPos old coordinates of the turtle; start of path
     * @param udcName name of the newly created user defined command
     * @param udcText the actual commands that entail the user defined command
     * @param clearScreen whether or not the turtle view should be cleared
     * @param isPenUp whether or not the pen is up
     * @param turtleVisibility whether or not to show the turtle
     * @param resetTurtle whether or not the turtle should be returned to 0, 0
     */
    public void interpretResult(double turtleRotate, Point2D turtlePos, List startPos, String variableName,
                                double variableValue, String udcName, String udcText, boolean clearScreen,
                                boolean isPenUp, boolean turtleVisibility, boolean resetTurtle, String errorMessage);
}
