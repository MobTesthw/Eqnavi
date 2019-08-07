package model

import javafx.scene.PerspectiveCamera

interface Move{
    fun zoomIn  (camera:PerspectiveCamera) {}
    fun zoomOut (camera:PerspectiveCamera) {}
    fun left    (camera:PerspectiveCamera) {}
    fun right   (camera:PerspectiveCamera) {}
    fun up      (camera:PerspectiveCamera) {}
    fun down    (camera:PerspectiveCamera) {}
    fun leftRotate    (camera:PerspectiveCamera) {}
    fun rightRotate   (camera:PerspectiveCamera) {}
    fun upRotate      (camera:PerspectiveCamera) {}
    fun downRotate    (camera:PerspectiveCamera) {}
}

abstract class PerspectiveCameraTransform: Move {
    override fun zoomIn(camera:PerspectiveCamera) {

    }
}