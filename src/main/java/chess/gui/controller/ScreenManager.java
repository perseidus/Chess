package chess.gui.controller;

import chess.gui.view.Screen;
import chess.sound.Sound;
import chess.sound.SoundPlayer;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ScreenManager extends Application {

  private static Stage stage;

  @Override
  public void start(Stage stage) throws Exception {
    ScreenManager.stage = stage;
    switchScene(Screen.START);

    stage.setMinHeight(chess.game.state.Parameters.minScreenHeight);
    stage.setMinWidth(chess.game.state.Parameters.minScreenWidth);
    stage.show();
  }

  public static void switchScene(Screen screen) {
    SoundPlayer.getInstance().playSound(Sound.CLICK);
    try {
      Parent root = FXMLLoader.load(ScreenManager.class.getResource(screen.getPath()));
      Scene scene = new Scene(root);
      stage.setScene(scene);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void closeApplication() {
    stage.close();
  }
}
