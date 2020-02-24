package slogo.FrontEnd;

import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to manage the display elements of the history, variables, and user defined commands
 */
public class ClearableEntriesBox {

    private TextFlow myTextFlow;
    private Text descriptionText;
    private List<String> entryList;

    public ClearableEntriesBox(Pane layout, Rectangle shape, Rectangle clearButtonShape, String description){
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
        descriptionText = new Text(description + "\n");
        descriptionText.setUnderline(true);
        descriptionText.setFill(Color.BLUE);
        myTextFlow.getChildren().add(descriptionText);
        entryList = new ArrayList<>();
    }

    /**
     * Removes all entries from the box and its display
     */
    public void clearEntryBox(){
        myTextFlow.getChildren().clear();
        myTextFlow.getChildren().add(descriptionText);
        entryList.clear();
    }

    /**
     * Takes in the latest user entry and stores it so it can be displayed. Deletes old entry if necessary
     * @param entry the string to be added to the displayed entries
     * @param overwrite the name of the entry that needs to be overwritten (or null)
     */
    public void addEntry(String entry, String overwrite){
        Text newText = new Text(entry + "\n");
        myTextFlow.getChildren().add(newText);
        if(overwrite != null){
            for(int i=0; i<entryList.size(); i++){
                if(overwrite.equals(entryList.get(i))){
                    myTextFlow.getChildren().remove(i);
                    break;
                }
            }
        }
        entryList.add(entry);
    }
}
