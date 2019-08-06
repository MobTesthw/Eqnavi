package view

import kotlin.math.sqrt

interface Move {

    fun up(){}
    fun down(){}
    fun left(){}
    fun right(){}
    fun moveViewport(deltaX:Double,deltaY:Double){}
    fun rotateViewport(deltaX: Double, deltaY: Double, viewPortWidth:Double, viewPortHeight:Double){}

    fun zoom(delta:Double){}
    fun zoomIn(){}
    fun zoomOut(){}

//    fun alongX(sceneBounds:Double){}
//    fun alongY(sceneBounds:Double){}
//    fun alongZ(sceneBounds:Double){}
//    fun backwardX(sceneBounds:Double){}  //Rerverse
//    fun backwaedY(sceneBounds:Double){}
//    fun backwardZ(sceneBounds:Double){}
//    fun along0(sceneBounds:Double){}

    fun reset(){}



}