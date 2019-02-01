package forDebug;

abstract public class Print {
  abstract protected void objData();

  protected void priLn(String str){
    System.out.println(str);
  }
  protected void priLn(){
    System.out.println();
  }
  protected void newLn(){
    priLn();
  }
  protected void pRint(String str){
    System.out.print(str);
  }
  protected void barPrrrrrrrrrrrrrrrrrrrrrrr(){
    priLn("===================================");
  }
  protected void headLine(){
    newLn();
    barPrrrrrrrrrrrrrrrrrrrrrrr();
    objData();
  }
  protected void footLine(){
    barPrrrrrrrrrrrrrrrrrrrrrrr();
    newLn();
  }
}
