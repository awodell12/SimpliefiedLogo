package slogo.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.stage.Stage;
import slogo.backend.SLogoBackEnd;
import slogo.backend.SLogoLanguageChanger;
import slogo.CommandResult;
import slogo.frontEnd.Visualizer;

public class Controller extends Application{

    private static final String LANGUAGE_INSTRUCTION = "language:";
    private static final int LI_LENGTH = LANGUAGE_INSTRUCTION.length();

    private final List<Visualizer> myVisualizers = new ArrayList<>();
    private final List<SLogoBackEnd> myModels = new ArrayList<>();
    private int numWorkspaces = 0;

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
     */
    @Override
    public void start(Stage primaryStage) {
        myModels.add(new SLogoBackEnd());
        int thisWorkspace = numWorkspaces; // need this variable because we don't want to pass in a dynamic value!
        ListChangeListener<String> instructionQueueListener = c -> processInstructionQueueEvent(thisWorkspace);
        myVisualizers.add(new Visualizer(instructionQueueListener, c-> {
            try {
                createNewWorkspace();
            } catch (Exception e) {
                System.out.println("error creating new workspace");
                System.out.println(e.getMessage());
            }
        }, thisWorkspace));
        myVisualizers.get(thisWorkspace).start(primaryStage);
        numWorkspaces++;
    }

    private void createNewWorkspace() throws Exception {
        start(new Stage());
    }

    private void processInstructionQueueEvent(int workspace){
        System.out.println(workspace);
        String input = myVisualizers.get(workspace).popInstructionQueue();
        if(input.length() >= LI_LENGTH && input.substring(0, LI_LENGTH).equals(LANGUAGE_INSTRUCTION)){
            SLogoLanguageChanger languageChanger = new SLogoLanguageChanger(input.substring(LI_LENGTH+1));
            myModels.get(workspace).applyChanger(languageChanger);
        }
        else {
            ArrayList<CommandResult> resultList = (ArrayList<CommandResult>) myModels.get(workspace).parseScript(input);
            for (CommandResult result : resultList) {
                if(result.isActualCommand()) {
                    result.setMyOriginalInstruction(input);
                    myVisualizers.get(workspace).processResult(result);
                }
            }
        }
    }

}
