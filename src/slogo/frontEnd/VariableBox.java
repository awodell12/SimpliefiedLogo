package slogo.frontEnd;

import javafx.scene.control.TextArea;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.function.Consumer;

public class VariableBox extends ClearableEntriesBox {

  private static final String LABEL_TEXT = myLanguageResources.getString("VariableChangerLabel");
  private static final Rectangle LABEL_SHAPE = new Rectangle(20, 20);
  private final TextArea valueText;

  public VariableBox(Rectangle shape, Rectangle clearButtonShape, String description, Visualizer visualizer) {
    super(shape, clearButtonShape, description, visualizer);
    Text label = new Text(LABEL_TEXT);
    valueText = new TextArea();
    valueText.setMaxSize(LABEL_SHAPE.getWidth(), LABEL_SHAPE.getHeight());
    rightSide.getChildren().addAll(label, valueText);
  }

  /**
   * Takes in the latest entry and stores it so it can be displayed. Deletes old entry if necessary
   * @param entry the name of the entry that needs to be overwritten (or null)
   * @param name the value of the variable
   * @param action the lambda defining what happens when the entry is clicked
   */
  @Override
  protected void addEntry(String entry, String name, Consumer<String> action){
    myTextFlow.getChildren().remove(myTextFlow.getChildren().size()-1);
    Text newText = new Text(entry + "\n");
    newText.setOnMouseClicked(event -> action.accept(valueText.getText()));
    myTextFlow.getChildren().add(newText);
    super.checkDuplicates(name);
  }

  @Override
  protected void clearEntryBox(){
    super.clearEntryBox();
  }
}
