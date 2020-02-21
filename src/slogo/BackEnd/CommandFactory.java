package slogo.BackEnd;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import slogo.BackEnd.commands.*;

public class CommandFactory {
  private static Map<String, AltCommand> myCommands;
  static {
    Map<String, AltCommand> newMap = new HashMap<>();
    newMap.put("Forward", new ForwardCommand());
    newMap.put("Right", new RightCommand());
    newMap.put("For", new ForLoopCommand());
    newMap.put("MakeVariable", new MakeCommand());
    newMap.put("If", new IfCommand());
    newMap.put("IfElse",new IfElseCommand());
    newMap.put("Repeat", new RepeatCommand());
    myCommands = Collections.unmodifiableMap(newMap);
  }


  public CommandFactory() {
  }

  //TODO: Make this throw a "DON'T KNOW HOW TO" exception.
  public static AltCommand makeCommand(String type) {
    if (myCommands.containsKey(type)) {
      return myCommands.get(type);
    }
    return null;
  }
}
