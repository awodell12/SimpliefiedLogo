package slogo.FrontEnd;

import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.HBox;
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

    private static final double SPACING = 10;

    public ClearableEntriesBox(Pane layout, Rectangle shape, Rectangle clearButtonShape, String description){
        myTextFlow = new TextFlow();
        myTextFlow.setPrefWidth(shape.getWidth());
        myTextFlow.setPrefHeight(shape.getHeight());
        //myTextFlow.setMinSize(Control.USE_PREF_SIZE-1, Control.USE_PREF_SIZE-1);
        myTextFlow.setMaxSize(Control.USE_PREF_SIZE + 1000, Control.USE_PREF_SIZE);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(myTextFlow);
        //scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        Button clearButton = Visualizer.makeButton("Clear", clearButtonShape);
        clearButton.setOnAction(event -> clearEntryBox());
        HBox hbox = new HBox(SPACING);
        hbox.getChildren().addAll(scrollPane,clearButton);
        layout.getChildren().add(hbox);
        descriptionText = new Text(description + "\n");
        descriptionText.setUnderline(true);
        descriptionText.setFill(Color.BLUE);
        myTextFlow.getChildren().add(descriptionText);
        entryList = new ArrayList<>();
    }

    /**
     * Removes all entries from the box and its display
     */
    protected void clearEntryBox(){
        myTextFlow.getChildren().clear();
        myTextFlow.getChildren().add(descriptionText);
        entryList.clear();
    }

    /**
     * Takes in the latest user entry and stores it so it can be displayed. Deletes old entry if necessary
     * @param entry the string to be added to the displayed entries
     * @param name the name of the entry that needs to be overwritten (or null)
     */
    protected void addEntry(String entry, String name){
        Text newText = new Text(entry + "\n");
        myTextFlow.getChildren().add(newText);
        if(name != null){
            for(int i=0; i<entryList.size(); i++){
                if(name.equals(entryList.get(i))){
                    myTextFlow.getChildren().remove(i+1); // add 1 to account for description text
                    entryList.remove(i);
                    break;
                }
            }
            entryList.add(name);
            // note that we add name, not entry, because we want to store only the NAME not the full text entry
        }

    }
}
