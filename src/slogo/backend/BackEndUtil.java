package slogo.backend;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.regex.Pattern;

public class BackEndUtil {

  public static final String RESOURCES_PACKAGE = "resources/languages/";
  public static final String SYNTAX_FILENAME = "Syntax";
  private static final ResourceBundle SYNTAX_BUNDLE = ResourceBundle.getBundle(RESOURCES_PACKAGE + SYNTAX_FILENAME);
  public static final String NO_MATCH_STRING = "NO MATCH";
  public static final String LIST_END = SYNTAX_BUNDLE.getString("ListEnd");
  public static final String LIST_START = SYNTAX_BUNDLE.getString("ListStart");
  public static final String COMMENT_LINE = "(^#(?s).*|\\s+)"; //The comment line in the resource file was leaving empty strings when splitting
  public static final String NEWLINE = SYNTAX_BUNDLE.getString("Newline");
  public static final String WHITESPACE = SYNTAX_BUNDLE.getString("Whitespace");


  private BackEndUtil() {
    //utility classes should not have public constructors, so this empty
    // constructor removes the implicit public constructor.
  }

  public static int distanceToEndBracket(String[] tokenList) {
    //FIXME: deal with brackets without spaces before/after IDs - "Don't know how to"
    int extraBrackets = 0;
    for (int i = 0; i < tokenList.length; i ++) {
      String token = tokenList[i];
      if (isOpenBracket(token)) {
        extraBrackets++;
      } else if (isClosedBracket(token)) {
        if (extraBrackets == 0) {
          return i + 1;
        } else {
          extraBrackets--;
        }
      }
    }
    return tokenList.length;
  }

  public static String getSymbol(String text) {
    for (String key : SYNTAX_BUNDLE.keySet()) {
      if (text.matches(SYNTAX_BUNDLE.getString(key))) {
        return key;
      }
    }
    return NO_MATCH_STRING;

  }

  public static void printRemainingTokens(String[] scriptTokens, int i) {
    String[] remaining = Arrays.copyOfRange(scriptTokens, i, scriptTokens.length);
    for (String string : remaining) {
      System.out.printf("(%s) ", string);
    }
    System.out.println();
  }

  public static String concatStringArray(String[] tokens) {
      StringBuffer sb = new StringBuffer();
      for (String string : tokens) {
        sb.append(string + " ");
      }
      return sb.toString();
  }

  public static List<String> getTokenList(String script) {
    String[] scriptLines = script.split(NEWLINE);
    List<String> scriptTokenList = new ArrayList<>();
    for (String line : scriptLines) {
      System.out.println(line);
      if (!line.matches(COMMENT_LINE)) {
        scriptTokenList.addAll(Arrays.asList(line.strip().split(WHITESPACE)));
      }
    }
    return scriptTokenList;
  }

  private static boolean isClosedBracket(String text) {
    return (text.matches(LIST_END));
  }
  private static boolean isOpenBracket(String text) {
    return (text.matches(LIST_START));
  }

  public static List<Double> getArgsFromStack(Stack<Double> values, int numArgs) {
    List<Double> argList = new ArrayList<>();
    for (int arg = 0; arg < numArgs; arg++) {
      argList.add(values.pop());
    }
    Collections.reverse(argList);
    return argList;
  }

  public static List<Entry<String, Pattern>> interpretPatterns(String syntax) {
    List<Entry<String, Pattern>> patterns = new ArrayList<>();
    ResourceBundle resources = ResourceBundle.getBundle(RESOURCES_PACKAGE + syntax);
    for (String key : Collections.list(resources.getKeys())) {
      String regex = resources.getString(key);
      patterns.add(new SimpleEntry<>(key, Pattern.compile(regex, Pattern.CASE_INSENSITIVE)));
    }
    return patterns;
  }
}
