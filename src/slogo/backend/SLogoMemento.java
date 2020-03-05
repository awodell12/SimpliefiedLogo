package slogo.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class SLogoMemento {

  private List<Turtle> myTurtles;
  private List<Integer> myActiveTurtleIDs;
  private Map<Integer, List<Integer>> myPalette;
  private int myBackGroundIndex;
  private int myPenColorIndex;
  private int myShapeIndex;
  private double myPenSize;
  private Map<String, Double> myVariables;
  private UserCommandManager myUserCommandManager;

  public SLogoMemento(List<Turtle> turtles, List<Integer> activeTurtles, Map<Integer,List<Integer>> palette,
                      int bgIndex, int pcIndex, int shapeIndex, double penSize,
                      Map<String,Double> variables, UserCommandManager manager) {
    myTurtles = new ArrayList<>(turtles);
    myActiveTurtleIDs = new ArrayList<>(activeTurtles);
    myPalette = palette;
    myBackGroundIndex = bgIndex;
    myPenColorIndex = pcIndex;
    myShapeIndex = shapeIndex;
    myPenSize = penSize;
    myVariables = variables;
    myUserCommandManager = manager;
  }

  public List<Turtle> getTurtles() {
    List<Turtle> retList = new ArrayList<>();
    for (Turtle turtle : myTurtles) {
      retList.add(turtle.getClone());
    }
    return retList;
  }

  public List<Integer> getActiveTurtles() {
    return new ArrayList<>(myActiveTurtleIDs);
  }

  public Map<Integer,List<Integer>> getPalette() {
    return new HashMap<>(myPalette);
  }

  public int getBackgroundIndex() {
    return myBackGroundIndex;
  }

  public int getPenColorIndex() {
    return myPenColorIndex;
  }

  public int getShapeIndex() {
    return myShapeIndex;
  }

  public double getPenSize() {
    return myPenSize;
  }

  public Map<String, Double> getVariables() {
    return new HashMap<>(myVariables);
  }

  public UserCommandManager getUserCommandManager() {
    return new UserCommandManager(myUserCommandManager);
  }
}
