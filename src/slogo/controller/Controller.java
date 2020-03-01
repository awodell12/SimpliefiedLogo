package slogo.controller;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.stage.Stage;
import slogo.backend.SLogoBackEnd;
import slogo.backend.SLogoLanguageChanger;
import slogo.CommandResult;
import slogo.frontEnd.Visualizer;

public class Controller extends Application{

    private static Visualizer myVisualizer;
    private static final String LANGUAGE_INSTRUCTION = "language:";
    private static final int LI_LENGTH = LANGUAGE_INSTRUCTION.length();

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

            if(input.length() >= LI_LENGTH && input.substring(0, LI_LENGTH).equals(LANGUAGE_INSTRUCTION)){
                SLogoLanguageChanger languageChanger = new SLogoLanguageChanger(input.substring(LI_LENGTH+1));
                myModel.applyChanger(languageChanger);
            }
            else {
                ArrayList<CommandResult> resultList = (ArrayList<CommandResult>) myModel.parseScript(input);
                for (CommandResult result : resultList) {
                    if(result.isActualCommand()) {
                        System.out.println(result.getActiveTurtleIDs().toString());
                        result.setMyOriginalInstruction(input);
                        myVisualizer.processResult(result);
                    }
                }
            }
        };
        myVisualizer = new Visualizer(instructionQueueListener);
        myVisualizer.start(primaryStage);
    }

}
