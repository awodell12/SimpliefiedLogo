package slogo.BackEnd;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import slogo.BackEnd.commands.booleancommands.AndCommand;
import slogo.BackEnd.commands.booleancommands.EqualCommand;
import slogo.BackEnd.commands.booleancommands.GreaterCommand;
import slogo.BackEnd.commands.booleancommands.LessCommand;
import slogo.BackEnd.commands.booleancommands.NotCommand;
import slogo.BackEnd.commands.booleancommands.NotEqualCommand;
import slogo.BackEnd.commands.booleancommands.OrCommand;
import slogo.BackEnd.commands.controlandvariables.DoTimesCommand;
import slogo.BackEnd.commands.controlandvariables.ForLoopCommand;
import slogo.BackEnd.commands.controlandvariables.IfCommand;
import slogo.BackEnd.commands.controlandvariables.IfElseCommand;
import slogo.BackEnd.commands.controlandvariables.MakeCommand;
import slogo.BackEnd.commands.controlandvariables.RepeatCommand;
import slogo.BackEnd.commands.controlandvariables.ToCommand;
import slogo.BackEnd.commands.mathcommands.ArcTanCommand;
import slogo.BackEnd.commands.mathcommands.CosCommand;
import slogo.BackEnd.commands.mathcommands.DiffCommand;
import slogo.BackEnd.commands.mathcommands.MinusCommand;
import slogo.BackEnd.commands.mathcommands.MultCommand;
import slogo.BackEnd.commands.mathcommands.NatLogCommand;
import slogo.BackEnd.commands.mathcommands.PiCommand;
import slogo.BackEnd.commands.mathcommands.PowerCommand;
import slogo.BackEnd.commands.mathcommands.QuotientCommand;
import slogo.BackEnd.commands.mathcommands.RandomCommand;
import slogo.BackEnd.commands.mathcommands.RemainderCommand;
import slogo.BackEnd.commands.mathcommands.SinCommand;
import slogo.BackEnd.commands.mathcommands.SumCommand;
import slogo.BackEnd.commands.mathcommands.TanCommand;
import slogo.BackEnd.commands.turtlecommands.BackCommand;
import slogo.BackEnd.commands.turtlecommands.ClearScreenCommand;
import slogo.BackEnd.commands.turtlecommands.ForwardCommand;
import slogo.BackEnd.commands.turtlecommands.GoHomeCommand;
import slogo.BackEnd.commands.turtlecommands.HideTurtleCommand;
import slogo.BackEnd.commands.turtlecommands.LeftCommand;
import slogo.BackEnd.commands.turtlecommands.PenDownCommand;
import slogo.BackEnd.commands.turtlecommands.PenUpCommand;
import slogo.BackEnd.commands.turtlecommands.RightCommand;
import slogo.BackEnd.commands.turtlecommands.SetHeadingCommand;
import slogo.BackEnd.commands.turtlecommands.SetPosCommand;
import slogo.BackEnd.commands.turtlecommands.ShowTurtleCommand;
import slogo.BackEnd.commands.turtlecommands.TowardCommand;
import slogo.BackEnd.commands.turtlequeries.HeadingQuery;
import slogo.BackEnd.commands.turtlequeries.IsPenDownQuery;
import slogo.BackEnd.commands.turtlequeries.IsShowingQuery;
import slogo.BackEnd.commands.turtlequeries.XCorQuery;
import slogo.BackEnd.commands.turtlequeries.YCorQuery;

public class CommandFactory {
  private static final Map<String, Command> myCommands;
  static {
    Map<String, Command> newMap = new HashMap<>();
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
    newMap.put("SetTowards", new TowardCommand());
    newMap.put("Or", new OrCommand());
    newMap.put("And", new AndCommand());
    newMap.put("Not", new NotCommand());
    newMap.put("XCoordinate", new XCorQuery());
    newMap.put("YCoordinate", new YCorQuery());
    newMap.put("Heading", new HeadingQuery());
    newMap.put("IsPenDown", new IsPenDownQuery());
    newMap.put("IsShowing", new IsShowingQuery());
    myCommands = Collections.unmodifiableMap(newMap);
  }

  private CommandFactory() {
    //This constructor exists to hide the implicit public constructor that would otherwise appear
  }

  public static Command makeCommand(String type) throws ParseException {
    if (myCommands.containsKey(type)) {
      return myCommands.get(type);
    }
    throw new ParseException("Don't know how to " + type);
  }

  public static boolean hasCommand(String type) {
    return myCommands.containsKey(type);
  }
}
