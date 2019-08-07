package view.flycamera

/**
 * Fly camera prepare camera, movement transitions related to viewport
 * and returns camera group
 */


import javafx.scene.Group
import javafx.scene.PerspectiveCamera
import javafx.scene.transform.Rotate
import kotlin.math.cos
import kotlin.math.sin

open class FlyCamera( val sceneBounds:Double) {
    //    val presets=FlyCameraPresets(this)
    val camera = PerspectiveCamera(true)
    private val cameraDistance = 500.0
    val cameraXform = Xform()
    private val cameraXform2 = Xform()
    private val cameraXform3 = Xform()
    private val cameraNearClip=0.1
    private val cameraFarClip=10000.0


//    val cameraRotateX = Rotate(0.0, Rotate.X_AXIS)
//    val cameraRotateY = Rotate(0.0,Rotate.Y_AXIS)
//    val cameraRotateZ = Rotate(0.0, Rotate.Z_AXIS)
//
//    val cameraInitX = 0.0
//    val cameraInitY = 0.0
//    val cameraInitZ = -500.0

    private val cameraMoveMultiplier = 2.0
    private val cameraRotateMultiplier = 10
    private val cameraZoomMultiplier = -2.0

//    val cameraGroup = Group()


    init {


        //To have Y axis directed up:
        camera.transforms.add(Rotate(180.0,Rotate.Z_AXIS))


        cameraXform.children.add(cameraXform2)
        cameraXform2.children.add(cameraXform3)
        cameraXform3.children.add(camera)

        cameraXform3.rotate=180.0

        camera.id = "Perspective Camera"
        camera.nearClip=cameraNearClip
        camera.farClip=cameraFarClip
        camera.translateZ=-cameraDistance


//        camera.transforms.addAll(cameraRotateX, cameraRotateY, cameraRotateZ)
//        reset()

    }

    fun moveViewport(deltaX: Double, deltaY: Double) {

//        val ax = cameraRotateX.angle * Math.PI / 180
//        val ay = cameraRotateY.angle * Math.PI / 180
//        camera.translateX += deltaX * cos(ay) * cameraMoveMultiplier
//        camera.translateY += deltaY * cos(ax) * cameraMoveMultiplier
//        camera.translateZ += (deltaX * sin(ay) - deltaY * sin(ax)) * cameraMoveMultiplier
    }

    fun rotateViewport(deltaX: Double, deltaY: Double, viewPortWidth: Double, viewPortHeight: Double):String {


//        //For Camera
//        val ax = cameraRotateX.angle * Math.PI / 180
//        val ay = cameraRotateY.angle * Math.PI / 180
////        val az=cameraRotateZ.angle * Math.PI / 180
//
//        val deltaAx = deltaY * cameraRotateMultiplier / viewPortHeight * 360 * (Math.PI / 180)
//        val deltaAy = deltaX * cameraRotateMultiplier / viewPortWidth * -360 * (Math.PI / 180)
//
//        val sinAx = sin(ax)
//        val sinAy = sin(ay)
//        val cosAx = cos(ax)
//        val cosAy = cos(ay)
//
//        val finDeltaAx =  - ( deltaAx * cos(ay) - deltaAy * sin(ax))
//        val finDeltaAy =  - ( deltaAy * cos(ax) - deltaAx * sin(ay))
//        val finDeltaAz=0.0
//        cameraRotateX.angle += finDeltaAx
//        cameraRotateY.angle += finDeltaAy

//        cameraRotateZ -= cameraRotateX.angle
        return ""//""Δx $finDeltaAx  Δy $finDeltaAy  Δz $finDeltaAz  sinAx $sinAx cosAx $cosAx  sinAy $sinAy cosAy $cosAy\n"
    }

    fun zoom(delta: Double) {
//        //Math calculates in radians!!!!!!!
//        val dr = delta * cameraZoomMultiplier
//        val dx: Double
//        val dy: Double
//        val dz: Double
//        val yaw: Double = cameraRotateY.angle * Math.PI / 180
////        val roll: Double
//        val pitch: Double = cameraRotateX.angle * Math.PI / 180
//
//        dz = dr * cos(pitch) * cos(yaw)
//        dx = dr * sin(yaw)
//        dy = -dr * sin(pitch) * cos(yaw)
//
//        camera.translateX = camera.translateX + dx
//        camera.translateY = camera.translateY + dy
//        camera.translateZ = camera.translateZ + dz
    }

}




//fun up(){}
//fun down(){}
//fun left(){}
//fun right(){}
//fun moveViewport(deltaX:Double,deltaY:Double){}
//fun rotateViewport(deltaX: Double, deltaY: Double, viewPortWidth:Double, viewPortHeight:Double){}
//
//fun zoom(delta:Double){}
//fun zoomIn(){}
//fun zoomOut(){}
//
//fun reset(){}

