package slogo.frontEnd;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;

/**
 * Refactored the makeButton method into its own factory pattern class to follow some OO design and
 * reduce the complexity of the Visualizer class.
 */
public class ButtonMaker {

  private String text;
  private Rectangle shape;
  private Object clazz;
  private ResourceBundle languageResources;

  public ButtonMaker(String text, Rectangle shape, Object clazz,
      ResourceBundle languageResources) {
    this.text = text;
    this.shape = shape;
    this.clazz = clazz;
    this.languageResources = languageResources;
  }

  protected Button invoke() {
    Method method = null;
    try {
      method = clazz.getClass().getDeclaredMethod(text);
    }
    catch (NoSuchMethodException e) {
      new ErrorDisplay(e.getMessage(), languageResources);
    }
    Button button = new Button(languageResources.getString(text));
    button.setLayoutY(shape.getY());
    button.setLayoutX(shape.getX());
    button.setMinSize(shape.getWidth(), shape.getHeight());
    Method finalMethod = method;
    button.setOnAction(event -> {
      try {
        assert finalMethod != null;
        finalMethod.invoke(clazz);
      } catch (IllegalAccessException | InvocationTargetException | NullPointerException e) {
        new ErrorDisplay(e.getMessage(), languageResources);
      }
    });
    return button;
  }

}