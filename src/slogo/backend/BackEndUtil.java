package slogo.backend;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class BackEndUtil {

  public static final String RESOURCES_PACKAGE = "resources/languages/";
  public static final String SYNTAX_FILENAME = "Syntax";
  public static final String NO_MATCH_STRING = "NO MATCH";
  public static final String LIST_END_LABEL = "ListEnd";
  public static final String LIST_START_LABEL = "ListStart";

  private static ResourceBundle myResources = ResourceBundle.getBundle(RESOURCES_PACKAGE + SYNTAX_FILENAME);

  public static int distanceToEndBracket(String[] tokenList) {
    int extraBrackets = 0;
    for (int i = 0; i < tokenList.length; i ++) {
      String token = tokenList[i];
      System.out.println("(" + token + ")");
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

  private String getSymbol(String text) {
    for (String key : myResources.keySet()) {
      if (text.matches(myResources.getString(key))) {
        return key;
      }
    }
    return NO_MATCH_STRING;
  }

  private static boolean isClosedBracket(String text) {
    return (text.matches(myResources.getString(LIST_END_LABEL)));
  }
  private static boolean isOpenBracket(String text) {
    return (text.matches(myResources.getString(LIST_START_LABEL)));
  }
}
