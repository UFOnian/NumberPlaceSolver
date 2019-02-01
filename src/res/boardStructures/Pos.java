package res.boardStructures;


import static res.Board.CellLength;

/**
 * Table上でのCellの存在位置を示す座標セット
 */
public class Pos {

  private static final Pos[][] OnBoardPos;

  static {
    OnBoardPos=new Pos[CellLength][CellLength];
    for (int i = 0; i < CellLength; i++) {
      for (int j = 0; j < CellLength; j++) {
        OnBoardPos[i][j] = new Pos(i, j);
      }
    }
  }

  public static Pos get(int i, int j){
    return OnBoardPos[i][j];
  }

  /**
   * ij と xy を入れ替えた pos オブジェクトを返す。
   *
   * @param i 変換前の eye
   * @param j 変換前の j
   * @return get(getX(),getY))
   */
  public static Pos getPosXYConverted(int i,int j){
    Pos pos =get(i,j);
    return pos.getPosXYConverted();
  }

  /**
   * 単純な行番号・列番号による位置を格納する。<br>
   * CellのTable上での位置が変化しては困るのでfinalメイクした。publicにしてgetterを不要にした。<br>
   * row,columnで命名しても良かったが、大抵for文から定義されるうえ定義側の書き方で行と列が入れ替わっちゃったりするので(eye,j)で表記した。<br>
   * 大規模に運用する場合には、IDE上でパラメータ名として表示される機能があるのでrow,columnのほうが説明が省けていいと思う。
   */
  public final int i, j;


  /**
   * 3x3のsubRegionを用いた位置表示に使うxを返す。i行j列をx県y市に変換するときのx。<br>
   *
   * @return (eye / 3) *3 + j/3   ただしi/3は先に計算される。(ガウスとってから*3する)
   */
  public int getX() { int x = i / 3; return x * 3 + j / 3; }

  /**
   * 3x3のsubRegionを用いた位置表示に使うyを返す。i行j列をx県y市に変換するときのy。
   * ちなみに(x,y)=f(eye,j)の逆関数f^-1が元の関数と同じ感じ(名前忘れた)なので、<br>
   * subRegionでのインデックス(x,y)を使ってnew Pos(x,y).getX()とすればiに戻すことが出来る。
   * すなわち、 pij=new Pos(eye,j)とした場合、<br>
   * (eye,j)== ( new Pos( pij.getX() , pij.getY() ).getX() , new Pos( pij.getX() , pij.getY() ).getY() )
   *
   * @return eye%3 *3 + j%3
   */
  public int getY() { return i % 3 * 3 + j % 3; }

  /**
   * ij と xy を入れ替えた pos オブジェクトを返す。
   *
   * @return get(getX(),getY))
   */
  public Pos getPosXYConverted(){
    return get(getX(),getY());
  }

  /**
   * デフォルトコンストラクタをアクセス不可にする。<br>
   * iとjをfinalにしたため初期化してやらないとコンパイルしてくれない。<br>
   * なにかの間違いでも呼び出されると不都合なので、{@link Integer#MIN_VALUE} (int型最小値)を格納する。
   */
  private Pos() { throw new ExceptionInInitializerError(); }

  /**
   * {@link #i}と{@link #j}を指定してPosオブジェクトを生成
   */
  private Pos(int i, int j) {
    this.i = i;
    this.j = j;
  }

  @Override
  public Pos clone() { return this; }

  public int getIJWorth(){
    return i*CellLength+j;
  }

  /**
   * "( eye , j )"の形式でiとjの情報をStringで返す。デバッグ用
   *
   * @return (eye, j)
   */
  public String getStringIJ() {
    return "( " + i + " , " + j + " )";
  }

}