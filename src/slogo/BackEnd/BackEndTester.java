package slogo.BackEnd;

public class BackEndTester {

  public static void main(String[] args) {

    SLogoBackEnd parser = new SLogoBackEnd();
    parser.addPatterns("English");
    parser.addPatterns("Syntax");

//    String userInput = "fd 50 rt 90 BACK\n400 Left 1";
//    String userInput = "fd 50 [ right 20 ] repeat 5 [ FORWARD 10.0 ] RIGHT -500. [ fd 11.4 ]";
//    String userInput = "for [ :a fd 0.5 4 1 ] [ fd :a ] make :b 5 right :b";
//    String userInput = "for [ :a 1 4 1 ] [ fd :a ] make :b 2 ifelse :b [ fd :b ] [ right 20.0 ] fd :b";
//    String userInput = "make :b 5 right :b";
    String userInput = "fd for [ :a 0 4 1 ] [ fd :a ]";
    parser.parseScript(userInput);
  }
}
