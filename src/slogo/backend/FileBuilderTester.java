package slogo.backend;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import slogo.backend.commands.controlandvariables.UserCommand;

public class FileBuilderTester {

  public static final String FILEPATH = "data/userlibraries/outputtest.xml";

  public static void main(String[] args) {
    FileBuilder fileBuilder = new SLogoFileBuilder();
    UserCommandManager manager = new UserCommandManager();
    Map<String, Double> variables = fileBuilder.loadVariablesFromFile(FILEPATH);
    Map<String, String> commandInstructions = fileBuilder.loadCommandInstructions(FILEPATH);
    Map<String, List<String>> commandArguments = fileBuilder.loadCommandArguments(FILEPATH);

    for (String commandName : commandInstructions.keySet()) {
      manager.addUserCommand(commandName,commandArguments.get(commandName),
          BackEndUtil.getTokenList(commandInstructions.get(commandName)));
    }

    System.out.println("variables.toString() = " + variables.toString());
    System.out.println("Args: " + manager.getArgumentsMap().toString());
    System.out.println("Instructions: " + manager.getScriptMap().toString());
    Command func = manager.getCommand("func");
    System.out.println(func);
    System.out.println("func.getNumArgs() = " + func.getNumArgs());
  }
}
