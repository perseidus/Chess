package chess.gui.view;

import chess.state.Parameters;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class NodeFactory {

  protected static Circle getSmallCircle(ImageView imageView) {
    Circle circle = new Circle();
    circle.radiusProperty().bind(imageView.fitHeightProperty().divide(5));
    circle.setStroke(Parameters.moveHighlightColor);
    circle.setFill(Parameters.moveHighlightColor);
    circle.setOpacity(0.3);
    return circle;
  }

  protected static Circle getBigCircle(ImageView imageView) {
    Circle circle = new Circle();
    circle.radiusProperty().bind(imageView.fitHeightProperty().divide(2.8));
    circle.setStroke(Parameters.moveHighlightColor);
    circle.setFill(Color.TRANSPARENT);
    circle.setOpacity(0.3);
    circle.setStrokeWidth(4);
    return circle;
  }
}
