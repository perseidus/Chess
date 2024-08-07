package chess.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ApplicationLauncher extends Application {

  @Override
  public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("/Start.fxml"));
    Scene scene = new Scene(root, chess.Parameters.minScreenWidth,
        chess.Parameters.minScreenHeight);
    stage.setScene(scene);
    stage.show();
  }
}
