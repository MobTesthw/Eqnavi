package model.randompath

import javafx.geometry.Point3D
import javafx.scene.Group
import javafx.scene.shape.Cylinder
import javafx.scene.transform.Rotate
import kotlin.math.cos
import kotlin.math.sin

class Path3D(
//        sceneBounds:Double=1000.0,
        iteration:Int=100,
        step:Double=20.0,
        yaw:Double=10.0,
        roll:Double=360.0,
        startPoint3D: Point3D=Point3D(0.0,0.0,0.0)   //Start point
        ){
    val content=Group()
    private val radius=1.0

    init {
        var oldYaw=Rnd.getBelow(yaw)
        var x = startPoint3D.x
        var y = startPoint3D.y
        var z = startPoint3D.z
        var tetha = 0.0
        for (i in 0..iteration){
            tetha += 2*(Math.toRadians(Rnd.getBelow(yaw)) - Math.toRadians(Rnd.getBelow(yaw)))
            val height=Rnd.getBelow(step)
            val cylinder = Cylinder(radius,height)
            cylinder.id = "Cylinder $i rnd"


            cylinder.translateX = x
            cylinder.translateY = y
            cylinder.translateZ = z
            cylinder.transforms.addAll(Rotate(tetha,Rotate.Z_AXIS))

            x += height * sin(tetha)
            y += height * cos(tetha)
            content.children.add(cylinder)
        }



    }

}