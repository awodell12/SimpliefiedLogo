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
     * Takes in a command result for the visualizer to process (after all other queued command results finish)
     * @param result a commandresult from controller, OR null if this is called by the step function
     */
    public void processResult(CommandResult result);
}
