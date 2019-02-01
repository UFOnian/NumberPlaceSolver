package res.Test;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CallAllMethodsSample {

  private  void aMethod() {
    System.out.println("CallAllMethodsSample.aMethod");
  }

  private  void bMethod() {
    System.out.println("CallAllMethodsSample.bMethod");
  }

  private  void cMethod() {
    System.out.println("CallAllMethodsSample.cMethod");
  }

  private int dMethod() {
    System.out.println("CallAllMethodsSample.dMethod");
    return 12;
  }
  CallAllMethodsSample(){
    aMethod();bMethod();cMethod();dMethod();
    System.out.println("cont");
  }

  //50 more methods.

  //method call the rest
  public void callAll() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
    Method[] methods = this.getClass().getMethods();
    for (Method m : methods) {
      System.out.println(m.getName());

      if (m.getName().endsWith("d")) {
        System.out.println(m.getName());
      }
    }
  }

  public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
    CallAllMethodsSample cams=new CallAllMethodsSample();
    cams.bMethod();
    cams.callAll();


  }

}