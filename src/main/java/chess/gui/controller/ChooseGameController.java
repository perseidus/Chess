package chess.gui.controller;

import chess.gui.view.Screen;
import chess.game.state.GameState;
import chess.game.state.MatchConfiguration;
import chess.game.state.Parameters;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

public class ChooseGameController implements Initializable {

  private MatchConfiguration config;

  @FXML
  ToggleGroup colorButtonGroup;
  @FXML
  Label computerLabel, colonLabel3;
  @FXML
  ComboBox<String> timeBox, incrementBox, computerBox;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    config = MatchConfiguration.getInstance();

    timeBox.getItems().addAll(Parameters.TIMES);
    incrementBox.getItems().addAll(Parameters.INCREMENTS);
    computerBox.getItems().addAll(Parameters.COMPUTERS);

    timeBox.setValue(Integer.toString(config.getTime()));
    incrementBox.setValue(Integer.toString(config.getIncrement()));
    computerBox.setValue(config.getComputer());
    ToggleButton select = (ToggleButton) colorButtonGroup.getToggles()
        .get(config.getSelectedColorButton());
    select.setSelected(true);

    if (MatchConfiguration.getInstance().isPvpMode()) {
      colorButtonGroup.getToggles().forEach(tb -> ((ToggleButton) tb).setDisable(true));
      computerLabel.setDisable(true);
      colonLabel3.setDisable(true);
      computerBox.setDisable(true);
    }
  }

  public void settingsClicked(ActionEvent actionEvent) {
  }

  public void themeClicked(ActionEvent actionEvent) {
  }

  public void backClicked(ActionEvent actionEvent) {
    saveChanges();
    ScreenManager.switchScene(Screen.START);
  }

  public void nextClicked(ActionEvent actionEvent) {
    saveChanges();
    GameState.getInstance().loadMatchConfiguration();
    ScreenManager.switchScene(Screen.CHESSBOARD);
  }

  public void saveChanges() {
    String id = ((ToggleButton) colorButtonGroup.getSelectedToggle()).getId();
    switch (id) {
      case "whiteButton":
        config.setSelectedColorButton(0);
        break;
      case "randomButton":
        config.setSelectedColorButton(1);
        break;
      case "blackButton":
        config.setSelectedColorButton(2);
        break;
    }

    config.setTime(Integer.parseInt(timeBox.getValue()));
    config.setIncrement(Integer.parseInt(incrementBox.getValue()));
    config.setComputer(computerBox.getValue());
  }
}
