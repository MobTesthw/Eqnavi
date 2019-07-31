package view

import javafx.scene.PerspectiveCamera
import javafx.scene.transform.Rotate

class CameraTransform:MoveCamera {
    val cameraInitX = 0.0
    val cameraInitY = 0.0
    val cameraInitZ = -500.0
    val cameraZoomFactor = 5.0

    override fun alongX(pCamera:PerspectiveCamera,sceneBounds:Double) {
            pCamera.rotate+=10 // rotate around Z axis
//        pCamera.rotationAxis.angle(0.0,-90.0,0.0)
////        cameraRotateX.setAngle(0.0)
////        cameraRotateY.setAngle(-90.0)
////        cameraRotateZ.setAngle(0.0)
//
        pCamera.translateX=sceneBounds * 3
        pCamera.translateY=0.0
        pCamera.translateZ=0.0

    }
    override fun alongY (pCamera:PerspectiveCamera,sceneBounds:Double){
        cameraRotateX.setAngle(90.0)
        cameraRotateY.setAngle(0.0)
        cameraRotateZ.setAngle(0.0)

        cameraTranslate.setX(0)
        cameraTranslate.setY(sceneBounds * 3)
        cameraTranslate.setZ(0)
    }

    override fun reset(pCamera: PerspectiveCamera) {
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

    override fun zoom(pCamera:PerspectiveCamera,delta:Double) {
        //Math calculates in radians!!!!!!!
        val dr = delta * cameraZoomFactor
        val dx: Double
        val dy: Double
        val dz: Double
        var yaw: Double
//        val roll: Double
        var pitch: Double

        pCamera.rotate.


        pitch = cameraRotateX.getAngle()
        yaw = cameraRotateY.getAngle()
        //Converting to radians
        pitch = pitch * Math.PI / 180
        yaw = yaw * Math.PI / 180



        dz = dr * Math.cos(pitch) * Math.cos(yaw)
        dx = dr * Math.sin(yaw)
        dy = -dr * Math.sin(pitch) * Math.cos(yaw)

        ta.appendText("dr: " + dr + "  dx: " + dx + "  dy: " + dy + "  dz: " + dz + " -   pitch: " + pitch + "  yaw: " + yaw + "   - sin( yaw ): " + Math.sin(yaw) + "  cos( yaw ): " + Math.cos(yaw) + "\n")


        cameraTranslate.setX(cameraTranslate.getX() + dx)
        cameraTranslate.setY(cameraTranslate.getY() + dy)
        cameraTranslate.setZ(cameraTranslate.getZ() + dz)
    }

}

interface MoveCamera{
    fun up(){}
    fun down(){}
    fun left(){}
    fun right(){}
    fun zoom(pCamera:PerspectiveCamera,delta:Double){}
    fun zoomIn(){}
    fun zoomOut(){}

    fun alongX(pCamera:PerspectiveCamera,sceneBounds:Double){}
    fun alongY(pCamera:PerspectiveCamera,sceneBounds:Double){}
    fun alongZ(pCamera:PerspectiveCamera,sceneBounds:Double){}

    fun reset(pCamera:PerspectiveCamera){}

}