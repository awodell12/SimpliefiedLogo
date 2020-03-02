package slogo.backend;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import slogo.backend.commands.AskCommand;
import slogo.backend.commands.IdCommand;
import slogo.backend.commands.TellCommand;
import slogo.backend.commands.booleancommands.AndCommand;
import slogo.backend.commands.booleancommands.EqualCommand;
import slogo.backend.commands.booleancommands.GreaterCommand;
import slogo.backend.commands.booleancommands.LessCommand;
import slogo.backend.commands.booleancommands.NotCommand;
import slogo.backend.commands.booleancommands.NotEqualCommand;
import slogo.backend.commands.booleancommands.OrCommand;
import slogo.backend.commands.controlandvariables.DoTimesCommand;
import slogo.backend.commands.controlandvariables.ForLoopCommand;
import slogo.backend.commands.controlandvariables.IfCommand;
import slogo.backend.commands.controlandvariables.IfElseCommand;
import slogo.backend.commands.controlandvariables.MakeCommand;
import slogo.backend.commands.controlandvariables.RepeatCommand;
import slogo.backend.commands.controlandvariables.ToCommand;
import slogo.backend.commands.mathcommands.ArcTanCommand;
import slogo.backend.commands.mathcommands.CosCommand;
import slogo.backend.commands.mathcommands.DiffCommand;
import slogo.backend.commands.mathcommands.MinusCommand;
import slogo.backend.commands.mathcommands.MultCommand;
import slogo.backend.commands.mathcommands.NatLogCommand;
import slogo.backend.commands.mathcommands.PiCommand;
import slogo.backend.commands.mathcommands.PowerCommand;
import slogo.backend.commands.mathcommands.QuotientCommand;
import slogo.backend.commands.mathcommands.RandomCommand;
import slogo.backend.commands.mathcommands.RemainderCommand;
import slogo.backend.commands.mathcommands.SinCommand;
import slogo.backend.commands.mathcommands.SumCommand;
import slogo.backend.commands.mathcommands.TanCommand;
import slogo.backend.commands.turtlecommands.BackCommand;
import slogo.backend.commands.turtlecommands.ClearScreenCommand;
import slogo.backend.commands.turtlecommands.ForwardCommand;
import slogo.backend.commands.turtlecommands.GoHomeCommand;
import slogo.backend.commands.turtlecommands.HideTurtleCommand;
import slogo.backend.commands.turtlecommands.LeftCommand;
import slogo.backend.commands.turtlecommands.PenDownCommand;
import slogo.backend.commands.turtlecommands.PenUpCommand;
import slogo.backend.commands.turtlecommands.RightCommand;
import slogo.backend.commands.turtlecommands.SetHeadingCommand;
import slogo.backend.commands.turtlecommands.SetPosCommand;
import slogo.backend.commands.turtlecommands.ShowTurtleCommand;
import slogo.backend.commands.turtlecommands.TowardCommand;
import slogo.backend.commands.turtlequeries.HeadingQuery;
import slogo.backend.commands.turtlequeries.IsPenDownQuery;
import slogo.backend.commands.turtlequeries.IsShowingQuery;
import slogo.backend.commands.turtlequeries.XCorQuery;
import slogo.backend.commands.turtlequeries.YCorQuery;

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
    newMap.put("Tell", new TellCommand());
    newMap.put("ID", new IdCommand());
    newMap.put("Ask", new AskCommand());
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
