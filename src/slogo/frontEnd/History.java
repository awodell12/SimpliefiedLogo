package slogo.frontEnd;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class History extends ClearableEntriesBox{

  private int currentlyHighlighted = 0;

  public History(Rectangle shape, Rectangle clearButtonShape, String description, Visualizer visualizer) {
    super(shape, clearButtonShape, description, visualizer);
  }

  /**
   * Removes all entries from the box and its display
   */
  @Override
  protected void clearEntryBox(){
    if(currentlyHighlighted == myTextFlow.getChildren().size()-2) {
      myTextFlow.getChildren().clear();
      myTextFlow.getChildren().add(descriptionText);
      myTextFlow.getChildren().add(new Text("\n\n\n\n\n"));
      entryList.clear();
      currentlyHighlighted = 0;
    }
  }

  protected void highlightNext() {
    if(currentlyHighlighted != 0){
      Text toUnhighlight = ((Text) myTextFlow.getChildren().get(currentlyHighlighted));
      toUnhighlight.setFill(Color.BLACK);
      toUnhighlight.setUnderline(false);
    }
    currentlyHighlighted++;
    Text toHighlight = ((Text) myTextFlow.getChildren().get(currentlyHighlighted));
    toHighlight.setFill(Color.GREEN);
    toHighlight.setUnderline(true);
  }
}
