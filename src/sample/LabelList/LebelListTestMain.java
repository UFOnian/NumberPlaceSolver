package sample.LabelList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LebelListTestMain extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("LabelListTest.fxml"));
    Scene scene = new Scene(root, 250, 450);
    primaryStage.setScene(scene);
    primaryStage.show();


  }
}

