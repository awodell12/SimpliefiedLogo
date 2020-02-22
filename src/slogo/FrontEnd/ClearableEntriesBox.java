package slogo.FrontEnd;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * This class is used to manage the display elements of the historu (i.e. commands that have been run)
 */
public class ClearableEntriesBox {

    private TextFlow myTextFlow;
    public ClearableEntriesBox(Group root, Rectangle historyShape, Rectangle clearButtonShape){
      myTextFlow = new TextFlow();
      myTextFlow.setMaxWidth(historyShape.getWidth());
      Button clearButton = new Button("Clear", clearButtonShape);
      clearButton.setOnAction(event -> clearEntryBox());
      root.getChildren().add(clearButton);
    }

    /**
     * Removes all entries from the History and its display
     */
    void clearEntryBox(){
      myTextFlow.getChildren().clear();
    }

    /**
     * Takes in the latest user entry and stores it into history so it can be displayed.
     * @param userInput The String copy of what the user enters into the CommandBox
     */
    void addHistoryEntry(String userInput){
      Text newText = new Text(userInput);
      myTextFlow.getChildren().add(newText);
    }
}
