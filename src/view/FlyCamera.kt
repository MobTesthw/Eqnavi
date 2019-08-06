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

    val cameraInitX = 0.0
    val cameraInitY = 0.0
    val cameraInitZ = -500.0

    private val cameraMoveMultiplier = 2.0
    private val cameraRotateMultiplier = 2.0
    private val cameraZoomMultiplier = 2.0



//    fun AngleAlongX() = cameraRotateX.angle
//    fun AngleAlongY() = cameraRotateY.angle
//    fun AngleAlongZ() = cameraRotateZ.angle


    init {
        camera.id = "Perspective Camera"
        camera.transforms.addAll(cameraRotateX,cameraRotateY,cameraRotateZ)

        reset()
    }

     fun moveViewport(deltaX: Double, deltaY: Double) {
         camera.translateX  += deltaX * cameraMoveMultiplier
         camera.translateY  += deltaY * cameraMoveMultiplier
     }

    fun rotateViewport(deltaX: Double, deltaY: Double, viewPortWidth:Double, viewPortHeight:Double) {
        //For Camera
        cameraRotateX.angle -=  deltaY * cameraRotateMultiplier / viewPortHeight*  360 * (Math.PI / 180)
        cameraRotateY.angle -=  deltaX * cameraRotateMultiplier/ viewPortWidth * -360 * (Math.PI / 180)

    }





     fun zoom(delta:Double) {
        //Math calculates in radians!!!!!!!
        val dr = delta * cameraZoomMultiplier
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






}

class Hero {
    fun useSuperpowers() {
        println("Applied super powers")
    }
}
fun Hero.savePlanet() {
    useSuperpowers()
}

