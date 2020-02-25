package slogo.BackEnd;

import java.util.List;

public class SLogoTurtle implements Turtle {

    private double myX;
    private double myY;
    private double myHeading;
    private boolean myPenUp;
    private boolean myVisible;
    private String myPenColor;

    public SLogoTurtle(){
        myX = 0; myY = 0;
        myHeading = 0;
        myPenUp = true;
        myVisible = true;
        myPenColor = "000000";
    }

    //TODO: add new constructors besides default

    @Override
    public double getX() { return myX; }

    @Override
    public double getY() { return myY; }

    /**
     * @return Current position as a list containing x then y
     */
    public List<Double> getPosition() {
        return List.of(myX,myY);
    }

    @Override
    public double setPos(double x, double y) {
        double distanceTraveled = Math.sqrt(Math.pow(myX - x, 2) + Math.pow(myY - y, 2));
        myX = x; myY = y;
        return distanceTraveled;
    }

    @Override
    public double moveForward(double distance) {
        myX += Math.cos(myHeading * Math.PI / 180) * distance;
        myY += Math.sin(myHeading * Math.PI / 180) * distance;
        return distance;
    }

    @Override
    public double moveBack(double distance) {
        myX -= Math.cos(myHeading * Math.PI / 180) * distance;
        myY -= Math.sin(myHeading * Math.PI / 180) * distance;
        return distance;
    }

    @Override
    public double turn(double degrees) {
        double newHeading = myHeading + degrees;
        newHeading = newHeading % 360;
        if(newHeading < 0)
            newHeading += 360;
        myHeading = newHeading;
        return Math.abs(degrees);
    }

    @Override
    public double setHeading(double direction) {
        double normalizedDir = direction % 360;
        if(normalizedDir < 0)
            normalizedDir += 360;
        double temp = myHeading;
        myHeading = normalizedDir;
        return Math.abs(temp - normalizedDir);
    }

    @Override
    public double getHeading() { return myHeading; }

    @Override
    public boolean getPenUp() { return myPenUp; }

    @Override
    public int setPenUp(boolean up) {
        myPenUp = up;
        if(up)
            return 1;
        return 0;
    }

    @Override
    public boolean getVisible(){ return myVisible; }

    @Override
    public int setVisible(boolean vis){
        myVisible = vis;
        if(vis)
            return 1;
        return 0;
    }
}
