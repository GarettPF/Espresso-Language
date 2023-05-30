//(1)
//#9: DD( ) has private access in 'DD'.
public class DD {
  private DD(int a) {}
}

public class EE {
  public void foo() {
    DD dd = new DD(1);
  }
}