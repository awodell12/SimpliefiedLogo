package slogo.Controller;

import static javafx.application.Application.launch;

import java.io.IOException;
import javafx.application.Application;
import slogo.CommandResult;
import slogo.FrontEnd.Visualizer;

public class Controller implements ModelListener {
  private Visualizer myVisualizer;
  @Override
  public void handleModelUpdate(CommandResult update) {

  }

  public static void main (String[] args) {
    Application.launch(Visualizer.class, args);
  }

}
