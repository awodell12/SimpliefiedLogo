package slogo.backend;

import java.util.ArrayList;
import java.util.List;

public class SLogoCareTaker {
  private List<SLogoMemento> myMementos;

  public SLogoCareTaker() {
    myMementos = new ArrayList<>();
  }

  public void add(SLogoMemento memento) {
    myMementos.add(memento);
  }

  //TODO: add error checking
  public SLogoMemento get(int index) {
    return myMementos.get(index);
  }

  public SLogoMemento pop() {
    SLogoMemento ret = myMementos.get(myMementos.size() -1);
    myMementos.remove(ret);
    return ret;
  }
}
