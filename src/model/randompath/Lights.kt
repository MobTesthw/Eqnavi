package model.randompath

import javafx.scene.AmbientLight
import javafx.scene.Group
import javafx.scene.PointLight
import javafx.scene.paint.Color

class Lights {
    fun buildLight():Group{
        val light =  PointLight(Color.WHITE)
        light.setTranslateX(50.0)
        light.setTranslateY(-300.0)
        light.setTranslateZ(-400.0)
        val light1 =  PointLight(Color.YELLOW)
        light1.setTranslateX(1000.0)
        light1.setTranslateY(1000.0)
        light1.setTranslateZ(1000.0)
        val light2 = PointLight(Color.color(0.6, 0.3, 0.4))
        light2.setTranslateX(400.0)
        light2.setTranslateY(0.0)
        light2.setTranslateZ(-400.0)
        val ambientLight1 =  AmbientLight(Color.color(0.2, 0.2, 0.2))
        val ambientLight2 =  AmbientLight(Color.WHITE)

        return Group(ambientLight1, light,light1, light2)
    }



}