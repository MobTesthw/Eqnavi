package model

import javafx.scene.Group
import javafx.scene.paint.Color
import javafx.scene.paint.PhongMaterial
import javafx.scene.shape.Cylinder
import javafx.scene.shape.Line
import javafx.scene.text.Text
import javafx.scene.transform.Rotate


class Axis(sceneBounds:Double) {

    private val labelsOnAxis = 10.0
    private val axisLabelOffset = 10.0
    private val axisLength = sceneBounds * 2
    private val axisRadius = axisLabelOffset / 2
    private val gridDivision = 20.0

    val axisGroup = Group()

    val axis = Group()
    val axisLabels = Group()
    val grid = Group()


    init {
        axisGroup.id = "Axis Group"
        axis.id="Axis"
        axisLabels.id="Axis label"
        grid.id="Grid"

        axisGroup.children.addAll(axis,axisLabels,grid)

        val axisX = Cylinder(axisRadius, axisLength)
        axisX.id="Axis X"
        val axisY = Cylinder(axisRadius, axisLength)
        axisY.id="Axis Y"
        val axisZ = Cylinder(axisRadius, axisLength)
        axisZ.id="Axis Z"
        axis.children.addAll(axisX,axisY,axisZ)

        val gridIterator = sceneBounds / gridDivision

        for(i in 0 .. gridDivision.toInt())  {
                grid.children.addAll(
                        Line(i * gridIterator, (-sceneBounds), i * gridIterator, sceneBounds),
                        Line((-sceneBounds), i * gridIterator, sceneBounds, i * gridIterator)
                )
        }

        val materialX = PhongMaterial()
        val materialY = PhongMaterial()
        val materialZ = PhongMaterial()


        axisX.translateX = axisX.height / 2
        axisX.transforms.addAll(Rotate(90.0, Rotate.Z_AXIS))
        materialX.specularColor = Color.rgb(255, 255, 0)
        materialX.diffuseColor = Color.rgb(255, 255, 125)
        axisX.id = "X axis"
        axisX.material = materialX

        axisY.translateY = axisY.height / 2
        materialY.specularColor = Color.rgb(0, 0, 255)
        materialY.diffuseColor = Color.rgb(125, 125, 255)
        axisY.id = "Y axis"
        axisY.material = materialY

        axisZ.translateZ = axisZ.height / 2
        axisZ.transforms.addAll(Rotate(90.0, Rotate.X_AXIS))
        materialZ.specularColor = Color.rgb(255, 0, 0)
        materialZ.diffuseColor = Color.rgb(255, 125, 125)
        axisZ.id = "Z axis"
        axisZ.material = materialZ

        //Labels Tick Marks
        var i = 0
        while (i < axisLength)
        {
            axisLabels.children.add(Text(i.toDouble(), axisLabelOffset * 4, "x $i : 0"))
            axisLabels.children.add(Text(axisLabelOffset * 4, i.toDouble(), "y $i : 0"))
            val text = Text(axisLabelOffset * 4, axisLabelOffset * 4, "Z 0 : 0 $i")
            text.id="Axis Z Tick Mark $i"
            text.translateZ = i.toDouble()
            axisLabels.children.add(text)
            i += (axisLength / labelsOnAxis).toInt()
        }

    }
}