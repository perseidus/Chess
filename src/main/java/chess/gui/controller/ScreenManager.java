package chess.gui.controller;

import chess.gui.view.Screen;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ScreenManager extends Application {

  private static Stage stage;
  private static Stage popUpStage;

  @Override
  public void start(Stage stage) throws Exception {
    ScreenManager.stage = stage;
    switchScene(Screen.START);

    stage.setMinHeight(chess.game.state.Parameters.minScreenHeight);
    stage.setMinWidth(chess.game.state.Parameters.minScreenWidth);
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

  public static void openMatchEndingPopUp() {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        popUpStage = new Stage();
        Parent root = null;
        try {
          root = FXMLLoader.load(ScreenManager.class.getResource(Screen.MATCHENDING.getPath()));
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        popUpStage.setScene(scene);
        popUpStage.setX((stage.getX() + (stage.getWidth()) / 2) - 150);
        popUpStage.setY((stage.getY() + (stage.getHeight()) / 2) - 200);
        popUpStage.setResizable(false);
        popUpStage.initModality(Modality.APPLICATION_MODAL);
        popUpStage.initStyle(StageStyle.TRANSPARENT);
        popUpStage.showAndWait();
      }
    });
  }

  public static void closeApplication() {
    stage.close();
  }

  public static void closeMatchEndingPopUp() {
    popUpStage.close();
  }
}
