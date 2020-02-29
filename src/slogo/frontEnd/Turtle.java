package slogo.frontEnd;


import javafx.scene.image.ImageView;

public class Turtle extends ImageView {
  private boolean isActive = true;
  private boolean visibility = true;

  public Turtle(String imageLocation){
    super(imageLocation);
  }

  protected boolean isActive(){
    return isActive;
  }

  protected void setActive(boolean value){
    isActive = value;
  }

  protected boolean getVisibility(){
    return visibility;
  }

  protected void setVisibility(boolean value){
    visibility = value;
  }
}
