package res.Test;
import forDebug.Print;
import res.boardStructures.*;

public class CellTest  {

  public static void main(String[] args) {
    int[][] values=new int[9][9];

    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        values[i][j]=0;
      }
    }

//    makeCellArray Test
    Table table= new Table(values);


//    printValuesTest
    table.print.values();

    Region[] rowRegions =Region.makeRegionArray(table,Region.mode_ROW);

    table.get(3,4).tryFill(5);
    rowRegions[3].valueFill(5);

    table.print.values();
    System.out.println("Possibles");
    rowRegions[3].print.allCellPossibles();

    table.get(3,3).deny(2);

    table.get(3,3).print.data();

    System.out.println("======================");


    Cell acell=table.get(0,0);
    System.out.println("-acell-00------");

    acell.print.data();

    System.out.println("deny 2");
    acell.deny(2);
    acell.print.data();

    System.out.println("try 1");
    System.out.println(acell.tryFill(1));
    System.out.println("try 2");
    System.out.println(acell.tryFill(2));


    acell=table.get(0,1);
    System.out.println("-acell-01------");
    acell.print.data();

    System.out.println("try 100");
    acell.tryFill(100);

    System.out.println("line44");
    System.out.println("deny 2");
    acell.deny(2);
    acell.print.data();
    System.out.println("tryFill 2: "+acell.tryFill(2));
    System.out.println("tryFill 1: "+acell.tryFill(1));
    System.out.println("tryFill 3: "+acell.tryFill(3));

    acell.print.data();

  }
}
