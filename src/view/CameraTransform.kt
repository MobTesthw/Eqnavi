package view

import javafx.scene.PerspectiveCamera
import javafx.scene.transform.Rotate

class CameraTransform:MoveCamera {
    val cameraInitX = 0.0
    val cameraInitY = 0.0
    val cameraInitZ = -500.0
    val cameraZoomFactor = 5.0


    private val cameraRotateX =  Rotate(0.0, Rotate.X_AXIS)
    private val cameraRotateY =  Rotate(0.0, Rotate.Y_AXIS)
    private val cameraRotateZ =  Rotate(0.0, Rotate.Z_AXIS)
    val pCamera = PerspectiveCamera(true)

    fun setAngleAlongX(angle:Double){
        cameraRotateX.angle=angle
    }
    fun setAngleAlongY(angle:Double){
        cameraRotateY.angle=angle
    }
    fun setAngleAlongZ(angle:Double){
        cameraRotateZ.angle=angle
    }

    fun getAngleAlongX() = cameraRotateX.angle
    fun getAngleAlongY() = cameraRotateY.angle
    fun getAngleAlongZ() = cameraRotateZ.angle


    init {
        pCamera.id = "Perspective Camera"
        pCamera.transforms.addAll(cameraRotateX,cameraRotateY,cameraRotateZ)
    }

    override fun moveViewport(deltaX: Double, deltaY: Double) {

    }

    override fun alongX(sceneBounds:Double) {
            pCamera.rotate+=10 // rotate around Z axis

        pCamera.translateX=sceneBounds * 3
        pCamera.translateY=0.0
        pCamera.translateZ=0.0

    }
    override fun alongY (sceneBounds:Double){
//        cameraRotateX.setAngle(90.0)
//        cameraRotateY.setAngle(0.0)
//        cameraRotateZ.setAngle(0.0)
//
//        cameraTranslate.setX(0)
//        cameraTranslate.setY(sceneBounds * 3)
//        cameraTranslate.setZ(0)
    }

    override fun reset() {
        //subScene.setCamera(perspectiveCamera);
//        Camera camera=subScene.getCamera();
//        PerspectiveCamera camera =perspectiveCamera;
        pCamera.transforms.removeAll()
        pCamera.translateX=cameraInitX
        pCamera.translateY=cameraInitY
        pCamera.translateZ=cameraInitZ

//        perspectiveCamera.getTransforms().clear()
//
//        cameraRotateX.setAngle(cameraInitAngleX)
//        cameraRotateY.setAngle(cameraInitAngleY)
//        cameraRotateZ.setAngle(cameraInitAngleZ)
//
//        cameraTranslate.setX(cameraInitX)
//        cameraTranslate.setY(cameraInitY)
//        cameraTranslate.setZ(cameraInitZ)
//
//        perspectiveCamera.getTransforms().addAll(cameraTranslate, cameraRotateX, cameraRotateY, cameraRotateZ)
//        perspectiveCamera.setFarClip(farFlip)
//        perspectiveCamera.setNearClip(nearFlip)
    }

    override fun zoom(delta:Double) {
//        //Math calculates in radians!!!!!!!
//        val dr = delta * cameraZoomFactor
//        val dx: Double
//        val dy: Double
//        val dz: Double
//        var yaw: Double
////        val roll: Double
//        var pitch: Double
//
//        pCamera.rotate.
//
//
//        pitch = cameraRotateX.getAngle()
//        yaw = cameraRotateY.getAngle()
//        //Converting to radians
//        pitch = pitch * Math.PI / 180
//        yaw = yaw * Math.PI / 180
//
//
//
//        dz = dr * Math.cos(pitch) * Math.cos(yaw)
//        dx = dr * Math.sin(yaw)
//        dy = -dr * Math.sin(pitch) * Math.cos(yaw)
//
////        ta.appendText("dr: " + dr + "  dx: " + dx + "  dy: " + dy + "  dz: " + dz + " -   pitch: " + pitch + "  yaw: " + yaw + "   - sin( yaw ): " + Math.sin(yaw) + "  cos( yaw ): " + Math.cos(yaw) + "\n")
//
//
//        pCamera.translateX = pCamera.translateX + dx
//        pCamera.translateY = pCamera.translateY + dy
//        pCamera.translateZ = pCamera.translateZ + dz
    }

    override fun rotateViewport(deltaX: Double, deltaY: Double, viewPortWidth:Double, viewPortHeight:Double) {
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

interface MoveCamera{
    fun up(){}
    fun down(){}
    fun left(){}
    fun right(){}
    fun moveViewport(deltaX:Double,deltaY:Double){}
    fun rotateViewport(deltaX: Double, deltaY: Double, viewPortWidth:Double, viewPortHeight:Double){}

    fun zoom(delta:Double){}
    fun zoomIn(){}
    fun zoomOut(){}

    fun alongX(sceneBounds:Double){}
    fun alongY(sceneBounds:Double){}
    fun alongZ(sceneBounds:Double){}

    fun reset(){}


}