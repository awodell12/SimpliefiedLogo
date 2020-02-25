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
    newMap.put("SetPosition", new SetPosCommand());
    newMap.put("PenUp", new PenUpCommand());
    newMap.put("PenDown", new PenDownCommand());
    newMap.put("ShowTurtle", new ShowTurtleCommand());
    newMap.put("HideTurtle", new HideTurtleCommand());
    newMap.put("Home", new GoHomeCommand());
    newMap.put("ClearScreen", new ClearScreenCommand());
    newMap.put("For", new ForLoopCommand());
    newMap.put("MakeVariable", new MakeCommand());
    newMap.put("If", new IfCommand());
    newMap.put("IfElse",new IfElseCommand());
    newMap.put("Repeat", new RepeatCommand());
    newMap.put("DoTimes", new DoTimesCommand());
    newMap.put("Sum", new SumCommand());
    newMap.put("Difference", new DiffCommand());
    newMap.put("Quotient", new QuotientCommand());
    newMap.put("Remainder", new RemainderCommand());
    newMap.put("Minus", new MinusCommand());
    newMap.put("Random", new RandomCommand());
    newMap.put("Product", new MultCommand());
    newMap.put("Sine", new SinCommand());
    newMap.put("Cosine", new CosCommand());
    newMap.put("Tangent", new TanCommand());
    newMap.put("ArcTangent", new ArcTanCommand());
    newMap.put("NaturalLog", new NatLogCommand());
    newMap.put("Power", new PowerCommand());
    newMap.put("Pi", new PiCommand());
    newMap.put("GreaterThan",new GreaterCommand());
    newMap.put("LessThan", new LessCommand());
    newMap.put("MakeUserInstruction", new ToCommand());
    newMap.put("Equal", new EqualCommand());
    newMap.put("NotEqual", new NotEqualCommand());
    myCommands = Collections.unmodifiableMap(newMap);
  }

  public static AltCommand makeCommand(String type) throws ParseException {
    if (myCommands.containsKey(type)) {
      return myCommands.get(type);
    }
    throw new ParseException("Don't know how to " + type);
  }

  public static boolean hasCommand(String type) {
    return myCommands.containsKey(type);
  }
}
