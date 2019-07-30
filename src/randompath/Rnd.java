package randompath;

//import javafx.scene.paint.Color;
import java.awt.*;

public class Rnd {
    public static Color getRndColor(){
        // Generate random  Color [0..255]
        Color c = new Color(getBelow(255),getBelow(255),getBelow(255));
        return c;
    }
    public static int getBelow(int max){
        // Generate random  including max
        return (int)(Math.random()*(max+1));
    }
    public static double getBelow(double max){
        // Generate random  including max
        return (Math.random()*(max+1));
    }
    public static int getBetween(int min, int max){
        // Generate random  from min to max including max
        return (getBelow(max)+ min);
    }
    public double getBetween(double min, double max){
        // Generate random  from min to max including max
        return (getBelow(max)+ min);
    }
}
