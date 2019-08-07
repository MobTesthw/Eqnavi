package view.flycamera

/**
 * Fly camera prepare camera, movement transitions related to viewport
 * and returns camera group
 */


import javafx.scene.PerspectiveCamera

open class FlyCamera( val sceneBounds:Double) {
    //    val presets=FlyCameraPresets(this)
    val camera = PerspectiveCamera(true)
    private val cameraDistance = 500.0
    private val cameraNearClip=0.1
    private val cameraFarClip=5000.0

    val cameraXform = Xform()
    private val cameraXform2 = Xform()
    private val cameraXform3 = Xform()



//    val cameraRotateX = Rotate(0.0, Rotate.X_AXIS)
//    val cameraRotateY = Rotate(0.0,Rotate.Y_AXIS)
//    val cameraRotateZ = Rotate(0.0, Rotate.Z_AXIS)
//
//    val cameraInitX = 0.0
//    val cameraInitY = 0.0
//    val cameraInitZ = -500.0

    private val cameraMoveMultiplier = 2.0
    private val cameraRotateMultiplier = 2
    private val cameraZoomMultiplier = 2.0

//    val cameraGroup = Group()


    init {


        //To have Y axis directed up:

        cameraXform.children.add(cameraXform2)
        cameraXform2.children.add(cameraXform3)
        cameraXform3.children.add(camera)
        cameraXform3.setRotateZ(180.0)

        camera.id = "Perspective Camera"


//        camera.transforms.addAll(cameraRotateX, cameraRotateY, cameraRotateZ)
        reset()

    }
    fun reset(){
        camera.fieldOfView=30.0
        camera.nearClip=cameraNearClip
        camera.farClip=cameraFarClip
        camera.translateZ=-cameraDistance

        cameraXform.ry.angle=320.0
        cameraXform.rx.angle=40.0

    }

    fun moveViewport(deltaX: Double, deltaY: Double) {
        cameraXform2.t.x = cameraXform2.t.x - deltaX * cameraMoveMultiplier  // -
        cameraXform2.t.y = cameraXform2.t.y - deltaY * cameraMoveMultiplier  // -
//        val ax = cameraRotateX.angle * Math.PI / 180
//        val ay = cameraRotateY.angle * Math.PI / 180
//        camera.translateX += deltaX * cos(ay) * cameraMoveMultiplier
//        camera.translateY += deltaY * cos(ax) * cameraMoveMultiplier
//        camera.translateZ += (deltaX * sin(ay) - deltaY * sin(ax)) * cameraMoveMultiplier
    }

    fun rotateViewport(deltaX: Double, deltaY: Double, viewPortWidth: Double, viewPortHeight: Double):String {
        cameraXform.ry.angle = cameraXform.ry.angle + deltaX * cameraRotateMultiplier  // +
        cameraXform.rx.angle = cameraXform.rx.angle - deltaY * cameraRotateMultiplier  // -

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

        val newZ = camera.translateZ +delta*cameraZoomMultiplier

        camera.setTranslateZ(newZ)

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

    fun up(){
//        if (event.isControlDown() && event.isShiftDown()) {
//        cameraXform2.t.setY(cameraXform2.t.getY() - 10.0 * CONTROL_MULTIPLIER)
//    } else if (event.isAltDown() && event.isShiftDown()) {
//        cameraXform.rx.setAngle(cameraXform.rx.getAngle() - 10.0 * ALT_MULTIPLIER)
//    } else if (event.isControlDown()) {
//        cameraXform2.t.setY(cameraXform2.t.getY() - 1.0 * CONTROL_MULTIPLIER)
//    } else if (event.isAltDown()) {
//        cameraXform.rx.setAngle(cameraXform.rx.getAngle() - 2.0 * ALT_MULTIPLIER)
//    } else if (event.isShiftDown()) {
//        val z = camera.getTranslateZ()
//        val newZ = z + 5.0 * SHIFT_MULTIPLIER
//        camera.setTranslateZ(newZ)
    }
    fun down(){
//        if (event.isControlDown() && event.isShiftDown()) {
//            cameraXform2.t.setY(cameraXform2.t.getY() + 10.0 * CONTROL_MULTIPLIER)
//        } else if (event.isAltDown() && event.isShiftDown()) {
//            cameraXform.rx.setAngle(cameraXform.rx.getAngle() + 10.0 * ALT_MULTIPLIER)
//        } else if (event.isControlDown()) {
//            cameraXform2.t.setY(cameraXform2.t.getY() + 1.0 * CONTROL_MULTIPLIER)
//        } else if (event.isAltDown()) {
//            cameraXform.rx.setAngle(cameraXform.rx.getAngle() + 2.0 * ALT_MULTIPLIER)
//        } else if (event.isShiftDown()) {
//            val z = camera.getTranslateZ()
//            val newZ = z - 5.0 * SHIFT_MULTIPLIER
//            camera.setTranslateZ(newZ)
//        }
    }
        fun right(){
//            if (event.isControlDown() && event.isShiftDown()) {
//                cameraXform2.t.setX(cameraXform2.t.getX() + 10.0 * CONTROL_MULTIPLIER)
//            } else if (event.isAltDown() && event.isShiftDown()) {
//                cameraXform.ry.setAngle(cameraXform.ry.getAngle() - 10.0 * ALT_MULTIPLIER)
//            } else if (event.isControlDown()) {
//                cameraXform2.t.setX(cameraXform2.t.getX() + 1.0 * CONTROL_MULTIPLIER)
//            } else if (event.isAltDown()) {
//                cameraXform.ry.setAngle(cameraXform.ry.getAngle() - 2.0 * ALT_MULTIPLIER)
//            }
//        }
    }
    fun left(){
//        if (event.isControlDown() && event.isShiftDown()) {
//            cameraXform2.t.setX(cameraXform2.t.getX() - 10.0 * CONTROL_MULTIPLIER)
//        } else if (event.isAltDown() && event.isShiftDown()) {
//            cameraXform.ry.setAngle(cameraXform.ry.getAngle() + 10.0 * ALT_MULTIPLIER)  // -
//        } else if (event.isControlDown()) {
//            cameraXform2.t.setX(cameraXform2.t.getX() - 1.0 * CONTROL_MULTIPLIER)
//        } else if (event.isAltDown()) {
//            cameraXform.ry.setAngle(cameraXform.ry.getAngle() + 2.0 * ALT_MULTIPLIER)  // -
//        }
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

