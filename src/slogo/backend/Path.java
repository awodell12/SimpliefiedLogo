package slogo.backend;


import java.util.ArrayList;
import java.util.List;


public class Path {

    private List<Double> myStart;
    private List<Double> myEnd;
    private String myColor;

    public Path(double startX, double startY, double endX, double endY, String color){
        myStart = new ArrayList<>(2);
        myStart.set(0, startX); myStart.set(1, startY);
        myEnd = new ArrayList<>(2);
        myEnd.set(0, endX); myEnd.set(1, endY);
        myColor = color;

    }

}
