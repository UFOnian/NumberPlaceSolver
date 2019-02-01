package sample.LabelList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LabelListTest {

  @FXML
  private List<Label> labelList ;

  public void initialize() {
    int count = 1 ;
    for (Label label : labelList) {

      label.setText(label.getId().equals("label3")?"true":"false");


    }
  }
}