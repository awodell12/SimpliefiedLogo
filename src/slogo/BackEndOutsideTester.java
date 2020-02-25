package slogo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import slogo.BackEnd.BackEndExternal;
import slogo.BackEnd.SLogoBackEnd;
import slogo.BackEnd.SLogoLanguageChanger;

public class BackEndOutsideTester {

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

      BackEndExternal parser = new SLogoBackEnd();
//    parser.addPatterns("English");
//    parser.addPatterns("Syntax");

//    String userInput = "fd 50 rt 90 BACK\n400 Left 1";
//    String userInput = "fd 50 [ right 20 ] repeat 5 [ FORWARD 10.0 ] RIGHT -500. [ fd 11.4 ]";
//    String userInput = "for [ :a fd 0.5 4 1 ] [ fd :a ] make :b 5 right :b";
//    String userInput = "for [ :a 1 4 1 ] [ fd :a ] make :b 2 ifelse :b [ fd :b ] [ right 20.0 ] fd :b";
//    String userInput = "right make :b 5 right :b";
      String userInput; // = "fd sum sum sum sum 10 20 30 5 5\n";
      parser.applyChanger(new SLogoLanguageChanger("Chinese"));
      userInput = new String(Files.readAllBytes(Paths.get("data/examples/languages/circle_chinese.logo")));
      parser.parseScript(userInput);
      parser.applyChanger(new SLogoLanguageChanger("English"));
      parser.parseScript("fd 10 rt "); //unexpected end of instructions
      parser.parseScript("fd 10 sum 1"); //unexpected end of instructions with multiple args
      parser.parseScript("10 fd 2"); //don't know what to do with 10
      parser.parseScript("to forward [ :dist ] [ fd :dist ] forward 20 sum 10 "); //can't redefine primitive
//    parser.parseScript("to spiral [ :size ] [ for [ :i 1 5 1 ] [ fd :size rt * :i 15 ] ] repeat 5 [ fd 10 spiral 18 ]"); //variable b isn't known
//    parser.parseScript("to spiral [ :size ] [ for [ :i 1 2 1 ] [ fd :size rt * :i 15 ] ] repeat 2 [ fd 10 spiral 18 ] fd 999.999"); //variable b isn't known
      parser.parseScript("make :b 3 ifelse greater? :b 2 [ fd 10 ] [ rt 45 ] fd 1.5");
      parser.parseScript("set :b 120 for [ :a 1 3 1 ] [ fd sum :b fd :a ] fd 1234.1234");
      parser.parseScript("to foo [ :distance ] [ fd :distance ] fd 3 foo foo foo 30.0\n"
          + "to foo [ :turn :fdamt :useless ] [ rt :turn forward :fdamt ] fd foo foo 88 21 -100.0 -200.0 0.1");
      List<CommandResult> results = parser.parseScript("fd 12.1 to banq [ :amount ] [ right 2019 ] rt sum 100 sum 1 repeat 5 [ ] fd sum 1.0 -2.12");
      System.out.println("results.size() = " + results.size());
      CommandResult result = results.get(results.size()-1);
      if (!result.getErrorMessage().equals("")) {
        System.out.println(result.getErrorMessage());
      }
      System.out.println("--------------------------------");
      parser.parseScript("to foo [ :n ] [ fd :n rt 45.0 if greater? 4 :n [ foo sum :n 3.12 ] ] foo 1");
//    parser.parseScript("to foo [ :n :useless :rightamt ] [ fd :n right :rightamt ] foo 100.0 1000.0 foo 3.0 1.0 -2.0");
//      parser.parseScript("to foo [ :n ] [ fd :n ] foo to foo [ :n ] [ right :n ] fd 100.0 foo 10.0");
//    System.out.println(SLogoBackEnd.distanceToEndBracketStatic(array));
    }
}
