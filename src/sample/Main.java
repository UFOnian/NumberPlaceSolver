package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

    // Mainで記述
//    Parent root=new Pane();
//    Button button =new Button("aaa");
//    ((Pane) root).getChildren().add(button);
//    Shape s=new Circle(50);
//    ((Pane) root).getChildren().add(s);
    //

    primaryStage.setTitle("Hello World");
    primaryStage.setScene(new Scene(root/*, 300, 275*/));
    primaryStage.show();
  }


  public static void main(String[] args) {
    launch(args);
  }
}
