package slogo.backend;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;
import static java.lang.Class.forName;

public class CommandFactory {
  private static final String RESOURCES_PACKAGE = "slogo/backend/resources/commands";
  private static final ResourceBundle RESOURCES = ResourceBundle.getBundle(RESOURCES_PACKAGE);
  private static final String PATH_TO_CLASSES = "slogo.backend.commands.";
  private static List<String> ALL_COMMANDS;


 /* private static final Map<String, Command> myCommands;
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
    myCommands = Collections.unmodifiableMap(newMap);
  } */
  private CommandFactory() {
    //This constructor exists to hide the implicit public constructor that would otherwise appear
    ALL_COMMANDS = new ArrayList<>();
    Enumeration<String> commands  = RESOURCES.getKeys();
    while(commands.hasMoreElements()){
      ALL_COMMANDS.add(commands.nextElement());
    }
  }


  public static Command makeCommand(String type) throws ParseException {
    try{
      String className = RESOURCES.getString(type);
      Class cls = forName(PATH_TO_CLASSES + className);
      Constructor cons = cls.getConstructor();
      Object obj = cons.newInstance();
      return (Command) obj;
    } catch(Exception e){
      throw new ParseException("Don't know how to " + type);
    }
  }

  public static boolean hasCommand(String type) {
    return ALL_COMMANDS.contains(type);
  }

  public static void main(String[] args) throws ParseException {
    CommandFactory.makeCommand("Forward");
  }
}
