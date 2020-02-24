package slogo.FrontEnd;

import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * This class is used to manage the display elements of the history, variables, and user defined commands
 */
public class ClearableEntriesBox {

    private TextFlow myTextFlow;
    public ClearableEntriesBox(Pane layout, Rectangle shape, Rectangle clearButtonShape){
        myTextFlow = new TextFlow();
        myTextFlow.setPrefWidth(shape.getWidth());
        myTextFlow.setMaxSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(myTextFlow);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        layout.getChildren().add(scrollPane);
        Button clearButton = new Button("Clear", clearButtonShape);
        clearButton.setOnAction(event -> clearEntryBox());
        layout.getChildren().add(clearButton);
    }

    /**
     * Removes all entries from the box and its display
     */
    void clearEntryBox(){
        myTextFlow.getChildren().clear();
    }

    /**
     * Takes in the latest user entry and stores it so it can be displayed.
     * @param entry the string to be added to the displayed entries
     */
    void addEntry(String entry){
        Text newText = new Text(entry);
        myTextFlow.getChildren().add(newText);
    }
}
