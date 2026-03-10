package chess.gui.view;

import chess.game.state.Parameters;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class NodeFactory {

  protected static Circle getSmallCircle(ImageView imageView) {
    Circle circle = new Circle();
    circle.radiusProperty().bind(imageView.fitHeightProperty().divide(5));
    circle.setStroke(Parameters.moveHighlightColor());
    circle.setFill(Parameters.moveHighlightColor());
    circle.setOpacity(0.3);
    return circle;
  }

  protected static Circle getBigCircle(ImageView imageView) {
    Circle circle = new Circle();
    circle.radiusProperty().bind(imageView.fitHeightProperty().divide(2.8));
    circle.setStroke(Parameters.moveHighlightColor());
    circle.setFill(Color.TRANSPARENT);
    circle.setOpacity(0.3);
    circle.setStrokeWidth(4);
    return circle;
  }

  protected static Circle getCheckHighlight(ImageView imageView) {
    Circle circle = new Circle();
    circle.radiusProperty().bind(imageView.fitHeightProperty().divide(2.5));

    RadialGradient gradient = new RadialGradient(0, 0, 0.5, 0.1, 1, true, CycleMethod.NO_CYCLE,
        new Stop(0, Parameters.checkHighlightColor()),
        new Stop(0.85, Color.TRANSPARENT));

    circle.setFill(gradient);
    return circle;
  }

  protected static Rectangle getFocusHighlight(ImageView imageView) {
    Rectangle rect = new Rectangle();
    rect.widthProperty().bind(imageView.fitWidthProperty().multiply(0.96));
    rect.heightProperty().bind(imageView.fitHeightProperty().multiply(0.96));
    rect.xProperty().bind(imageView.xProperty());
    rect.yProperty().bind(imageView.yProperty());

    rect.setStroke(Parameters.focusHighlightColor());
    rect.setFill(Color.TRANSPARENT);
    rect.setStrokeWidth(4);

    return rect;
  }

  protected static Pane addFocusToCircle(ImageView image, Circle circle) {
    StackPane stackPane = new StackPane();
    stackPane.prefWidthProperty().bind(image.fitWidthProperty());
    stackPane.prefHeightProperty().bind(image.fitHeightProperty());

    stackPane.getChildren().add(circle);
    stackPane.getChildren().add(getFocusHighlight(image));

    return stackPane;
  }

  protected static Circle extractCircle(Node node) {
    if (node instanceof StackPane) {
      for (Node child : ((StackPane) node).getChildren()) {
        if (child instanceof Circle) {
          return (Circle) child;
        }
      }
    }
    return null;
  }
}
