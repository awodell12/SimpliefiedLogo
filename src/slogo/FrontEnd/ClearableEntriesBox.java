package slogo.FrontEnd;

import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * This class is used to manage the display elements of the historu (i.e. commands that have been run)
 */
public class ClearableEntriesBox {

    private TextFlow myTextFlow;
    public ClearableEntriesBox(Pane layout, Rectangle historyShape, Rectangle clearButtonShape){
      myTextFlow = new TextFlow();
      myTextFlow.setPrefWidth(historyShape.getWidth());
      myTextFlow.setPrefHeight(historyShape.getHeight());
      myTextFlow.setMaxSize(Control.USE_PREF_SIZE,Control.USE_PREF_SIZE);
      Button clearButton = new Button("Clear", clearButtonShape);
      clearButton.setOnAction(event -> clearEntryBox());
      HBox hbox = new HBox(10);
      myTextFlow.setStyle("-fx-border-color: black");
      hbox.getChildren().addAll(myTextFlow,clearButton);
      layout.getChildren().addAll(hbox);

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
      Text newText = new Text(userInput + "\n");
      myTextFlow.getChildren().add(newText);
    }
}
