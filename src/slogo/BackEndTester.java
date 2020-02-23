package slogo;

public class BackEndTester {

  public static void main(String[] args) {

    SLogoBackEnd parser = new SLogoBackEnd();
    parser.addPatterns("English");
    parser.addPatterns("Syntax");

//    String userInput = "fd 50 rt 90 BACK\n400 Left 1";
//    String userInput = "fd 50 right 20 FORWARD 10.0 RIGHT -500. [ fd 10 ]";
//    String userInput = "for [ :a fd 0.5 4 1 ] [ fd :a ] make :b 5 right :b";
    String userInput = "for [ :a 1 4 1 ] [ fd :a ] make :b 2 ifelse :b [ fd :b ] [ right 20.0 ] fd 1";
//    String userInput = "make :b 5 right :b";
    parser.parseScript(userInput);
  }
}
