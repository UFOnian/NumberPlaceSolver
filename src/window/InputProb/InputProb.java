package window.InputProb;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class InputProb extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {

    try {
      Parent root = FXMLLoader.load(getClass().getResource("InputProb.fxml"));

      Scene scene = new Scene(root);

      primaryStage.setScene(scene);
    }catch (IOException ioe){
      ioe.printStackTrace();
    }
    primaryStage.setTitle("数独解くゾ");
    primaryStage.show();
  }
}
