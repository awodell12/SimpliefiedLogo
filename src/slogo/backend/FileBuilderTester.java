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
    Map<String,Double> variables = Map.of("a",5.0,"b",3333.3);
    Map<String,List<String>> commandArgs = Map.of("foo",List.of("varfirst","varsecond"));
    Map<String,String> commandScript = Map.of("foo", "fd sum :varfirst foo");

    fileBuilder.makeLibraryFile(FILEPATH,variables,commandArgs,commandScript);
    Map<String, Double> varOut = fileBuilder.loadVariablesFromFile(FILEPATH);
    Map<String, String> commandInstructions = fileBuilder.loadCommandInstructions(FILEPATH);
    Map<String, List<String>> commandArguments = fileBuilder.loadCommandArguments(FILEPATH);

    for (String commandName : commandInstructions.keySet()) {
      manager.addUserCommand(commandName,commandArguments.get(commandName),
          BackEndUtil.getTokenList(commandInstructions.get(commandName)));
    }

    System.out.println("variables.toString() = " + varOut.toString());
    System.out.println("Args: " + manager.getArgumentsMap().toString());
    System.out.println("Instructions: " + manager.getScriptMap().toString());
//    Command func = manager.getCommand("func");
//    System.out.println(func);
//    System.out.println("func.getNumArgs() = " + func.getNumArgs());
  }
}
