package slogo.FrontEnd;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import javax.swing.SizeRequirements;
import org.xml.sax.SAXException;
import slogo.CommandResult;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Visualizer extends Application {
    private static final double HEIGHT = 600;
    private static final double ASPECT_RATIO = (16.0/9.0);
    private static final double WIDTH = HEIGHT * ASPECT_RATIO;
    private static final Paint BACKGROUND = Color.WHITE;
    private static final double MILLISECOND_DELAY = 1000;
    private static final Rectangle COMMAND_BOX_SHAPE = new Rectangle(50, 800, 650, 125);
    private static final Rectangle TURTLE_VIEW_SHAPE = new Rectangle(50, 100, 650, 600);
    private static final Rectangle HISTORY_VIEW_SHAPE = new Rectangle(750, 100, 200, 250);
    private static final Rectangle UDC_VIEW_SHAPE = new Rectangle(750, 400, 200, 250);
    private static final Rectangle VARIABLES_VIEW_SHAPE = new Rectangle(750, 700, 200, 200);
    private static final Rectangle ERROR_MESSAGE_SHAPE = new Rectangle(100, 720, 650, 60);
    private static final Rectangle RUN_BUTTON_SHAPE = new Rectangle(300, 750, 60, 40);
    private static final Rectangle CLEAR_HISTORY_BUTTON_SHAPE = new Rectangle(950, 100, 50, 50);
    private static final Rectangle CLEAR_COMMAND_BOX_SHAPE = new Rectangle(900, 925, 75, 50);
    private static final Rectangle CLEAR_UDC_BUTTON_SHAPE = new Rectangle(950, 400, 50, 50);
    private static final Rectangle CLEAR_VARIABLES_BUTTON_SHAPE = new Rectangle(950, 700, 50, 50);
    private static final Rectangle HELP_BUTTON_SHAPE = new Rectangle(850, 25, 75, 50);
    private static final Rectangle SET_TURTLE_IMAGE_BUTTON_SHAPE = new Rectangle(750, 25, 75, 50);
    private static final double SPACING = 10;
    //TODO: add menu shapes and label shapes

    private Button myClearCommandBoxButton;
    private Button myClearHistoryButton;
    private CommandBox myCommandBox;
    private ClearableEntriesBox myHistory;
    private ClearableEntriesBox myUserDefinedCommands;
    private ClearableEntriesBox myVariables;
    //private Map<String, Integer> myVariableMap;
    private TurtleView myTurtleView;
    private Queue<String> myInstructionQueue;
    private Stage myStage;
    private Group myRoot;
    private VBox myLeftVBox;
    private VBox myRightVBox;
    private HBox myLayout;

    /**
     * Constructor for the visualizer class, which manages the display components and state
     */
    public Visualizer() throws IOException {
        //myStage = new Stage();
        //Scene display = setUpDisplay();
        //myStage.setScene(display);
        //myStage.show();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        myStage = primaryStage;
        Scene display = setUpDisplay();
        myStage.setScene(display);
        myStage.show();
    }

    /**
     * Pops the first element of the instruction queue, which contains strings that are either scripts taken directly
     *      from the command box, or special instructions generated by buttons which need to interact with model
     * Relevant Features:
     * React to the text and update the model
     * Choose a language in which slogo commands are understood (with a button/menu)
     * @return the instruction string, uninterpreted
     */
    public String popInstructionQueue(){
        return myInstructionQueue.poll();
    }

    /**
     * Interpret result of CommandResults object, update everything that is updateable
     * Relevant Features:
     * React to the text and update the model
     * See the results of the turtle executing commands displayed visually
     * See resulting errors in user friendly way
     * see user defined commands currently available
     * @param result
     */
    public void interpretResult(CommandResult result){
        // maybe make this take exact parameters
    }

    private Scene setUpDisplay() throws IOException{
        myInstructionQueue = new PriorityQueue<>();
        //myVariableMap = new HashMap<>();

        myRoot = new Group();
        myLayout = new HBox(20);
        myLayout.setMaxSize(WIDTH, HEIGHT);


        myLeftVBox = new VBox(SPACING);
        myLeftVBox.setMaxSize(WIDTH * 0.5, HEIGHT);
        myLeftVBox.setMinSize(myLeftVBox.getMaxWidth(), myLeftVBox.getMaxHeight());

        myRightVBox = new VBox(SPACING);
        myRightVBox.setMaxSize(WIDTH*0.25, HEIGHT);
        setUpRightPane();

        setUpLeftPane();

        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> {
            try {
                step();
            /*} catch (IOException ex) {
                System.out.println("Caught IO Exception");
            } catch (SAXException ex) {
                System.out.println("Caught SAXException");*/
            } catch (Exception ex) {

                System.out.println("Caught Exception");

            }
        });
        myLayout.getChildren().addAll(myLeftVBox,myRightVBox);
        myLayout.setMargin(myLeftVBox, new Insets( SPACING, 0, 0, 50));
        myLayout.setMargin(myRightVBox, new Insets(SPACING,50,0,0));
        myRoot.getChildren().add(myLayout);
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
        return new Scene(myRoot, WIDTH, HEIGHT , BACKGROUND);
    }

    private void setUpLeftPane() {

        myTurtleView = new TurtleView(myLeftVBox,300*ASPECT_RATIO,300);
        myCommandBox = new CommandBox(myLeftVBox, COMMAND_BOX_SHAPE, CLEAR_COMMAND_BOX_SHAPE);
    }

    private void setUpRightPane() {
        setUpTopButtons();
        myHistory = new ClearableEntriesBox(myRightVBox, HISTORY_VIEW_SHAPE, CLEAR_HISTORY_BUTTON_SHAPE, "HISTORY");
        myUserDefinedCommands = new ClearableEntriesBox(myRightVBox, UDC_VIEW_SHAPE, CLEAR_UDC_BUTTON_SHAPE, "USER-DEFINED COMMANDS");
        myVariables = new ClearableEntriesBox(myRightVBox, VARIABLES_VIEW_SHAPE, CLEAR_VARIABLES_BUTTON_SHAPE, "ENVIRONMENT VARIABLES");
        setUpBottomButtons();
    }

    private void setUpTopButtons() {

        HBox topButtons = new HBox(SPACING);
        Button myHelpButton = new Button("Help", HELP_BUTTON_SHAPE);
        myHelpButton.setOnAction(event -> displayHelp());
        Button mySetTurtleImageButton = new Button("Set Turtle Image", SET_TURTLE_IMAGE_BUTTON_SHAPE);
        mySetTurtleImageButton.setOnAction(event -> setTurtleImage());
        topButtons.getChildren().add(myHelpButton);
        topButtons.getChildren().add(mySetTurtleImageButton);
        myRightVBox.getChildren().add(topButtons);
    }

    private void setTurtleImage() {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(myStage);
        if(file != null) {
            try {
                BufferedImage buffImage = ImageIO.read(file);
                WritableImage fximage = new WritableImage(buffImage.getWidth(), buffImage.getHeight());
                Image image = SwingFXUtils.toFXImage(buffImage, fximage);
                myTurtleView.setTurtleImage(image);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void setUpBottomButtons() {
        HBox bottomButtons = new HBox(SPACING);
        Button runButton = new Button("Run", RUN_BUTTON_SHAPE);
        runButton.setOnAction(event -> runButtonEvent());
        Button clearButton = new Button("Clear", CLEAR_COMMAND_BOX_SHAPE);
        clearButton.setOnAction(event -> myCommandBox.clearContents());
        bottomButtons.getChildren().addAll(runButton, clearButton);
        myRightVBox.getChildren().add(bottomButtons);
    }

    private void step(){

    }

    // maybe make it a rect so it can also resize the turtle
    private void updateTurtle(Point pos){

    }

    private void makeButtons(){
        /*String[] buttonTexts = new String[]{"Run", "Help"};
        Rectangle[] buttonShapes = new Rectangle[]{RUN_BUTTON_SHAPE, HELP_BUTTON_SHAPE};
        EventHandler<ActionEvent>[] actions = new EventHandler<ActionEvent>[]{event -> myInstructionQueue.add(myCommandBox.getContents()),
            event -> displayHelp()};
        for(int i=0; i<buttonShapes.length; i++){
            Button b = new Button(buttonTexts[i], buttonShapes[i]);
            b.setOnAction(actions[i]);
        }*/

        //TODO: add other buttons
    }

    private void addVariable(String name, int value){
        myVariables.addEntry(name + " : " + value, name);
        /*if(myVariableMap.containsKey(name)){
            myVariables.addEntry(name + " : " + value, name);
        }
        else{
            myVariables.addEntry(name + " : " + value, null);
        }
        myVariableMap.put(name, value);*/
    }

    private void runButtonEvent(){
        String instruction = myCommandBox.getContents();
        myInstructionQueue.add(instruction);
        myHistory.addEntry(instruction, null);
        addVariable("wow", 8);
        addVariable("nice", 6);
        addVariable("wow", 5);
    }

    private void displayHelp(){

    }
}
