package slogo.FrontEnd;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;

/**
 * This class is used to manage the display elements of the historu (i.e. commands that have been run)
 */
public class History {

    public History(Group root, Rectangle historyShape, Rectangle clearButtonShape){

      Button clearButton = new Button("Clear", clearButtonShape);
      clearButton.setOnAction(new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
          clearHistory();
        }
      });
      root.getChildren().add(clearButton);
    }

    /**
     * Removes all entries from the History and its display
     */
    void clearHistory(){};

    /**
     * Takes in the latest user entry and stores it into history so it can be displayed.
     * @param userInput The String copy of what the user enters into the CommandBox
     */
    void addHistoryEntry(String userInput){};
}
