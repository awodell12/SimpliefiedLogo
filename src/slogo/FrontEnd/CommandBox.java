package slogo.FrontEnd;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * This class is used to manage the text box where the user can enter in their Slogo commands
 * It is able to extract a String of what is in the box to pass to the Back End when the Run button is pressed
 */
public class CommandBox {

    //private TextField inputField; // need a better data structure for this
    private TextArea inputArea;
    private HBox myHBox;

    public CommandBox(Pane root, Rectangle commandBoxShape, Rectangle clearButtonShape){
        myHBox = new HBox(10);
        inputArea = new TextArea("Enter commands here");
        inputArea.setPrefWidth(commandBoxShape.getWidth());
        inputArea.maxHeight(commandBoxShape.getHeight());
        myHBox.getChildren().add(inputArea);
        myHBox.setStyle("-fx-border-color: black");
        root.getChildren().add(myHBox);
    }

    /**
     * This method is used to access what the user has entered into the "Command Line" area
     * @return a String containing whatever the user had entered
     */
    public String getContents(){
        return inputArea.getText();
    }

    /**
     * Clear whatever the user has entered into the CommandBox by resetting its contents to empty,
     * used when Clear button is pressed
     */
    public void clearContents(){inputArea.clear();};


}
