package model.randompath;

import javafx.scene.Group;
import javafx.scene.effect.BoxBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import static model.randompath.Rnd.getBelow;

//Random Material

 public class RndFX {
      private static Material getMaterial(){
        final PhongMaterial material = new PhongMaterial();
//        switch (rnd) {
//            case 0:
//                material.setSpecularColor(Color.YELLOW);
//                material.setDiffuseColor(Color.YELLOWGREEN);
//                //material.getSpecularColor(Color.RED);
//                break;
//            case 1:
//                material.setSpecularColor(Color.ORANGE);
//                material.setDiffuseColor(Color.RED);
//                break;
//            case 2:
//                material.setDiffuseColor(Color.BLUE);

//                material.setSpecularColor(Color.LIGHTBLUE);
//                break;
//            case 3:
//                material.setDiffuseColor(Color.DARKGREY);
//                material.setSpecularColor(Color.GREY);
//                break;
//            case 4:
//                material.setDiffuseColor(Color.ANTIQUEWHITE);
//                material.setSpecularColor(Color.DIMGREY);
//                break;
//            default:
//                material.setSpecularColor(Color.BEIGE);
//                material.setDiffuseColor(Color.AZURE);
////                ta.appendText("  ---default color \n");
//                break;
        //Color.color(getBelow(255),getBelow(255),getBelow(255));
        material.setSpecularColor( Color.rgb(getBelow(255),getBelow(255),getBelow(255)));
        material.setDiffuseColor( Color.rgb(getBelow(255),getBelow(255),getBelow(255)));
        return material;
    }
    static public Group getBoxes(int number, double maxWidth, double maxHeight, double maxDepth, double farX, double farY, double farZ){
         Group boxes = new Group();
         boxes.setId("Multiple random boxes");
         for(int i=0;i<number;i++){

             Box box = new Box();
//             Box boxNoRotate = new Box();
             double width, height,x,y,z,depth,angleX,angleY,angleZ;
             width = getBelow(maxWidth)/2;
             height = getBelow(maxHeight)/2;
             depth =getBelow(maxDepth);

             x = getBelow(farX);
             y = getBelow(farY);
             z =getBelow(farZ);

             angleX = getBelow(360);
             angleY = getBelow(360);
             angleZ = getBelow(360);

             //Setting the properties of the Box
             box.setWidth(width);
             box.setHeight(height);
             box.setDepth(depth);
             box.getTransforms().addAll(new Translate(x,y,z));

//             boxNoRotate.setWidth(width);
//             boxNoRotate.setHeight(height);
//             boxNoRotate.setDepth(depth);
//             boxNoRotate.getTransforms().addAll(new Translate(x,y,z));
//             box.getTransforms();

             //box.setRotate(angleZ);
             box.getTransforms().addAll(
                     new Rotate(angleX,Rotate.X_AXIS),
                     new Rotate(angleY,Rotate.Y_AXIS),
                     new Rotate(angleZ,Rotate.Z_AXIS)
             );

//             new Rotate(box.getTranslateX(),box.getTranslateY(),box.getTranslateZ(),angleX,Rotate.X_AXIS),
//                     new Rotate(box.getTranslateX(),box.getTranslateY(),box.getTranslateZ(),angleY,Rotate.Y_AXIS),
//                     new Rotate(box.getTranslateX(),box.getTranslateY(),box.getTranslateZ(),angleZ,Rotate.Z_AXIS)

             box.setMaterial( getMaterial());
             box.setId("AutoBox# "+i);
             box.setDrawMode(DrawMode.FILL);

//              boxNoRotate.setMaterial( getMaterial());
//              boxNoRotate.setId("AutoBoxNoRotate# "+i);
//              boxNoRotate.setDrawMode(DrawMode.FILL);



             boxes.getChildren().addAll(box/*,boxNoRotate*/);

         }
         return boxes;
     }
     static public Group getSpheres(int number, double maxRadius, double farX, double farY, double farZ){
         Group spheres = new Group();
         for(int i=0;i<number;i++){

             Sphere sphere = new Sphere();

             double radius,x,y,z;
             radius = getBelow(maxRadius);


             x = getBelow(farX);
             y = getBelow(farY);
             z =getBelow(farZ);


             //Setting the properties of the Box
             sphere.setRadius(radius);

             sphere.getTransforms().addAll(
                     new Translate(x,y,z)
             );

             sphere.setMaterial( getMaterial());
             sphere.setId("AutoSphere# "+i);
             sphere.setDrawMode(DrawMode.FILL);
             spheres.getChildren().add(sphere);

         }
         return spheres;
     }
     static public Group getCircles(int number){
          Group circles = new Group();
          for (int j = 0; j < number; j++) {

              Circle circle = new Circle(150, Color.web("white", 0.05));
              circle.setCenterX(j*2);
              circle.setCenterY(j*2);
              circle.setStrokeType(StrokeType.OUTSIDE);
              circle.setStroke(Color.web("white", 0.16));
              circle.setStrokeWidth(4);
              circles.getChildren().add(circle);
         }
         circles.setEffect(new BoxBlur(10, 10, 3));
      return circles;
      }
}
