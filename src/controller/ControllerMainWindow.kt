package controller

import customcomponent.MiniSpinner
import model.Axis
import model.EnvironmentNodes
import javafx.application.Platform
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.embed.swing.SwingFXUtils
import javafx.scene.*
import javafx.scene.control.*
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.input.*
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.text.Text
import javafx.stage.Popup
import view.*



import javax.imageio.ImageIO
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar


class ControllerMainWindow {



    //    private final Set<KeyCode> pressedKeys = new HashSet<>();
    @FXML     lateinit var treeView: TreeView<*>
    @FXML     lateinit var gridPaneMain: GridPane
    @FXML     lateinit var viewportPane: Pane
    @FXML     lateinit var mainSplitPane: SplitPane
    @FXML     lateinit var toolSplitPane: SplitPane
    @FXML     lateinit var spnIteration: Spinner<Int>
    @FXML     lateinit var spnSceneBounds: Spinner<Int>

//    @FXML     private val btnPrintAllScene: Button? = null

//    @FXML     lateinit var selectSavePath: TextField
    @FXML     lateinit var ta: TextArea
    @FXML     lateinit var sFieldOfView: Slider
    @FXML     lateinit var sRotateZ: Slider
    @FXML     lateinit var cbShowAxes: CheckBox

    @FXML     lateinit var hbCameraStatusBar: HBox
    @FXML     lateinit var hbNodeStatusBar: HBox

    private val shiftIncrement = 10.0
    private val rotateIncrement = 5.0

    private val msnCameraX: MiniSpinner=MiniSpinner(" X:", -999999.0, 0.0, 999999.0, shiftIncrement)
    private val msnCameraY: MiniSpinner = MiniSpinner(" Y:", -999999.0, 0.0, 999999.0, shiftIncrement)
    private val msnCameraZ: MiniSpinner = MiniSpinner(" Z:", -999999.0, 0.0, 999999.0, shiftIncrement)
    private val msnCameraAngleX: MiniSpinner = MiniSpinner(" ∠X:", -999999.0, 0.0, 999999.0, rotateIncrement)
    private val msnCameraAngleY: MiniSpinner = MiniSpinner(" ∠Y:", -999999.0, 0.0, 999999.0, rotateIncrement)
    private val msnCameraAngleZ: MiniSpinner = MiniSpinner(" ∠Z:", -999999.0, 0.0, 999999.0, rotateIncrement)
    private val msnCameraFieldOfView: MiniSpinner = MiniSpinner(" Field of View:", -999999.0, 0.0, 999999.0, 0.1)
    private val msnCameraNearClip: MiniSpinner = MiniSpinner(" Near Clip:", -999999.0, 0.0, 999999.0, 0.1)
    private val msnCameraFarClip: MiniSpinner = MiniSpinner(" Far Clip:", -999999.0, 0.0, 999999.0, 0.1)

    private val tfNodeID: TextField = TextField("?")
    private val msnNodeX: MiniSpinner = MiniSpinner(" X:", -999999.0, 0.0, 999999.0, shiftIncrement)
    private val msnNodeY: MiniSpinner = MiniSpinner(" Y:", -999999.0, 0.0, 999999.0, shiftIncrement)
    private val msnNodeZ: MiniSpinner = MiniSpinner(" Z:", -999999.0, 0.0, 999999.0, shiftIncrement)
    private val msnNodeAngleX: MiniSpinner = MiniSpinner(" ∠X:", -999999.0, 0.0, 999999.0, rotateIncrement)
    private val msnNodeAngleY: MiniSpinner = MiniSpinner(" ∠Y:", -999999.0, 0.0, 999999.0, rotateIncrement)
    private val msnNodeAngleZ: MiniSpinner = MiniSpinner(" ∠Z:", -999999.0, 0.0, 999999.0, rotateIncrement)

    private var onPrssSceneX: Double = 0.toDouble()
    private var onPressSceneY: Double = 0.toDouble()
    //    private double orgTranslateX, orgTranslateY;
    private var mousePosX: Double = 0.toDouble()
    private var mousePosY: Double = 0.toDouble()
//    private val mouseOldX = 0.0
//    private val mouseOldY = 0.0
//    private val nearFlip = 0.1
//    private val farFlip = 50000.0

    private var viewportHeight: Double = 0.toDouble()
    private var viewPortWidth: Double = 0.toDouble()



//    private val cameraInitAngleX = 0.0
//    private val cameraInitAngleY = 0.0
//    private val cameraInitAngleZ = 0.0

    private var selectedNode: Node? = null
    private var sceneBounds = 1000

    private val root = Group()
    private val content = Group()//Group for content of the scene
    private var subScene: SubScene? = null
    private val axis = Axis(sceneBounds.toDouble())
    private var environmentNodes: EnvironmentNodes? = null
    private val flyCamera = FlyCamera(sceneBounds.toDouble())

    //Initialization
    fun initialize() {


        spnIteration.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000000000, 10, 2)
        spnSceneBounds.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000000000, 100, 100)

        //Camera properties
//        MSN_cameraX = MiniSpinner(" X:", -999999.0, 0.0, 999999.0, shiftIncrement)
        hbCameraStatusBar.children.add(msnCameraX.component)
        hbCameraStatusBar.children.add(msnCameraY.component)
        hbCameraStatusBar.children.add(msnCameraZ.component)
        hbCameraStatusBar.children.add(msnCameraAngleX.component)
        hbCameraStatusBar.children.add(msnCameraAngleY.component)
        hbCameraStatusBar.children.add(msnCameraAngleZ.component)
        hbCameraStatusBar.children.add(msnCameraFieldOfView.component)
        hbCameraStatusBar.children.add(msnCameraNearClip.component)
        hbCameraStatusBar.children.add(msnCameraFarClip.component)

        //Selected Node properties

        hbNodeStatusBar.children.add(tfNodeID)
        hbNodeStatusBar.children.add(msnNodeX.component)
        hbNodeStatusBar.children.add(msnNodeY.component)
        hbNodeStatusBar.children.add(msnNodeZ.component)
        hbNodeStatusBar.children.add(msnNodeAngleX.component)
        hbNodeStatusBar.children.add(msnNodeAngleY.component)
        hbNodeStatusBar.children.add(msnNodeAngleZ.component)


        content.children.clear()

        root.children.addAll(flyCamera.camera, axis.axis, axis.axisLabels, axis.grid, content)
        //SubScene
        subScene = SubScene(root, 500.0, 400.0, true, SceneAntialiasing.BALANCED)
        viewportPane.children.clear()
        setViewportSize()
        viewportPane.children.add(subScene)

        //        //Fill camera selector
        //        ComboBox_CameraSelector.getItems().clear();
        //        for(int i=0;i<camerasGroup.getChildren().size();i++){
        //            ComboBox_CameraSelector.getItems().add(camerasGroup.getChildren().get(i).getId());
        //        }
        //        ComboBox_CameraSelector.setValue(perspectiveCamera.getId());

        setSelectedCamera()

        resetCamera()

        //Register Events and listeners
        lookAndFeel()
        ShowCameraTransform()
    }

    private fun setViewportSize() {
        viewPortWidth = viewportPane.width
        viewportHeight = viewportPane.height
        subScene!!.width = viewPortWidth
        subScene!!.height = viewportHeight
        //        ta.appendText("Viewport sizes set to w: "+viewPortWidth+" h: "+viewportHeight+"\n");
    }

    @FXML
    private fun drawAxes() {
        if (cbShowAxes.isSelected)
            root.children.addAll(axis.axis)
        else
            root.children.removeAll(axis.axis)
    }

    @FXML
    private fun btnGoClick(event: ActionEvent) {
        content.children.clear()
        //SetViewportSize();
        val itr = spnIteration.value
        sceneBounds = spnSceneBounds.value
        ta.text = "Starting new iteration number of iterations: $itr  scene bound: $sceneBounds\n"

        environmentNodes = EnvironmentNodes(sceneBounds, itr)
        content.children.add(environmentNodes!!.content)

        ta.appendText("\n  Calculation finished.. ? Scene nodes: " + root.children.toTypedArray().size + "\n")

    }

    @FXML
    private fun btnDoClick(event: ActionEvent) {


    }

    @FXML
    private fun btnDoClick1(event: ActionEvent) {


    }

    @FXML
    private fun btnDoClick2(event: ActionEvent) {

    }

    @FXML
    private fun resetCamera() {
        flyCamera.reset()
    }

    @FXML
    private fun setSelectedCamera() {
        subScene!!.camera = flyCamera.camera
    }

    @FXML
    private fun btnSaveClick(event: ActionEvent) {

        val path = "D:\\1\\JavaFx\\"
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("YYYY_MM_DD__HH_mm_ss")
        val FILE_NAME = sdf.format(cal.time)

        val snapshot = viewportPane.snapshot(SnapshotParameters(), null)
        val name = path + FILE_NAME.replace("\\.[a-zA-Z]{3,4}", "")
        val file = File("$name.png")

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", file)
        } catch (exception: IOException) {
            // handle exception here
        }

        ta.appendText("Saved snapshot :  $name\n")
    }

    @FXML
    private fun btnPrintAllScene() {
        ta.appendText("\n  Scene nodes: " + "\n")
        for (n in getAllNodes(/*gridPaneMain*/viewportPane)) {
            printNodeProperties(n)
        }
    }

    private fun lookAndFeel() {
        //Mouse Scroll
        viewportPane.setOnScroll { e -> flyCamera.zoom(e.deltaY) }
        //Mouse pressed
        viewportPane.setOnMousePressed { e ->

            if (e.button == MouseButton.PRIMARY) {
                onPrssSceneX = e.sceneX
                onPressSceneY = e.sceneY

            } else if (e.button == MouseButton.MIDDLE) {
                mousePosX = e.sceneX
                mousePosY = e.sceneY
                //                ta.appendText("Middle button pressed X:"+mousePosX+"  Y: "+mousePosY+"\n");
            }
        }
        //Mouse dragged
        viewportPane.setOnMouseDragged { e ->
            if (e.button == MouseButton.PRIMARY) {

                flyCamera.moveViewport(e.sceneX - onPrssSceneX, e.sceneY - onPressSceneY)

            } else if (e.button == MouseButton.MIDDLE) {
                val dx = mousePosX - e.sceneX
                val dy = mousePosY - e.sceneY
                flyCamera.rotateViewport(dx, dy, viewportPane.width, viewportPane.height)

            }
        }
        //Mouse click
        viewportPane.setOnMouseClicked { e ->
            selectedNode = e.pickResult.intersectedNode
            printNodeProperties(e.pickResult.intersectedNode)

            //Show node properties statusbar if node selected
            if (selectedNode!!.id === viewportPane.id) {
                hbNodeStatusBar.isVisible = false
                printNodeProperties(flyCamera.camera)
            } else {
                hbNodeStatusBar.isVisible = true
                ShowSelectedNodeTransformBar()

                //RMB Selected object properties
                if (e.button == MouseButton.SECONDARY) {
                    //          Worked
                    //                    final Stage dialog = new Stage();
                    //                    dialog.initModality(Modality.APPLICATION_MODAL);
                    ////                    dialog.initOwner(this.get);
                    //                    VBox dialogVbox = new VBox(20);
                    //                    dialogVbox.getChildren().add(new Text("This is a Dialog"));
                    //                    Scene dialogScene = new Scene(dialogVbox, 300, 200);
                    //                    dialog.setScene(dialogScene);
                    //                    dialog.show();

                    val popup = Popup()
                    popup.x = e.x
                    popup.y = e.y
                    val deleteShape = Label("Delete Shape")
                    deleteShape.setOnMouseClicked {
                        root.children.remove(selectedNode)
                        popup.hide()
                    }
                    popup.content.addAll(HBox(deleteShape))
                    popup.show(viewportPane.scene.window)


                }

            }
        }

        //Hide node status when no nodes selected
        hbNodeStatusBar.managedProperty().bind(hbNodeStatusBar.visibleProperty())

        viewportPane.widthProperty().addListener { _ -> setViewportSize() }
        viewportPane.heightProperty().addListener {_ -> setViewportSize() }

        //Split panes auto divider

        mainSplitPane.widthProperty().addListener { _, _, _-> mainSplitPane.setDividerPositions(0.9) }

        val w1: Double
        val pos1: Double
//        val w2: Double
        w1 = 798.4
        pos1 = 0.785
        //System.out.println("Start ToolSplitPane.getWidth: " +ToolSplitPane.getWidth());
        toolSplitPane.widthProperty().addListener { _, _, _-> toolSplitPane.setDividerPositions(1 - w1 * (1 - pos1) / toolSplitPane.width) }

        //Slider camera Field of View
        sFieldOfView.widthProperty().addListener { _,_,new_val ->flyCamera.camera.fieldOfView=new_val.toDouble() }
        //Slider rotate around Z axis
        sRotateZ.valueProperty().addListener {_,_,new_val->flyCamera.cameraRotateZ.angle=new_val.toDouble()}

        //Set listener to all camera transforms
        val observableList = FXCollections.observableList(subScene!!.camera.transforms)
        observableList.forEach { node -> node.setOnTransformChanged { _ -> ShowCameraTransform() } }




        msnCameraX.textField.setOnKeyReleased { flyCamera.camera.translateX = msnCameraX.value }
        msnCameraY.textField.setOnKeyReleased {  flyCamera.camera.translateY = msnCameraY.value }
        msnCameraZ.textField.setOnKeyReleased {  flyCamera.camera.translateZ = msnCameraZ.value }
        msnCameraAngleX.textField.setOnKeyReleased {  flyCamera.cameraRotateX.angle = msnCameraAngleX.value }
        msnCameraAngleY.textField.setOnKeyReleased {  flyCamera.cameraRotateY.angle = msnCameraAngleY.value }
        msnCameraAngleZ.textField.setOnKeyReleased {
            flyCamera.cameraRotateZ.angle = msnCameraAngleZ.value
            sRotateZ.value = msnCameraAngleZ.value
        }

        msnCameraFieldOfView.textField.setOnKeyReleased {
            flyCamera.camera.fieldOfViewProperty().value = msnCameraFieldOfView.value
            sFieldOfView.value = msnCameraNearClip.value
        }
        msnCameraNearClip.textField.setOnKeyReleased { flyCamera.camera.nearClipProperty().value = msnCameraNearClip.value }
        msnCameraFarClip.textField.setOnKeyReleased { flyCamera.camera.farClipProperty().value = msnCameraNearClip.value }


        //Selected Node
        tfNodeID.textProperty().addListener { _: ObservableValue<out String>, _: String, newValue: String ->
            // expand the TextField
            // Do this in a Platform.runLater because of Textfield has no padding at first time and so on
            Platform.runLater {
                val text = Text(newValue)
                text.font = tfNodeID.font // Set the same font, so the size is the same
                val width = (text.layoutBounds.width // This big is the Text in the TextField

                        + tfNodeID.padding.left + tfNodeID.padding.right // Add the padding of the TextField

                        + 2.0) // Add some spacing
                tfNodeID.prefWidth = width // Set the width
                tfNodeID.positionCaret(tfNodeID.caretPosition) // If you remove this line, it flashes a little bit
            }
        }
        msnNodeX.textField.setOnKeyReleased { _ -> selectedNode!!.translateX = msnNodeX.value }
        msnNodeY.textField.setOnKeyReleased { _ -> selectedNode!!.translateY = msnNodeY.value }
        msnNodeZ.textField.setOnKeyReleased { _ -> selectedNode!!.translateZ = msnNodeZ.value }

        //        MSN_nodeAngleX.textField.setOnKeyReleased(eh->selectedNode.getTransforms().addAll(nodeRotateX.setAngle(MSN_nodeAngleX.getValue())));
        //        MSN_nodeAngleY.textField.setOnKeyReleased(eh->nodeRotateY.setAngle(MSN_nodeAngleY.getValue()));
        //        MSN_nodeAngleZ.textField.setOnKeyReleased(eh->nodeRotateZ.setAngle(MSN_nodeAngleZ.getValue()));

    }

    private fun ShowCameraTransform() {
        //int caret=0;
        msnCameraX.setText(flyCamera.camera.translateX)
        msnCameraY.setText(flyCamera.camera.translateY)
        msnCameraZ.setText(flyCamera.camera.translateZ)
        msnCameraAngleX.setText(flyCamera.cameraRotateX.angle)
        msnCameraAngleY.setText(flyCamera.cameraRotateY.angle)
        msnCameraAngleZ.setText(flyCamera.cameraRotateZ.angle)
        msnCameraFieldOfView.setText(flyCamera.camera.fieldOfViewProperty().value!!)
        msnCameraNearClip.setText(flyCamera.camera.nearClipProperty().value!!)
        msnCameraFarClip.setText(flyCamera.camera.farClipProperty().value!!)

    }

    private fun ShowSelectedNodeTransformBar() {
        tfNodeID.text = selectedNode!!.id
        msnNodeX.setText(selectedNode!!.translateX)
        msnNodeY.setText(selectedNode!!.translateY)
        msnNodeZ.setText(selectedNode!!.translateZ)

    }

    @FXML     private fun cameraAlongX() = flyCamera.alongX()
    @FXML     private fun cameraAlongY() = flyCamera.alongY()
    @FXML     private fun cameraAlongZ() = flyCamera.alongZ()
    @FXML     private fun cameraBackwardX() = flyCamera.backwardX()
    @FXML     private fun cameraBackwardY() = flyCamera.backwardY()
    @FXML     private fun cameraBackwardZ() = flyCamera.backwardZ()
    @FXML     private fun cameraAlong0() = flyCamera.alongX()


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


