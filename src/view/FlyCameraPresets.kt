package view

import kotlin.math.sqrt


fun FlyCamera.alongX() {

    camera.translateX=sceneBounds * 3
    camera.translateY= 0.0
    camera.translateZ= 0.0

    cameraRotateX.angle = 0.0
    cameraRotateY.angle = -90.0
    cameraRotateZ.angle = 0.0
}

    fun FlyCamera.backwardX() {
        camera.translateX= - sceneBounds
        camera.translateY= 0.0
        camera.translateZ= 0.0

        cameraRotateX.angle = 0.0
        cameraRotateY.angle = 90.0
        cameraRotateZ.angle = 0.0
    }
    fun FlyCamera.alongY (){
        camera.translateX=0.0
        camera.translateY= sceneBounds * 3
        camera.translateZ=0.0

        cameraRotateX.angle = 90.0
        cameraRotateY.angle = 0.0
        cameraRotateZ.angle = 0.0
    }
    fun FlyCamera.backwardY (){
        camera.translateX=0.0
        camera.translateY= - sceneBounds
        camera.translateZ=0.0

        cameraRotateX.angle = - 90.0
        cameraRotateY.angle = 0.0
        cameraRotateZ.angle = 0.0
    }

    fun FlyCamera.alongZ() {
        camera.translateX=0.0
        camera.translateY= 0.0
        camera.translateZ=sceneBounds * 3

        cameraRotateX.angle = 0.0
        cameraRotateY.angle = 180.0
        cameraRotateZ.angle = 0.0
    }
    fun FlyCamera.backwardZ() {
        camera.translateX=0.0
        camera.translateY= 0.0
        camera.translateZ= - sceneBounds

        cameraRotateX.angle = 0.0
        cameraRotateY.angle = - 180.0
        cameraRotateZ.angle = 0.0
    }

    fun FlyCamera.along0() {
        camera.translateX= -56.0
        camera.translateY= -135.0
        camera.translateZ=  146.0

        cameraRotateX.angle = sceneBounds*2/ sqrt(2.0)
        cameraRotateY.angle = sceneBounds*2
        cameraRotateZ.angle = sceneBounds*2/ sqrt(2.0)
    }