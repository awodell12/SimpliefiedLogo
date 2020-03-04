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

  private CommandFactory() {
    //This constructor exists to hide the implicit public constructor that would otherwise appear
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
    return RESOURCES.containsKey(type);
  }

  public static void main(String[] args) throws ParseException {
    CommandFactory.makeCommand("Forward");
  }
}
