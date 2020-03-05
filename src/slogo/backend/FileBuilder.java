package slogo.backend;

import java.util.List;
import java.util.Map;

public interface FileBuilder {
  void makeLibraryFile(Map<String, Double> variables, Map<String, List<String>> commandArgs, Map<String,String> commandContents);


  Map<String,Double> loadVariablesFromFile(String filepath);

  //TODO: Use lambdas to for-each this so that it is well-designed.
  Map<String,List<String>> loadCommandArguments(String filepath);

  Map<String,String> loadCommandInstructions(String filepath);
}
