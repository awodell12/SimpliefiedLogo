package slogo.backend;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SLogoFileBuilder implements FileBuilder{
  public static final String FILEPATH = "data/userlibraries/outputtest.xml";

  public void makeXMLFile(String xmlFilePath, Map<String,Double> varMap, Map<String, List<String>> argMap, Map<String,String> instructionMap) {
    try {
      Document doc = instantiateDocument();
      Element userLib = doc.createElement("UserLib");
      try {
        Element varList = constructCommandList(argMap, instructionMap,doc);
        userLib.appendChild(varList);
      } catch (FileBuilderException e) {
        Element errorInfo = doc.createElement("Invalid");
        userLib.appendChild(errorInfo);
      }
      Element commandList = constructVarList(varMap,doc);;
      doc.appendChild(userLib);
      userLib.appendChild(commandList);

      File xmlFile = new File(xmlFilePath);
      StreamResult streamResult = new StreamResult(xmlFile);
      DOMSource domSource = new DOMSource(doc);

      makeTransformer().transform(domSource,streamResult);
    } catch (ParserConfigurationException | TransformerException e) {
      //Failed to make XMLFile due to factors outside the scope of the program to fix.
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

  private Element constructVarList(Map<String,Double> varMap, Document doc) {
    Element varList = doc.createElement("VariableList");
    for (Entry variable : varMap.entrySet()) {
      varList.appendChild(makeVariableElement(doc,variable.getKey().toString(), variable.getValue().toString()));
    }
    return varList;
  }

  private Element constructCommandList(Map<String,List<String>> argMap, Map<String,String> instructionMap, Document doc)
      throws FileBuilderException {
    Element commandList = doc.createElement("CommandsList");
    //Traverses through by key instead of by entry because there are two maps that
    //share a keySet. An error is thrown and handled if this isn't the case.
    if (!instructionMap.keySet().equals(argMap.keySet())) {
      throw new FileBuilderException("Script map not aligned with argument map for commands.");
    }
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

  @Override
  public Map<String,List<String>> loadCommandArguments(String filepath) {
    File info = new File(filepath);
    try {
      NodeList commandsInFile = getNodesWithTag(info, "Command");
      Map<String,List<String>> commandArguments = new HashMap<>();
      for (int i = 0; i < commandsInFile.getLength(); i ++) {
        Entry<String,List<String>> argEntry = makeCommandArgumentEntry(commandsInFile.item(i));
        commandArguments.put(argEntry.getKey(),argEntry.getValue());
      }
      return commandArguments;
    } catch (Exception e) {
      e.printStackTrace();
      return new HashMap<>();
    }
  }

  private Entry<String, List<String>> makeCommandArgumentEntry(Node command) {
    String cmdName = command.getAttributes().getNamedItem("name").getTextContent();
    NodeList argsInCommand = command.getChildNodes();
    List<String> args = new ArrayList<>();
    for (int j = 1; j < argsInCommand.getLength(); j += 2) {
      args.add(argsInCommand.item(j).getAttributes().item(0).getTextContent());
    }
    return new AbstractMap.SimpleEntry(cmdName,args);
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

  public static void main(String[] args) {
    Map<String, Double> vars = Map.of("a",3.0,"b",23.1,"c",44.0);
    Map<String,List<String>> commandArgs = Map.of("foo",List.of("firstvar","secondvar"));
    Map<String,String> commandInstructions = Map.of("foo","fd :firstvar bk :secondvar");

    new SLogoFileBuilder().makeXMLFile(FILEPATH,vars,commandArgs,commandInstructions);
  }

  private Document instantiateDocument() throws ParserConfigurationException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = factory.newDocumentBuilder();
    return docBuilder.newDocument();
  }
}