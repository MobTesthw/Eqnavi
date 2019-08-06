package model.eqipment;

public abstract class TopClass {
    int tClassVariableNumOfInstances = 0;
    int tOverwriteVar = 0;
    static int tStaticInt=0;
    final int finalVariable = 100;
    public static void showClassProperties1(){
//        System.out.println("TopClass tClassVariableNumOfInstances: "+ tClassVariableNumOfInstances +"  static: "+tStaticInt+"  tOverwriteVar: "+tOverwriteVar+ " final: "+finalVariable);
        System.out.println("  static: "+tStaticInt);
    }
}

