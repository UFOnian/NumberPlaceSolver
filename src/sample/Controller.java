package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Controller {

  @FXML
  Button button,ボタン;
  @FXML
  public void onClick(){
    button.setText(ボタン.getText());
  }
  @FXML
  public void onClick2(){
    ボタン.setText("button"+(x++));
  }

  public int x=0;


}
