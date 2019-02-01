package res;

import org.jetbrains.annotations.NotNull;
import res.boardStructures.*;

import java.util.ArrayList;

public class Board {

  /* Field */

  /**
   * 数独の問題の cell の行・列・サブリージョンの大きさ。また、問題で使用される数字の最大値。<br>
   * この数字は 自然数n の 2乗 である必要がある。この定数は{@link Board}に置かれている。
   */
  public static final int CellLength = 9;

  public ModCount modCount;

  /**
   * 更新回数を数えたり、更新差分が発生したかどうかを管理するためのクラス。
   * 簡単な int の足し算とか差分の前の値を撮ったりとかだけ。
   */
  class ModCount {
    /**
     * この Board に存在する cell や region に更新が加えられた回数
     */
    private int modCount;
    private int tmpModCount;

    ModCount() {
      modCount = 0;
      tmpModCount = 0;
    }

    public int get() {
      return modCount;
    }

    private void inc() {
      modCount++;
    }

    public void setTmp() {
      tmpModCount = modCount;
    }

    public boolean changed() {
      return tmpModCount == modCount;
    }
  }


//  Cell のデータを扱う容れ物群

  /**
   * cells集合 を格納した Table。 HashMap で用意されている。
   */
  public Table table;

//  Regions
//  /**
//   * 行を示す region の配列。
//   */
//  public Region[] rowRegions;
//  /**
//   * 列を示す region の配列。
//   */
//  public Region[] colRegions;
//  /**
//   * サブリージョンを示す region の配列。
//   */
//  public Region[] subRegions;

  // TODO: 2019/01/24 regions 仕様を更新
  public Region[][] regions;
  private static final int row = 0;
  private static final int col = 1;
  private static final int sub = 2;






  /* Constructor */

  private Board() {}

  /**
   * 入力された int[][] 配列 から、新しくインスタンスを生成する。
   * 最初の問題を渡された時点で使用される。
   *
   * @param values 問題のインプットに用いる int[][] 配列
   */
  public Board(@NotNull int[][] values) {

//    values validation
    if (values == null) {
      throw new NullPointerException("Null values");
    } else if (values.length != CellLength) {
      throw new IllegalArgumentException("Invalid values length");
    } else if (values[0].length != CellLength) {
      throw new IllegalArgumentException("Invalid values[] length");
    }

//    make Table
    table = new Table(values);

////    make regions
//    rowRegions = Region.makeRegionArray(table, Region.mode_ROW);
//    colRegions = Region.makeRegionArray(table, Region.mode_COL);
//    subRegions = Region.makeRegionArray(table, Region.mode_SUB);

    regions=new Region[3][];
    regions[row]=Region.makeRegionArray(table,Region.mode_ROW);
    regions[col]=Region.makeRegionArray(table,Region.mode_COL);
    regions[sub]=Region.makeRegionArray(table,Region.mode_SUB);

//    modCount
    modCount = new ModCount();

  }

  /**
   * make clone of arg
   * <p>
   * board の複製を作る際に使用。具体的には再帰処理でもともとのデータを残しておく必要がある場合に使用する。
   *
   * @param board source
   */
  public Board(@NotNull Board board) {

    this.table = new Table(board.table);

//    this.rowRegions = Region.cloneRegionArray(this.table, board.rowRegions);
//    this.colRegions = Region.cloneRegionArray(this.table, board.colRegions);
//    this.subRegions = Region.cloneRegionArray(this.table, board.subRegions);


    regions=new Region[3][];
    this.regions[row]=Region.cloneRegionArray(this.table, board.regions[row]);
    this.regions[col]=Region.cloneRegionArray(this.table, board.regions[col]);
    this.regions[sub]=Region.cloneRegionArray(this.table, board.regions[sub]);


    this.modCount = board.modCount;

  }



  /* Methods */


  /* Property */


  /**
   * 引数 pos と関係のある rowRegion, colRegion, subRegion をそれぞれの配列から呼び出し返す。<br>
   * 配列は決まって[0]:row , [1]:col , [2]:sub Region となる。(もち、処理内容に違いはないので意識する必要性はない。)
   *
   * @param pos これを含む Region を返す。 実引数として、cell.pos とするのが推奨される。
   * @return 関連のある row,col,sub Region 要素の配列。length==3
   */
  public Region[] getRegionsInclude(Pos pos) {
    Region[] regions = new Region[3];

//    regions[0] = rowRegions[pos.i];
//    regions[1] = colRegions[pos.j];
//    regions[2] = subRegions[pos.getX()];

    regions[row]=this.regions[row][pos.i];
    regions[col]=this.regions[col][pos.j];
    regions[sub]=this.regions[sub][pos.getX()];

    return regions;
  }


  /* フィールドを更新するメソッド群 */

  /**
   * pos の示す場所の cell に value を tryFill し、 関連する region で valueFill を呼び出す。
   *
   * @param pos   tryFill する cell の座標
   * @param value tryFill の引数
   * @return tryFill の返り値
   */
  public boolean trySetCell(Pos pos, int value) {
    if (table.get(pos).tryFill(value)) {

      for (Region r : getRegionsInclude(pos)) {
        r.valueFill(value);
      }

      modCount.inc();
      return true;
    } else return false;
  }

  /**
   * {@link #trySetCell(Pos, int)}を呼び出す。
   *
   * @param cell Pos と value を決定する元
   * @return tryFill の返り値
   */
  public boolean trySetCell(TentativeCell cell) {
    return trySetCell(cell.pos, cell.getValue());
  }

  public boolean trySetCell(Cell cell,int value){
    return trySetCell(cell.pos,value);
  }


}




