package slogo.frontEnd;

import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ErrorDisplay {

  private String message;
  private ResourceBundle languageResources;

  public ErrorDisplay(String message, ResourceBundle languageResources) {
    this.message = message;
    this.languageResources = languageResources;
  }

  public void invoke() {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle(languageResources.getString("IOError"));
    alert.setContentText(message);
    alert.showAndWait();
  }
}
