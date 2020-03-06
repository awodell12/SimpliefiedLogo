package slogo.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import slogo.backend.commands.controlandvariables.UserCommand;

public class UserCommandManager {

  private Map<String, UserCommand> myCommands;

  public UserCommandManager() {
    myCommands = new HashMap<>();
  }

  public UserCommandManager(Map<String,List<String>> argMap, Map<String,String> instructionMap) {
    //Looping through keySet() because there are two maps with matching keys
    // (the file builder ensures that the keys match before they are stored).
    myCommands = new HashMap<>();
    for (String cmdName : argMap.keySet()) {
      addUserCommand(cmdName,argMap.get(cmdName),BackEndUtil.getTokenList((instructionMap.get(cmdName))));
    }
  }

  public UserCommandManager(UserCommandManager original) {
    myCommands = original.myCommands;
  }

  public boolean containsCommand(String name) {
    return myCommands.containsKey(name);
  }

  public void addUserCommand(String name, List<String> arguments,List<String> commands) {
    UserCommand newCommand = new UserCommand(arguments,commands);
    myCommands.put(name,newCommand);
  }

  public List<String> getArguments(String name) {
    return new ArrayList<>(myCommands.get(name).getArguments());
  }

  public Map<String, List<String>> getArgumentsMap() {
    Map<String,List<String>> argMap = new HashMap<>();
    for (String commandName : myCommands.keySet()) {
      argMap.put(commandName,getArguments(commandName));
    }
    return argMap;
  }

  public String getCommandScript(String name) {
    return myCommands.get(name).getInstructions();
  }

  public Map<String, String> getScriptMap() {
    Map<String,String> scriptMap = new HashMap<>();
    for (String commandName : myCommands.keySet()) {
      scriptMap.put(commandName,getCommandScript(commandName));
    }
    return scriptMap;
  }

  public Command getCommand(String name) {
    return myCommands.get(name);
  }
}
