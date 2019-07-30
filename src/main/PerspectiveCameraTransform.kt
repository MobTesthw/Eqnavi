package main

interface Move{
    fun zoomIn() {}
    fun zoomOut(){}
    fun left()   {}
    fun right()  {}
    fun up()     {}
    fun down()   {}
}

class PerspectiveCameraTransform:Move {
    override fun zoomIn() {
        super.zoomIn()
    }
}