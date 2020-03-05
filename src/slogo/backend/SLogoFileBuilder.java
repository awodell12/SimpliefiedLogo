package slogo.backend;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SLogoFileBuilder implements FileBuilder{
  public static final String FILEPATH = "data/userlibraries/outputtest.xml";

  public void makeXMLFile(String xmlFilePath, Map<String,Double> varMap, Map<String, List<String>> argMap, Map<String,String> instructionMap) {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = factory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();
      Element userLib = doc.createElement("UserLib");
      Element varList = constructCommandList(argMap, instructionMap,doc);
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

  private Element makeCommandElement(Document doc,  String name, List<String> arguments, String contents) {
    Element command = doc.createElement("Command");
    command.setAttribute("name", name);
    command.setAttribute("contents", contents);
    for (String arg : arguments) {
      Element enumArg = doc.createElement("argument");
      enumArg.setAttribute("label",arg);
      command.appendChild(enumArg);
    }
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

    Map<String, List<String>> commandArgs = new HashMap<>();
    commandArgs.put("func",List.of("arg","farg"));

    Map<String,String> commandContents = new HashMap<>();
    commandContents.put("func","fd 50");

    new SLogoFileBuilder().makeXMLFile(FILEPATH, vars, commandArgs, commandContents);
    Map<String, Double> variableMap = new SLogoFileBuilder().loadVariablesFromFile(FILEPATH);
    System.out.println(variableMap.toString());
    Map<String, String> commandMap = new SLogoFileBuilder().loadCommandInstructions(FILEPATH);
    System.out.println(commandMap.toString());
    Map<String, List<String>> commandArgMap = new SLogoFileBuilder().loadCommandArguments(FILEPATH);
    System.out.println(commandArgMap.toString());

  }

  private Element constructVarList(Map<String,Double> varMap, Document doc) {
    Element varList = doc.createElement("VariableList");
    for (Entry variable : varMap.entrySet()) {
      varList.appendChild(makeVariableElement(doc,variable.getKey().toString(), variable.getValue().toString()));
    }
    return varList;
  }

  private Element constructCommandList(Map<String,List<String>> argMap, Map<String,String> instructionMap, Document doc) {
    Element commandList = doc.createElement("CommandsList");
    for (String varName : instructionMap.keySet()) {
      commandList.appendChild(makeCommandElement(doc,varName, argMap.get(varName), instructionMap.get(varName)));
    }
    return commandList;
  }

  @Override
  public Map<String,String> loadCommandInstructions(String filepath) {
    File info = new File(filepath);
    try {
      NodeList commandsInFile = getNodesWithTag(info, "Command");
      Map<String,List<String>> commandArguments = new HashMap<>();
      Map<String,String> commandInstructions = new HashMap<>();
      for (int i = 0; i < commandsInFile.getLength(); i ++) {
        String cmdName = commandsInFile.item(i).getAttributes().getNamedItem("name").getTextContent();
        String content = commandsInFile.item(i).getAttributes().getNamedItem("contents").getTextContent();
        commandInstructions.put(cmdName,content);
      }
      return commandInstructions;
    } catch (Exception e) {
      return new HashMap<>();
    }
  }

  //TODO: Use lambdas to for-each this so that it is well-designed.
  @Override
  public Map<String,List<String>> loadCommandArguments(String filepath) {
    File info = new File(filepath);
    try {
      NodeList commandsInFile = getNodesWithTag(info, "Command");
      Map<String,List<String>> commandArguments = new HashMap<>();
      for (int i = 0; i < commandsInFile.getLength(); i ++) {
        String cmdName = commandsInFile.item(i).getAttributes().getNamedItem("name").getTextContent();
        NodeList argsInCommand = commandsInFile.item(i).getChildNodes();
        List<String> args = new ArrayList<>();
        for (int j = 1; j < argsInCommand.getLength(); j += 2) {
          args.add(argsInCommand.item(j).getAttributes().item(0).getTextContent());
        }
        commandArguments.put(cmdName,args);
      }
      return commandArguments;
    } catch (Exception e) {
      return new HashMap<>();
    }
  }

  @Override
  public Map<String,Double> loadVariablesFromFile(String filepath) {
    File info = new File(filepath);
    try {
      NodeList variables = getNodesWithTag(info, "Variable");
      Map<String,Double> vars = new HashMap<>();
      for (int i = 0; i < variables.getLength(); i ++) {
        String varName = variables.item(i).getAttributes().getNamedItem("name").getTextContent();
        double varValue = Double.parseDouble(variables.item(i).getAttributes().getNamedItem("value").getTextContent());
        vars.put(varName,varValue);
      }
      return vars;
    } catch (SAXException | IOException | ParserConfigurationException e) {
      return new HashMap<>();
    }
  }

  private NodeList getNodesWithTag(File info, String tag)
      throws ParserConfigurationException, SAXException, IOException {
    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = docFactory.newDocumentBuilder();
    Document doc = builder.parse(info);
    Element root = doc.getDocumentElement();
    return root.getElementsByTagName(tag);
  }

  @Override
  public void makeLibraryFile(Map<String, Double> variables, Map<String, List<String>> commandArgs, Map<String,String> commandContents) {
    makeXMLFile(FILEPATH, variables, commandArgs, commandContents);
  }
}