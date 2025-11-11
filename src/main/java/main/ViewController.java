package main;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.converter.NumberStringConverter;

public class ViewController implements Initializable {
    @FXML Pane controller;
    @FXML Pane shape;
    @FXML Group reactive;

    Slider[] sliders = new Slider[6];
    TextField[] textFields = new TextField[6];
    double[] values = new double[]{1,1,1,3,3,3};

    Rectangle[] body = new Rectangle[2];
    Rectangle[] neck = new Rectangle[2];
    Label[] reactiveLabels = new Label[2];


    // boolean needUpdate = false;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        for(int i = 0; textFields.length > i; i++){
            sliders[i] = (Slider)((Group)controller.getChildren().get(i)).getChildren().get(0);
            textFields[i] = (TextField)((Group)controller.getChildren().get(i)).getChildren().get(1);
        } 

        // // App.stage.maximizedProperty().addListener((obs, oldVal, newVal)->draw());

        // for (int i = 0; i < sliders.length; i++) {
        //     final int indexMe = i;
        //     sliders[indexMe].valueProperty().addListener((observable, oldValue, newValue) -> {
        //         values[indexMe] = newValue.doubleValue();
        //         textFields[indexMe].setText(String.valueOf(values[indexMe]));
        //         draw();
        //     });
        // }
        // for (int i = 0; i < textFields.length; i++) {
        //     final int indexMe = i;
        //     textFields[indexMe].focusedProperty().addListener((observable, oldValue, newValue) -> {
        //         try {
        //             values[indexMe] = Double.valueOf(textFields[indexMe].getText());
        //         } catch (NumberFormatException e) {
        //             return;
        //         }
        //         sliders[indexMe].setValue(values[indexMe]);
        //         draw();
        //     });
        // }
        for(int i=0; i<textFields.length; i++){
            textFields[i].textProperty().bindBidirectional(sliders[i].valueProperty(), new NumberStringConverter());
        }



        neck[0] = (Rectangle)shape.getChildren().get(0);
        body[0] = (Rectangle)shape.getChildren().get(1);
        neck[1] = (Rectangle)shape.getChildren().get(2);
        body[1] = (Rectangle)shape.getChildren().get(3);

        reactiveLabels[0] = (Label)reactive.getChildren().get(0);
        reactiveLabels[1] = (Label)reactive.getChildren().get(1);
    }

    double unit;
    void draw(){
        System.out.println(Arrays.toString(values));
        reactiveLabels[0].setText(String.format(
            "=1000×9.8×(%.2f+%.2f)×(%.2f×%.2f)=%.2f N",
            values[2], values[5], values[3], values[4],
            1000*9.8*(values[2]+values[5])*values[3]*values[4]
        ));
        reactiveLabels[1].setText(String.format(
            "=1000×9.8×(%.2f×%.2f×%.2f+%.2f×%.2f×%.2f)=%.2f N",
            values[0], values[1], values[2], values[3], values[4], values[5],
            1000*9.8*(values[0]*values[1]*values[2]+values[3]*values[4]*values[5])
        ));


        double valueMax = Arrays.stream(values).max().getAsDouble();
        unit = 1/valueMax * (Math.min(shape.getWidth()/2, shape.getHeight())-8);
        System.out.println(shape.getHeight()+" "+unit);
        var valuesC = values.clone();
        for(int i=0; i<valuesC.length; i++) valuesC[i]=valuesC[i]*unit;
        neck[0].setWidth(valuesC[0]);
        neck[0].setHeight(valuesC[2]);  
        neck[1].setWidth(valuesC[0]);   
        neck[1].setHeight(valuesC[1]);

        body[0].setWidth(valuesC[3]); 
        body[0].setHeight(valuesC[5]);
        body[1].setWidth(valuesC[3]); 
        body[1].setHeight(valuesC[4]);

        body[0].setLayoutX(shape.getWidth()/4-valuesC[3]/2);
        body[0].setLayoutY(shape.getHeight()-valuesC[5]);
        neck[0].setLayoutX(body[0].getLayoutX()+valuesC[3]/2-valuesC[0]/2);
        neck[0].setLayoutY(body[0].getLayoutY()-valuesC[2]);

        body[1].setLayoutX(3*shape.getWidth()/4-valuesC[3]/2);
        body[1].setLayoutY(shape.getHeight()/2-valuesC[4]/2);
        neck[1].setLayoutX(body[1].getLayoutX()+valuesC[3]/2-valuesC[0]/2);
        neck[1].setLayoutY(body[1].getLayoutY()+valuesC[4]/2-valuesC[1]/2);

        // addDimension(body[0].getLayoutX(), body[0].getLayoutY(), body[0].getLayoutX()+body[0].getWidth(), body[0].getLayoutY());
    }

    // void addDimension(double x1, double y1, double x2, double y2){
    //     Line line = new Line(x1, y1, x2, y2);
    //     line.setStrokeWidth(2);
    //     line.setStroke(Color.RED);
    //     shape.getChildren().add(line);
    //     Label label = new Label(String.format("%.2f", Math.sqrt(Math.pow(x2-x1, 2)+Math.pow(y2-y1, 2))));
    //     label.setLayoutX((x1+x2)/2);
    //     label.setLayoutY((y1+y2)/2);
    //     shape.getChildren().add(label);
    // }
}
