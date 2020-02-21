package slogo.BackEnd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class BackEndTester {

  /**
   * Commands fully implemented:
   * MAKE|SET
   *  (+) assigns the value of expr to variable, creating the variable if necessary
   *  (+) returns expr
   * REPEAT
   *  (+) runs given commands the given number of times
   *  (+) returns the value of the final command executed ( or 0 if no commands were executed )
   *  (+) sets :repcount to be equal to the value of the current iteration starting at 1.
   * DOTIMES
   *  (+) runs command(s) for each value specified in the range, i.e., from (1 - limit) inclusive
   *  (+) returns the value of the final command executed (or 0 if no commands are executed)
   *  (+) note, variable is assigned to each succeeding value so that it can be accessed by the command(s)
   * FOR
   *  (+) runs command(s) for each value specified in the range, i.e., from (start - end), going by increment
   *  (+) returns the value of the final command executed (or 0 if no commands are executed)
   *  (+) note, variable is assigned to each succeeding value so that it can be accessed by the command(s)
   * IF
   *  (+) if expr is not 0, runs the command(s) given in the list
   *  (+) returns the value of the final command executed (or 0 if no commands are executed)
   * IFELSE
   *  (+) if expr is not 0, runs the trueCommands given in the first list, otherwise runs the falseCommands given in the second list
   *  (+) returns the value of the final command executed (or 0 if no commands are executed)
   */
  public static void main(String[] args) throws IOException {

    SLogoBackEnd parser = new SLogoBackEnd();
    parser.addPatterns("English");
    parser.addPatterns("Syntax");

//    String userInput = "fd 50 rt 90 BACK\n400 Left 1";
//    String userInput = "fd 50 [ right 20 ] repeat 5 [ FORWARD 10.0 ] RIGHT -500. [ fd 11.4 ]";
//    String userInput = "for [ :a fd 0.5 4 1 ] [ fd :a ] make :b 5 right :b";
//    String userInput = "for [ :a 1 4 1 ] [ fd :a ] make :b 2 ifelse :b [ fd :b ] [ right 20.0 ] fd :b";
//    String userInput = "right make :b 5 right :b";
    String userInput; // = "fd sum sum sum sum 10 20 30 5 5\n";
//    File logoFile = new File("data/examples/simple/forward.logo");
    userInput = new String(Files.readAllBytes(Paths.get("data/examples/simple/forward_forward.logo")));
    parser.parseScript(userInput);
  }
}
