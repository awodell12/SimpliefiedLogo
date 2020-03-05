package slogo.backend;

public class SLogoLanguageChanger implements Changer {
  private String myLanguage;

  public SLogoLanguageChanger(String language) {
    myLanguage = language;
  }

  @Override
  public void doChanges(Interpreter backEnd) {
    backEnd.setLanguage(myLanguage);
  }
}
