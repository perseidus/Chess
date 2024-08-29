package chess.gui.controller;

import chess.gui.view.BoardRenderer;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ChessBoardController implements Initializable {

  private BoardRenderer renderer;

  @FXML
  GridPane grid;
  @FXML
  HBox innerHBox;
  @FXML
  VBox outerVBox;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    final NumberBinding minLength = Bindings.min(outerVBox.widthProperty(),
        outerVBox.heightProperty());
    innerHBox.prefHeightProperty().bind(minLength);
    innerHBox.prefWidthProperty().bind(minLength);

    renderer = new BoardRenderer(grid);
    renderer.drawPieces();
  }

  public void squareClicked(ActionEvent actionEvent) {
  }

  public void giveUpClicked(ActionEvent actionEvent) {
    renderer.flipBoard();
  }

  public void settingsClicked(ActionEvent actionEvent) {
  }

  public void themeClicked(ActionEvent actionEvent) {
  }
}
