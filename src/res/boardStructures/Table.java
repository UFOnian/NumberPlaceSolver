package res.boardStructures;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static res.Board.CellLength;

public class Table extends HashMap<Pos, Cell> {

  /* Field */
//  フィールド無しか？


  /* Constructor */

  private Table() {}

  /**
   * cells 集合から table を生成する。
   * このコンストラクタは cells 配列を使う仕様が排除された場合使われなくなる。
   *
   * @param cells cells 集合
   */
  public Table(@NotNull Cell[][] cells) {
    for (Cell[] cell : cells) {
      for (Cell cel : cell) {
        put(cel.pos, cel);
      }
    }
  }

  /**
   * values のデータを基に{@link Cell}クラスのオブジェクト群を生成する。
   * このドキュメントでは、このオブジェクト群を、 cells集合 と呼ぶことがあるが、
   * これは、以前の仕様で、 cell オブジェクトを二次元配列で用意していた時の命名の名残である。
   *
   * @param values cells 集合を成すためのもととなるデータ
   */
  public Table(@NotNull int[][] values) {

//    values validation
    if (values.length != CellLength) {
      throw new IllegalArgumentException("Illegal values length");
    } else if (values[0].length != CellLength) {
      throw new IllegalArgumentException("Illegal values[] length");
    }

    for (int i = 0; i < CellLength; i++) {
      for (int j = 0; j < CellLength; j++) {
        Cell cell = new Cell(Pos.get(i, j), values[i][j]);
        put(cell.pos, cell);
      }
    }
  }

  /**
   * コピーコンストラクタ。NPS.Algorithm#asume() にて、 value の仮置きをする際などに、別のオブジェクトでないと不都合が生じる。
   *
   * @param table コピー元のsource
   */
  public Table(Table table) {
    for (Cell cell : table.cells()) {
      put(cell.pos, new Cell(cell));
    }
  }

  /* Methods */


  /**
   * {@link HashMap#values()}まんま。名前がややこしくてめんどいので新設した。
   *
   * @return {@link HashMap#values()}
   */
  public Collection<Cell> cells() { return super.values(); }


  /* Property Methods */

  /**
   * 指定された場所の cell を返す。
   * 正直、super の get で、 get(Pos.get(eye,j)) って書くのめんどかっただけ。
   *
   * @param i pos.eye
   * @param j pos.j
   * @return eye 行 j 列にあるはずの cell
   */
  public Cell get(int i, int j) {
    return super.get(Pos.get(i, j));
  }

  /* RemainCount */
// 器側に寄った情報。数字の残り使用可能回数

  /**
   * 引数 value を取りうる cell の個数を返す。
   *
   * @param value 幾つあるか数える対象。
   * @return {@link res.Board#CellLength}- ({@link Cell#getValue()}== value が true な総数)
   */
  public int remainCountOf(int value) {
    int remainCount = CellLength;
    for (Cell cell : cells()) {
      if (cell.getValue() == value) remainCount--;
    }
    return remainCount;
  }

  /**
   * {@link #remainCountOf(int)}の返り値が、 count であるような value について、
   * boolean[value]==true となる配列を返します。
   *
   * @param count 残り出現可能回数を指定
   * @return boolean[value]== (remainCountOf(value)==count)
   */
  public boolean[] getValueWithRemainCountIs(int count) {
    boolean[] retBool = new boolean[CellLength + 1];
    for (int value = 1; value < retBool.length; value++) {
      retBool[value] = (remainCountOf(value) == count);
    }
    return retBool;
  }

  /* BecomeCounts */
// セルの情報に寄ったステータス。数字が埋まる「かもしれない」場所の数

  /**
   * この region の中で value が possible な cell の数を返す。<br>
   * 回答途中では 該当する cell の数に不整合が発生する可能性があるが、 <br>
   * deny の呼び出しでミスしなければ確実にこのカウントが 1 の場合には該当 cell に fill できることがわかる。
   *
   * @param value {@link Cell#isBecome(int)}に渡す値。
   * @return {@link Cell#isBecome(int)}のカウント
   */
  public int becomeCountOf(int value) {
    int cnt = 0;
    for (Cell cell : cells()) {
      if (cell.isBecome(value)) cnt++;
    }
    return cnt;
  }

  /**
   * becomeCountOf(value) == 1 な value のリストを配列で返す。1なんだから tryFill しよね。
   *
   * @return becomeCountOf(value) == 1 な value 一覧の配列
   */
  public boolean[] getValueWithBecomeCountIs(int CNT) {
    boolean[] retBool = new boolean[CellLength + 1];
    for (int value = 1; value < retBool.length; value++) {
      retBool[value] = (becomeCountOf(value) == CNT);
    }

    return retBool;
  }

  /* EmptyCell */

  /**
   * isEmpty な cell の個数を返す
   *
   * @return sum(cell.isEmpty ? 1 : 0)
   */
  public ArrayList<Cell> getEmptyCells() {
    ArrayList<Cell> retAL=new ArrayList<>();

    for (Cell cell : cells()) {
      if(cell.isEmpty()){
        retAL.add(cell);
      }
    }
    return retAL;
  }

  /**
   * Empty な cell の個数を返す
   *
   * @return cell.isEmpty==true の個数
   */
  public int countEmptyCells() {
    return getEmptyCells().size();
  }

  /**
   * 矛盾が発生していることを示す{@link Cell#isNotAvailable()}が true であるような cell を含んでいることの真偽値を返す。
   *
   * @return has cell(.isNotAvailable)
   */
  public boolean hasNotAvailableCell() {
    for (Cell cell : this.cells()) {
      if (cell.isNotAvailable()) return true;
    }
    return false;
  }


  /* デバッグ用 */
  public Print print = new Print();

  public class Print extends forDebug.Print {

    @Override
    protected void objData() {
      priLn("Table : "+Table.this);
    }

    public void values() {
      headLine();
      System.out.println("Print.values");
      newLn();

      for (int i = 0; i < CellLength; i++) {
        for (int j = 0; j < CellLength; j++) {
          pRint(" " + get(i, j).getValue());
        }
        newLn();
      }
      newLn();
      System.out.println("Print.values : end");
      footLine();
    }

    public void remainCounts(){
      headLine();
      System.out.println("Print.remainCounts");
      newLn();

      pRint("num : ");
      for (int i = 1; i < CellLength; i++) {
        pRint(" " + i);
      }
      newLn();

      pRint("cnt : ");
      for (int value = 1; value < CellLength + 1; value++) {
        pRint(" " + Table.this.remainCountOf(value));
      }

      newLn();
      System.out.println("Print.remainCounts : end");
      footLine();
    }

    public void becomeCounts(){
      headLine();
      System.out.println("Print.becomeCounts");
      newLn();

      pRint("num : ");
      for (int i = 1; i < CellLength; i++) {
        pRint(" " + i);
      }
      newLn();

      pRint("cnt : ");
      for (int value = 1; value < CellLength + 1; value++) {
        pRint(" " + Table.this.becomeCountOf(value));
      }
      newLn();

      System.out.println("Print.becomeCounts : end");
      footLine();
    }

    public void emptyCellCount(){
      headLine();
      System.out.println("countEmptyCells() = " + countEmptyCells());
      footLine();
    }

    public void data() {
      headLine();
      System.out.println("Print.data");
      newLn();
      values();
      remainCounts();
      becomeCounts();
      emptyCellCount();
      System.out.println("Print.data : end");
      footLine();
    }

  }

}
