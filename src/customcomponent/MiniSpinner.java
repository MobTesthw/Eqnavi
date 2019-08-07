package customcomponent;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
//Round digit 0
//Text Filed value update with Increment/Decrement button clock performed with requesting focus



public class MiniSpinner extends Control {
    public HBox hBox;//=new HBox();
    public StackPane labelArea;//=new StackPane();
    public TextField textField;//=new TextField();
    public Label labelDescription;
    public Label buttonIncrement;
    public Label buttonDecrement;
    public String description, incrementDescription ="+", decrementDescription ="-";
    private int buttonCase=-1;
    private TextFormatter<Double> textFormatter;

//    public Button buttonIncrement;
//    public Button buttonDecrement;


    double min, value, max,increment=0.1;
    private int roundDigit=3;

    public  MiniSpinner(String Description, double minimum, double startValue, double maximum,double Increment){
        min=minimum;
        value=startValue;
        max=max;
        description=Description;
        increment=Increment;

        labelDescription=new Label(description);
        buttonIncrement=new Label();
        buttonDecrement=new Label();
//        buttonIncrement=new Button();
//        buttonDecrement=new Button();
        buttonIncrement.setAlignment(Pos.CENTER);
        buttonDecrement.setAlignment(Pos.CENTER);
        buttonIncrement.setText(incrementDescription);
        buttonDecrement.setText(decrementDescription);

//        buttonIncrement.setStyle("-fx-border-color: black;");
//        buttonIncrement.setStyle("-fx-border-radius: 20 20 0 0;");
//        -fx-border-radius: 20 20 0 0;
        buttonIncrement.setStyle(
//                "-fx-padding: 5 22 5 22;   \n" +
                "    -fx-border-color: black;\n" +
                "    -fx-border-width: 1;\n" +
                "    -fx-background-radius: 8;\n" +
                "    -fx-background-color: #60BD7a;\n" +
//                "    -fx-font-family: \"Segoe UI\", Helvetica, Arial, sans-serif;\n" +
//                "    -fx-font-size: 11pt;\n" +
//                "    -fx-text-fill: #d8d8d8;\n" +
//                "    -fx-background-insets: 0 0 0 0, 0, 1, 2;" +
                        "");
//        buttonDecrement.setStyle("-fx-border-color: black;");

        javafx.scene.paint.Color col = javafx.scene.paint.Color.rgb(199,216,249);
        CornerRadii corn = new CornerRadii(5);
        Background background = new Background(new BackgroundFill(col, corn, Insets.EMPTY));

        buttonDecrement.setBackground(background);
//        buttonDecrement.setBorder(background);
//        System.out.println(description+"  Desc width "+labelDescription.getWidth()+"  buttonIncrement "+buttonIncrement.getWidth());


        labelArea=new StackPane(labelDescription,buttonIncrement,buttonDecrement);
        //Resize buttons to StackPane
        buttonIncrement.prefWidthProperty().bind(labelArea.widthProperty());
        buttonIncrement.prefHeightProperty().bind(labelArea.heightProperty());
        buttonDecrement.prefWidthProperty().bind(labelArea.widthProperty());
        buttonDecrement.prefHeightProperty().bind(labelArea.heightProperty());

        textField=new TextField();
        hBox=new HBox(labelArea,textField);

        buttonDecrement.setVisible(false);//.setOpacity(0);
        buttonIncrement.setVisible(false);//.setOpacity(0);
        labelDescription.setVisible(true);//.toFront();//setOpacity(1);

        //Adjust TextField Width to Content
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // expand the TextField
                // Do this in a Platform.runLater because of Textfield has no padding at first time and so on
                Platform.runLater(() -> {
                    Text text = new Text(newValue);
                    text.setFont(textField.getFont()); // Set the same font, so the size is the same
                    double width = text.getLayoutBounds().getWidth() // This big is the Text in the TextField
                            + textField.getPadding().getLeft() + textField.getPadding().getRight() // Add the padding of the TextField
                            + 2d; // Add some spacing
                    textField.setPrefWidth(width); // Set the width
                    textField.positionCaret(textField.getCaretPosition()); // If you remove this line, it flashes a little bit
                });
            }
        });
        //OnScroll
        textField.setOnScroll(e->{
            if(e.getDeltaY()>0)Increment();
            else if(e.getDeltaY()<0)Decrement();
//            System.out.println(e.getDeltaY());
        });

        //Force TextField to operate with double values only
        Pattern validEditingState = Pattern.compile("-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?");

        UnaryOperator<TextFormatter.Change> filter = c -> {
            String text = c.getControlNewText();
            if (validEditingState.matcher(text).matches()) {
                return c ;
            } else {
                return null ;
            }
        };

        StringConverter<Double> converter = new StringConverter<Double>() {

            @Override
            public Double fromString(String s) {
                if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s)) {
                    return 0.0 ;
                } else {
                    return Double.valueOf(s);
                }
            }
            @Override
            public String toString(Double d) {
                return d.toString();
            }
        };
        textFormatter = new TextFormatter<>(converter, 0.0, filter);
        textField.setTextFormatter(textFormatter);

        //Switching between buttons
        labelArea.setOnMouseMoved(eh->{
            //System.out.println("Height: "+labelArea.getHeight()+"  x: "+eh.getX()+"  y: "+eh.getY());
            if(eh.getY()>labelArea.getHeight()/2){
                buttonDecrement.setVisible(true);//toFront();//setOpacity(1);
                buttonIncrement.setVisible(false);//setOpacity(0);
                labelDescription.setVisible(false);//setOpacity(0);
                buttonCase=0;
            }
            else{
                buttonDecrement.setVisible(false);//Opacity(0);
                buttonIncrement.setVisible(true);//.toFront();//setOpacity(1);
                labelDescription.setVisible(false);//setOpacity(0);
                buttonCase=1;
            }
        });
        labelArea.setOnMouseExited(eh->{
            buttonDecrement.setVisible(false);//setOpacity(0);
            buttonIncrement.setVisible(false);//setOpacity(0);
            labelDescription.setVisible(true);//.toFront();//setOpacity(1);
            buttonCase=-1;
        });
        labelArea.setOnMouseClicked(eh->{

          //  System.out.println("Click buttonCase= "+buttonCase);
            if(buttonCase==0)Decrement();
            if(buttonCase==1)Increment();
            if(buttonCase==-1)System.out.println("MiniSpinner Class Wrong button case!!!");
//            textField.setTextFormatter(textFormatter);
        });
        //Don'tWork
        textField.setOnKeyPressed(eh->{
           if(eh.getCode()== KeyCode.UP){
               Increment();
//               System.out.println(" Key UP");
           }
           else if(eh.getCode()== KeyCode.DOWN){
               Decrement();
//               System.out.println(" Key Down");
           }
//           else if(eh.getCode()== KeyCode.UP){
//
//               System.out.println(" UP");
//           }
        });


//        buttonIncrement.setOnMouseClicked(eh->{
//            setText(getValue()+increment);
//        });
//        buttonDecrement.setOnMouseClicked(eh->{
//            Decrement();
//        });
//        buttonIncrement.addEventHandler(ActionEvent.ANY, a->System.out.println("Action event type: "+a.getEventType()+"  source: "+a.getSource()+" target: "+a.getTarget()+"  string: "+a.toString()+"  hash: "+a.hashCode()+"  class: "+a.getClass()+"\n"));


    }

    public void setRoundDigit(int number){
        roundDigit=number;
    }



    //Setting new value
    public void setText(double newValue){
        //Save caret position if text field is focused
        int caretPosition=0,textLength=0;
        if(textField.isFocused()){
            caretPosition=textField.getCaretPosition();
            textLength=textField.getText().length();
//            System.out.println("text field focused current caret position is : "+textField.getCaretPosition());
        }
        //Round the value before setting to TextField
        value=Math.round(newValue*Math.pow(10,roundDigit))/(Math.pow(10,roundDigit));
        textField.setText(Double.toString(value));
        if(textField.isFocused()){
            int textChange=textField.getText().length()-textLength;
            if(textChange>0)textField.positionCaret(caretPosition+1);
            else if (textChange<0)textField.positionCaret(caretPosition-1);
            else if(textChange==0)textField.positionCaret(caretPosition);
//            System.out.println("text field focused caret position after setting the text is : "+textField.getCaretPosition());
        }
    }
    public double getValue(){
        return Double.parseDouble(textField.getText());
    }
    public Node getComponent(){
        return hBox;
    }
    public void Increment(){
        setText(getValue()+increment);
        pressEnter();
    }
    public void Decrement(){
        setText(getValue()-increment);
        pressEnter();
    }
    private void pressEnter(){
        try {
            textField.requestFocus();
            Robot r = new Robot();
            //there are other methods such as positioning mouse and mouseclicks etc.
            r.keyPress(KeyEvent.VK_ENTER);
            r.keyRelease(KeyEvent.VK_ENTER);
        } catch (AWTException e) {
            //Teleport penguins
        }

    }



}
