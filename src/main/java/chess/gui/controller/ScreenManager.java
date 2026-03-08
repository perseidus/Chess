package chess.gui.controller;

import chess.gui.view.Screen;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ScreenManager extends Application {

  private static Stage stage;
  private static Stage popUpStage;
  private static Screen currentScreen;
  private static boolean lightMode = true;

  private static Scene lastScene;

  @Override
  public void start(Stage stage) throws Exception {
    ScreenManager.stage = stage;
    stage.getIcons().add(new Image(ScreenManager.class.getResource("/misc/icon.png").toExternalForm()));
    switchScene(Screen.START);

    stage.setMinHeight(chess.game.state.Parameters.minScreenHeight);
    stage.setMinWidth(chess.game.state.Parameters.minScreenWidth);
    stage.show();
  }

  public static void switchScene(Screen screen) {
    currentScreen = screen;
    try {
      Parent root = FXMLLoader.load(ScreenManager.class.getResource(screen.getPath()));

      Scene scene;
      if (lastScene != null) {
        scene = new Scene(root, lastScene.getWidth(), lastScene.getHeight());
      } else {
        scene = new Scene(root);
      }
      lastScene = scene;

      setStylesheets(scene);
      stage.setScene(scene);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void openMatchEndingPopUp() {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        popUpStage = new Stage();
        Parent root = null;
        try {
          root = FXMLLoader.load(ScreenManager.class.getResource(Screen.MATCHENDING.getPath()));
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        setStylesheets(scene);
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

  private static void setStylesheets(Scene scene) {
    String style = lightMode ? "/styles/light.css" : "/styles/dark.css";
    scene.getStylesheets().remove(ScreenManager.class.getResource("/styles/light.css").toExternalForm());
    scene.getStylesheets().remove(ScreenManager.class.getResource("/styles/dark.css").toExternalForm());
    scene.getStylesheets().add(ScreenManager.class.getResource("/styles/base.css").toExternalForm());
    scene.getStylesheets().add(ScreenManager.class.getResource(style).toExternalForm());
  }

  public static void closeApplication() {
    stage.close();
  }

  public static Stage getMatchEndedStage() {
    return popUpStage;
  }

  public static void closeMatchEndingPopUp() {
    popUpStage.close();
  }

  public static boolean isLightMode() {
    return lightMode;
  }

  public static void switchTheme() {
    lightMode = !lightMode;
    switchScene(currentScreen);
  }
}
