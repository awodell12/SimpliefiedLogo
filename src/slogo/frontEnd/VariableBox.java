package slogo.frontEnd;

import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.function.Consumer;

public class VariableBox extends ClearableEntriesBox {

  private static final double SPACING = 10;

  public VariableBox(Rectangle shape, Rectangle clearButtonShape, String description) {
    super(shape, clearButtonShape, description);
  }

  /**
   * Takes in the latest entry and stores it so it can be displayed. Deletes old entry if necessary
   * @param name the name of the entry that needs to be overwritten (or null)
   * @param value the value of the variable
   * @param action the lambda defining what happens when the entry is clicked
   */
  protected void addVariable(String name, double value, Consumer<String> action){
    myTextFlow.getChildren().remove(myTextFlow.getChildren().size()-1);
    //HBox hBox = new HBox(SPACING);
    TextArea valueText = new TextArea(value + "\n");
    Text nameText = new Text(name + " : ");
    valueText.setOnMouseClicked(event -> action.accept(valueText.getText()));
    myTextFlow.getChildren().add(valueText);
    super.checkDuplicates(name);
  }

  @Override
  protected void clearEntryBox(){
    super.clearEntryBox();
  }
}
