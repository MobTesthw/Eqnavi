package model.eqipment;

public class MiddleClass1 extends TopClass{
    public MiddleClass1(int overwrite){
       tClassVariableNumOfInstances++;
        tOverwriteVar=overwrite;
        tStaticInt+=11;
        showClassProperties();
    }
    public void showClassProperties(){
        System.out.println("TopClass tClassVariableNumOfInstances: "+ tClassVariableNumOfInstances +"  static: "+tStaticInt+"  tOverwriteVar: "+tOverwriteVar+ " final: "+finalVariable);
    }
}
