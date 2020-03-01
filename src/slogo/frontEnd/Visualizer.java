package slogo.frontEnd;

import java.io.File;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import slogo.CommandResult;

import javax.imageio.ImageIO;


import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("unused")
public class Visualizer extends Application implements FrontEndExternal{
  private static final String RESOURCE_LOCATION = "slogo/frontEnd/Resources.config";
  private static final ResourceBundle myResources = ResourceBundle.getBundle(RESOURCE_LOCATION);
  private static final double HEIGHT = Double.parseDouble(myResources.getString("WindowHeight"));
  private static final double ASPECT_RATIO = (16.0/9.0);
  private static final double WIDTH = HEIGHT * ASPECT_RATIO;
  private static final Paint BACKGROUND = Color.WHITE;
  private static final Rectangle COMMAND_BOX_SHAPE = new Rectangle(650, 125);
  private static final Rectangle TURTLE_VIEW_SHAPE = new Rectangle(300*ASPECT_RATIO,300);
  private static final Rectangle HISTORY_VIEW_SHAPE = new Rectangle(225, 125);
  private static final Rectangle UDC_VIEW_SHAPE = new Rectangle(225, 125);
  private static final Rectangle VARIABLES_VIEW_SHAPE = new Rectangle(225, 125);
  private static final Rectangle RUN_BUTTON_SHAPE = new Rectangle(60, 40);
  private static final Rectangle CLEAR_HISTORY_BUTTON_SHAPE = new Rectangle(30, 30);
  private static final Rectangle CLEAR_UDC_BUTTON_SHAPE = new Rectangle(30, 30);
  private static final Rectangle CLEAR_VARIABLES_BUTTON_SHAPE = new Rectangle(30, 30);
  private static final Rectangle HELP_BUTTON_SHAPE = new Rectangle(75, 50);
  private static final Rectangle SET_TURTLE_IMAGE_BUTTON_SHAPE = new Rectangle(75, 50);
  private static final Rectangle TURTLE_BUTTON_SHAPE = new Rectangle(60, 30);
  private static final Rectangle HELP_WINDOW_SHAPE = new Rectangle(600, 600);
  private static final Rectangle TURTLE_MOVEMENT_LABEL_SHAPE = new Rectangle(20, 5);
  private static final Rectangle TURTLE_INFO_SHAPE = new Rectangle(275 ,75);
  private static final double SPACING = 10;
  private static final double MARGIN = 25;
  private static final double BOTTOM_INSET = 0.15;
  private static final double MENU_LABEL_SIZE = 20;
  private static final int NUM_TURTLE_MOVE_BUTTONS = 4;
  private static final String[] MENU_NAMES = new String[]{"Color", "Language", "Background", "PenUp", "TurtleImage"};
  private static final String[][] MENU_OPTIONS = new String[][]{{"0", "1", "2", "3", "4", "5", "6", "7", "8"},
          {"Chinese", "English", "French", "German", "Italian", "Portuguese", "Russian", "Spanish", "Syntax", "Urdu"},
          {"0", "1", "2", "3", "4", "5", "6", "7", "8"},
          {"penUp", "penDown"},
          {"0", "1", "2"}};
  private Map<String, Color> myColorPalette = new HashMap<>(){{
    put("0", Color.RED);
    put("1", Color.WHITE);
    put("2", Color.GRAY);
    put("3", Color.AZURE);
    put("4", Color.LEMONCHIFFON);
    put("5", Color.ROYALBLUE);
    put("6", Color.LAWNGREEN);
    put("7", Color.DARKSALMON);
    put("8", Color.BLACK);
  }};
  private static final Map<String, String> HELP_CATEGORIES = new HashMap<>(){{
    put("Basic Syntax", "Basic_Syntax");
    put("Turtle Commands", "Turtle_Commands");
    put("Turtle Queries", "Turtle_Queries");
    put("Math Operations", "Math");
    put("Boolean Operations", "Booleans");
    put("Variables, Control Structures, and User-Defined Commands", "Variables_Control_UDC");
    put("Display Commands", "Display_Commands");
    put("Multiple Turtles", "Multiple_Turtle_Commands");
  }};
  private static final List<String> imageList = List.of(myResources.getString("Duke"), myResources.getString("DefaultTurtle"),
          myResources.getString("Duval"));
  private static final String[] BOTTOM_BUTTON_METHOD_NAMES = new String[]{"runButton", "clearButton", "undoButton", "redoButton"};
  private static final String[] BOTTOM_BUTTON_HOVER_NAMES = new String[]{"RunHover", "ClearHover", "UndoHover", "RedoHover"};
  private static final List<List<Integer>> BOTTOM_BUTTON_POSITIONS = List.of(List.of(0,0), List.of(0,1), List.of(1,0), List.of(1,1));

  private static final String DEFAULT_HELP_CATEGORY_FILE = "Basic_Syntax";
  private static final double FPS = 24;
  private static final double MILLISECOND_DELAY = 1000/FPS;
  private static final double SIGNIFICANT_DIFFERENCE = 0.001;
  private static final double MIN_SPEED = 0.1;
  private static final double MAX_SPEED = 10;
  private static final double DEFAULT_SPEED = 1;

  private CommandBox myCommandBox;
  private History myHistory;
  private ClearableEntriesBox myUserDefinedCommands;
  private VariableBox myVariables;
  private TurtleView myTurtleView;
  private final ObservableList<String> myInstructionQueue;
  private Stage myStage;
  private VBox myLeftVBox;
  private VBox myCenterVBox;
  private VBox myRightVBox;
  private Text myErrorMessage;
  private Point2D myDesiredTurtlePosition;
  private Point2D myCurrentTurtlePosition = new Point2D(0, 0);
  private double xIncrement;
  private double yIncrement;
  private Point2D myStartPos = null;
  private boolean isReady = true;
  private boolean paused = false;
  private final Queue<CommandResult> resultQueue = new LinkedList<>();
  private CommandResult previousResult = new CommandResult(0, 0, 0, 0,
          List.of(0.0,0.0), null, 0, null, 0.0, null, null,
          false, false, true, false, 0, Arrays.asList(0, 0, 0),
          1.0, Collections.singletonList(0), 0, 0, "", true);
  private CommandResult currentResult = previousResult;
  //private Map<String, Double> previousVariableMapping = new HashMap<>(); //TODO: move this to variable class?
  //private Map<String, Double> currentVariableMapping = previousVariableMapping;
  //private Map<String, Double> previousUDCMapping = new HashMap<>(); //TODO: move this to variable class?
  //private Map<String, Double> currentUDCMapping = previousUDCMapping; // TODO: make a state class?
  private boolean undone = false;
  private String myCurrentlyHighlighted = null;
  private String myCurrentInstruction = null;
  private Timeline animation;
  private List<TextArea> turtleMovementButtons = new ArrayList<>();
  private int myCurrentTurtleID;
  private Text myPenText;
  private TextFlow myTurtleInfo = new TextFlow();

  /**
   * Constructor for the visualizer class, which manages the display components and state
   */
  public Visualizer(ListChangeListener<String> instructionQueueListener) {
    myInstructionQueue = new ObservableQueue();
    myInstructionQueue.addListener(instructionQueueListener);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    myStage = primaryStage;
    Scene display = setUpDisplay();
    myStage.setScene(display);
    myStage.setTitle(myResources.getString("AppTitle"));
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
   * Takes in a command result for the visualizer to process (after all other queued command results finish)
   * @param result a commandresult from controller, OR null if this is called by the step function
   */
  public void processResult(CommandResult result){
    if(!isReady){
      if(result != null) {
        resultQueue.add(result);
      }
    }
    else{
      isReady = false;
      if(result == null){
        result = resultQueue.poll();
      }
      assert result != null; // intellij wants us to do this but it's not really necessary
      dissectCommand(result);
    }
  }

  private void dissectCommand(CommandResult result) {
    if(result.getMyOriginalInstruction() != myCurrentInstruction) {
      previousResult = currentResult;
      //previousVariableMapping = currentVariableMapping;
      //previousUDCMapping = currentUDCMapping;
      myCurrentInstruction = result.getMyOriginalInstruction();
    }
    currentResult = result;
    //currentVariableMapping = myVariables.getMap();
    //currentUDCMapping = myUserDefinedCommands.getMap();
    Point2D startPos = null;
    if(result.getPathStart() != null){
      startPos = new Point2D(result.getPathStart().get(0), -result.getPathStart().get(1));
    }
    interpretResult(result.getMyRotation(), new Point2D(result.getTurtlePosition().get(0), -result.getTurtlePosition().get(1)),
            startPos, result.getMyVariableName(),
            result.getMyVariableValue(), result.getMyUDCName(), result.getMyUDCText(), result.isMyScreenClear(),
            result.isMyPenUp(), result.isMyTurtleVisible(), result.getErrorMessage(), result.getMyOriginalInstruction(),
            result.getTurtleID(), result.getActiveTurtleIDs(), result.getPaletteIndex(), result.getPenColor(),
            result.getBackgroundColor(), result.getNewPaletteColor(), result.getShapeIndex(), result.getPenSize());
  }

  /**
   * Interpret result of CommandResults object, update everything that is updatable
   * Relevant Features:
   * React to the text and update the model
   * See the results of the turtle executing commands displayed visually
   * See resulting errors in user friendly way
   * see user defined commands currently available
   * @param turtleRotate new angle to set turtle to
   * @param turtlePos new coordinates for turtle
   * @param startPos start of path to draw
   * @param variableName string name for variable to be created/overwritten
   * @param variableValue value for new variable
   * @param udcName name of the newly created user defined command
   * @param udcText the actual commands that entail the user defined command
   * @param clearScreen whether or not the turtle view should be cleared
   * @param isPenUp whether or not the pen is up
   * @param turtleVisibility whether or not to show the turtle
   * @param originalInstruction the original instruction text that this command result corresponds to
   * @param errorMessage error message string, if any
   * @param activeTurtles list of active turtle IDs
   * @param turtleID which turtle this command is for
   * @param paletteIndex if a new color is being created, what is its index
   * @param penColorIndex color index to set the pen color to (for all turtles)
   * @param backgroundColorIndex color index to set the background color to
   * @param newColorRGB the rgb values for a new color that's being created
   * @param imageIndex what image to set all turtles to
   * @param penSize what thickness to set the pen to (for all turtles)
   *                take in a turtle id as well. if it doesn't exist create it.
   *                     store a list of active turtles, and a list of existing turtles
   *                     clicking on a turtle executes a tell command, adding that turtle to active turtles and then doing tell for the whole list
   *                     commands also return a list of active turtles
   *                     color: index for palette, color for palette, index for pen, index for background
   *                     image: index for the turtle's image
   */
  private void interpretResult(double turtleRotate, Point2D turtlePos, Point2D startPos, String variableName,
                               double variableValue, String udcName, String udcText, boolean clearScreen,
                               boolean isPenUp, boolean turtleVisibility, String errorMessage, String originalInstruction,
                               int turtleID, List<Integer> activeTurtles, int paletteIndex, int penColorIndex,
                               int backgroundColorIndex, List<Integer> newColorRGB, int imageIndex, double penSize) {
    myCurrentTurtleID = turtleID;
    if(!myTurtleView.getExistingTurtleIDs().contains(turtleID)){
      createTurtle(turtlePos, turtleID);
    }
    myTurtleView.activateTurtles(activeTurtles);
    for(int id : myTurtleView.getExistingTurtleIDs()){
      updateTurtleInfo(id);
    }
    myTurtleView.setTurtleHeading(turtleRotate, turtleID);
    myDesiredTurtlePosition = turtlePos;
    myCurrentTurtlePosition = myTurtleView.getUnalteredTurtlePositions().get(turtleID);
    xIncrement = (myDesiredTurtlePosition.getX()-myCurrentTurtlePosition.getX())/FPS;
    yIncrement = (myDesiredTurtlePosition.getY()-myCurrentTurtlePosition.getY())/FPS;
    myStartPos = startPos;
    if(variableName != null) addVariable(variableName, variableValue);
    if(udcName != null) addUserDefinedCommand(udcName, udcText);
    if(clearScreen) myTurtleView.clearPaths();
    myTurtleView.setTurtleVisibility(turtleVisibility, turtleID);
    myTurtleView.setIsPenUp(isPenUp);
    setPenText();
    displayErrorMessage(errorMessage);
    if(originalInstruction != myCurrentlyHighlighted) {
      myHistory.highlightNext();
      myCurrentlyHighlighted = originalInstruction;
    }
    undone = false;
    myRightVBox.requestLayout(); // make sure everything is updated graphically
    if (newColorRGB != null){
      myColorPalette.put(Integer.toString(paletteIndex), Color.color(newColorRGB.get(0), newColorRGB.get(1),newColorRGB.get(2) ));
      updateColorMenus(paletteIndex);
    }
  }

  private void updateColorMenus(int paletteIndex) {
    // TODO newly created color in palette to Background and penColor menus
  }

  private void createTurtle(Point2D turtlePos, int turtleID) {
    myTurtleView.makeTurtle(turtleID, this::activateTurtle);
    myTurtleView.getExistingTurtleIDs().add(turtleID);
    myTurtleView.getUnalteredTurtlePositions().put(turtleID, turtlePos);
    String[] activityAndHeading = myTurtleView.getTurtleInfo(myCurrentTurtleID);
    myTurtleInfo.getChildren().add(new Text("Turtle " + myCurrentTurtleID + ": \nActive: " + activityAndHeading[0] + "  Position: ("
            + (int)myTurtleView.getUnalteredTurtlePositions().get(myCurrentTurtleID).getX() + "," + (int)myTurtleView.getUnalteredTurtlePositions().get(myCurrentTurtleID).getY()
            + ")  Heading: " + activityAndHeading[1] + "\n"));
  }

  private Scene setUpDisplay() {
    Group myRoot = new Group();
    HBox myLayout = new HBox(SPACING * 2);

    myLayout.setMaxSize(WIDTH, HEIGHT);
    myLayout.setMinSize(WIDTH,HEIGHT);

    myLeftVBox = new VBox(SPACING);
    myLeftVBox.setMaxSize(WIDTH/2, HEIGHT);
    myLeftVBox.setMinSize(myLeftVBox.getMaxWidth(), myLeftVBox.getMaxHeight());

    myRightVBox = new VBox(SPACING);
    myRightVBox.setMaxSize(WIDTH/3, HEIGHT);

    setUpLeftPane();
    setUpCenterPane();
    setUpRightPane();

    KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> {
      try {
        step(false);
      } catch (Exception ex) {
        System.out.println("Caught Exception");
        ex.printStackTrace();
      }
    });

    myLayout.getChildren().addAll(myLeftVBox,myCenterVBox,myRightVBox);
    HBox.setMargin(myLeftVBox, new Insets(SPACING, 0, 0, MARGIN));
    HBox.setMargin(myRightVBox, new Insets(SPACING,MARGIN,0,0));
    myLayout.setStyle("-fx-border-color: black");
    myRoot.getChildren().add(myLayout);
    animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames().add(frame);
    animation.play();
    return new Scene(myRoot, WIDTH, HEIGHT , BACKGROUND);
  }

  private void setUpCenterPane() {
    myCenterVBox = new VBox(SPACING);
    myCenterVBox.setPrefHeight(HEIGHT);
    setUpTopCenterButtons();
    setUpBottomButtons();
    myCenterVBox.setAlignment(Pos.BOTTOM_CENTER);
    int lastIndex = myCenterVBox.getChildren().size();
    VBox.setMargin(myCenterVBox.getChildren().get(lastIndex-1), new Insets(0,0,HEIGHT * BOTTOM_INSET,0));
  }

  private void setUpLeftPane() {
    setUpMenus();
    myTurtleView = new TurtleView(TURTLE_VIEW_SHAPE.getWidth(), TURTLE_VIEW_SHAPE.getHeight());
    createTurtle(new Point2D(0,0), 0);
    myErrorMessage = new Text(myResources.getString("DefaultErrorMessage"));
    myErrorMessage.setFill(Color.RED);
    myCommandBox = new CommandBox(COMMAND_BOX_SHAPE);
    myLeftVBox.getChildren().addAll(myTurtleView, myErrorMessage, myCommandBox);
  }

  private void activateTurtle(boolean dummy) {
    StringBuilder instruction = new StringBuilder("tell [ ");
    for(int id : myTurtleView.getActiveTurtles()){
      instruction.append(id).append(" ");
    }
    instruction.append("]");
    executeInstruction(instruction.toString());
  }

  private void setUpRightPane() {
    setUpTopButtons();
    myHistory = new History(HISTORY_VIEW_SHAPE, CLEAR_HISTORY_BUTTON_SHAPE, myResources.getString("HistoryLabel"));
    myUserDefinedCommands = new ClearableEntriesBox(UDC_VIEW_SHAPE, CLEAR_UDC_BUTTON_SHAPE, myResources.getString("UDCLabel"));
    myVariables = new VariableBox(VARIABLES_VIEW_SHAPE, CLEAR_VARIABLES_BUTTON_SHAPE, myResources.getString("VariablesLabel"));
    myPenText = new Text();
    setPenText();
    myTurtleInfo.setMaxSize(TURTLE_INFO_SHAPE.getWidth(), TURTLE_INFO_SHAPE.getHeight());
    ScrollPane turtleInfoPane = new ScrollPane();
    turtleInfoPane.setContent(myTurtleInfo);
    turtleInfoPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    turtleInfoPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    myRightVBox.getChildren().addAll(myHistory, myUserDefinedCommands, myVariables, myPenText, turtleInfoPane);
  }

  private void setPenText(){
    String[] penState = myTurtleView.getPenState();
    double thick = Double.parseDouble(penState[2]);
    String penThick = String.format("%.3f", thick);
    myPenText.setText("Pen Up: " + penState[0] + " Pen Color: " + penState[1] + " Pen Thickness: " + penThick);
  }

  private void endPause(){
    paused = false;
  }
  private void setPause(){
    paused = true;
  }

  private void singleStep(){
    step(true);
  }
  private void setUpTopCenterButtons() {
    String[] buttonNames = new String[]{"moveForward", "moveBackward", "rotateRight", "rotateLeft", "endPause",
                          "setPause", "resetAnimation", "singleStep"};
    List<Button> buttons = new ArrayList<>();
    for(String buttonName : buttonNames){
      buttons.add(makeButton(buttonName, TURTLE_BUTTON_SHAPE, this));
    }
    for(int i=0; i<NUM_TURTLE_MOVE_BUTTONS; i++){
      HBox hbox = new HBox(SPACING);
      TextArea valueSetter = new TextArea();
      valueSetter.setMaxSize(TURTLE_MOVEMENT_LABEL_SHAPE.getWidth(), TURTLE_MOVEMENT_LABEL_SHAPE.getHeight());
      turtleMovementButtons.add(valueSetter);
      hbox.getChildren().addAll(buttons.get(i), valueSetter);
      myCenterVBox.getChildren().add(hbox);
    }
    for(Button button : buttons.subList(NUM_TURTLE_MOVE_BUTTONS, buttons.size())){
      myCenterVBox.getChildren().add(button);
    }
    Slider speedSlider = new Slider(MIN_SPEED, MAX_SPEED, DEFAULT_SPEED);
    speedSlider.valueProperty().addListener((ov, old_val, new_val) -> animation.setRate(speedSlider.getValue()));
    speedSlider.setShowTickMarks(true);
    speedSlider.setShowTickLabels(true);
    Text sliderLabel = new Text("Animation Speed");
    sliderLabel.setUnderline(true);
    Slider penSlider = new Slider(MIN_SPEED, MAX_SPEED, DEFAULT_SPEED);
    penSlider.valueProperty().addListener((ov, old_val, new_val) -> {
      myTurtleView.setPenThickness(penSlider.getValue());
      setPenText();
      myRightVBox.requestLayout(); // make sure everything is updated graphically
    });
    penSlider.setShowTickMarks(true);
    penSlider.setShowTickLabels(true);
    Text penSliderLabel = new Text("Pen Thickness");
    penSliderLabel.setUnderline(true);
    myCenterVBox.getChildren().addAll(sliderLabel, speedSlider, penSliderLabel, penSlider);
  }

  private void setUpTopButtons() {
    HBox topButtons = new HBox(SPACING);
    Button myHelpButton = makeButton("displayHelp", HELP_BUTTON_SHAPE, this);
    myHelpButton.setOnAction(event -> displayHelp());
    Button mySetTurtleImageButton = makeButton("setTurtleImage", SET_TURTLE_IMAGE_BUTTON_SHAPE, this);
    mySetTurtleImageButton.setOnAction(event -> setTurtleImage());
    topButtons.getChildren().add(myHelpButton);
    topButtons.getChildren().add(mySetTurtleImageButton);
    myRightVBox.getChildren().add(topButtons);
  }

  protected static Button makeButton(String text, Rectangle shape, Object clazz){
    Method method = null;
    try {
      method = clazz.getClass().getDeclaredMethod(text);
    }
    catch (NoSuchMethodException e) {
      e.printStackTrace();
      showError(e.getMessage());
    }
    Button button = new Button(myResources.getString(text));
    button.setLayoutY(shape.getY());
    button.setLayoutX(shape.getX());
    button.setMinSize(shape.getWidth(), shape.getHeight());
    Method finalMethod = method;
    button.setOnAction(event -> {
      try {
        assert finalMethod != null;
        finalMethod.invoke(clazz);
      } catch (IllegalAccessException | InvocationTargetException | NullPointerException e) {
        e.printStackTrace();
        showError(e.getMessage());
      }
    });
    return button;
  }

  private void moveForward(){
    executeInstruction("fd " + turtleMovementButtons.get(0).getText());
    //TODO: make this work for different languages
  }

  private void moveBackward(){
    executeInstruction("bk " + turtleMovementButtons.get(1).getText());
  }

  private void rotateRight(){
    executeInstruction("rt " + turtleMovementButtons.get(2).getText());
  }

  private void rotateLeft(){
    executeInstruction("lt " + turtleMovementButtons.get(3).getText());
  }

  private void resetAnimation() {
    executeInstruction("clearscreen");
    //myTurtleView.clearPaths(); // TODO: fix clear screen command
  }

  private void undoButton(){
    myHistory.addEntry("undo " + myCurrentInstruction, null, e->myCommandBox.setText(myCurrentInstruction));
    /*myVariables.clearEntryBox();
    for(Map.Entry<String, Double> entry : previousVariableMapping.entrySet()){
      addVariable(entry.getKey(), entry.getValue());
    }
    myUserDefinedCommands.clearEntryBox();
    for(Map.Entry<String, String> entry : previousUDCMapping.entrySet()){
      addUserDefinedCommand(entry.getKey(), entry.getValue());
    }*/
    processResult(previousResult);
    // TODO: make this work for multiple turtles
    undone = true;
  }

  private void redoButton(){
    if(undone) {
      myHistory.addEntry("redo " + myCurrentInstruction, null, e->myCommandBox.setText(myCurrentInstruction));
      processResult(previousResult);
    }
  }

  private void setPenColor(String colorIndex){
    //executeInstruction("setpencolor " + colorIndex); // TODO: uncomment out when implemented in backend
    myTurtleView.setPenColor(myColorPalette.get(colorIndex));
  }

  private void setPenUp(String menuName){
    executeInstruction(menuName + ""); // need the blank string so it registers as a new distinct string object
  }

  private void setBackGroundColor(String colorIndex){
    //executeInstruction("setbackground " + colorIndex); // TODO: uncomment out when implemented in backend
    myTurtleView.setBackGroundColor(myColorPalette.get(colorIndex));
  }

  private void setLanguage(String language){
    executeInstruction("language: " + language);
  }

  private void setTurtleImageIndex(String num){
    Image image = new Image(imageList.get(Integer.parseInt(num)));
    myTurtleView.setTurtleImage(image);
    //TODO: move the above 2 lines to interpretResult once this command is supported by backend
    //executeInstruction("setshape " + Integer.parseInt(num));
  }

  private void runButton(){
    String instruction = myCommandBox.getContents();
    executeInstruction(instruction);
  }

  private void clearButton(){
    myCommandBox.clearContents();
  }

  private Node getColorLabel(String index){
    return new Rectangle(MENU_LABEL_SIZE, MENU_LABEL_SIZE, myColorPalette.get(index));
  }

  private Node getTurtleImageLabel(String index){
    ImageView imageView = new ImageView(imageList.get(Integer.parseInt(index)));
    imageView.setFitHeight(MENU_LABEL_SIZE);
    imageView.setFitWidth(MENU_LABEL_SIZE);
    return imageView;
  }

  private Node getLanguageLabel(String irrelevant){
    return null;
  }

  private Node getPenUpLabel(String irrelevant){
    return null;
  }

  private void setUpMenus(){
    MenuBar menuBar = new MenuBar();
    myLeftVBox.getChildren().add(menuBar);
    for(int i=0; i<MENU_NAMES.length; i++){
      Menu menu = new Menu(MENU_NAMES[i]);
      menuBar.getMenus().add(menu);
      for(String entry : MENU_OPTIONS[i]){
        MenuItem menuItem = new MenuItem(entry);
        String methodName = myResources.getString(MENU_NAMES[i]);
        String labelGetterName = myResources.getString(MENU_NAMES[i] + "Label");
        // get another method name that will give us the label corresponding to this menu name
        // the method should return a node object
        try {
          Method method = this.getClass().getDeclaredMethod(methodName, String.class);
          Method labelGetter = this.getClass().getDeclaredMethod(labelGetterName, String.class);
          menuItem.setGraphic((Node) labelGetter.invoke(this, entry));
          menuItem.setOnAction(event -> {
            try {
              method.invoke(this, entry);
            } catch (IllegalAccessException | InvocationTargetException e) {
              showError("Error invoking the method");
            }
          });
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
          showError("Method doesn't exist or error invoking the method");
        }
        menu.getItems().add(menuItem);
      }
    }
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
      } catch (IOException | NullPointerException ex) {
        showError(myResources.getString("LoadTurtle"));
      }
    }
  }

  private static void showError(String message) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle(myResources.getString("IOError"));
    alert.setContentText(message);
    alert.showAndWait();
  }

  private void setUpBottomButtons() {
    GridPane buttonGrid = new GridPane();
    buttonGrid.setHgap(SPACING);
    buttonGrid.setVgap(SPACING);
    for(int i=0; i<BOTTOM_BUTTON_METHOD_NAMES.length; i++){
      Button button = makeButton(BOTTOM_BUTTON_METHOD_NAMES[i], RUN_BUTTON_SHAPE, this);
      button.setTooltip(new Tooltip(myResources.getString(BOTTOM_BUTTON_HOVER_NAMES[i])));
      buttonGrid.add(button, BOTTOM_BUTTON_POSITIONS.get(i).get(0), BOTTOM_BUTTON_POSITIONS.get(i).get(1));
    }
    myCenterVBox.getChildren().add(buttonGrid);
  }

  private void step(boolean overridePause){
    if(!paused || overridePause) {
      if (myDesiredTurtlePosition != null && (Math.abs(myCurrentTurtlePosition.getX() - myDesiredTurtlePosition.getX()) >= SIGNIFICANT_DIFFERENCE ||
              Math.abs(myCurrentTurtlePosition.getY() - myDesiredTurtlePosition.getY()) >= SIGNIFICANT_DIFFERENCE)) {
        myCurrentTurtlePosition = new Point2D(myCurrentTurtlePosition.getX() + xIncrement, myCurrentTurtlePosition.getY() + yIncrement);
        myTurtleView.getUnalteredTurtlePositions().put(myCurrentTurtleID, myCurrentTurtlePosition);
        myTurtleView.setTurtlePosition(myCurrentTurtlePosition.getX(), myCurrentTurtlePosition.getY(), myCurrentTurtleID);
        if (myStartPos != null) {
          myTurtleView.addPath(myStartPos, myCurrentTurtlePosition);
          myStartPos = myCurrentTurtlePosition;
        }
        updateTurtleInfo(myCurrentTurtleID);
      } else if (!isReady) {
        isReady = true;
        if (resultQueue.size() > 0) {
          processResult(null);
        }
      }
    }
  }

  private void updateTurtleInfo(int id) {
    Text turtleInfo = (Text) myTurtleInfo.getChildren().get(myTurtleView.getExistingTurtleIDs().indexOf(id));
    String[] activityAndHeading = myTurtleView.getTurtleInfo(id);
    turtleInfo.setText("Turtle " + id + ": \nActive: " + activityAndHeading[0] + "  Position: ("
            + (int)myTurtleView.getUnalteredTurtlePositions().get(id).getX() + "," + (int)myTurtleView.getUnalteredTurtlePositions().get(id).getY()
            + ")  Heading: " + activityAndHeading[1] + "\n");
  }

  private void displayErrorMessage(String message){
    myErrorMessage.setText(message);
  }

  private void addVariable(String name, double value){
    myVariables.addEntry(name + " : " + value, name, newValue->executeInstruction("make :"+name+" "+newValue));
  }

  private void addUserDefinedCommand(String name, String command){
    myUserDefinedCommands.addEntry(name + ":\n" + command, name, e->myCommandBox.setText(name));
  }

  private void executeInstruction(String instruction) {
    myHistory.addEntry(instruction, null, e->myCommandBox.setText(instruction));
    if(instruction != myCurrentlyHighlighted && isReady) { // want to compare object references here
      myHistory.highlightNext();
      myCurrentlyHighlighted = instruction;
    }
    myRightVBox.requestLayout(); // make sure everything is updated graphically
    myInstructionQueue.add(instruction);
  }

  private void displayHelp(){
    Stage stage = new Stage();
    stage.setTitle(myResources.getString("HelpWindowTitle"));
    VBox vBox = new VBox(SPACING);
    MenuBar menuBar = new MenuBar();
    vBox.getChildren().add(menuBar);
    Menu menu = new Menu(myResources.getString("HelpMenu"));
    menuBar.getMenus().add(menu);
    for(Map.Entry<String, String> helpCategory : HELP_CATEGORIES.entrySet()){
      MenuItem menuItem = new Menu(helpCategory.getKey());
      menuItem.setOnAction(event -> changeHelpImage(helpCategory.getValue(), vBox));
      menu.getItems().add(menuItem);
    }
    vBox.getChildren().add(new ImageView("slogo/frontEnd/Resources/" + DEFAULT_HELP_CATEGORY_FILE + ".png"));
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setContent(vBox);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    stage.setScene(new Scene(scrollPane, HELP_WINDOW_SHAPE.getWidth(), HELP_WINDOW_SHAPE.getHeight()));
    stage.show();
  }

  private void changeHelpImage(String imageName, VBox vBox){
    vBox.getChildren().remove(1);
    vBox.getChildren().add(new ImageView("slogo/frontEnd/Resources/" + imageName + ".png"));
  }
}
