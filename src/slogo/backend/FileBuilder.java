package slogo.backend;

import java.util.List;
import java.util.Map;

public interface FileBuilder {
  void makeLibraryFile(Map<String, Double> variables, Map<String, List<String>> commandArgs, Map<String,String> commandContents);


  Map<String,Double> loadVariablesFromFile(String filepath);

  Map<String,List<String>> loadCommandArguments(String filepath);

  Map<String,String> loadCommandInstructions(String filepath);
}
