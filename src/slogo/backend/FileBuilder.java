package slogo.backend;

import java.util.Map;

public interface FileBuilder {
  void makeLibraryFile(Map<String, Double> variables, Map<String,String> commands);

  Map<String,Double> loadVariablesFromFile(String filepath);

  Map<String,String> loadCommandsFromFile(String filepath);
}
