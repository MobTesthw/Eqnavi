package main;

import customcomponent.MiniSpinner;
import randompath.RndFX;
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
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Popup;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ControllerMainWindow {
    //    private final Set<KeyCode> pressedKeys = new HashSet<>();
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

//    private Popup popupViewPort;


    private double orgSceneX, orgSceneY;
    private double orgTranslateX, orgTranslateY;
    private double mousePosX,mousePosY;
    private double mouseOldX=0;
    private double mouseOldY=0;
    private double nearFlip=0.1;
    private double farFlip=50000;
    private double cameraZoomFactor = 5;
    private double viewportHeight,viewPortWidth;
    private double cameraInitX =0, cameraInitY =0, cameraInitZ =-500;
    private double cameraInitAngleX =0, cameraInitAngleY =0, cameraInitAngleZ =0;
    private double gridDivision=20;

    private Node selectedNode;

    private int sceneBounds=1000;
    //Axis
    private double /*boundX=500,boundY=500,boundZ=500,*/ labelsOnAxes =10, axesLabelOffset =10;
    private double axesLength = sceneBounds*2, axesRadius= axesLabelOffset /2;

    private double rotateIncrement =5, shiftIncrement=10;
    private Rotate    cameraRotateX = new Rotate(0, Rotate.X_AXIS);
    private Rotate    cameraRotateY = new Rotate(0, Rotate.Y_AXIS);
    private Rotate    cameraRotateZ = new Rotate(0, Rotate.Z_AXIS);
    private Translate cameraTranslate=new Translate(cameraInitX, cameraInitY, cameraInitZ);
//    private final double TURN_FACTOR = 0.5;

    private Rotate    nodeRotateX = new Rotate(0, Rotate.X_AXIS);
    private Rotate    nodeRotateY = new Rotate(0, Rotate.Y_AXIS);
    private Rotate    nodeRotateZ = new Rotate(0, Rotate.Z_AXIS);
    private Translate nodeTranslate=new Translate(0, 0, 0);

    private Group axes=new Group();
    private Group axesLabels=new Group();
    private Group aim=new Group();
    private Group grid=new Group();
    private PerspectiveCamera perspectiveCamera =new PerspectiveCamera(true);
//    private ParallelCamera parallelCamera=new ParallelCamera();
//    private Group camerasGroup;
    private Group root= new Group();
    private Group content= new Group();//Group for content of the scene
    private SubScene subScene;

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
        //camerasGroup=new Group(perspectiveCamera,parallelCamera);

        //Axes
        axes=InitAxes();
        content.getChildren().clear();
        root.getChildren().addAll(perspectiveCamera,axes,content);
        //SubScene
        subScene= new SubScene(root, 500, 400, true,SceneAntialiasing.BALANCED);
        viewportPane.getChildren().clear();
        SetViewportSize();
        viewportPane.getChildren().add(subScene);
        //Fill camera selector

//        ComboBox_CameraSelector.getItems().clear();
//        for(int i=0;i<camerasGroup.getChildren().size();i++){
//            ComboBox_CameraSelector.getItems().add(camerasGroup.getChildren().get(i).getId());
//        }
//        ComboBox_CameraSelector.setValue(perspectiveCamera.getId());

        SetSelectedCamera();
        ResetCamera();

        //Register Events and listeners
        LookAndFeel();
        ShowCameraTransform();
    }

    private void SetViewportSize(){
        viewPortWidth=  viewportPane.getWidth();
        viewportHeight= viewportPane.getHeight();
        subScene.setWidth(viewPortWidth);
        subScene.setHeight(viewportHeight);
//        ta.appendText("Viewport sizes set to w: "+viewPortWidth+" h: "+viewportHeight+"\n");
    }

    @FXML
    private void DrawAxes (){
        if(CB_ShowAxes.isSelected()){
            root.getChildren().addAll(axes);
        }
        else
            root.getChildren().removeAll(axes);
    }
    @FXML
    private void BtnGoClick(ActionEvent event) {
        content.getChildren().clear();
        //SetViewportSize();
        int itr=SPN_Iteration.getValue();
        sceneBounds=SPN_SceneBounds.getValue();
        ta.setText("Starting new iteration number of iterations: "+itr+"  scene bound: "+sceneBounds+"\n");

//        Box slab = new Box(46,230,5);
//        slab.setTranslateX(400);
//        slab.setTranslateX(400);
//        slab.setTranslateX(400);
//        PhongMaterial slabMat = new PhongMaterial();
//        slabMat.setDiffuseMap(new Image("file:D:\\Dist\\img\\Gravel_Diffuse.png"));
//        slabMat.setBumpMap(new Image("file:D:\\Dist\\img\\Gravel_Normal.png"));
//        slabMat.setSpecularMap(new Image("file:D:\\Dist\\img\\Gravel_Specular.png"));
//        slab.setMaterial(slabMat);
//        content.getChildren().add(slab);

        Box slabX = new Box(200,200,1);
        Box slabZ = new Box(200,200,1);
        slabX.setTranslateZ(100);
        slabZ.setTranslateX(100);
//        slabX.setTranslateY(-100);
//        slabZ.setTranslateY(-100);
        slabX.getTransforms().addAll(new Rotate(-45,Rotate.X_AXIS));
        slabZ.getTransforms().addAll(new Rotate(-45,Rotate.Z_AXIS));
        slabZ.getTransforms().addAll(new Rotate(-90,Rotate.X_AXIS));
        content.getChildren().addAll(slabX,slabZ);


        Text text = new Text("RHUB group");
        text.setX(10);
        text.setY(50);
        text.setFont(new Font(20));
        text.getTransforms().add(new Rotate(30, 50, 30));
        content.getChildren().add(text);

        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(
                0.0, 0.0,
                20.0, 10.0,
                10.0, 20.0 );
        polygon.setTranslateZ(100);
        content.getChildren().add(polygon);

//        Sphere sphere = new Sphere();
//        sphere.setRadius(50);
//        sphere.setTranslateX(200);
//        sphere.setTranslateY(200);
//        sphere.setTranslateZ(200);
//        PhongMaterial mat = new PhongMaterial();
//        Image diffuseMap = new Image("file:D:\\Dist\\img\\diffuseMap1.png");
////        Image diffuseMap = new Image("file:D:\\Dist\\img\\2_no_clouds_4k.jpg");
////        2_no_clouds_4k.jpg
////        elev_bump_4k.jpg
////        Image normalMap = new Image("file:D:\\Dist\\img\\elev_bump_4k.jpg");
//        Image normalMap = new Image("file:D:\\Dist\\img\\normalMap1.png");
//        mat.setDiffuseMap(diffuseMap);
//        mat.setBumpMap(normalMap);
//        mat.setSpecularColor(Color.CORNFLOWERBLUE);
////        m`at.sp
//        sphere.setMaterial(mat);
//        content.getChildren().add(sphere);

        Cylinder cylinder=new Cylinder(2,4000) ;
        cylinder.getTransforms().addAll(new Translate(707,707,1000),new Rotate(-54,45,-54));
        content.getChildren().add(cylinder);


//        content.getChildren().add(RndFX.getCircles(3));
        content.getChildren().add(RndFX.getBoxes(itr,50,50,50,sceneBounds,sceneBounds,sceneBounds));
//        content.getChildren().add(RndFX.getSpheres(30,50,boundX,boundY,boundZ));

        //ResetCamera();

        ta.appendText("\n  Calculation finished.. Scene nodes: "+root.getChildren().toArray().toString()+"\n");

    }
    @FXML
    private void BtnDoClick(ActionEvent event){
//        perspectiveCamera.setRotate(perspectiveCamera.getRotate()+10);
        selectedNode.setRotate(selectedNode.getRotate()+10);

}

    @FXML
    private void ResetCamera(){
        //subScene.setCamera(perspectiveCamera);
//        Camera camera=subScene.getCamera();
//        PerspectiveCamera camera =perspectiveCamera;
        perspectiveCamera.getTransforms().clear();

        cameraRotateX.setAngle(cameraInitAngleX);
        cameraRotateY.setAngle(cameraInitAngleY);
        cameraRotateZ.setAngle(cameraInitAngleZ);

        cameraTranslate.setX(cameraInitX);
        cameraTranslate.setY(cameraInitY);
        cameraTranslate.setZ(cameraInitZ);

        perspectiveCamera.getTransforms().addAll (cameraTranslate,cameraRotateX, cameraRotateY,cameraRotateZ);
        perspectiveCamera.setFarClip(farFlip);
        perspectiveCamera.setNearClip(nearFlip);
    }
    @FXML
    private void SetSelectedCamera(){
//        for(int i=0;i<ComboBox_CameraSelector.getItems().size();i++)
//            if(camerasGroup.getChildren().get(i).getId()==ComboBox_CameraSelector.getValue().toString()){
////              subScene.setCamera(camerasGroup.getChildren().get(i));
//                ta.appendText("Camera: "+ camerasGroup.getChildren().get(i).getId()+" is selected..\n");
//            }
//        subScene.setCamera(parallelCamera);
        subScene.setCamera(perspectiveCamera);
    }
    private Group InitAxes(){
        Cylinder axeX = new Cylinder(axesRadius,axesLength);
        Cylinder axeY = new Cylinder(axesRadius,axesLength);
        Cylinder axeZ = new Cylinder(axesRadius,axesLength);

//        Line lineX = new Line(-);
        double gridIterator=sceneBounds/gridDivision;
        for (int i=0;i<gridDivision;i++)
            grid.getChildren().addAll(
                    new Line(i*gridIterator,-sceneBounds,i*gridIterator,sceneBounds),
                    new Line(-sceneBounds,i*gridIterator,sceneBounds,i*gridIterator)
            );

        final PhongMaterial materialX = new PhongMaterial();
        final PhongMaterial materialY = new PhongMaterial();
        final PhongMaterial materialZ = new PhongMaterial();

        axeX.setTranslateX(axeX.getHeight()/2);
        axeX.getTransforms().addAll(new Rotate(90,Rotate.Z_AXIS));
        materialX.setSpecularColor(Color.rgb(255,255,0));
        materialX.setDiffuseColor(Color.rgb(255,255,125));
        axeX.setId("X axis");
        axeX.setMaterial(materialX);

        axeY.setTranslateY(axeY.getHeight()/2);
        materialY.setSpecularColor(Color.rgb(0,0,255));
        materialY.setDiffuseColor(Color.rgb(125,125,255));
        axeY.setId("Y axis");
        axeY.setMaterial(materialY);

        axeZ.setTranslateZ(axeZ.getHeight()/2);
        axeZ.getTransforms().addAll(new Rotate(90,Rotate.X_AXIS));
        materialZ.setSpecularColor(Color.rgb(255,0,0));
        materialZ.setDiffuseColor(Color.rgb(255,125,125));
        axeZ.setId("Z axis");
        axeZ.setMaterial(materialZ);

//        //Aim
//        Line middleHorizontalLine = new Line(viewportPane.getWidth()/2,0,viewportPane.getWidth()/2,viewportPane.getHeight());
//        Line middleVerticalLine = new Line(0,viewportPane.getHeight()/2,viewportPane.getWidth(),viewportPane.getHeight()/2);
//        aim.getChildren().addAll(middleHorizontalLine,middleVerticalLine);
//        viewportPane.getChildren().addAll(aim);
        //Grid

        //Labels Tick Marks
        for(int i=0;i<axesLength;i+=axesLength/ labelsOnAxes) {
            axesLabels.getChildren().add(new Text(i, axesLabelOffset * 4, "x " + i + " : 0"));
            axesLabels.getChildren().add(new Text(axesLabelOffset * 4, i, "y " + i + " : 0"));
            Text text=                   new Text(axesLabelOffset * 4, axesLabelOffset * 4, "Z 0 :" + " 0 " + i);
            text.setTranslateZ(i);
            axesLabels.getChildren().add(text);
        }

        return new Group(axeX,axeY,axeZ,axesLabels,grid);
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
        viewportPane.setOnScroll(e-> {

//            taa.appendText(" Camera X: "+sin(cameraRotateX.getAngle())+"  Y: "+cameraRotateY.getAngle()+"  Z: "+cameraRotateZ.getAngle()+"\n");
//            double dX=e.getDeltaY()*cameraZoomFactor;
//            double dY=e.getDeltaY()*cameraZoomFactor;
//            double dZ=e.getDeltaY()*cameraZoomFactor;
//
//            subScene.getCamera().setTranslateZ(subScene.getCamera().getTranslateX()-dX*sin(cameraRotateX.getAngle()));
//            subScene.getCamera().setTranslateZ(subScene.getCamera().getTranslateY()-dY*sin(cameraRotateY.getAngle()));
//            subScene.getCamera().setTranslateZ(subScene.getCamera().getTranslateZ()-dZ*sin(cameraRotateZ.getAngle()));
//            ta.appendText("Scroll dY: "+e.getDeltaY()+" Camera angle X: "+cameraRotateX.getAngle()+"  Y: "+cameraRotateY.getAngle()+"  Z: "+cameraRotateZ.getAngle()+"\n");
//            ta.appendText(" Camera  sin angle X: "+sin(cameraRotateX.getAngle())+"  Y: "+cameraRotateY.getAngle()+"  Z: "+cameraRotateZ.getAngle()+"\n");

            //Math calculates in radians!!!!!!!
            double dr,dx,dy,dz;
            double yaw,roll,pitch;
            dr=e.getDeltaY()*cameraZoomFactor;
            pitch=cameraRotateX.getAngle();
            yaw=cameraRotateY.getAngle();
            //Converting to radians
            pitch=pitch*Math.PI/180;
            yaw=yaw*Math.PI/180;


//            dz=dr*Math.cos(yaw);
//            dy=-dz*Math.tan(pitch);
//            dx= dr*Math.sin(yaw);


//            double alfa, alfa1, rxz;
//            alfa=-pitch;
//            alfa1=Math.atan(Math.tan(alfa)/Math.sin(yaw));
//            rxz=dr*Math.cos(alfa1);

            dz=dr*Math.cos(pitch)*Math.cos(yaw);
            dx=dr*Math.sin(yaw);
            dy=-dr*Math.sin(pitch)*Math.cos(yaw);

            ta.appendText("dr: "+dr+"  dx: "+dx+"  dy: "+dy+"  dz: "+dz+" -   pitch: "+pitch+"  yaw: "+yaw+"   - sin( yaw ): "+Math.sin(yaw)+"  cos( yaw ): "+Math.cos(yaw)+"\n");


            cameraTranslate.setX(cameraTranslate.getX()+dx);
            cameraTranslate.setY(cameraTranslate.getY()+dy);
            cameraTranslate.setZ(cameraTranslate.getZ()+dz);
//            cameraTranslate.setZ(cameraTranslate.getZ()-e.getDeltaY()*cameraZoomFactor);
        });
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
//                if (e.isMiddleButtonDown()) {
//                    cameraRotateX.setPivotX(0);
//                    cameraRotateY.setPivotY(0);
//                    cameraRotateZ.setPivotZ(0);
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
            if(selectedNode.getId().equals(viewportPane.getId())){
                HB_nodeStatusBar.setVisible(false);
                printNodeProperties(perspectiveCamera);
            }
            else {
                HB_nodeStatusBar.setVisible(true);
                ShowSelectedNodeTransformBar();

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
        S_FieldOfView.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
               // if(subScene.getCamera().getTypeSelector()==perspectiveCamera.getTypeSelector()) {
                    perspectiveCamera.setFieldOfView(S_FieldOfView.getValue());
                    //ta.appendText("Perspective camera is the current camera, Field of View property changed to: "+S_FieldOfView.getValue()+"\n");
                //}
            }
        });
        //Slider rotate around Z axis
        S_RotateZ.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                cameraRotateZ.setAngle(S_RotateZ.getValue());
            }
        });

        //Set listener to all camera transforms
        ObservableList<Transform> observableList = FXCollections.observableList(subScene.getCamera().getTransforms());
            for(int i=0;i<observableList.size();i++){
                observableList.get(i).setOnTransformChanged(transformChangedEvent -> {
                    ShowCameraTransform();});
//                ta.appendText(observableList.get(i).toString());
            }


        MSN_cameraX.textField.setOnKeyReleased(eh->cameraTranslate.setX(MSN_cameraX.getValue()));
        MSN_cameraY.textField.setOnKeyReleased(eh->cameraTranslate.setY(MSN_cameraY.getValue()));
        MSN_cameraZ.textField.setOnKeyReleased(eh->cameraTranslate.setZ(MSN_cameraZ.getValue()));
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
        TF_nodeID.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
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
        });
        MSN_nodeX.textField.setOnKeyReleased(eh->selectedNode.setTranslateX(MSN_nodeX.getValue()));
        MSN_nodeY.textField.setOnKeyReleased(eh->selectedNode.setTranslateY(MSN_nodeY.getValue()));
        MSN_nodeZ.textField.setOnKeyReleased(eh->selectedNode.setTranslateZ(MSN_nodeZ.getValue()));

//        MSN_nodeAngleX.textField.setOnKeyReleased(eh->selectedNode.getTransforms().addAll(nodeRotateX.setAngle(MSN_nodeAngleX.getValue())));
//        MSN_nodeAngleY.textField.setOnKeyReleased(eh->nodeRotateY.setAngle(MSN_nodeAngleY.getValue()));
//        MSN_nodeAngleZ.textField.setOnKeyReleased(eh->nodeRotateZ.setAngle(MSN_nodeAngleZ.getValue()));

    }

    private void ShowCameraTransform(){
        //int caret=0;
        MSN_cameraX.setText(cameraTranslate.getX());
        MSN_cameraY.setText(cameraTranslate.getY());
        MSN_cameraZ.setText(cameraTranslate.getZ());
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

        cameraRotateX.setAngle(0);
        cameraRotateY.setAngle(-90);
        cameraRotateZ.setAngle(0);

        cameraTranslate.setX(sceneBounds*3);
        cameraTranslate.setY(0);
        cameraTranslate.setZ(0);
    }
    @FXML     private void CameraAlongY(){

        cameraRotateX.setAngle(90);
        cameraRotateY.setAngle(0);
        cameraRotateZ.setAngle(0);

        cameraTranslate.setX(0);
        cameraTranslate.setY(sceneBounds*3);
        cameraTranslate.setZ(0);
    }
    @FXML     private void CameraAlongZ(){

        cameraRotateX.setAngle(0);
        cameraRotateY.setAngle(180);
        cameraRotateZ.setAngle(0);

        cameraTranslate.setX(0);
        cameraTranslate.setY(0);
        cameraTranslate.setZ(sceneBounds*3);
    }
    @FXML     private void CameraAlong0(){

        cameraRotateX.setAngle(-56);
        cameraRotateY.setAngle(-135);
        cameraRotateZ.setAngle(146);

        cameraTranslate.setX(sceneBounds*2/Math.sqrt(2));
        cameraTranslate.setY(sceneBounds*2);
        cameraTranslate.setZ(sceneBounds*2/Math.sqrt(2));
    }

//    public void traverse (Node root){ // Each child of a tree is a root of its subtree.
//        root.
//        if (root.add.chi.left != null){
//            traverse (root.left);
//        }
//        System.out.println(root.data);
//        if (root.right != null){
//            traverse (root.right);
//        }
//    }
private void printNodeProperties(Node node){

    Bounds boundsInScene = node.localToScene(node.getBoundsInLocal());

    ta.appendText(
//            "\n"+
                    node.getTypeSelector().toString()+
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


public static ArrayList<Node> getAllNodes(Parent root) {
    ArrayList<Node> nodes = new ArrayList<Node>();
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