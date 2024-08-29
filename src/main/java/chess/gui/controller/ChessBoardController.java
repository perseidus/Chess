package chess.gui.controller;

import chess.gui.model.BoardInteractionManager;
import chess.gui.view.BoardRenderer;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ChessBoardController implements Initializable {

  private BoardRenderer renderer;
  private BoardInteractionManager manager;

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
    manager = new BoardInteractionManager(renderer);
    renderer.drawPieces();
  }

  public void squareClicked(ActionEvent actionEvent) {
    String id = ((Button) actionEvent.getSource()).getId();
    manager.handleButtonClick(id.charAt(2) - '0', id.charAt(1) - '0');
  }

  public void giveUpClicked(ActionEvent actionEvent) {

  }

  public void settingsClicked(ActionEvent actionEvent) {
  }

  public void themeClicked(ActionEvent actionEvent) {
  }
}
