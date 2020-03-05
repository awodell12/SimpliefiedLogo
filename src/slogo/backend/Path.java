package slogo.backend;


import java.util.ArrayList;
import java.util.List;


public class Path {

    public static final int PATH_DIMENSIONS = 2;

    private List<Double> myStart;
    private List<Double> myEnd;
    private String myColor;

    public Path(double startX, double startY, double endX, double endY, String color){
        myStart = new ArrayList<>(PATH_DIMENSIONS);
        myStart.set(0, startX);
        myStart.set(1, startY);
        myEnd = new ArrayList<>(PATH_DIMENSIONS);
        myEnd.set(0, endX);
        myEnd.set(1, endY);
        myColor = color;

    }

}
