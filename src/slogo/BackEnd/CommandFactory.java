package slogo.BackEnd;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import slogo.BackEnd.commands.*;

public class CommandFactory {
  private static final Map<String, AltCommand> myCommands;
  static {
    Map<String, AltCommand> newMap = new HashMap<>();
    newMap.put("Forward", new ForwardCommand());
    newMap.put("Backward", new BackCommand());
    newMap.put("Right", new RightCommand());
    newMap.put("Left", new LeftCommand());
    newMap.put("SetHeading", new SetHeadingCommand());
    newMap.put("For", new ForLoopCommand());
    newMap.put("MakeVariable", new MakeCommand());
    newMap.put("If", new IfCommand());
    newMap.put("IfElse",new IfElseCommand());
    newMap.put("Repeat", new RepeatCommand());
    newMap.put("DoTimes", new DoTimesCommand());
    newMap.put("Sum", new SumCommand());
    newMap.put("Product", new MultCommand());
    newMap.put("GreaterThan",new GreaterCommand());
    newMap.put("MakeUserInstruction",new ToCommand());
    myCommands = Collections.unmodifiableMap(newMap);
  }


  public CommandFactory() {
  }

  public static AltCommand makeCommand(String type) throws ParseException {
    if (myCommands.containsKey(type)) {
      return myCommands.get(type);
    }
    throw new ParseException();
  }

  public static boolean hasCommand(String type) {
    return myCommands.containsKey(type);
  }
}
