package slogo.backend;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FileBuilder {
  private Map<String, Double> myVariables;
  private Map<String, Command> myUserCommands;

  public void makeXMLFile(String xmlFilePath, Map<String,Double> varMap, Map<String,String> commandMap) {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = factory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();
      Element userLib = doc.createElement("UserLib");
      Element varList = constructCommandList(commandMap,doc);
      Element commandList = constructVarList(varMap,doc);;
      doc.appendChild(userLib);
      userLib.appendChild(commandList);
      userLib.appendChild(varList);

      File xmlFile = new File(xmlFilePath);
      StreamResult streamResult = new StreamResult(xmlFile);
      DOMSource domSource = new DOMSource(doc);

      makeTransformer().transform(domSource,streamResult);
    } catch (ParserConfigurationException | TransformerConfigurationException e) {

    } catch (TransformerException e) {

    }
  }

  private Transformer makeTransformer() throws TransformerConfigurationException {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    return transformer;
  }

  private Element makeCommandElement(Document doc, String name, String contents) {
    Element command = doc.createElement("Command");
    command.setAttribute("name", name);
    command.setAttribute("contents", contents);
    return command;
  }

  private Element makeVariableElement(Document doc, String name, String value) {
    Element command = doc.createElement("Variable");
    command.setAttribute("name", name);
    command.setAttribute("value", value);
    return command;
  }

  public static void main(String[] args) {
    Map<String, Double> vars = new HashMap<>();
    vars.put("a", 44.4);
    vars.put("b", 99.0);

    Map<String,String> commands = new HashMap<>();
    commands.put("func","fd 50");
    new FileBuilder().makeXMLFile("data/userlibraries/outputTest.xml", vars, commands);
  }

  private Element constructVarList(Map<String,Double> varMap, Document doc) {
    Element varList = doc.createElement("VariableList");
    for (String varName : varMap.keySet()) {
      varList.appendChild(makeVariableElement(doc,varName, varMap.get(varName).toString()));
    }
    return varList;
  }

  private Element constructCommandList(Map<String,String> commandMap, Document doc) {
    Element commandList = doc.createElement("CommandsList");
    for (String varName : commandMap.keySet()) {
      commandList.appendChild(makeVariableElement(doc,varName, commandMap.get(varName)));
    }
    return commandList;
  }
}
