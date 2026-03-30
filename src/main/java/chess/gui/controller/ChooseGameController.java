package chess.gui.controller;

import chess.gui.view.PopOn;
import chess.gui.view.PopOnType;
import chess.gui.view.Screen;
import chess.game.state.GameState;
import chess.game.state.MatchConfiguration;
import chess.game.state.Parameters;
import chess.sound.Sound;
import chess.sound.SoundPlayer;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

public class ChooseGameController implements Initializable {

  private MatchConfiguration config;

  @FXML
  AnchorPane pane;
  @FXML
  ToggleGroup colorButtonGroup;
  @FXML
  Label computerLabel, colonLabel3;
  @FXML
  ComboBox<String> timeBox, incrementBox, computerBox;
  @FXML
  Button settingsButton;

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

    pane.addEventFilter(KeyEvent.KEY_PRESSED, this::keyPressedFallback);
  }

  public void settingsClicked() {
    PopOver popOver = PopOn.getInstance(PopOnType.SETTINGS);
    popOver.setArrowLocation(ArrowLocation.TOP_LEFT);
    popOver.setDetachable(false);
    popOver.show(settingsButton);
  }

  public void themeClicked() {
    ScreenManager.switchTheme();
  }

  public void backClicked() {
    saveChanges();
    ScreenManager.switchScene(Screen.START);
  }

  public void nextClicked() {
    saveChanges();
    GameState.getInstance().loadMatchConfiguration();
    ScreenManager.switchScene(Screen.CHESSBOARD);
    SoundPlayer.getInstance().playSound(Sound.CLICK);
  }

  public void keyPressedFallback(KeyEvent event) {
    KeyCode key = event.getCode();

    if (key == KeyCode.TAB) {
      if (timeBox.isFocused()) {
        incrementBox.requestFocus();
      } else if (incrementBox.isFocused() && !computerBox.isDisable()) {
        computerBox.requestFocus();
      } else {
        timeBox.requestFocus();
      }
    }

    if (event.isControlDown() || event.isAltDown()) {
      if (key == KeyCode.RIGHT) {
        ScreenManager.switchScene(Screen.CHESSBOARD);
      } else if (key == KeyCode.LEFT) {
        ScreenManager.switchScene(Screen.START);
      }
    }

    event.consume();
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
