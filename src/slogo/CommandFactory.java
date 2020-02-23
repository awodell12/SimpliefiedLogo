package slogo;

import java.util.HashMap;
import java.util.Map;
import slogo.commands.ForLoopCommand;
import slogo.commands.ForwardCommand;
import java.util.Collections;
import slogo.commands.IfCommand;
import slogo.commands.IfElseCommand;
import slogo.commands.MakeCommand;
import slogo.commands.RightCommand;

public class CommandFactory {
  private static final Map<String, AltCommand> myCommands;
  static {
    Map<String, AltCommand> newMap = new HashMap<>();
    newMap.put("Forward", new ForwardCommand());
    newMap.put("Right", new RightCommand());
    newMap.put("For", new ForLoopCommand());
    newMap.put("MakeVariable", new MakeCommand());
    newMap.put("If", new IfCommand());
    newMap.put("IfElse",new IfElseCommand());
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
