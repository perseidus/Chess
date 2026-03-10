package chess.game.state;

import chess.gui.controller.ScreenManager;
import java.util.Arrays;
import java.util.List;
import javafx.scene.paint.Color;

public class Parameters {

  public static final double minScreenHeight = 600.0;
  public static final double minScreenWidth = 800.0;

  public static final int DEFAULT_COLOR = 1;
  public static final int DEFAULT_TIME = 10;      //minutes
  public static final int DEFAULT_INCREMENT = 0;  //seconds
  public static final String DEFAULT_COMPUTER = "Standard";  //seconds
  public static final List<String> TIMES = Arrays.asList(
      "1", "3", "5", "10", "15", "30", "45", "60", "90", "120");
  public static final List<String> INCREMENTS = Arrays.asList(
      "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "15", "30", "60", "120", "180");
  public static final List<String> COMPUTERS = Arrays.asList("Standard");

  private static Color moveHighlightColorL = new Color(0.6509, 0.3882, 0.8, 1);
  private static Color checkHighlightColor = Color.INDIANRED;
  private static String moveFromColorL = "rgba(199, 125, 255, 0.25)";
  private static String moveToColorL = "rgba(224, 170, 255, 0.25)";

  // TODO pick colors
  private static Color moveHighlightColorD = new Color(0.1647, 0.6156, 0.5607, 1);
  private static String moveFromColorD = "rgba(90, 167, 134, 0.25)";
  private static String moveToColorD = "rgba(42, 157, 143, 0.25)";

  private static Color focusHighlightColorL = new Color(0.89, 0.04, 0.36, 1);
  private static Color focusHighlightColorD = new Color(0.89, 0.04, 0.36, 1);

  private static String lightTileL = "#fbf8f3";
  private static String darkTileL = "#8fc9a8";
  private static String lightTileD = "#4f517d";
  private static String darkTileD = "#242038";


  public static String moveFromColor() {
    return ScreenManager.isLightMode() ? moveFromColorL : moveFromColorD;
  }

  public static String moveToColor() {
    return ScreenManager.isLightMode() ? moveToColorL : moveToColorD;
  }

  public static Color moveHighlightColor() {
    return ScreenManager.isLightMode() ? moveHighlightColorL : moveHighlightColorD;
  }

  public static Color checkHighlightColor() {
    return checkHighlightColor;
  }

  public static Color focusHighlightColor() {
    return ScreenManager.isLightMode() ? focusHighlightColorL : focusHighlightColorD;
  }

  public static String darkTile() {
    return ScreenManager.isLightMode() ? darkTileL : darkTileD;
  }

  public static String lightTile() {
    return ScreenManager.isLightMode() ? lightTileL : lightTileD;
  }
}
