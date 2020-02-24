package slogo.BackEnd;

public class SLogoTurtle implements Turtle {

    private double myX;
    private double myY;
    private double myHeading;
    private boolean myPenUp;
    private boolean myVisible;

    public SLogoTurtle(){
        myX = 0; myY = 0;
        myHeading = 0;
        myPenUp = true;
        myVisible = true;
    }

    //TODO: add new constructors besides default

    @Override
    public double getX() { return myX; }

    @Override
    public double getY() { return myY; }

    @Override
    public void setPos(double x, double y) {
        myX = x; myY = y;
    }

    @Override
    public void moveForward(double distance) {
        myX += Math.cos(myHeading * Math.PI / 180) * distance;
        myY += Math.sin(myHeading * Math.PI / 180) * distance;
    }

    @Override
    public void moveBack(double distance) {
        myX -= Math.cos(myHeading * Math.PI / 180) * distance;
        myY -= Math.sin(myHeading * Math.PI / 180) * distance;
    }

    @Override
    public void turn(double degrees) {
        double newHeading = myHeading + degrees;
        newHeading = newHeading % 360;
        if(newHeading < 0)
            newHeading += 360;
        myHeading = newHeading;
    }

    @Override
    public void setHeading(double direction) {
        direction = direction % 360;
        if(direction < 0)
            direction += 360;
        myHeading = direction;
    }

    @Override
    public double getHeading() { return myHeading; }

    @Override
    public boolean getPenUp() { return myPenUp; }

    @Override
    public void setPenUp(boolean up) { myPenUp = up; }

    @Override
    public boolean getVisible(){ return myVisible; }

    @Override
    public void setVisible(boolean vis){
        myVisible = vis;
    }
}
