package res;

import org.jetbrains.annotations.NotNull;
import res.boardStructures.Region;
import res.boardStructures.TentativeCell;
import res.boardStructures.Cell;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

import static res.Board.CellLength;

/**
 * 名前ながいからあとで名前もどそね
 * NPS
 */
public class NPS {

  private Board board;

  /**
   * 仮置き( Assumption )の深さを示す。
   */
  private final int assumptionDepth;

  private static int assumeConstructorCallCount;

  private static int MAXAssumptionDepth;

  private static final int AssumptionDepthSafety = CellLength * CellLength;


  private boolean wrongProblem = false;


  /* Constructors */

  /**
   * デフォルトコンストラクタは使用不可に
   */
  private NPS() {
    throw new ExceptionInInitializerError();
  }

  /**
   * NPS のエントリーポイント（てきとう）
   * 最初に問題を渡す際にはここから呼び出される。
   * 問題そのものに間違いがある場合は、{@link res.boardStructures.Cell.TryToDenyTheFilledValueException}をcatchして、
   * この問題がエラーを含んでいることを記録（WrongProblem = true)し、リターンする。
   *
   * @param inputValues 問題
   */
  public NPS(int[][] inputValues) {

//    set assumptionDepth
    assumptionDepth = 0;
    assumeConstructorCallCount = 0;

//    set board
    try {
      board = new Board(inputValues);
    } catch (Cell.TryToDenyTheFilledValueException denyFilledValue) {
      wrongProblem = true;
      return;
    }

    while (!completeSolve()) {
      System.out.println(board.table.countEmptyCells());
//      Scanner sc=new Scanner(System.in);
//      String tmp=sc.nextLine();

      // TODO: 2019/01/24 難しい問題入れると時間かかりすぎる問題

      if (!trySolve()) {
        return;
      } else if (hasContradiction()) {
        return;
      }
    }
  }

  /**
   * NPS#solveで呼び出されるメソッドから呼ばれ、数字を仮置きした場合の再帰処理を形成する。
   *
   * @param nps           このコンストラクタを呼び出す NPS オブジェクト
   * @param tentativeCell このコンストラクタを呼び出した NPS の board との差分。座標値 Pos と代入されるべき value が指定されている必要がある。
   */
  private NPS(@NotNull NPS nps, TentativeCell tentativeCell) {

//    set assumptionDepth
    assumptionDepth = nps.assumptionDepth + 1;

    System.out.println("NPS.Assume-Constructor");
    System.out.println(" assumptionDepth = " + assumptionDepth);
    System.out.println("MAXAssumptionDepth = " + MAXAssumptionDepth);
    System.out.println("assumeConstructorCallCount = " + assumeConstructorCallCount++);

    if (assumptionDepth > MAXAssumptionDepth) {
      MAXAssumptionDepth = assumptionDepth;
    }
    if (assumptionDepth > AssumptionDepthSafety) {
      throw new FatalAssumptionDepthException();
    }

//    set board
    this.board = new Board(nps.board);
    if (!this.board.trySetCell(tentativeCell)) {
      return;
    }

    while (!completeSolve()) {
      if (!trySolve()) {
        break;
      } else if (hasContradiction()) {
        break;
      }
    }
  }


  /**
   * 問題の解き終わりを検出する。
   * {@link Cell#tryFill(int)}と、{@link res.boardStructures.Region#valueFill(int)}の仕様により、
   * deny された数字が cell に fill されること
   * 埋まっている cell に対し deny されること
   * が排除できるので、全ての cell が埋まっている状態(すなわちこのメソッドの return == true )ができるのは、
   * 出題時に Region 内に重複がある場合 または 回答完了時のみ。
   * 前者はコンストラクタ呼び出し時に排除する。
   *
   * @return board.table.countEmptyCells()
   */
  public boolean completeSolve() {
    return board.table.countEmptyCells() == 0;
  }

  /**
   * board 上で矛盾が発生していることの真偽値を返す。
   * 実装当初は埋められる value が存在しない cell があるかどうかのみ。
   * もしほかの条件が見つかった場合はこちらの return に || で追加する。
   *
   * @return board 上に矛盾があることの真偽値
   */
  public boolean hasContradiction() {
    return board.table.hasNotAvailableCell();
  }

  public boolean isWrongProblem() {
    return wrongProblem;
  }

  public int getValueAt(int i, int j) {
    return board.table.get(i, j).getValue();
  }


  private boolean trySolve() {
// 差分チェッカマーク
    board.modCount.setTmp();

//                     todo : WRITE ALGORITHM METHODS
//  todo :  NPS(this,new TentativeCell(pos,val));  ...とか、再帰


    algorithm.assume();
    algorithm.onlyPlaceLeft();


// 差分チェック結果リターン
    return board.modCount.changed();
  }


  Algorithm algorithm = new Algorithm();

  private class Algorithm {
    private NPS nps = NPS.this;

    /**
     * 数字の仮置きをして、矛盾が発生しないかどうかを確認する。
     * 矛盾が発生しなければ、回答完了となる。
     */
    void assume() {

      ArrayList<Cell> emptyCells = board.table.getEmptyCells();

      emptyCells.sort(Comparator.comparing(Cell::countPossible));

      System.out.println("Algorithm.assume # setToOnlyPossible section");

      while (emptyCells.get(0).countPossible() == 1) {
        if (setToOnlyPossible(emptyCells.get(0))) {
          emptyCells.remove(emptyCells.get(0));
          if (emptyCells.size() == 0) break;
        }
        emptyCells.sort(Comparator.comparing(Cell::countPossible));
      }

      System.out.println("Algorithm.assume # setToOnlyPossible section : end");

      for (Cell cell : emptyCells) {
        for (int value = 1; value <= CellLength; value++) {
          if (cell.isBecome(value)) {

            System.out.println("Algorithm.assume # Assume TRY");
            System.out.println("at "+cell.pos.getStringIJ()+" : Value = "+value);

            NPS assumer = new NPS(nps, new TentativeCell(cell, value));
            if (assumer.completeSolve()) {
              nps.board = assumer.board;
              return;
            } else {
              cell.deny(value);
              if (setToOnlyPossible(cell)) {
                return;
              }
            }

          }
        }

      }// for(emptyCells)/
    }

    /**
     * possible[value]==true となるような value が、唯一存在する場合に、<br>
     * value を{@link Cell#tryFill(int)}したうえで返す。存在しない場合は何もせずに 0 を返す。
     *
     * @return cell への値代入が成功したことの真偽値
     */
    public boolean setToOnlyPossible(Cell cell) {
      if (cell.countPossible() != 1) return false;

      int ans = 0;

      for (int value = 1; value <= CellLength; value++) {
        if (cell.isBecome(value)) {
          ans = value;
          break;
        }
      }

      nps.print.barPrrrrrrrrrrrrrrrrrrrrrrr();
      System.out.println("Algorithm.setToOnlyPossible # TRY");
      cell.print.data();
      System.out.println("Value : "+ans);
      nps.print.barPrrrrrrrrrrrrrrrrrrrrrrr();


      return board.trySetCell(cell, ans);
    }

    void onlyPlaceLeft() {
      for (Region[] regionGroup : board.regions) {
        for (Region region : regionGroup) {
          for (int value = 1; value <= CellLength; value++) {
            if(region.becomeCountOf(value)==1){
              for (Cell cell : region) {
                if (board.trySetCell(cell,value)) {
                  break;
                }
              }
            }
          }
        }
      }
    }

    // TODO: 2019/01/15 make Algorithm Methods

  }

  private static class FatalAssumptionDepthException extends RuntimeException {
    public FatalAssumptionDepthException() {
      super("\nAssumptionDepth over the AssumptionDepthSafety");
    }
  }

  public int[][] getIntArray() {
    int[][] retInts = new int[CellLength][CellLength];

    for (int i = 0; i < CellLength; i++) {
      for (int j = 0; j < CellLength; j++) {
        retInts[i][j] = board.table.get(i, j).getValue();
      }
    }
    return retInts;
  }

  /* デバッグ用 */

  public Print print = new Print();

  public class Print extends forDebug.Print {
    @Override
    protected void objData() {
      priLn("NPS : " + NPS.this);
    }


    @Override
    public void barPrrrrrrrrrrrrrrrrrrrrrrr() {
      super.barPrrrrrrrrrrrrrrrrrrrrrrr();
    }

    public void values() {
      headLine();
      NPS.this.board.table.print.values();
      System.out.println("Print.values : end");
      footLine();
    }
  }

}
