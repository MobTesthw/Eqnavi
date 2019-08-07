package view.flycamera

fun FlyCamera.reset() {
    camera.transforms.removeAll()
    camera.translateX=this.cameraInitX
    camera.translateY=cameraInitY
    camera.translateZ=cameraInitZ

    cameraRotateX.angle = 0.0
    cameraRotateY.angle = 0.0
    cameraRotateZ.angle = 0.0

    camera.farClip = sceneBounds*5
    camera.nearClip = 0.1
    camera.fieldOfView=30.0

}

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
    camera.translateZ=-sceneBounds

    cameraRotateX.angle = 0.0
    cameraRotateY.angle = 0.0
    cameraRotateZ.angle = 0.0
}

fun FlyCamera.backwardZ() {
    camera.translateX=0.0
    camera.translateY= 0.0
    camera.translateZ= sceneBounds*3

    cameraRotateX.angle = 0.0
    cameraRotateY.angle = 180.0
    cameraRotateZ.angle = 0.0
}

fun FlyCamera.along0() {
    camera.translateX= sceneBounds
    camera.translateY= 0.0
    camera.translateZ=  sceneBounds

    cameraRotateX.angle = 0.0
    cameraRotateY.angle = -135.0
    cameraRotateZ.angle = 0.0
}