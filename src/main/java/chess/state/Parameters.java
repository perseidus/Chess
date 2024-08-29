package chess.state;

import javafx.scene.paint.Color;

public interface Parameters {

  public static final double minScreenHeight = 600.0;
  public static final double minScreenWidth = 800.0;

  public static final int DEFAULT_TIME = 10;      //minutes
  public static final int DEFAULT_INCREMENT = 0;  //seconds

  public static Color moveHighlightColor = Color.FORESTGREEN;
}
