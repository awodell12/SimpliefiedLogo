package slogo.frontEnd;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * The purpose of this class is to be able to save properties to a .properties file for later use.
 * Assuming all the necessary properties are passed as a map into the constructor it will work.
 * Might adjust so there is defaults and then the passed Map can override the defaults. 
 */
public class PropertiesWriter {

  /**
   * Constructor that does the writing to a new file
   * @param fileName the name of the file to be put in front end resources file. Must end with .properties
   * @param map The map that will be converted to the properties file.
   */
  public PropertiesWriter(String fileName, Map<String,String> map) {
    try (OutputStream output = new FileOutputStream("src/slogo/frontEnd/Resources/"+fileName)) {

      Properties prop = new Properties();

      // set the properties value
      for (String s : map.keySet()){
        prop.setProperty(s, map.get(s));
      }

      // save properties to project root folder
      prop.store(output, null);


    } catch (IOException io) {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setContentText(io.getMessage());
      alert.showAndWait();
    }

  }
}