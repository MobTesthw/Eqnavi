package model.eqipment;

public class MiddleClass2 extends TopClass{
    public MiddleClass2(int overwrite){
        tClassVariableNumOfInstances++;
        tOverwriteVar=overwrite;
        tStaticInt+=222;
        showClassProperties();
    }
    public void showClassProperties(){
        System.out.println("TopClass tClassVariableNumOfInstances: "+ tClassVariableNumOfInstances +"  static: "+tStaticInt+"  tOverwriteVar: "+tOverwriteVar+ " final: "+finalVariable);
    }
}
