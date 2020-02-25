package slogo.FrontEnd;

import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * This class is used to manage the text box where the user can enter in their Slogo commands
 * It is able to extract a String of what is in the box to pass to the Back End when the Run button is pressed
 */
public class CommandBox extends HBox {

    private final TextArea inputArea;
    private static final String prompt = "Enter commands here";

    public CommandBox(Rectangle commandBoxShape){
        this.setSpacing(10);
        inputArea = new TextArea(prompt);
        inputArea.setOnMouseClicked(event -> removePrompt());
        inputArea.setPrefWidth(commandBoxShape.getWidth());
        inputArea.maxHeight(commandBoxShape.getHeight());
        this.getChildren().add(inputArea);
        this.setStyle("-fx-border-color: black");
    }

    /**
     * This method is used to access what the user has entered into the "Command Line" area
     * @return a String containing whatever the user had entered
     */
    protected String getContents(){
        return inputArea.getText();
    }

    /**
     * Clear whatever the user has entered into the CommandBox by resetting its contents to empty,
     * used when Clear button is pressed
     */
    protected void clearContents(){inputArea.clear();}

    private void removePrompt() {
        if(inputArea.getText().equals(prompt)){
            inputArea.clear();
        }
    }

}
