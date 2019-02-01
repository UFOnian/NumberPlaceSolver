package res.boardStructures;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static res.Board.CellLength;

// TODO: 2019/01/14 access can be weaker は off にできるゾ

public class Region extends ArrayList<Cell> {

  /* Field (mode のみ) */

  /**
   * この region オブジェクトが行・列・サブのどのRegionを指すものかを記録する。<br>
   * 開発段階では必要でない可能性が高いが、いつかほしくなるかもしれないので追加さた。<br>
   * 代入されるべき値として、{@link #mode_ROW}、{@link #mode_COL}、{@link #mode_SUB}、が用意されている。
   */
  public final String mode;

  /**
   * {@link #mode}に格納され、その region が 行リージョン であることを示す値。
   */
  public static final String mode_ROW = "row";
  /**
   * {@link #mode}に格納され、その region が 列リージョン であることを示す値。
   */
  public static final String mode_COL = "col";
  /**
   * {@link #mode}に格納され、その region が サブリージョン であることを示す値。
   */
  public static final String mode_SUB = "sub";


  /* Constructor */

  private Region() {throw new ExceptionInInitializerError();}

  Region(String mode) { this.mode = mode; }


  /* Instance Methods */

  // TODO: 2019/01/22 Table クラスに沿った設計

  /**
   * {@link Table}クラスオブジェクトから、場所(pos)と cell データをとりだし、 regions の区切りに格納する
   *
   * @param table cells集合 データのソース
   * @param mode この region が 行・列・サブ のいずれであるかを示す。
   * @return table のデータ群( cells集合 ) を、region に格納したものの配列
   */
  public static Region[] makeRegionArray(@NotNull Table table, @NotNull String mode) {

//    返り値の用意。
    Region[] regions = new Region[CellLength];

//    regions にforで用意したregionを入れる
    for (int i = 0; i < CellLength; i++) {
      Region region = new Region(mode);

//      region へ cell を追加していく
      for (int j = 0; j < CellLength; j++) {

//        mode毎に異なる値の格納
        switch (mode) {
          case mode_ROW:
            region.add(table.get(i,j));
            break;
          case mode_COL:
            region.add(table.get(j,i));
            break;
          case mode_SUB:
            Pos pos = Pos.getPosXYConverted(i, j);
            region.add(table.get(pos));
            break;
          default:
            throw new IllegalArgumentException("invalid mode select");
        }//switch mode/

      }

//      値が入ったregionを配列に追加
      regions[i] = region;
    }

//    各cellに対してdeny
    for (Region region : regions) {
      for (Cell cell : region) {
        if(cell.isFilled()){
          region.valueFill(cell.getValue());
        }
      }
    }

    return regions;
  }

  /**
   * 新たに region オブジェクトを作成し、そこに値を格納する。
   * もともと持っているフィールドが mode だけなので、新しいインスタンスを作る意味は非常に小さいが、将来的にフィールドが増える可能性もあるのでこのようにしている。
   *
   * @param table cells集合 データのソース
   * @param sourceRegions フィールドをコピーするソース。今のところ mode のみ
   * @return table のデータ群( cells集合 ) を、新しい region に格納したものの配列
   */
  public static Region[] cloneRegionArray(@NotNull Table table, @NotNull Region[] sourceRegions) {
    return makeRegionArray(table, sourceRegions[0].mode);
    /* regions の field を再初期化する（sourceに合わせる）必要が出るかも */
//    本来必要なセクションだが、フィールドがmodeだけである限り不要
  }



  /* Methods */




  /* Property */

  /* Remain */
// 器側に寄った情報。数字の残り使用可能回数

  /**
   * この region に属する cell で、 value を埋められた cell が無いことの真偽値を返す。
   *
   * @param value 存在する値であるかどうかを確認する
   * @return !contains(int value)
   */
  public boolean remains(int value) { return !contains(value); }

  /**
   * 格納している cell の中に、 cell.value==value となるような cell が存在しているかを返す。
   *
   * @param value 格納している cell の中で、既に存在しているかどうか確認する対象となる値
   * @return false or (for eye {or(this.get(eye).getValue == value)} )
   */
  public boolean contains(int value) {
    for (Cell cell : this) {
      if (cell.getValue() == value) return true;
    }
    return false;
  }


  /* BecomeCount */
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
    for (Cell cell : this) {
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
//  からっぽの cell についての情報

  /**
   * value がまだ埋められていない（isEmpty()==true) cell をリストで返す
   *
   * @return ArrayList.addAll(cell ( .isEmpty ()))
   */
  public ArrayList<Cell> getEmptyCells() {
    ArrayList<Cell> cells = new ArrayList<>();
    for (Cell cell : this) {
      if (cell.isEmpty()) cells.add(cell);
    }
    return cells;
  }

  /**
   * 含まれている cell のうち、 isEmpty な cell の数を数える
   *
   * @return {@link #getEmptyCells()#size()}
   */
  public int countEmptyCells() {
    return getEmptyCells().size();
  }

  /**
   * 全ての値が埋められている。すなわち、 {@link #countEmptyCells()} ==0 の真偽値を返す。
   *
   * @return {@link #countEmptyCells()} ==0
   */
  public boolean isFinished() { return countEmptyCells() == 0; }


  /* NotAvailable */

  /**
   * 矛盾が発生していることを示す{@link Cell#isNotAvailable()}が true であるような cell を含んでいることの真偽値を返す。
   *
   * @return has cell(.isNotAvailable)
   * @deprecated このメソッドはおそらく不要
   */
  public boolean hasNotAvailableCell() {
    for (Cell cell : this) {
      if (cell.isNotAvailable()) return true;
    }
    return false;
  }


  /* cell に対して処理を行うメソッド */

  /**
   * region 内部の cell に対して、valueが埋まっているものとして、 region 内の全ての cell に対して value を denyする。<br>
   * region.get(index).tryFill(value)==true の場合に、 region.get(eye).deny(value)を行う。<br>
   * 別に index 番目の cell が deny されても問題ないので行け。
   *
   * @param value indexが示すcellにFillされ、index以外ではdenyされるべき値
   * @return このメソッド呼び出しの結果、更新が発生したことの真偽値（ cell.deny の返り値に true があるか)
   * @see Cell#tryFill(int)
   * @see Cell#deny(int)
   */
  public boolean valueFill(int value) {
    boolean changed = false;
    for (int i = 0; i < CellLength; i++) {
      try{
        changed |= get(i).deny(value);
      } catch (Cell.TryToDenyTheFilledValueException e){
      }
    }
    return changed;
  }




  /* デバック用 */

  public Print print = new Print();
  public class Print extends forDebug.Print{
    @Override
    protected void objData() {
      priLn("Region : "+Region.this.toString());
      priLn("mode : "+mode+" ;  Pos : "+get(0).pos.getStringIJ());
    }

    public void values() {
      headLine();
      System.out.println("Print.values");
      newLn();
      for (Cell cell : Region.this) {
        pRint(" " + cell.getValue());
      }
      newLn();
      newLn();
      System.out.println("Print.values : end");
      barPrrrrrrrrrrrrrrrrrrrrrrr();
      newLn();
    }

    public void becomeCounts() {
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
        pRint(" " + Region.this.becomeCountOf(value));
      }
      newLn();
      System.out.println("Print.becomeCounts : end");
      footLine();
    }

    public void remainList(){
      headLine();
      System.out.println("Print.remainList");
      newLn();
      pRint("num : ");
      for (int i = 1; i < CellLength; i++) {
        pRint(" " + i);
      }
      newLn();

      pRint("rem : ");
      for (int value = 1; value < CellLength + 1; value++) {
        pRint(" " + (remains(value)?1:0));
      }
      System.out.println("Print.remainList : end");
      footLine();
    }

    public void allCellPossibles() {
      headLine();
      System.out.println("Print.allCellPossibles");

      pRint("num : ");
      for (int i = 0; i <= CellLength; i++) {
        pRint(" " + i);
      }
      newLn();

      for (int i = 0; i < CellLength; i++) {
        pRint("bl" + i + " : ");
        for (boolean b : get(i).getPossible()) {
          pRint(" " + (b ? 1 : 0));
        }
        newLn();
      }

      pRint("num : ");
      for (int i = 0; i <= CellLength; i++) {
        pRint(" " + i);
      }
      newLn();
      priLn("Region.print.AllCellPossibles : end");
      barPrrrrrrrrrrrrrrrrrrrrrrr();
      newLn();
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
      becomeCounts();
      remainList();
      emptyCellCount();
      System.out.println("Print.data : end");
      footLine();
    }
  }

}