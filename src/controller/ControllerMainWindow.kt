package controller


import customcomponent.MiniSpinner
import javafx.application.Platform
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.*
import javafx.scene.control.*
import javafx.scene.input.MouseButton
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.text.Text
import javafx.scene.transform.Rotate
import javafx.stage.Popup
import model.Axis
import model.EnvironmentNodes
import view.flycamera.*
import view.scenetree.SceneTree
import java.awt.Color
import java.awt.Paint

import java.util.*
import javafx.animation.Timeline
import org.graalvm.compiler.nodes.PauseNode.pause
import sun.jvm.hotspot.gc.shared.CollectedHeapName.Z
import javafx.scene.Scene






class ControllerMainWindow {

    @FXML     lateinit var treeView: TreeView<*>
    @FXML     lateinit var gridPaneMain: GridPane
    @FXML     lateinit var viewportPane: Pane
    @FXML     lateinit var mainSplitPane: SplitPane
    @FXML     lateinit var treeSplitPane: SplitPane
    @FXML     lateinit var toolSplitPane: SplitPane
    @FXML     lateinit var spnIteration: Spinner<Int>
    @FXML     lateinit var spnSceneBounds: Spinner<Int>

    @FXML     lateinit var ta: TextArea
    @FXML     lateinit var sFieldOfView: Slider
    @FXML     lateinit var sRotateZ: Slider
    @FXML     lateinit var cbShowAxes: CheckBox
    @FXML     lateinit var hbCameraStatusBar: HBox
    @FXML     lateinit var hbNodeStatusBar: HBox

    private val shiftIncrement = 10.0
    private val rotateIncrement = 0.1
    private val minVal = -999999.0
    private val maxVal = 999999.0
    private val fieldOfViewStartVal=30.0

    private val msnCameraX: MiniSpinner=MiniSpinner(" X:", minVal, 0.0, maxVal, shiftIncrement)
    private val msnCameraY: MiniSpinner = MiniSpinner(" Y:", minVal, 0.0, maxVal, shiftIncrement)
    private val msnCameraZ: MiniSpinner = MiniSpinner(" Z:", minVal, 0.0, maxVal, shiftIncrement)
    private val msnCameraAngleX: MiniSpinner = MiniSpinner(" ∠X:", minVal, 0.0, maxVal, rotateIncrement)
    private val msnCameraAngleY: MiniSpinner = MiniSpinner(" ∠Y:", minVal, 0.0, maxVal, rotateIncrement)
    private val msnCameraAngleZ: MiniSpinner = MiniSpinner(" ∠Z:", minVal, 0.0, maxVal, rotateIncrement)
    private val msnCameraFieldOfView: MiniSpinner = MiniSpinner(" Field of View:", minVal, fieldOfViewStartVal, maxVal, 0.1)
    private val msnCameraNearClip: MiniSpinner = MiniSpinner(" Near Clip:", minVal, 0.0, maxVal, 0.1)
    private val msnCameraFarClip: MiniSpinner = MiniSpinner(" Far Clip:", minVal, 0.0, maxVal, 0.1)

    private val tfNodeID: TextField = TextField("?")
    private val msnNodeX: MiniSpinner = MiniSpinner(" X:", minVal, 0.0, maxVal, shiftIncrement)
    private val msnNodeY: MiniSpinner = MiniSpinner(" Y:", minVal, 0.0, maxVal, shiftIncrement)
    private val msnNodeZ: MiniSpinner = MiniSpinner(" Z:", minVal, 0.0, maxVal, shiftIncrement)
    private val msnNodeAngleX: MiniSpinner = MiniSpinner(" ∠X:", minVal, 0.0, maxVal, rotateIncrement)
    private val msnNodeAngleY: MiniSpinner = MiniSpinner(" ∠Y:", minVal, 0.0, maxVal, rotateIncrement)
    private val msnNodeAngleZ: MiniSpinner = MiniSpinner(" ∠Z:", minVal, 0.0, maxVal, rotateIncrement)

    private var onPressLMBx = 0.0
    private var onPressLMBy = 0.0
    private var onPressMMBx = 0.0
    private var onPressMMBy = 0.0

    private var viewportHeight = 0.0
    private var viewPortWidth = 0.0

    private var selectedNode: Node? = null
    private var sceneBounds = 1000


//    private val content = Group() //Group for content of the scene
    private var subScene: SubScene? = null
    private val axis = Axis(sceneBounds.toDouble())
    private var environmentNodes: EnvironmentNodes? = null  //Initiated after parameters received from user
    private val flyCamera = FlyCamera(sceneBounds.toDouble())


    private lateinit var sceneTree:SceneTree  //fill scene both treeView

    //Initialization
    fun initialize() {




        spnIteration.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000000000, 10, 2)
        spnSceneBounds.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000000000, 100, 100)

        //Camera properties
        hbCameraStatusBar.children.addAll(msnCameraX.component,msnCameraY.component,msnCameraZ.component)
        hbCameraStatusBar.children.addAll(msnCameraAngleX.component,msnCameraAngleY.component,msnCameraAngleZ.component)
        hbCameraStatusBar.children.addAll(msnCameraFieldOfView.component,msnCameraNearClip.component,msnCameraFarClip.component)

        //Selected Node properties
        hbNodeStatusBar.children.addAll(tfNodeID,msnNodeX.component,msnNodeY.component,msnNodeZ.component)
        hbNodeStatusBar.children.addAll(msnNodeAngleX.component,msnNodeAngleY.component,msnNodeAngleZ.component)

//        content.children.clear()
//        content.id="Content in controller"

//        root.children.addAll(flyCamera.camera, axis.axis, axis.axisLabels, axis.grid, content)

        //SubScene
        sceneTree=SceneTree(treeView)
        sceneTree.addAlltoRoot(flyCamera.cameraXform, axis.axisGroup)

        subScene = SubScene(sceneTree.sceneRoot, 500.0, 400.0, true, SceneAntialiasing.BALANCED)
        subScene!!.camera=flyCamera.camera
//        subScene.fill=Paint.


        val timeline: Timeline
        val timelinePlaying = false
        val ONE_FRAME = 1.0 / 24.0
        val DELTA_MULTIPLIER = 200.0
        val CONTROL_MULTIPLIER = 0.1
        val SHIFT_MULTIPLIER = 0.1
        val ALT_MULTIPLIER = 0.5

        val mousePosX: Double
        val mousePosY: Double
        val mouseOldX: Double
        val mouseOldY: Double
        val mouseDeltaX: Double
        val mouseDeltaY: Double

        handleKeyboard(scene, world);
        handleMouse(scene, world);

        viewportPane.children.clear()
        setViewportSize()
        viewportPane.children.add(subScene)

//        setSelectedCamera()
//        flyCamera.reset()

        //Register Events and listeners

//        lookAndFeel()
        showCameraTransform()



    }

    private fun setViewportSize() {
        viewPortWidth = viewportPane.width
        viewportHeight = viewportPane.height
        subScene!!.width = viewPortWidth
        subScene!!.height = viewportHeight
        //        ta.appendText("Viewport sizes set to w: "+viewPortWidth+" h: "+viewportHeight+"\n");
    }

    @FXML     private fun drawAxes() {
//        if (cbShowAxes.isSelected)
//            root.children.addAll(axis.axis)
//        else
//            root.children.removeAll(axis.axis)
    }

    @FXML     private fun btnGoClick(event: ActionEvent) {
//        content.children.clear()
        //SetViewportSize();
        val itr = spnIteration.value
        sceneBounds = spnSceneBounds.value
        ta.text = "Starting new iteration number of iterations: $itr  scene bound: $sceneBounds\n"

        environmentNodes = EnvironmentNodes(sceneBounds, itr)
        sceneTree.addAlltoRoot(environmentNodes!!.content)

//        ta.appendText("\n  Calculation finished.. ? Scene nodes: " + root.children.toTypedArray().size + "\n")

    }

    @FXML     private fun btnDoClick(event: ActionEvent) {

    }

    @FXML     private fun btnDoClick1(event: ActionEvent) {

    }

    @FXML     private fun btnDoClick2(event: ActionEvent) {

    }

    @FXML     private fun resetCamera() {
//        flyCamera.reset()
    }

    @FXML     private fun setSelectedCamera() {
//        subScene!!.camera = flyCamera.camera
    }

    @FXML     private fun btnSaveClick(event: ActionEvent) {
        val name=SaveImage.toDefault(viewportPane.snapshot(SnapshotParameters(), null))
        ta.appendText("Saved snapshot :  $name\n")
    }

    @FXML     private fun btnPrintAllScene() {
        ta.appendText("\n  Scene nodes: " + "\n")
        for (n in getAllNodes(/*gridPaneMain*/viewportPane)) {
            printNodeProperties(n)
        }

    }

//    private fun lookAndFeel() {
//        //Mouse Scroll
//        viewportPane.setOnScroll { e -> flyCamera.zoom(e.deltaY) }
//        //Mouse pressed
//        viewportPane.setOnMousePressed { e ->
//
//            if (e.button == MouseButton.PRIMARY) {
//                onPressLMBx = e.sceneX
//                onPressLMBy = e.sceneY
//
//            } else if (e.button == MouseButton.MIDDLE) {
//                onPressMMBx = e.sceneX
//                onPressMMBy = e.sceneY
//
//            }
//        }
//        //Mouse dragged
//        viewportPane.setOnMouseDragged { e ->
//            if (e.button == MouseButton.PRIMARY) {
//                //Viewport move
//                flyCamera.moveViewport( onPressLMBx - e.sceneX, onPressLMBy - e.sceneY )
//                onPressLMBx = e.sceneX
//                onPressLMBy = e.sceneY
//
//            } else if (e.button == MouseButton.MIDDLE) {
//                //Viewport rotate
//                val dx = onPressMMBx - e.sceneX
//                val dy = onPressMMBy - e.sceneY
//                ta.appendText(flyCamera.rotateViewport(dx, dy, viewportPane.width, viewportPane.height))
//                onPressMMBx = e.sceneX
//                onPressMMBy = e.sceneY
//            }
//        }
//        //Select node on Mouse click
//        viewportPane.setOnMouseClicked { e ->
//            selectedNode = e.pickResult.intersectedNode
//            printNodeProperties(e.pickResult.intersectedNode)
//
//            //Show node properties statusbar if node selected
//            if (selectedNode!!.id === viewportPane.id) {
//                hbNodeStatusBar.isVisible = false
////                printNodeProperties(flyCamera.camera)
//            } else {
//                hbNodeStatusBar.isVisible = true
//                showSelectedNodeTransformBar()
//
//                //RMB Selected object properties
//                if (e.button == MouseButton.SECONDARY) {
//                    //          Worked
//                    //                    final Stage dialog = new Stage();
//                    //                    dialog.initModality(Modality.APPLICATION_MODAL);
//                    ////                    dialog.initOwner(this.get);
//                    //                    VBox dialogVbox = new VBox(20);
//                    //                    dialogVbox.getChildren().add(new Text("This is a Dialog"));
//                    //                    Scene dialogScene = new Scene(dialogVbox, 300, 200);
//                    //                    dialog.setScene(dialogScene);
//                    //                    dialog.show();
//
//                    val popup = Popup()
//                    popup.x = e.x
//                    popup.y = e.y
//                    val deleteShape = Label("Delete Shape")
//                    deleteShape.setOnMouseClicked {
////                        root.children.remove(selectedNode)
//                        popup.hide()
//                    }
//                    popup.content.addAll(HBox(deleteShape))
//                    popup.show(viewportPane.scene.window)
//                }
//
//            }
//        }
//
//        //Hide node status when no nodes selected
//        hbNodeStatusBar.managedProperty().bind(hbNodeStatusBar.visibleProperty())
//
//        viewportPane.widthProperty().addListener  {_ -> setViewportSize() }
//        viewportPane.heightProperty().addListener {_ -> setViewportSize() }
//
//        //Split panes auto divider
//        mainSplitPane.widthProperty().addListener { _,_,_-> mainSplitPane.setDividerPositions(0.9) }
//        treeSplitPane.widthProperty().addListener { _,_,_-> treeSplitPane.setDividerPositions(0.2) }
//
//        //Toolbar width save during changes
//        val w1 = 798.4
//        val pos1 = 0.785
//        toolSplitPane.widthProperty().addListener { _, _, _ -> toolSplitPane.setDividerPositions(1 - w1 * (1 - pos1) / toolSplitPane.width) }
//
////        //Slider camera Field of View
////        sFieldOfView.widthProperty().addListener { _,_,new_val ->flyCamera.camera.fieldOfView=new_val.toDouble() }
////        //!!!
////        flyCamera.camera.fieldOfView=fieldOfViewStartVal
////
//        //Slider rotate around Z axis
////        sRotateZ.valueProperty().addListener {_,_,new_val->flyCamera.camera.rotate=new_val.toDouble()}
//
////        //Set listener to all camera transforms
////        val observableList = FXCollections.observableList(subScene!!.camera.transforms)
////        observableList.forEach { node -> node.setOnTransformChanged { showCameraTransform() } }
//
////        msnCameraX.textField.setOnKeyReleased { flyCamera.camera.translateX = msnCameraX.value }
////        msnCameraY.textField.setOnKeyReleased { flyCamera.camera.translateY = msnCameraY.value }
////        msnCameraZ.textField.setOnKeyReleased { flyCamera.camera.translateZ = msnCameraZ.value }
////        msnCameraAngleX.textField.setOnKeyReleased {  flyCamera.cameraRotateX.angle = msnCameraAngleX.value }
////        msnCameraAngleY.textField.setOnKeyReleased {  flyCamera.cameraRotateY.angle = msnCameraAngleY.value }
////        msnCameraAngleZ.textField.setOnKeyReleased {
////            flyCamera.cameraRotateZ.angle = msnCameraAngleZ.value
//////            sRotateZ.value = msnCameraAngleZ.value
////        }
//
////        msnCameraFieldOfView.textField.setOnKeyReleased {
////            flyCamera.camera.fieldOfViewProperty().value = msnCameraFieldOfView.value
////            sFieldOfView.value = msnCameraNearClip.value
////        }
////        msnCameraNearClip.textField.setOnKeyReleased { flyCamera.camera.nearClipProperty().value = msnCameraNearClip.value }
////        msnCameraFarClip.textField.setOnKeyReleased { flyCamera.camera.farClipProperty().value = msnCameraNearClip.value }
////
//
//        //Selected Node
//        tfNodeID.textProperty().addListener { _: ObservableValue<out String>, _: String, newValue: String ->
//            // expand the TextField
//            // Do this in a Platform.runLater because of Textfield has no padding at first time and so on
//            Platform.runLater {
//                val text = Text(newValue)
//                text.font = tfNodeID.font // Set the same font, so the size is the same
//                val width = (text.layoutBounds.width // This big is the Text in the TextField
//                        + tfNodeID.padding.left + tfNodeID.padding.right // Add the padding of the TextField
//                        + 2.0) // Add some spacing
//                tfNodeID.prefWidth = width // Set the width
//                tfNodeID.positionCaret(tfNodeID.caretPosition) // If you remove this line, it flashes a little bit
//            }
//        }
//        msnNodeX.textField.setOnKeyReleased { selectedNode!!.translateX = msnNodeX.value }
//        msnNodeY.textField.setOnKeyReleased { selectedNode!!.translateY = msnNodeY.value }
//        msnNodeZ.textField.setOnKeyReleased { selectedNode!!.translateZ = msnNodeZ.value }
//
//    }

    private fun showCameraTransform() {
//        msnCameraX.setText(flyCamera.camera.translateX)
//        msnCameraY.setText(flyCamera.camera.translateY)
//        msnCameraZ.setText(flyCamera.camera.translateZ)
//        msnCameraAngleX.setText(flyCamera.cameraRotateX.angle)
//        msnCameraAngleY.setText(flyCamera.cameraRotateY.angle)
//        msnCameraAngleZ.setText(flyCamera.cameraRotateZ.angle)
//        msnCameraFieldOfView.setText(flyCamera.camera.fieldOfViewProperty().value!!)
//        msnCameraNearClip.setText(flyCamera.camera.nearClipProperty().value!!)
//        msnCameraFarClip.setText(flyCamera.camera.farClipProperty().value!!)

    }

    private fun showSelectedNodeTransformBar() {
        tfNodeID.text = selectedNode!!.id
        msnNodeX.setText(selectedNode!!.translateX)
        msnNodeY.setText(selectedNode!!.translateY)
        msnNodeZ.setText(selectedNode!!.translateZ)

    }

    @FXML     private fun cameraAlongX()    {}//= flyCamera.alongX()
    @FXML     private fun cameraAlongY()    {}//= flyCamera.alongY()
    @FXML     private fun cameraAlongZ()    {}//= flyCamera.alongZ()
    @FXML     private fun cameraBackwardX() {}//= flyCamera.backwardX()
    @FXML     private fun cameraBackwardY() {}//= flyCamera.backwardY()
    @FXML     private fun cameraBackwardZ() {}//= flyCamera.backwardZ()
    @FXML     private fun cameraAlong0()    {}//= flyCamera.along0()


    private fun printNodeProperties(node: Node) {

        val boundsInScene = node.localToScene(node.boundsInLocal)

        ta.appendText(
                //            "\n"+
                node.typeSelector +
                        " " + node.style +
                        "  x: "/*+ nodeTranslate.getX()+*/ + boundsInScene.minX.toInt() +
                        "  y: "/*+ nodeTranslate.getY()+*/ + boundsInScene.minY.toInt() +
                        "  z: "/*+ nodeTranslate.getZ()+*/ + boundsInScene.minZ.toInt() +
                        "  " + node.id +
                        "  to sting: " + node.toString() +
                        "  Bound in local: " + node.boundsInLocal.toString() +
                        "  Layout bounds: " + node.layoutBounds.toString() +
                        //                    "  Layout bounds: "+selectedNode..getLayoutBounds().toString()+
                        //                    "  transforms: "+selectedNode.getTransforms().toArray().toString()+
                        "  Compute area in screen: " + node.computeAreaInScreen() +
                        "\n")
        for (i in 0 until node.transforms.toTypedArray().size) ta.appendText(node.transforms.toTypedArray()[i].toString() + "\n")
    }


    private fun getAllNodes(root: Parent?): ArrayList<Node> {
        val nodes = ArrayList<Node>()
        addAllDescendants(root!!, nodes)
        return nodes
    }

    private fun addAllDescendants(parent: Parent, nodes: ArrayList<Node>) {
        for (node in parent.childrenUnmodifiable) {
            nodes.add(node)
            if (node is Parent)
                addAllDescendants(node, nodes)
        }
    }






}


/*
    Issue:
    Z rotation local pivot
    MiniSpinner:
        V Caret jumping while editing
        V Change on scroll
        Round Value 0
        Try buttons instead of Labels
    ViewPort:
        Popup
        orientation 550 offset
        add aim and grid
    Scene content to xml
    selected element:
        Modifying
        Display properties
    screenshot:
        File Choosing to save
        screenshot resolution
    Initial information
    - Boundaries
    subScene AntiAliasing
    Aligning camera and node properties
    */


