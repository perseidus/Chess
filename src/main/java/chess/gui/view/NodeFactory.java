package chess.gui.view;

import chess.game.state.Parameters;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
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

  protected static Circle getCheckHighlight(ImageView imageView) {
    Circle circle = new Circle();
    circle.radiusProperty().bind(imageView.fitHeightProperty().divide(2.5));

    RadialGradient gradient = new RadialGradient(0, 0, 0.5, 1, 1, true, CycleMethod.NO_CYCLE,
        new Stop(0, Parameters.checkHighlightColor),
        new Stop(0.85, Color.TRANSPARENT));

    circle.setFill(gradient);
    return circle;
  }
}
