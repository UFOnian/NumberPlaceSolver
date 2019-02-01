package res.boardStructures;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import res.Board;

import static res.Board.CellLength;


/**
 * 数独を解くゲーム盤のうち、最も小さい単位となるひとつのマスを表す。<br>
 * <br>
 * {@link Region} や {@link Board} に同一のゲーム盤の同一の位置にある cell を同一のオブジェクトとして扱ってもらうため、インスタンス生成を制限している。<br>
 * 具体的には、<br>
 * コンストラクタの不可視化。int の二次元配列を引数に持つメソッド または Cell の二次元配列を引数に持つ複製を生成するメソッド によってのみインスタンスが渡される。<br>
 * 参照値の異なる自身の複製を返す{@link Object#clone()}メソッドを OverRide し、使用不可に。<br>
 * 以上の措置をとった。<br>
 * インスタンスの生成には{@link Table}クラスのコンストラクタを用いる。<br>
 * <br>
 * このドキュメントでは、<br>
 * {@link Table}クラスコンストラクタにより生成される Cell のオブジェクト群を cells集合 と呼ぶ。<br>
 * 数字が確定して(あるいは仮置きで) cell に数字を書き込むことを「埋める」と表現する。<br>
 * ※この「埋める」には、人間が普通に解くときによく行う「メモ書き」を含まない。<br>
 * 何も「埋まっていない」状態を「空欄である」と表現する。<br>
 */
public class Cell {


  /* Field */

  /**
   * この cell が cells集合 上で存在している位置
   */
  public final Pos pos;

  /**
   * この cell に埋まっている数字。 何も埋まっていない場合は 0。<br>
   * 空欄であるかどうかを確認するには{@link #isFilled()}を呼び出すこと。<br>
   * この値の外部からの変更はコンストラクタ又は{@link #tryFill(int)}のみに制限される。
   */
  private int value;

  /**
   * possible[value] がこの cell に埋まる可能性があるかどうかを格納。<br>
   * {@link #deny(int)}に value を引数として呼び出すことで value が埋まらないことを確定させられる。<br>
   * possible[0] は基本的に何も示していないが、この cell が埋まっている場合は false になる。
   */
  private boolean[] possible;


  /* Constructor */

  /**
   * 意味のない cell オブジェクト の生成を防ぐため、デフォルトコンストラクタを無効化。
   */
  @Contract(" -> fail")
  protected Cell() {throw new RuntimeException("this constructor is NOT supposed to be called");}

  /**
   * cellオブジェクト は単体では存在せず、集合体として存在し、cells集合 を成すものとして設計されている。<br>
   * オブジェクトの生成は、{@link Table} クラスのコレクションの一部としてのみ可能（にしたい）。<br>
   * このコンストラクタは {@link Table}によって呼ばれ、コレクションの一要素としての cellオブジェクト をインスタンス生成する。
   *
   * @param pos   この cell が、 cells 集合上のどの位置に存在しているかを示す {@link Pos}オブジェクト
   * @param value この cell の初期 value を指定する値。
   */
  protected Cell(@NotNull Pos pos, int value) {

//    initialize pos
    this.pos = pos;

//    initialize Value
    this.value = value;

//    initialize possible[]
    possible = new boolean[CellLength + 1];
    if (value < 0 | CellLength < value) {
      throw new OutOfRangeException();
    } else if (isFilled()) {
      possible[value] = true;
    } else {
      for (int i = 0; i < possible.length; i++) {
        possible[i] = true;
      }
    }

  }

  /**
   * Cellオブジェクトは単体では存在せず、集合体として存在し、cells集合 を成すものとして設計されている。<br>
   * オブジェクトの生成は、{@link Table}クラスのコレクションの一部としてのみ可能（にしたい）<br>
   * このコンストラクタは {@link Table}コンストラクタによって呼ばれ、配列の一要素としての cellオブジェクト をインスタンス生成する。
   *
   * @param cell 複製元の cellオブジェクト
   */
  Cell(@NotNull Cell cell) {
    this.pos = cell.pos;  // pos は複製でなくて良い。場所しか示してない上、final make。
    this.value = cell.value;  // プリミティブな値(int)のコピーなので問題nothing
    this.possible = cell.possible.clone(); //配列はcloneしないと、壊れますねぇ
  }



  /* Static Method */

  /**
   * 引数valueが、 1 から 9 の整数値の範囲内にあるか(ゲームで使う数字であるか)を確認する。
   *
   * @param value チェックする値
   * @return 引数value が 0以下 又は 10以上 の真偽値
   */
  private static boolean OutOfRangeCheck(int value) {
    if (value <= 0 | CellLength < value) {
      return true;
    } else return false;
  }


  /* Methods */

  /* Property */

  public int getValue() {
    return value;
  }

  public boolean[] getPossible() {
    return possible;
  }

  /**
   * このcellに、既に値が埋められているかを返す。
   *
   * @return {@link #value}が0以外であることの真偽値
   */
  public boolean isFilled() {
    return value != 0;
  }

  /**
   * このcellに、値が埋められていないかを返す。
   * {@link #isFilled()}と機能は重複する
   *
   * @return {@link #value}が0であることの真偽値
   */
  public boolean isEmpty() {
    return value == 0;
  }

  /**
   * targetValue がこの cell に埋まりうるかどうかを返す。埋まりうるなら true。
   *
   * @param targetValue 埋まりうるかを確認する値。
   * @return {@link #possible}[targetValue]
   */
  public boolean isBecome(int targetValue) {
    if (OutOfRangeCheck(targetValue)) {
      System.err.println("cell.isBecome : out of range(==[1,9]) targetValue");
      return false;
    } else {
      if (isFilled()) {
//        System.out.println("cell.isBecome : this cell is Filled");
      }
      return possible[targetValue];
    }
  }

  /**
   * この cell が、値を Fill されておらず、また、 possible な、すなわち埋めることのできる値を持たない場合、 true を返す。
   *
   * @return !{@link #isFilled()} && ( {@link #countPossible()} == 0 )
   */
  public boolean isNotAvailable() {
    if (isFilled()) return false;
    return countPossible() == 0;
  }

  /**
   * possible[value] == true となるような value の個数が返る。
   *
   * @return sum ( eye:=[1,CellLength] , possible[eye] ? 1 : 0 )
   */
  public int countPossible() {
    int cnt = 0;
    for (int value = 1; value < possible.length; value++) {
      if (possible[value]) cnt++;
    }
    return cnt;
  }


  /* フィールドを更新する動作メソッド */

  /**
   * targetValueをこのcellには埋まらないことが確定した値として、{@link #possible}を更新する。
   *
   * @param targetValue このcellに埋まらない値として扱われる値。
   * @return このメソッドの呼び出しの結果、 possible が更新されたことの真偽値。
   */
  public boolean deny(int targetValue) {
    boolean changed = false;
    if (OutOfRangeCheck(targetValue)) {
      System.err.println("cell.deny : out of range(==[1,9]) targetValue");
    } else if (getValue()==targetValue) {
      System.err.println();
      return false;
    } else {
      changed = possible[targetValue];
      possible[targetValue] = false;
    }
    return changed;
  }

  public class TryToDenyTheFilledValueException extends RuntimeException{
    TryToDenyTheFilledValueException(int value){
      super("try to Deny the Filled value at " + pos.getStringIJ() +" to "+value);
    }
  }



  /**
   * 引数 value をこの cell の {@link #value}に埋める。<br>
   * ただし、denyメソッドにより埋まらないことが確定している値は埋めることができない。
   *
   * @param value 　このcellに埋める値。
   * @return 処理の結果、this.value が更新された場合はtrueを、更新されない倍委はfalseを返す。
   */
  public boolean tryFill(int value) {
    if (OutOfRangeCheck(value)) {
      System.err.println("cell.tryFill : out of range(==[1,9]) value");
      return false;
    } else if (isFilled()) {
//      System.out.println("cell.tryFill : this cell is Filled");
      return false;
//      return getValue() == value;
    } else if (!isBecome(value)) {
      return false;
    } else {
      this.value = value;
      for (int i = 0; i < possible.length; i++) {
        possible[i] = (i == value);
      }
      return true;
    }
  }


  /* その他 */



  /**
   * 全ての{@link Region}または{@link Board}に対して同一のcellオブジェクトへの参照値を渡し、それを操作することで同期をとるので、<br>
   * 既存のオブジェクトから同一で別のオブジェクトが生成されるこのメソッドは呼び出されると困る。<br>
   * 自分一人で開発してるので注意していればいいだけだが、前提を覆す可能性があるので殺す。（発生させないようにする）
   *
   * @return なし。例外なく例外を投げる。例外を投げることに対する例外を認めない。俺が例外だ。
   * @throws CloneNotSupportedException インスタンスのクローン生成を制限
   */
  @Override
  protected Object clone() throws CloneNotSupportedException {
    System.err.println("cell.clone : DONT MAKE CLONE. prefer the existing prefer-value");
    throw new CloneNotSupportedException();
  }


  /**
   * この例外はコンストラクタ {@link #Cell(Pos, int)}によって呼ばれ、引数として与えられた value として、ありえない数値を渡されたことを示す。
   */
  class OutOfRangeException extends IllegalArgumentException {
    OutOfRangeException(){
      this("\nvalue ("+value+") is OutOfRange(==[0,"+CellLength+"])\n" +
                              "where pos points @ > " + pos.getStringIJ());
    }
    OutOfRangeException(String message){ super(message); }
  }


  /* デバッグ用 */

  public Print print = new Print();

  /**
   * デバッグに使用。各種データを標準出力に表示する。
   * todo : 最終的には削除、または private make すること。
   */
  public class Print extends forDebug.Print {
    @Override
    protected void objData() {
      priLn("Cell : "+Cell.this.toString());
    }

    public void possibles() {
      headLine();
      System.out.println("Print.possibles");
      pRint("num : ");
      for (int i = 0; i < possible.length; i++) {
        pRint(" " + i);
      }
      newLn();

      pRint("bol : ");
      for (boolean b : possible) {
        pRint(" " + (b ? 1 : 0));
      }
      newLn();
      System.out.println("Print.possibles : end");
      barPrrrrrrrrrrrrrrrrrrrrrrr();
    }

    public void data() {
      Cell cell=Cell.this;
      headLine();
      System.out.println("Print.data");
      priLn("*  pos     : " + pos.getStringIJ());
      priLn("*  value   : " + getValue());
      pRint("* possibles()");
      print.possibles();
      priLn("*  Filled  : " + cell.isFilled());
      priLn("isNotAvailable() = " + isNotAvailable());
      System.out.println("Print.data : end");
      barPrrrrrrrrrrrrrrrrrrrrrrr();
    }
  }
}

