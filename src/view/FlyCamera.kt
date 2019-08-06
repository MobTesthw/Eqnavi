package view


import javafx.scene.PerspectiveCamera
import javafx.scene.transform.Rotate
import kotlin.math.cos
import kotlin.math.sin

open class FlyCamera(val sceneBounds:Double) {
//    val presets=FlyCameraPresets(this)
    val camera = PerspectiveCamera(true)

    val cameraRotateX =  Rotate(0.0, Rotate.X_AXIS)
    val cameraRotateY =  Rotate(0.0, Rotate.Y_AXIS)
    val cameraRotateZ =  Rotate(0.0, Rotate.Z_AXIS)

    protected val cameraInitX = 0.0
    protected val cameraInitY = 0.0
    protected val cameraInitZ = -500.0
    protected val cameraZoomFactor = 5.0



    fun getAngleAlongX() = cameraRotateX.angle
    fun getAngleAlongY() = cameraRotateY.angle
    fun getAngleAlongZ() = cameraRotateZ.angle


    init {
        camera.id = "Perspective Camera"
        camera.transforms.addAll(cameraRotateX,cameraRotateY,cameraRotateZ)

    }

     fun moveViewport(deltaX: Double, deltaY: Double) {

    }




     fun reset() {

        camera.transforms.removeAll()
        camera.translateX=cameraInitX
        camera.translateY=cameraInitY
        camera.translateZ=cameraInitZ

        camera.farClip = sceneBounds*5
        camera.nearClip = 0.1
    }

     fun zoom(delta:Double) {
        //Math calculates in radians!!!!!!!
        val dr = delta * cameraZoomFactor
        val dx: Double
        val dy: Double
        val dz: Double
        val yaw: Double = cameraRotateY.angle * Math.PI / 180
//        val roll: Double
        val pitch: Double = cameraRotateX.angle * Math.PI / 180

        dz =  dr * cos(pitch) * cos(yaw)
        dx =  dr * sin(yaw)
        dy = -dr * sin(pitch) * cos(yaw)

//        ta.appendText("dr: " + dr + "  dx: " + dx + "  dy: " + dy + "  dz: " + dz + " -   pitch: " + pitch + "  yaw: " + yaw + "   - sin( yaw ): " + Math.sin(yaw) + "  cos( yaw ): " + Math.cos(yaw) + "\n")


        camera.translateX = camera.translateX + dx
        camera.translateY = camera.translateY + dy
        camera.translateZ = camera.translateZ + dz
    }

     fun rotateViewport(deltaX: Double, deltaY: Double, viewPortWidth:Double, viewPortHeight:Double) {
        //For Camera
        cameraRotateX.angle = cameraRotateX.angle + deltaY / viewPortHeight*  360 * (Math.PI / 180)
        cameraRotateY.angle = cameraRotateY.angle + deltaX / viewPortWidth * -360 * (Math.PI / 180)




//                }
//                //For Nodes
//                mouseOldX = mousePosX;
//                mouseOldY = mousePosY;
//                ta.appendText("Middle button Dragged X: "+cameraRotateX.getAngle()+"  Y: "+cameraRotateY.getAngle()+"  dx: "+dx+"  dy: "+dy+"\n");
    }




}

class Hero {
    fun useSuperpowers() {
        println("Applied super powers")
    }
}
fun Hero.savePlanet() {
    useSuperpowers()
}

