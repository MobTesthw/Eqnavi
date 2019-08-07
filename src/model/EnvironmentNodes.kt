package model

import javafx.scene.Group
import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.scene.paint.PhongMaterial
import javafx.scene.shape.Box
import javafx.scene.shape.Cylinder
import javafx.scene.shape.Polygon
import javafx.scene.shape.Sphere
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.scene.transform.Rotate
import javafx.scene.transform.Translate
import model.randompath.RndFX

class EnvironmentNodes(sceneBounds:Int,iteration:Int) {
    val content = Group()//Group for content of the scene


    init {
        content.id = "Environment"

//        Box slab = new Box(46,230,5);
//        slab.setTranslateX(400);
//        slab.setTranslateX(400);
//        slab.setTranslateX(400);
//        PhongMaterial slabMat = new PhongMaterial();
//        slabMat.setDiffuseMap(new Image("file:D:\\Dist\\img\\Gravel_Diffuse.png"));
//        slabMat.setBumpMap(new Image("file:D:\\Dist\\img\\Gravel_Normal.png"));
//        slabMat.setSpecularMap(new Image("file:D:\\Dist\\img\\Gravel_Specular.png"));
//        slab.setMaterial(slabMat);
//        content.getChildren().add(slab);

        val slabX = Box(200.0, 200.0, 1.0)
        slabX.id = "Slab X"
        val slabZ = Box(200.0, 200.0, 1.0)
        slabZ.id = "Slab Z"
        slabX.translateZ = 100.0
        slabZ.translateX = 100.0
//        slabX.setTranslateY(-100);
//        slabZ.setTranslateY(-100);
        slabX.transforms.addAll(Rotate(-45.0, Rotate.X_AXIS))
        slabZ.transforms.addAll(Rotate(-45.0, Rotate.Z_AXIS))
        slabZ.transforms.addAll(Rotate(-90.0, Rotate.X_AXIS))
        content.getChildren().addAll(slabX, slabZ)


        val text = Text("RHUB group")
        text.id="RHUB group"
        text.x = 10.0
        text.y = 50.0
        text.font = Font(20.0)
        text.transforms.add(Rotate(30.0, 50.0, 30.0))
        content.getChildren().add(text)

        val polygon = Polygon()
        polygon.id = "Polygon x6"
        polygon.points.addAll(
                0.0, 0.0,
                20.0, 10.0,
                10.0, 20.0)
        polygon.translateZ = 100.0
        content.children.add(polygon)

        val sphere =  Sphere()
        sphere.id = "Sphere with material"
        sphere.radius = 50.0
        sphere.translateX = 200.0
        sphere.translateY = 200.0
        sphere.translateZ = 200.0
        val mat =  PhongMaterial()
        val diffuseMap = Image("file:D:\\Dist\\img\\diffuseMap1.png")
//        Image diffuseMap = new Image("file:D:\\Dist\\img\\2_no_clouds_4k.jpg");
//        2_no_clouds_4k.jpg
//        elev_bump_4k.jpg
//        Image normalMap = new Image("file:D:\\Dist\\img\\elev_bump_4k.jpg");
        val normalMap = Image("file:D:\\Dist\\img\\normalMap1.png")
        mat.diffuseMap = diffuseMap
        mat.bumpMap = normalMap
        mat.specularColor = Color.CORNFLOWERBLUE

        sphere.material = mat
        content.children.add(sphere)

        val cylinder = Cylinder(2.0, 4000.0)
        cylinder.id = "Long test cylinder"
        cylinder.transforms.addAll(Translate(707.0, 707.0, 1000.0), Rotate(-54.0, 45.0, -54.0))
        content.children.add(cylinder)


//        content.getChildren().add(RndFX.getCircles(3));
        content.children.add(RndFX.getBoxes(iteration, 50.0, 50.0, 50.0, sceneBounds.toDouble(), sceneBounds.toDouble(), sceneBounds.toDouble()))
//        content.getChildren().add(RndFX.getSpheres(30,50,boundX,boundY,boundZ));

        //ResetCamera();

    }
}