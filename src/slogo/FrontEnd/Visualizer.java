package slogo.FrontEnd;

import java.io.File;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.imageio.ImageIO;


import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class Visualizer extends Application implements FrontEndExternal{
    private static final double HEIGHT = 600;
    private static final double ASPECT_RATIO = (16.0/9.0);
    private static final double WIDTH = HEIGHT * ASPECT_RATIO;
    private static final Paint BACKGROUND = Color.WHITE;
    private static final double MILLISECOND_DELAY = 1000;
    private static final Rectangle COMMAND_BOX_SHAPE = new Rectangle(50, 800, 650, 125);
    private static final Rectangle TURTLE_VIEW_SHAPE = new Rectangle(50, 100, 650, 600);
    private static final Rectangle HISTORY_VIEW_SHAPE = new Rectangle(750, 100, 250, 125);
    private static final Rectangle UDC_VIEW_SHAPE = new Rectangle(750, 400, 250, 125);
    private static final Rectangle VARIABLES_VIEW_SHAPE = new Rectangle(750, 700, 250, 125);
    private static final Rectangle RUN_BUTTON_SHAPE = new Rectangle(300, 750, 60, 40);
    private static final Rectangle CLEAR_HISTORY_BUTTON_SHAPE = new Rectangle(950, 100, 50, 50);
    private static final Rectangle CLEAR_COMMAND_BOX_SHAPE = new Rectangle(900, 925, 75, 50);
    private static final Rectangle CLEAR_UDC_BUTTON_SHAPE = new Rectangle(950, 400, 50, 50);
    private static final Rectangle CLEAR_VARIABLES_BUTTON_SHAPE = new Rectangle(950, 700, 50, 50);
    private static final Rectangle HELP_BUTTON_SHAPE = new Rectangle(850, 25, 75, 50);
    private static final Rectangle SET_TURTLE_IMAGE_BUTTON_SHAPE = new Rectangle(750, 25, 75, 50);
    private static final double SPACING = 10;
    private static final double MARGIN = 25;
    private static final String[] MENU_NAMES = new String[]{"Color", "Language", "Background"};
    private static final String[][] MENU_OPTIONS = new String[][]{{"Red", "Dark Salmon", "Billion Dollar Grass"},
            {"Chinese", "English", "French", "German", "Italian", "Portuguese", "Russian", "Spanish", "Syntax", "Urdu"},
            {"White", "Duke Blue", "Gray", "Red", "Azure", "LemonChiffon"}};
    private static final Map<String, Color> COLOR_MAP = new HashMap<>(){{
        put("Red", Color.RED);
        put("White", Color.WHITE);
        put("Gray", Color.GRAY);
        put("Azure", Color.AZURE);
        put("LemonChiffon", Color.LEMONCHIFFON);
        put("Duke Blue", Color.ROYALBLUE);
        put("Billion Dollar Grass", Color.LAWNGREEN);
        put("Dark Salmon", Color.DARKSALMON);
    }};
    private static final Map<String, String> HELP_CATEGORIES = new HashMap<>(){{
        put("Basic Syntax", "Basic_Syntax");
        put("Turtle Commands", "Turtle_Commands");
        put("Turtle Queries", "Turtle_Queries");
        put("Math Operations", "Math");
        put("Boolean Operations", "Booleans");
        put("Variables, Control Structures, and User-Defined Commands", "Variables_Control_UDC");
    }};
    private static final String DEFAULT_HELP_CATEGORY_FILE = "Basic_Syntax";

    private CommandBox myCommandBox;
    private ClearableEntriesBox myHistory;
    private ClearableEntriesBox myUserDefinedCommands;
    private ClearableEntriesBox myVariables;
    private TurtleView myTurtleView;
    private ObservableList<String> myInstructionQueue;
    private Stage myStage;
    private Group myRoot;
    private VBox myLeftVBox;
    private VBox myCenterVBox;
    private VBox myRightVBox;
    private HBox myLayout;
    private Text myErrorMessage;

    /**
     * Constructor for the visualizer class, which manages the display components and state
     */
    public Visualizer(ListChangeListener<String> instructionQueueListener) {
        myInstructionQueue = new ObservableQueue();
        myInstructionQueue.addListener(instructionQueueListener);
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
        return myInstructionQueue.remove(0);
    }

    /**
     * Interpret result of CommandResults object, update everything that is updateable
     * Relevant Features:
     * React to the text and update the model
     * See the results of the turtle executing commands displayed visually
     * See resulting errors in user friendly way
     * see user defined commands currently available
     * @param turtleRotate new angle to set turtle to
     * @param turtlePos new coordinates for turtle
     * @param variableName string name for variable to be created/overwritten
     * @param variableValue value for new variable
     * @param path path object to draw
     * @param udcName name of the newly created user defined command
     * @param udcText the actual commands that entail the user defined command
     * @param clearScreen whether or not the turtle view should be cleared
     * @param isPenUp whether or not the pen is up
     * @param turtleVisibility whether or not to show the turtle
     * @param resetTurtle whether or not the turtle should be returned to 0, 0
     * @param errorMessage error message string, if any
     */
    public void interpretResult(double turtleRotate, Point2D turtlePos, Path path, String variableName,
                                double variableValue, String udcName, String udcText, boolean clearScreen,
                                boolean isPenUp, boolean turtleVisibility, boolean resetTurtle, String errorMessage){
        myTurtleView.setTurtleHeading(turtleRotate);
        myTurtleView.setTurtlePosition(turtlePos.getX(), turtlePos.getY());
        if(path != null) myTurtleView.addPath(path);
        if(variableName != null) addVariable(variableName, variableValue);
        if(udcName != null) addUserDefinedCommand(udcName, udcText);
        if(clearScreen) myTurtleView.clearPaths();
        if(resetTurtle) myTurtleView.resetTurtle();
        myTurtleView.setTurtleVisibility(turtleVisibility);
        myTurtleView.setIsPenUp(isPenUp);
        if(errorMessage != null) displayErrorMessage(errorMessage);
    }

    private Scene setUpDisplay() throws IOException{
        myRoot = new Group();
        myLayout = new HBox(SPACING * 2);
        myLayout.setMaxSize(WIDTH, HEIGHT);
        myLayout.setMinSize(WIDTH,HEIGHT);


        myLeftVBox = new VBox(SPACING);
        myLeftVBox.setMaxSize(WIDTH * 0.5, HEIGHT);
        myLeftVBox.setMinSize(myLeftVBox.getMaxWidth(), myLeftVBox.getMaxHeight());

        myRightVBox = new VBox(SPACING);
        myRightVBox.setMaxSize(WIDTH*0.33, HEIGHT);
        setUpRightPane();

        setUpLeftPane();
        setUpCenterPane();

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
        myLayout.getChildren().addAll(myLeftVBox,myCenterVBox,myRightVBox);
        myLayout.setMargin(myLeftVBox, new Insets(SPACING, 0, 0, MARGIN));
        myLayout.setMargin(myRightVBox, new Insets(SPACING,MARGIN,0,0));
        myLayout.setStyle("-fx-border-color: black");
        myRoot.getChildren().add(myLayout);
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
        return new Scene(myRoot, WIDTH, HEIGHT , BACKGROUND);
    }

    private void setUpCenterPane() {
        myCenterVBox = new VBox(SPACING);
        myCenterVBox.setPrefHeight(HEIGHT);
        setUpBottomButtons();
        myCenterVBox.setAlignment(Pos.BOTTOM_CENTER);
        int lastIndex = myCenterVBox.getChildren().size();
        myCenterVBox.setMargin(myCenterVBox.getChildren().get(lastIndex-1), new Insets(0,0,HEIGHT * 0.15,0));
    }

    private void setUpLeftPane() {
        setUpMenus();
        myTurtleView = new TurtleView(myLeftVBox,300*ASPECT_RATIO,300);
        myErrorMessage = new Text("Error Message Goes Here");
        myErrorMessage.setFill(Color.RED);
        myLeftVBox.getChildren().add(myErrorMessage);
        myCommandBox = new CommandBox(myLeftVBox, COMMAND_BOX_SHAPE, CLEAR_COMMAND_BOX_SHAPE);
    }

    private void setUpRightPane() {
        setUpTopButtons();
        myHistory = new ClearableEntriesBox(myRightVBox, HISTORY_VIEW_SHAPE, CLEAR_HISTORY_BUTTON_SHAPE, "HISTORY");
        myUserDefinedCommands = new ClearableEntriesBox(myRightVBox, UDC_VIEW_SHAPE, CLEAR_UDC_BUTTON_SHAPE, "USER-DEFINED COMMANDS");
        myVariables = new ClearableEntriesBox(myRightVBox, VARIABLES_VIEW_SHAPE, CLEAR_VARIABLES_BUTTON_SHAPE, "ENVIRONMENT VARIABLES");
    }


    private void setUpTopButtons() {

        HBox topButtons = new HBox(SPACING);
        Button myHelpButton = makeButton("Help", HELP_BUTTON_SHAPE);
        myHelpButton.setOnAction(event -> displayHelp());
        Button mySetTurtleImageButton = makeButton("Set Turtle Image", SET_TURTLE_IMAGE_BUTTON_SHAPE);
        mySetTurtleImageButton.setOnAction(event -> setTurtleImage());
        topButtons.getChildren().add(myHelpButton);
        topButtons.getChildren().add(mySetTurtleImageButton);
        myRightVBox.getChildren().add(topButtons);
    }

    static Button makeButton(String text, Rectangle shape){
        Button button = new Button(text);
        button.setLayoutY(shape.getY());
        button.setLayoutX(shape.getX());
        button.setMinSize(shape.getWidth(), shape.getHeight());
        return button;
    }


    private void setUpMenus(){
        MenuBar menuBar = new MenuBar();
        myLeftVBox.getChildren().add(menuBar);
        for(int i=0; i<MENU_NAMES.length; i++){
            Menu menu = new Menu(MENU_NAMES[i]);
            menuBar.getMenus().add(menu);
            for(String entry : MENU_OPTIONS[i]){
                MenuItem menuItem = new MenuItem(entry);
                switch (i) {
                    case 0:
                        menuItem.setOnAction(event -> myTurtleView.setPenColor(COLOR_MAP.get(entry)));
                        break;
                    case 1:
                        menuItem.setOnAction(event -> setLanguage(entry));
                        break;
                    case 2:
                        menuItem.setOnAction(event -> myTurtleView.setBackGroundColor(COLOR_MAP.get(entry)));
                        break;
                }
                menu.getItems().add(menuItem);
            }
        }
    }

    private void setLanguage(String language){
        myInstructionQueue.add("language: " + language);
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
        Button runButton = makeButton("Run", RUN_BUTTON_SHAPE);
        runButton.setOnAction(event -> runButtonEvent());
        Button clearButton = makeButton("Clear", CLEAR_COMMAND_BOX_SHAPE);
        clearButton.setOnAction(event -> myCommandBox.clearContents());
        myCenterVBox.getChildren().addAll(runButton,clearButton);
    }

    private void step(){

    }

    private void displayErrorMessage(String message){
        myErrorMessage.setText(message);
    }

    private void addVariable(String name, double value){
        myVariables.addEntry(name + " : " + value, name);
    }

    private void addUserDefinedCommand(String name, String command){
        myUserDefinedCommands.addEntry(name + ":\n" + command, name);
    }

    private void runButtonEvent(){
        String instruction = myCommandBox.getContents();
        myInstructionQueue.add(instruction);
        myHistory.addEntry(instruction, null);
    }

    private void displayHelp(){
        Stage stage = new Stage();
        stage.setTitle("Help Window");
        VBox vBox = new VBox(SPACING);
        MenuBar menuBar = new MenuBar();
        vBox.getChildren().add(menuBar);
        Menu menu = new Menu("Select Help Category");
        menuBar.getMenus().add(menu);
        for(String helpCategory : HELP_CATEGORIES.keySet()){
            MenuItem menuItem = new Menu(helpCategory);
            menuItem.setOnAction(event -> changeHelpImage(HELP_CATEGORIES.get(helpCategory), vBox));
            menu.getItems().add(menuItem);
        }
        vBox.getChildren().add(new ImageView("slogo/FrontEnd/Resources/" + DEFAULT_HELP_CATEGORY_FILE + ".png"));
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(vBox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        stage.setScene(new Scene(scrollPane, 600, 600));
        stage.show();
    }

    private void changeHelpImage(String imageName, VBox vBox){
        vBox.getChildren().remove(1);
        vBox.getChildren().add(new ImageView("slogo/FrontEnd/Resources/" + imageName + ".png"));
    }
}
