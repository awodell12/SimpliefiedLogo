package slogo.Controller;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import slogo.BackEnd.SLogoBackEnd;
import slogo.BackEnd.SLogoLanguageChanger;
import slogo.CommandResult;
import slogo.FrontEnd.Visualizer;

public class Controller extends Application{
    private Visualizer myVisualizer;

    public static void main (String[] args) {
        launch(Controller.class, args);
    }

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     * @throws Exception if something goes wrong
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        SLogoBackEnd myModel = new SLogoBackEnd();
        ListChangeListener<String> instructionQueueListener = c -> {
            String input = myVisualizer.popInstructionQueue();

            if(input.length() >= 9 && input.substring(0, 9).equals("language:")){
                SLogoLanguageChanger languageChanger = new SLogoLanguageChanger(input.substring(10));
                myModel.applyChanger(languageChanger);
            }
            else {
                ArrayList<CommandResult> resultList = (ArrayList<CommandResult>) myModel.parseScript(input);
                for (CommandResult result : resultList) {
                    myVisualizer.interpretResult(result.getMyRotation(), new Point2D(result.getMyPosition().get(0), result.getMyPosition().get(1)),
                            result.getPathStart(), result.getMyVariableName(), result.getMyVariableValue(), result.getMyUDCName(),
                            result.getMyUDCText(), result.isMyScreenClear(), result.isMyPenUp(), result.isMyTurtleVisible(),
                            result.isMyTurtleReset(), result.getErrorMessage());
                }
            }
        };
        myVisualizer = new Visualizer(instructionQueueListener);
        myVisualizer.start(primaryStage);
    }

}
