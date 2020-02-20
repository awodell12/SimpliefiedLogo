package slogo.FrontEnd;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;

/**
 * This class is used to manage the text box where the user can enter in their Slogo commands
 * It is able to extract a String if what is in the box to pass to the Back End when the Run button is pressed
 */
public class CommandBox {

    private TextField inputField; // need a better data structure for this

    public CommandBox(Group root, Rectangle commandBoxShape, Rectangle clearButtonShape){
        inputField = new TextField();
        Button clearButton = new Button("Clear", clearButtonShape);
        clearButton.setOnAction(event -> clearContents());
        root.getChildren().add(clearButton);
    }

    /**
     * This method is used to access what the user has entered into the "Command Line" area
     * @return a String containing whatever the user had entered
     */
    public String getContents(){
        return inputField.getText();
    }

    /**
     * Clear whatever the user has entered into the CommandBox by resetting its contents to empty,
     * used when Clear button is pressed
     */
    public void clearContents(){};


}
