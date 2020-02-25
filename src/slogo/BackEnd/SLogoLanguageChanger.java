package slogo.BackEnd;

public class SLogoLanguageChanger implements Changer {
  private String myLanguage;

  public SLogoLanguageChanger(String language) {
    myLanguage = language;
  }

  @Override
  public void doChanges(BackEndInternal backEnd) {
    backEnd.setLanguage(myLanguage);
  }
}
