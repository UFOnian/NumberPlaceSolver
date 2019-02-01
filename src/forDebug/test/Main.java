package forDebug.test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class Main {

  public static void main(String[] args) {

    Random rnd =new Random();

    Test test=new Test();

    for (int i = 0; i < 100; i++) {
      test.add(new Koi(rnd.nextInt(1000)));
    }

    for (Koi koi : test) {
      System.out.print(","+koi.getEye());
    }

    System.out.println();
    System.out.println();


    test.sort();

    System.out.println("sort");
    for (Koi koi : test) {
      System.out.print(","+koi.getEye());
    }




  }



}


class Test extends ArrayList<Koi> {

  public void sort() {
    super.sort(Comparator.comparing(Koi::getEye));
  }
}

class Koi{

  private int eye;

  public int getEye(){
    return eye;
  }

  Koi(int eye){
    this.eye = eye;
  }
}
