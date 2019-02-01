package res.boardStructures;

import java.security.InvalidParameterException;

import static res.Board.CellLength;

public class TentativeCell extends Cell {
  public final Pos pos;
  public final int value;
  private final boolean[] possible = new boolean[0];

  public Pos getPos() {
    return pos;
  }

  @Override
  public int getValue() {
    return value;
  }


  public TentativeCell(Cell cell,int value){
    super(cell.pos,value);
    if(cell.isFilled()){
      throw new InvalidParameterException("try to make TentativeCell at "+cell.pos.getStringIJ()+" is Filled Cell");
    } else {
      this.pos=cell.pos;
      this.value=value;
      if (value <= 0 || CellLength < value) {
        throw new OutOfRangeException();
      }
    }

  }

  Cell getCell(){
    return new Cell(pos,value);
  }

}