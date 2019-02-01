package window.InputProb;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import res.NPS;

import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import static res.Board.CellLength;


public class Controller implements Initializable {

  private final double CELL_SIZE = 30.0; // inputGrid cell size

  @FXML
  private Group displayGroup;
  @FXML
  Group outputGroup;
  @FXML
  private GridPane inputGrid; // user input data will filled in TextField in me
  @FXML
  private GridPane outputGrid;
  TextField[][] inputTextField; // user input values will filled in me
  Label[][] outputLabels;
  private int[][] inputValues;

  private final int columnSize = 9;
  private final int rowSize = 9;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    setMessageLabel(ORDER_INPUT); // 入力を促すメッセージの表示
    setInputGrid(); // 問題を入力するGridPaneの初期化
  }

  //  Group内にCELL_SIZEに基づく罫線を引く
  private void showRuler(Group group) {
    for (int i = 0; i <= 3; i++) {
      Line hline = new Line(CELL_SIZE * 3 * i, 0, CELL_SIZE * 3 * i, CELL_SIZE * 9);
      Line vline = new Line(0, CELL_SIZE * 3 * i, CELL_SIZE * 9, CELL_SIZE * 3 * i);
      hline.setStrokeWidth(1.5);
      vline.setStrokeWidth(1.5);

      group.getChildren().addAll(hline, vline);
    }
  }

  //  inputGridを初期化(入力待ちの状態にする)
  private void setInputGrid() {
    inputGrid.getChildren().removeAll();

    // input grid's column & row size

    inputTextField = new TextField[rowSize][columnSize];

// ChangeListenerの定義
    HashMap<Integer, TextField> hashMap = new HashMap<Integer, TextField>(81, 1f);
    ChangeListener<String> cl = new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        String str;
        if (newValue.matches("[0-9]?")) {
          if (newValue.length() > 0) setMessageLabel(ORDER_INPUT);
          str = newValue;
        } else if (newValue.matches("[0-9]{2}")) {
          setMessageLabel(ORDER_INPUT);
          str = String.valueOf(newValue.charAt(1));
        } else {
          setMessageLabel(NOT_INTEGER);
          Toolkit.getDefaultToolkit().beep();
          str = oldValue.matches("[0-9]") ? oldValue : "";
        }
        StringProperty sp = (StringProperty)observable;
//        System.out.println(sp.getBean());
        hashMap.get(sp.getBean().hashCode()).setText(str);
      }
    };

//    TextFieldの生成とinputGridのChildren化
    for (int row = 0; row < rowSize; row++) {
      for (int col = 0; col < columnSize; col++) {

        TextField textField = new TextField("");

//        ChangeListenerの適用
        if (hashMap.containsKey(textField.textProperty().getBean().hashCode())) {
          System.err.println("textField.textProperty().getBean() Duplicated");
        }
        hashMap.put(textField.textProperty().getBean().hashCode(), textField);

//        System.out.println(textField.textProperty().getBean());
//  add NumbersOnlyLimitation for the TextField

        textField.textProperty().addListener(cl);

//        各種プロパティの適用
        textField.setPrefSize(CELL_SIZE, CELL_SIZE);
        textField.setAlignment(Pos.CENTER);

//        inputGridへ追加
        inputTextField[row][col] = textField;
        inputGrid.add(inputTextField[row][col], row, col);
      }
    }
    showRuler(displayGroup);
  }


  //  messageLabel関連
  @FXML
  private Label messageLabel;
  private static final String ORDER_INPUT = "Fill numbers. You can use 0 as empty";
  private static final String NOT_INTEGER = "Value must be Integer";
  private static final String WrongProblem ="Your Problem has Error";
  private static final String Solved ="Problem Accomplished";

  private void setMessageLabel(String mode) {
    if (mode == null) mode = ORDER_INPUT;
    switch (mode) {
      case NOT_INTEGER:
        messageLabel.setText(NOT_INTEGER);
        messageLabel.setTextFill(Color.RED);
        break;

      default:
      case ORDER_INPUT:
        messageLabel.setText(ORDER_INPUT);
        messageLabel.setTextFill(Color.BLACK);
        break;
      case WrongProblem:
        messageLabel.setText(WrongProblem);
        messageLabel.setTextFill(Color.RED);
      case Solved:
        messageLabel.setText(Solved);
        messageLabel.setTextFill(Color.LIGHTBLUE);
    }
  }


  @FXML
  public void onClickSolve() {
    inputValues = new int[9][9];
    for (int row = 0; row < rowSize; row++) {
      for (int col = 0; col < columnSize; col++) {
        String str= inputTextField[row][col].getText();
        inputValues[row][col] =
            str.matches("[1-9]") ?
                Integer.parseInt(str)
                : 0;
      }
    }


// TODO: 2018/12/24     NPS nps =new NPS(inputValues);
    NPS nps =new NPS(inputValues);

    if(nps.isWrongProblem()){
      setMessageLabel(WrongProblem);
    }else if(nps.completeSolve()){
      setMessageLabel(Solved);

      for (int i = 0; i < CellLength; i++) {
        for (int j = 0; j < CellLength; j++) {
          Label outputLabel=new Label();

          outputLabel.setText(String.valueOf(nps.getValueAt(i,j)));
          outputLabel.setPrefSize(CELL_SIZE,CELL_SIZE);
          outputLabel.setAlignment(Pos.CENTER);

          outputGrid.add(outputLabel,i,j);
        }
      }
      showRuler(outputGroup);
    }


  }

  @FXML
  public void onClickResetButton() {
    for (int row = 0; row < rowSize; row++) {
      for (int col = 0; col < columnSize; col++) {
        inputTextField[row][col].setText("");
      }
    }
  }


}
