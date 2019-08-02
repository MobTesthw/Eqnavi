package view

import javafx.scene.PerspectiveCamera
import javafx.scene.transform.Rotate
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class CameraTransform:MoveCamera {
    private val cameraInitX = 0.0
    private val cameraInitY = 0.0
    private val cameraInitZ = -500.0
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
        pCamera.translateX=sceneBounds * 3
        pCamera.translateY= 0.0
        pCamera.translateZ= 0.0

        cameraRotateX.angle = 90.0
        cameraRotateY.angle = -90.0
        cameraRotateZ.angle = 0.0

    }
    override fun alongY (sceneBounds:Double){
        pCamera.translateX=0.0
        pCamera.translateY= sceneBounds * 3
        pCamera.translateZ=0.0

        cameraRotateX.angle = 90.0
        cameraRotateY.angle = 0.0
        cameraRotateZ.angle = 0.0
    }

    override fun alongZ(sceneBounds:Double) {
        pCamera.translateX=0.0
        pCamera.translateY= 0.0
        pCamera.translateZ=sceneBounds * 3

        cameraRotateX.angle = 90.0
        cameraRotateY.angle = 180.0
        cameraRotateZ.angle = 0.0
    }

    override fun along0(sceneBounds:Double) {
        pCamera.translateX= -56.0
        pCamera.translateY= -135.0
        pCamera.translateZ=  146.0

        cameraRotateX.angle = sceneBounds*2/ sqrt(2.0)
        cameraRotateY.angle = sceneBounds*2
        cameraRotateZ.angle = sceneBounds*2/ sqrt(2.0)
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


        pCamera.translateX = pCamera.translateX + dx
        pCamera.translateY = pCamera.translateY + dy
        pCamera.translateZ = pCamera.translateZ + dz
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
    fun along0(sceneBounds:Double){}

    fun reset(){}


}