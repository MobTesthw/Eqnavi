package model

import javafx.scene.Group
import javafx.scene.paint.Color
import javafx.scene.paint.PhongMaterial
import javafx.scene.shape.Cylinder
import javafx.scene.shape.Line
import javafx.scene.text.Text
import javafx.scene.transform.Rotate

class Axis() {
    val axisGroup=Group()


    val axeX = Cylinder(axesRadius, axesLength)
    val axeY = Cylinder(axesRadius, axesLength)
    val axeZ = Cylinder(axesRadius, axesLength)

//        Line lineX = new Line(-);
    val gridIterator = sceneBounds / gridDivision
    run {
        var i = 0
        while (i < gridDivision) {
            grid.getChildren().addAll(
                    Line(i * gridIterator, (-sceneBounds).toDouble(), i * gridIterator, sceneBounds.toDouble()),
                    Line((-sceneBounds).toDouble(), i * gridIterator, sceneBounds.toDouble(), i * gridIterator)
            )
            i++
        }
    }

    val materialX = PhongMaterial()
    val materialY = PhongMaterial()
    val materialZ = PhongMaterial()

    axeX.translateX = axeX.height / 2
    axeX.transforms.addAll(Rotate(90.0, Rotate.Z_AXIS))
    materialX.specularColor = Color.rgb(255, 255, 0)
    materialX.diffuseColor = Color.rgb(255, 255, 125)
    axeX.id = "X axis"
    axeX.material = materialX

    axeY.translateY = axeY.height / 2
    materialY.specularColor = Color.rgb(0, 0, 255)
    materialY.diffuseColor = Color.rgb(125, 125, 255)
    axeY.id = "Y axis"
    axeY.material = materialY

    axeZ.translateZ = axeZ.height / 2
    axeZ.transforms.addAll(Rotate(90.0, Rotate.X_AXIS))
    materialZ.specularColor = Color.rgb(255, 0, 0)
    materialZ.diffuseColor = Color.rgb(255, 125, 125)
    axeZ.id = "Z axis"
    axeZ.material = materialZ

//        //Aim
//        Line middleHorizontalLine = new Line(viewportPane.getWidth()/2,0,viewportPane.getWidth()/2,viewportPane.getHeight());
//        Line middleVerticalLine = new Line(0,viewportPane.getHeight()/2,viewportPane.getWidth(),viewportPane.getHeight()/2);
//        aim.getChildren().addAll(middleHorizontalLine,middleVerticalLine);
//        viewportPane.getChildren().addAll(aim);
    //Grid

    //Labels Tick Marks
    var i = 0
    while (i < axesLength) {
        axesLabels.getChildren().add(Text(i.toDouble(), axesLabelOffset * 4, "x $i : 0"))
        axesLabels.getChildren().add(Text(axesLabelOffset * 4, i.toDouble(), "y $i : 0"))
        val text = Text(axesLabelOffset * 4, axesLabelOffset * 4, "Z 0 : 0 $i")
        text.translateZ = i.toDouble()
        axesLabels.getChildren().add(text)
        i += (axesLength / labelsOnAxes).toInt()
    }

    return Group(axeX, axeY, axeZ, axesLabels, grid)


}