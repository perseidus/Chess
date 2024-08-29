package chess.gui.controller;

import chess.gui.view.Screen;
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

    stage.setMinHeight(chess.state.Parameters.minScreenHeight);
    stage.setMinWidth(chess.state.Parameters.minScreenWidth);
    stage.show();
  }

  public static void switchScene(Screen screen) {
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
