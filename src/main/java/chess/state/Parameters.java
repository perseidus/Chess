package chess.state;

import java.util.Arrays;
import java.util.List;
import javafx.scene.paint.Color;

public interface Parameters {

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

  public static Color moveHighlightColor = Color.FORESTGREEN;
}
