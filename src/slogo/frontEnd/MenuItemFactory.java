package slogo.frontEnd;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;

/**
 * Refactored the setUpMenus and addMenuItem methods into this new MenuFactory class.
 * This helped reduce the complexity/clutter of visualizer
 * Also it follows the OO principles by creating a class with a focused purpose
 */
class MenuFactory {
  private static final ResourceBundle RESOURCES = ResourceBundle.getBundle("slogo/frontEnd/Resources.config");
  private static final List<String> MENU_TYPES = Arrays.asList(RESOURCES.getString("MenuTypes").split(","));

  Class clazz;
  Visualizer myVis;
  private ResourceBundle myLanguageResources;
  private MenuBar myMenuBar;
  private ResourceBundle myWorkSpaceResources;
  private Set<String> myColors;
  private DisplayableTextHolder myDisplayableTextHolder;
  private VBox myLayoutBox;

  public MenuFactory(Visualizer visualizer, Class clazz,
      ResourceBundle languageBundle, ResourceBundle workSpaceResources,
      Set<String> colors, VBox vBox, DisplayableTextHolder textHolder) {

    this.clazz = clazz;
    myLanguageResources = languageBundle;
    myWorkSpaceResources = workSpaceResources;
    myColors = colors;
    myLayoutBox = vBox;
    myDisplayableTextHolder = textHolder;
   myVis = visualizer;
  }

  protected List<String> makeMenus(){
    List<String> myMenuNames = Arrays.asList(myLanguageResources.getString("MenuNames").split(","));
    List<List<String>> myMenuOptions = new ArrayList<>();
    for(String menuType : MENU_TYPES){
      myMenuOptions.add(Arrays.asList(myWorkSpaceResources.getString(menuType+"Options").split(",")));
    }
    int penIndex = MENU_TYPES.indexOf("PenColor");
    int backIndex = MENU_TYPES.indexOf("Background");
    List<String> colorIndices = new ArrayList<>();
    colorIndices.addAll(myColors);
    myMenuOptions.set(penIndex,colorIndices);
    myMenuOptions.set(backIndex,colorIndices);
     myMenuBar = new MenuBar();
    myLayoutBox.getChildren().add(myMenuBar);
    for(int i=0; i<myMenuNames.size(); i++){
      Menu menu = new Menu(myMenuNames.get(i));
      myMenuBar.getMenus().add(menu);
      myDisplayableTextHolder.addMenu(menu, MENU_TYPES.get(i));
      for(String entry : myMenuOptions.get(i)){
        addMenuItem(i, entry);
      }
    }
    return myMenuNames;
  }



  protected void addMenuItem (int menuNameIndex, String menuItemName){
    Menu menu = myMenuBar.getMenus().get(menuNameIndex);
    String menuType = MENU_TYPES.get(menuNameIndex);
    String menuItemNameTranslation = menuItemName;
    if(!menuItemName.matches("-?\\d+(\\.\\d+)?")) {
      menuItemNameTranslation = myLanguageResources.getString(menuItemName);
    }
    MenuItem menuItem = new MenuItem(menuItemNameTranslation);
    myDisplayableTextHolder.addMenuItem(menuItem, menuItemName);
    String methodName = RESOURCES.getString(menuType);
    String labelGetterName = RESOURCES.getString(menuType + "Label");
    // get another method name that will give us the label corresponding to this menu name
    // the method should return a node object
    try {
      Method method = clazz.getDeclaredMethod(methodName, String.class);
      Method labelGetter = clazz.getDeclaredMethod(labelGetterName, String.class);
      menuItem.setGraphic((Node) labelGetter.invoke(myVis, menuItemName));
      menuItem.setOnAction(event -> {
        try {
          method.invoke(myVis, menuItemName);
        } catch (IllegalAccessException | InvocationTargetException e) {
          new ErrorDisplay(
              myLanguageResources.getString("InvokeError"), myLanguageResources);
        }
      });
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      new ErrorDisplay(
          myLanguageResources.getString("NoMethodError"), myLanguageResources);
    }
    String finalMenuItemNameTranslation = menuItemNameTranslation; // intellij makes us put this in a variable. Don't delete it.
    menu.getItems().removeIf(oldMenuItem -> oldMenuItem.getText().equals(finalMenuItemNameTranslation));
    menu.getItems().add(menuItem);
  }
}
