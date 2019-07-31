package controller;

import customcomponent.MiniSpinner;
import model.Axis;
import model.EnvironmentNodes;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Bounds;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Popup;
import view.CameraTransform;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ControllerMainWindow {
    //    private final Set<KeyCode> pressedKeys = new HashSet<>();
    @FXML    private TreeView treeView;
    @FXML    private GridPane gridPaneMain;
    @FXML    private Pane viewportPane;
    @FXML    private SplitPane MainSplitPane;
    @FXML    private SplitPane ToolSplitPane;
    @FXML    private Spinner <Integer> SPN_Iteration;
    @FXML    private Spinner <Integer> SPN_SceneBounds;

    @FXML    private Button BtnGo;
    @FXML    private Button BtnSave;
    @FXML    private Button BtnResetCamera;
    @FXML    private Button BtnDo;
    @FXML    private Button BtnSavePath;
    @FXML    private Button BtnCameraAlongX;
    @FXML    private Button BtnCameraAlongY;
    @FXML    private Button BtnCameraAlongZ;
    @FXML    private Button BtnCameraAlong0;
    @FXML    private Button BtnPrintAllScene;
//    @FXML    private Button BtnLowPerspective;
    @FXML    private TextField SelectSavePath;
    @FXML    private TextArea ta;
    @FXML    private Slider S_FieldOfView;
    @FXML    private Slider S_RotateZ;
    @FXML    private CheckBox CB_ShowAxes;
    @FXML    private ComboBox ComboBox_CameraSelector;
    @FXML    private HBox HB_CameraStatusBar;
    @FXML    private HBox HB_nodeStatusBar;


    private MiniSpinner MSN_cameraX;
    private MiniSpinner MSN_cameraY;
    private MiniSpinner MSN_cameraZ;
    private MiniSpinner MSN_cameraAngleX;
    private MiniSpinner MSN_cameraAngleY;
    private MiniSpinner MSN_cameraAngleZ;
    private MiniSpinner MSN_cameraFieldOfView;
    private MiniSpinner MSN_cameraNearClip;
    private MiniSpinner MSN_cameraFarClip;

    private TextField   TF_nodeID;
    private MiniSpinner MSN_nodeX;
    private MiniSpinner MSN_nodeY;
    private MiniSpinner MSN_nodeZ;
    private MiniSpinner MSN_nodeAngleX;
    private MiniSpinner MSN_nodeAngleY;
    private MiniSpinner MSN_nodeAngleZ;

    private double orgSceneX, orgSceneY;
    private double orgTranslateX, orgTranslateY;
    private double mousePosX,mousePosY;
    private double mouseOldX=0;
    private double mouseOldY=0;
    private double nearFlip=0.1;
    private double farFlip=50000;

    private double viewportHeight,viewPortWidth;

    private double cameraInitAngleX =0, cameraInitAngleY =0, cameraInitAngleZ =0;

    private Node selectedNode;
    private int sceneBounds=1000;
    private double rotateIncrement =5, shiftIncrement=10;
    private Rotate    cameraRotateX = new Rotate(0, Rotate.X_AXIS);
    private Rotate    cameraRotateY = new Rotate(0, Rotate.Y_AXIS);
    private Rotate    cameraRotateZ = new Rotate(0, Rotate.Z_AXIS);

//    private Translate cameraTranslate=new Translate(cameraInitX, cameraInitY, cameraInitZ);

    private Rotate    nodeRotateX = new Rotate(0, Rotate.X_AXIS);
    private Rotate    nodeRotateY = new Rotate(0, Rotate.Y_AXIS);
    private Rotate    nodeRotateZ = new Rotate(0, Rotate.Z_AXIS);
    private Translate nodeTranslate=new Translate(0, 0, 0);




    private PerspectiveCamera perspectiveCamera =new PerspectiveCamera(true);
//    private ParallelCamera parallelCamera=new ParallelCamera();
//    private Group camerasGroup;
    private Group root= new Group();
    private Group content= new Group();//Group for content of the scene
    private SubScene subScene;
    private Axis axis = new Axis(sceneBounds);
    private EnvironmentNodes environmentNodes ;
    private CameraTransform cameraTransform =new CameraTransform();;

    //Initialization
    public void initialize() {
        SPN_Iteration.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000000000,10,2)      );
        SPN_SceneBounds.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000000000,100,100) );

        //Camera properties
        MSN_cameraX=new MiniSpinner(" X:",-999999,0,999999,shiftIncrement) ;
        HB_CameraStatusBar.getChildren().add(MSN_cameraX.getComponent());
        MSN_cameraY=new MiniSpinner(" Y:",-999999,0,999999,shiftIncrement) ;
        HB_CameraStatusBar.getChildren().add(MSN_cameraY.getComponent());
        MSN_cameraZ=new MiniSpinner(" Z:",-999999,0,999999,shiftIncrement) ;
        HB_CameraStatusBar.getChildren().add(MSN_cameraZ.getComponent());
        MSN_cameraAngleX=new MiniSpinner(" ∠X:",-999999,0,999999, rotateIncrement) ;
        HB_CameraStatusBar.getChildren().add(MSN_cameraAngleX.getComponent());
        MSN_cameraAngleY=new MiniSpinner(" ∠Y:",-999999,0,999999, rotateIncrement) ;
        HB_CameraStatusBar.getChildren().add(MSN_cameraAngleY.getComponent());
        MSN_cameraAngleZ=new MiniSpinner(" ∠Z:",-999999,0,999999, rotateIncrement) ;
        HB_CameraStatusBar.getChildren().add(MSN_cameraAngleZ.getComponent());
        MSN_cameraFieldOfView=new MiniSpinner(" Field of View:",-999999,0,999999,0.1) ;
        HB_CameraStatusBar.getChildren().add(MSN_cameraFieldOfView.getComponent());
        MSN_cameraNearClip=new MiniSpinner(" Near Clip:",-999999,0,999999,0.1) ;
        HB_CameraStatusBar.getChildren().add(MSN_cameraNearClip.getComponent());
        MSN_cameraFarClip=new MiniSpinner(" Far Clip:",-999999,0,999999,0.1) ;
        HB_CameraStatusBar.getChildren().add(MSN_cameraFarClip.getComponent());

        //Selected Node properties
        TF_nodeID=new TextField("?");
        HB_nodeStatusBar.getChildren().add(TF_nodeID);
        MSN_nodeX=new MiniSpinner(" X:",-999999,0,999999,shiftIncrement) ;
        HB_nodeStatusBar.getChildren().add(MSN_nodeX.getComponent());
        MSN_nodeY=new MiniSpinner(" Y:",-999999,0,999999,shiftIncrement) ;
        HB_nodeStatusBar.getChildren().add(MSN_nodeY.getComponent());
        MSN_nodeZ=new MiniSpinner(" Z:",-999999,0,999999,shiftIncrement) ;
        HB_nodeStatusBar.getChildren().add(MSN_nodeZ.getComponent());
        MSN_nodeAngleX=new MiniSpinner(" ∠X:",-999999,0,999999, rotateIncrement) ;
        HB_nodeStatusBar.getChildren().add(MSN_nodeAngleX.getComponent());
        MSN_nodeAngleY=new MiniSpinner(" ∠Y:",-999999,0,999999, rotateIncrement) ;
        HB_nodeStatusBar.getChildren().add(MSN_nodeAngleY.getComponent());
        MSN_nodeAngleZ=new MiniSpinner(" ∠Z:",-999999,0,999999, rotateIncrement) ;
        HB_nodeStatusBar.getChildren().add(MSN_nodeAngleZ.getComponent());



        perspectiveCamera.setId("Perspective Camera");
//        parallelCamera.setId("Parallel camera");
//        camerasGroup=new Group(perspectiveCamera,parallelCamera);



        content.getChildren().clear();

        root.getChildren().addAll(perspectiveCamera,axis.getAxis(), axis.getAxisLabels(),axis.getGrid(),content);
        //SubScene
        subScene= new SubScene(root, 500, 400, true,SceneAntialiasing.BALANCED);
        viewportPane.getChildren().clear();
        SetViewportSize();
        viewportPane.getChildren().add(subScene);



        //        //Fill camera selector
//        ComboBox_CameraSelector.getItems().clear();
//        for(int i=0;i<camerasGroup.getChildren().size();i++){
//            ComboBox_CameraSelector.getItems().add(camerasGroup.getChildren().get(i).getId());
//        }
//        ComboBox_CameraSelector.setValue(perspectiveCamera.getId());

        SetSelectedCamera();

//        ResetCamera();

        //Register Events and listeners
        LookAndFeel();
        ShowCameraTransform();
    }

    private void SetViewportSize(){
        viewPortWidth =  viewportPane.getWidth();
        viewportHeight = viewportPane.getHeight();
        subScene.setWidth(viewPortWidth);
        subScene.setHeight(viewportHeight);
//        ta.appendText("Viewport sizes set to w: "+viewPortWidth+" h: "+viewportHeight+"\n");
    }

    @FXML
    private void DrawAxes (){
        if(CB_ShowAxes.isSelected())
            root.getChildren().addAll(axis.getAxis());
        else
            root.getChildren().removeAll(axis.getAxis());
    }
    @FXML
    private void BtnGoClick(ActionEvent event) {
        content.getChildren().clear();
        //SetViewportSize();
        int itr=SPN_Iteration.getValue();
        sceneBounds=SPN_SceneBounds.getValue();
        ta.setText("Starting new iteration number of iterations: "+itr+"  scene bound: "+sceneBounds+"\n");

        environmentNodes= new EnvironmentNodes(sceneBounds,itr);
        content.getChildren().add(environmentNodes.getContent());

        ta.appendText("\n  Calculation finished.. ? Scene nodes: " /*+root.getChildren().toArray().toString()*/+"\n");

    }
    @FXML
    private void BtnDoClick(ActionEvent event){
        selectedNode.setRotate(selectedNode.getRotate()+10);
}

    @FXML
    private void ResetCamera(){
        cameraTransform.reset(perspectiveCamera);
    }
    @FXML
    private void SetSelectedCamera(){
        subScene.setCamera(perspectiveCamera);
    }




    @FXML
    private void BtnSaveClick(ActionEvent event) {

        String path="D:\\1\\JavaFx\\";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY_MM_DD__HH_mm_ss");
        String FILE_NAME=sdf.format(cal.getTime());

        final WritableImage SNAPSHOT = viewportPane.snapshot(new SnapshotParameters(), null);
        final String        NAME     = path+FILE_NAME.replace("\\.[a-zA-Z]{3,4}", "");
        final File          FILE     = new File(NAME + ".png");

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(SNAPSHOT, null), "png", FILE);
        } catch (IOException exception) {
            // handle exception here
        }
        ta.appendText("Saved snapshot :  "+NAME+"\n");
    }

    @FXML private void BtnPrintAllScene(){
        ta.appendText("\n  Scene nodes: "+"\n");
        for (Node n:getAllNodes(/*gridPaneMain*/viewportPane)){
            printNodeProperties(n);
        }
    }
    private void LookAndFeel(){
        //Mouse Scroll
        viewportPane.setOnScroll(e-> cameraTransform.zoom(perspectiveCamera, e.getDeltaY())  );
        //Mouse pressed
        viewportPane.setOnMousePressed(e-> {

            if (e.getButton() == MouseButton.PRIMARY){
                orgSceneX = e.getSceneX();
                orgSceneY = e.getSceneY();
                orgTranslateX = cameraTranslate.getX();
                orgTranslateY = cameraTranslate.getY();
            }
            else if (e.getButton() == MouseButton.MIDDLE){
                mousePosX = e.getSceneX();
                mousePosY = e.getSceneY();
//                ta.appendText("Middle button pressed X:"+mousePosX+"  Y: "+mousePosY+"\n");
            }
        });
        //Mouse dragged
        viewportPane.setOnMouseDragged(e-> {
            if (e.getButton() == MouseButton.PRIMARY){
                double offsetX = e.getSceneX() - orgSceneX;
                double offsetY = e.getSceneY() - orgSceneY;
                double newTranslateX = orgTranslateX - offsetX;
                double newTranslateY = orgTranslateY - offsetY;

                cameraTranslate.setX(newTranslateX);
                cameraTranslate.setY(newTranslateY);

            }
            else if (e.getButton() == MouseButton.MIDDLE){
                double dx = (mousePosX - e.getSceneX()) ;
                double dy = (mousePosY - e.getSceneY());

                    //For Camera
                    cameraRotateX.setAngle(cameraRotateX.getAngle() +
                            (dy / viewportPane.getHeight() * 360) * (Math.PI / 180));
                    cameraRotateY.setAngle(cameraRotateY.getAngle() +
                            (dx / viewportPane.getWidth() * -360) * (Math.PI / 180));
//                    ta.appendText("Rotation pivot x: "+cameraRotateX.getPivotX()+"  y: "+cameraRotateX.getPivotY()+" -  x: "+cameraRotateY.getPivotX()+"  y: "+cameraRotateY.getPivotY()+"\n");
//                    ta.appendText("Camera angle X: "+cameraRotateX.getAngle()+"  Y: "+cameraRotateY.getAngle()+"  Z: "+cameraRotateZ.getAngle()+"\n");

//                }
//                //For Nodes
//                mouseOldX = mousePosX;
//                mouseOldY = mousePosY;
//                ta.appendText("Middle button Dragged X: "+cameraRotateX.getAngle()+"  Y: "+cameraRotateY.getAngle()+"  dx: "+dx+"  dy: "+dy+"\n");
            }
        });
        //Mouse click
        viewportPane.setOnMouseClicked(e->{
            selectedNode=e.getPickResult().getIntersectedNode();
            printNodeProperties(e.getPickResult().getIntersectedNode());

            //Show node properties statusbar if node selected
            if(selectedNode.getId()==viewportPane.getId()){
                HB_nodeStatusBar.setVisible(false);
                printNodeProperties(perspectiveCamera);
            }
            else {
                HB_nodeStatusBar.setVisible(true);
                ShowSelectedNodeTransformBar();

                //RMB Selected object properties
                if(e.getButton()==MouseButton.SECONDARY){
//          Worked
//                    final Stage dialog = new Stage();
//                    dialog.initModality(Modality.APPLICATION_MODAL);
////                    dialog.initOwner(this.get);
//                    VBox dialogVbox = new VBox(20);
//                    dialogVbox.getChildren().add(new Text("This is a Dialog"));
//                    Scene dialogScene = new Scene(dialogVbox, 300, 200);
//                    dialog.setScene(dialogScene);
//                    dialog.show();

                    final Popup popup = new Popup();
                    popup.setX(e.getX());
                    popup.setY(e.getY());
                    Label deleteShape =new Label("Delete Shape");
                    deleteShape.setOnMouseClicked(eh->{
                        root.getChildren().remove(selectedNode);
                        popup.hide();
                    });
                    popup.getContent().addAll(new HBox(deleteShape));
                    popup.show(viewportPane.getScene().getWindow());


                }

            }
        });

        //Hide node status when no nodes selected
        HB_nodeStatusBar.managedProperty().bind(HB_nodeStatusBar.visibleProperty());

        viewportPane.widthProperty().addListener(cl->SetViewportSize() );
        viewportPane.heightProperty().addListener(cl->SetViewportSize() );

        //Split panes auto divider
        ChangeListener<Number>  MainSplitPaneSizeListener = (observable, oldValue, newValue) -> {
                MainSplitPane.setDividerPositions(0.9);//System.out.println("Height: " + ToolSplitPane.getHeight() + " Width: " + ToolSplitPane.getWidth());
            };
        MainSplitPane.widthProperty().addListener( MainSplitPaneSizeListener);
        double w1,pos1,w2;
        w1=798.4;
        pos1=0.785;
        //System.out.println("Start ToolSplitPane.getWidth: " +ToolSplitPane.getWidth());
        ChangeListener<Number> ToolSplitPaneSizeListener = (observable, oldValue, newValue) ->
            ToolSplitPane.setDividerPositions(1-w1*(1-pos1)/ ToolSplitPane.getWidth());

        ToolSplitPane.widthProperty().addListener(ToolSplitPaneSizeListener);

        //Slider camera Field of View
        S_FieldOfView.valueProperty().addListener((ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) ->
                    perspectiveCamera.setFieldOfView(new_val.doubleValue())        );

        //Slider rotate around Z axis
        S_RotateZ.valueProperty().addListener((ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) ->
                cameraRotateZ.setAngle(new_val.doubleValue())        );

        //Set listener to all camera transforms
        ObservableList<Transform> observableList = FXCollections.observableList(subScene.getCamera().getTransforms());
        observableList.forEach(node->node.setOnTransformChanged(transformChangedEvent -> ShowCameraTransform()));




        MSN_cameraX.textField.setOnKeyReleased(eh->perspectiveCamera.setTranslateX(MSN_cameraX.getValue()));
        MSN_cameraY.textField.setOnKeyReleased(eh->perspectiveCamera.setTranslateY(MSN_cameraY.getValue()));
        MSN_cameraZ.textField.setOnKeyReleased(eh->perspectiveCamera.setTranslateZ(MSN_cameraZ.getValue()));
        MSN_cameraAngleX.textField.setOnKeyReleased(eh->cameraRotateX.setAngle(MSN_cameraAngleX.getValue()));
        MSN_cameraAngleY.textField.setOnKeyReleased(eh->cameraRotateY.setAngle(MSN_cameraAngleY.getValue()));
        MSN_cameraAngleZ.textField.setOnKeyReleased(eh->{
            cameraRotateZ.setAngle(MSN_cameraAngleZ.getValue());
            S_RotateZ.setValue(MSN_cameraAngleZ.getValue());
        });

        MSN_cameraFieldOfView.textField.setOnKeyReleased(eh->{
            perspectiveCamera.fieldOfViewProperty().setValue( MSN_cameraFieldOfView.getValue());
            S_FieldOfView.setValue(MSN_cameraNearClip.getValue());
        });
        MSN_cameraNearClip.textField.setOnKeyReleased(eh->perspectiveCamera.nearClipProperty().setValue(MSN_cameraNearClip.getValue()));
        MSN_cameraFarClip.textField.setOnKeyReleased(eh->perspectiveCamera.farClipProperty().setValue(MSN_cameraNearClip.getValue()));


        //Selected Node
        TF_nodeID.textProperty().addListener(
            (ObservableValue<? extends String> observable, String oldValue, String newValue) ->
            {
                    // expand the TextField
                    // Do this in a Platform.runLater because of Textfield has no padding at first time and so on
                    Platform.runLater(() -> {
                        Text text = new Text(newValue);
                        text.setFont(TF_nodeID.getFont()); // Set the same font, so the size is the same
                        double width = text.getLayoutBounds().getWidth() // This big is the Text in the TextField
                                + TF_nodeID.getPadding().getLeft() + TF_nodeID.getPadding().getRight() // Add the padding of the TextField
                                + 2d; // Add some spacing
                        TF_nodeID.setPrefWidth(width); // Set the width
                        TF_nodeID.positionCaret(TF_nodeID.getCaretPosition()); // If you remove this line, it flashes a little bit
                    });
                }
            );
        MSN_nodeX.textField.setOnKeyReleased(eh->selectedNode.setTranslateX(MSN_nodeX.getValue()));
        MSN_nodeY.textField.setOnKeyReleased(eh->selectedNode.setTranslateY(MSN_nodeY.getValue()));
        MSN_nodeZ.textField.setOnKeyReleased(eh->selectedNode.setTranslateZ(MSN_nodeZ.getValue()));

//        MSN_nodeAngleX.textField.setOnKeyReleased(eh->selectedNode.getTransforms().addAll(nodeRotateX.setAngle(MSN_nodeAngleX.getValue())));
//        MSN_nodeAngleY.textField.setOnKeyReleased(eh->nodeRotateY.setAngle(MSN_nodeAngleY.getValue()));
//        MSN_nodeAngleZ.textField.setOnKeyReleased(eh->nodeRotateZ.setAngle(MSN_nodeAngleZ.getValue()));

    }

    private void ShowCameraTransform(){
        //int caret=0;
        MSN_cameraX.setText(perspectiveCamera.getTranslateX());
        MSN_cameraY.setText(perspectiveCamera.getTranslateY());
        MSN_cameraZ.setText(perspectiveCamera.getTranslateZ());
        MSN_cameraAngleX.setText( cameraRotateX.getAngle());
        MSN_cameraAngleY.setText( cameraRotateY.getAngle());
        MSN_cameraAngleZ.setText( cameraRotateZ.getAngle());
        MSN_cameraFieldOfView.setText(perspectiveCamera.fieldOfViewProperty().getValue());
        MSN_cameraNearClip.setText(perspectiveCamera.nearClipProperty().getValue());
        MSN_cameraFarClip.setText(perspectiveCamera.farClipProperty().getValue());

//        ta.appendText("\nPivot ∠X X: "+cameraRotateX.getPivotX()+"  Y: "+cameraRotateX.getPivotY()+"  Z: "+cameraRotateX.getPivotZ()+"\n");
//        ta.appendText("Pivot ∠Y X: "+cameraRotateY.getPivotX()+"  Y: "+cameraRotateY.getPivotY()+"  Z: "+cameraRotateY.getPivotZ()+"\n");
//        ta.appendText("Pivot ∠Z X: "+cameraRotateZ.getPivotX()+"  Y: "+cameraRotateZ.getPivotY()+"  Z: "+cameraRotateZ.getPivotZ()+"\n");

    }
    private void ShowSelectedNodeTransformBar(){
        TF_nodeID.setText(selectedNode.getId());
        MSN_nodeX.setText(selectedNode.getTranslateX());
        MSN_nodeY.setText(selectedNode.getTranslateY());
        MSN_nodeZ.setText(selectedNode.getTranslateZ());
//        MSN_nodeAngleX.setText( selectedNode.getTransforms().gcameraRotateX.getAngle());
//        MSN_nodeAngleY.setText( cameraRotateY.getAngle());
//        MSN_nodeAngleZ.setText( cameraRotateZ.getAngle());
    }
    @FXML     private void CameraAlongX(){
        cameraTransform.alongX(perspectiveCamera,sceneBounds);
    }
    @FXML     private void CameraAlongY(){
        cameraTransform.alongY(perspectiveCamera,sceneBounds);
    }
    @FXML     private void CameraAlongZ(){
        cameraTransform.alongZ(perspectiveCamera,sceneBounds);
    }
    @FXML     private void CameraAlong0(){

    }


private void printNodeProperties(Node node){

    Bounds boundsInScene = node.localToScene(node.getBoundsInLocal());

    ta.appendText(
//            "\n"+
                    node.getTypeSelector()+
                    " "+node.getStyle()+
                    "  x: "/*+ nodeTranslate.getX()+*/+((int)boundsInScene.getMinX())+
                    "  y: "/*+ nodeTranslate.getY()+*/+((int)boundsInScene.getMinY())+
                    "  z: "/*+ nodeTranslate.getZ()+*/+((int)boundsInScene.getMinZ())+
                    "  "+node.getId()+
                    "  to sting: "+node.toString()+
                    "  Bound in local: "+node.getBoundsInLocal().toString()+
                    "  Layout bounds: "+node.getLayoutBounds().toString()+
//                    "  Layout bounds: "+selectedNode..getLayoutBounds().toString()+
//                    "  transforms: "+selectedNode.getTransforms().toArray().toString()+
                    "  Compute area in screen: "+node.computeAreaInScreen()+
                    "\n");
    for (int i=0;i<node.getTransforms().toArray().length;i++)ta.appendText(node.getTransforms().toArray()[i].toString()+"\n");
}


private static ArrayList<Node> getAllNodes(Parent root) {
    ArrayList<Node> nodes = new ArrayList<>();
    addAllDescendants(root, nodes);
    return nodes;
}

    private static void addAllDescendants(Parent parent, ArrayList<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent)
                addAllDescendants((Parent)node, nodes);
        }
    }


}



//        PointLight light = new PointLight(Color.WHITE);
//        light.setTranslateX(50);
//        light.setTranslateY(-300);
//        light.setTranslateZ(-400);
//        PointLight light1 = new PointLight(Color.YELLOW);
//        light1.setTranslateX(1000);
//        light1.setTranslateY(1000);
//        light1.setTranslateZ(1000);
//        PointLight light2 = new PointLight(Color.color(0.6, 0.3, 0.4));
//        light2.setTranslateX(400);
//        light2.setTranslateY(0);
//        light2.setTranslateZ(-400);
//AmbientLight ambientLight = new AmbientLight(Color.color(0.2, 0.2, 0.2));
//        AmbientLight ambientLight = new AmbientLight(Color.WHITE);
//        root.getChildren().addAll(ambientLight/*, light,light1, light2*/);



//        FileChooser fileChooser = new FileChooser();
//
//        //Set extension filter
//        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png files (*.png)", "*.png"));
//
//        //Prompt user to select a file
//        File file = fileChooser.showSaveDialog(null);
//
//        if(file != null){
//            try {
//                //Pad the capture area
//                //WritableImage writableImage = new WritableImage((int)viewportPane.getWidth(), (int)viewportPane.getHeight());
//                SnapshotResult(null, viewportPane);
//                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
//                //Write the snapshot to the chosen file
//                ImageIO.write(renderedImage, "png", file);
//            } catch (IOException ex) { ex.printStackTrace(); }
//        }


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