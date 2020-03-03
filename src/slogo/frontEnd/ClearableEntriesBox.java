package slogo.frontEnd;

import java.util.ResourceBundle;

import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * This class is used to manage the display elements of the history, variables, and user defined commands
 */
public class ClearableEntriesBox extends HBox implements DisplayableTextOwner {

    protected final TextFlow myTextFlow;
    protected final Text descriptionText;
    protected final List<String> entryList;
    protected final VBox rightSide;
    protected final Button clearButton;
    protected final List<Text> displayableEntries = new ArrayList<>();
    private final String myDescriptionKey;

    private static final double SPACING = 10;

    public ClearableEntriesBox(Rectangle shape, Rectangle clearButtonShape, String descriptionKey, ResourceBundle languageResources){
        myTextFlow = new TextFlow();
        myTextFlow.setPrefWidth(shape.getWidth());
        myTextFlow.setPrefHeight(shape.getHeight());
        myTextFlow.setMaxSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(myTextFlow);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        clearButton = Visualizer.makeButton("clearEntryBox", clearButtonShape, this, languageResources);
        clearButton.setTooltip(new Tooltip(languageResources.getString("HoverText")));
        clearButton.setOnAction(event -> clearEntryBox());
        rightSide = new VBox(SPACING);
        rightSide.getChildren().add(clearButton);
        this.setSpacing(SPACING);
        this.getChildren().addAll(scrollPane, rightSide);
        myDescriptionKey = descriptionKey;
        descriptionText = new Text(languageResources.getString(descriptionKey) + "\n");
        descriptionText.setUnderline(true);
        descriptionText.setFill(Color.BLUE);
        myTextFlow.getChildren().add(descriptionText);
        myTextFlow.getChildren().add(new Text("\n\n\n\n\n"));
        entryList = new ArrayList<>();
    }

    /**
     * change the language and translate all displayable texts to the new language
     * @param languageResources the new language config to translate with
     */
    @Override
    public void setDisplayableTexts(ResourceBundle languageResources){
        clearButton.setText(languageResources.getString("clearButton"));
        clearButton.setTooltip(new Tooltip(languageResources.getString("HoverText")));
        descriptionText.setText(languageResources.getString(myDescriptionKey) + "\n");
        setChildDisplayableTexts(languageResources);
    }

    /**
     * Subclasses should override this method. This method exists because subclasses can't override setDisplayableTexts
     * @param languageResources the new language config to translate with
     */
    protected void setChildDisplayableTexts(ResourceBundle languageResources){
        for(int i=0; i<displayableEntries.size(); i++){
            String commandPart = displayableEntries.get(i).getText().substring(entryList.get(i).length()+2);
            displayableEntries.get(i).setText(entryList.get(i) + ": " + translateCommand(commandPart, languageResources));
        }
    }

    protected String translateCommand(String script, ResourceBundle languageResources){
        return script;
        //return languageResources.getBaseBundleName() + script; //TODO: implement this
    }

    /**
     * Removes all entries from the box and its display
     */
    protected void clearEntryBox(){
        myTextFlow.getChildren().clear();
        myTextFlow.getChildren().add(descriptionText);
        myTextFlow.getChildren().add(new Text("\n\n\n\n\n"));
        entryList.clear();
        displayableEntries.clear();
    }

    /**
     * Takes in the latest user entry and stores it so it can be displayed. Deletes old entry if necessary
     * @param entry the string to be added to the displayed entries
     * @param name the name of the entry that needs to be overwritten (or null)
     * @param action the lambda defining what happens when the entry is clicked
     */
    protected void addEntry(String entry, String name, Consumer<String> action) {
        myTextFlow.getChildren().remove(myTextFlow.getChildren().size() - 1);
        Text newText = new Text(entry + "\n");
        newText.setOnMouseClicked(event -> action.accept(entry));
        myTextFlow.getChildren().add(newText);
        displayableEntries.add(newText);
        checkDuplicates(name);
    }

    // not part of the API
    protected void checkDuplicates(String name){
        myTextFlow.getChildren().add(new Text("\n\n\n\n\n"));
        if(name != null){
            for(int i=0; i<entryList.size(); i++){
                if(name.equals(entryList.get(i))){
                    myTextFlow.getChildren().remove(i+1); // add 1 to account for description text
                    entryList.remove(i);
                    displayableEntries.remove(i);
                    break;
                }
            }
            entryList.add(name);
            // note that we add name, not entry, because we want to store only the NAME not the full text entry
        }
    }
}
