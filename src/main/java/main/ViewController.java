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
import main.dimension.ShapeWithDimension;
import main.dimension.ShapeWithDimension.DimensionType;

public class ViewController implements Initializable {
    @FXML Pane pane_controller;
    @FXML Pane pane_shape;
    @FXML Group reactive;

    Slider[] sliders = new Slider[6];
    TextField[] textFields = new TextField[6];
    double[] values = new double[]{1,1,1,3,3,3};

    ShapeWithDimension<Rectangle>[] body = new ShapeWithDimension[2];
    ShapeWithDimension<Rectangle>[] neck = new ShapeWithDimension[2];

    Label[] reactiveLabels = new Label[2];

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        for(int i = 0; textFields.length > i; i++){
            sliders[i] = (Slider)((Group)pane_controller.getChildren().get(i)).getChildren().get(0);
            textFields[i] = (TextField)((Group)pane_controller.getChildren().get(i)).getChildren().get(1);
        } 

        for(short i=0; i<textFields.length; i++){
            textFields[i].textProperty().bindBidirectional(sliders[i].valueProperty(), new NumberStringConverter());
            final int index = i;
            sliders[i].valueProperty().addListener((obs, oldVal, newVal)->{
                values[index] = sliders[index].getValue();
                double valueMax = Arrays.stream(values).max().getAsDouble();
                draw(1/valueMax * (Math.min(pane_shape.getWidth()/2, pane_shape.getHeight())-8));
            });
        }

        neck[0] = new ShapeWithDimension<>(
            (Rectangle)pane_shape.getChildren().get(0),
            new DimensionType[]{
                DimensionType.WIDTH.customTextBindTo(textFields[0].textProperty()).customTextPoisition(-0.5f), 
                DimensionType.HEIGHT.customTextBindTo(textFields[2].textProperty())
            }
        );
        body[0] = new ShapeWithDimension<>(
            (Rectangle)pane_shape.getChildren().get(1),
            new DimensionType[]{
                DimensionType.WIDTH.customTextBindTo(textFields[3].textProperty()).customTranslate(0,5), 
                DimensionType.HEIGHT.customTextBindTo(textFields[5].textProperty())
            }
        );
        neck[1] = new ShapeWithDimension<>(
            (Rectangle)pane_shape.getChildren().get(2),
            new DimensionType[]{
                DimensionType.WIDTH.customTextBindTo(textFields[0].textProperty()).customTextPoisition(-0.5f),
                DimensionType.HEIGHT.customTextBindTo(textFields[1].textProperty()),
            }
        );
        body[1] = new ShapeWithDimension<>(
            (Rectangle)pane_shape.getChildren().get(3),
            new DimensionType[]{
                DimensionType.WIDTH.customTextBindTo(textFields[3].textProperty()).customTextPoisition(-0.5f),
                DimensionType.HEIGHT.customTextBindTo(textFields[4].textProperty())
            }
        );
        // neck[1].setDimensionTypeDefault();
        // neck[0].setDimensionTypeDefault();

        reactiveLabels[0] = (Label)reactive.getChildren().get(0);
        reactiveLabels[1] = (Label)reactive.getChildren().get(1);
// 绑定位置
        body[0].shape.layoutXProperty().bind(body[0].shape.widthProperty().map(w->pane_shape.getWidth()/4 - (double)w/2));
        neck[0].shape.layoutXProperty().bind(neck[0].shape.widthProperty().map(w->pane_shape.getWidth()/4 - (double)w/2));
        body[0].shape.layoutYProperty().bind(body[0].shape.heightProperty().map(h->pane_shape.getHeight() - (double)h));
        // neck[0].layoutYProperty需要绑定两个属性，一个是body[0].shape.layoutYProperty，一个是neck[0].shape.heightProperty
        // 所以交给draw使之每次输入变化都更新(实际只需要body[0].shape.layoutYProperty或neck[0].shape.heightProperty变化才更新)
        body[1].shape.layoutXProperty().bind(body[1].shape.widthProperty().map(w->pane_shape.getWidth()*3/4 - (double)w/2));
        neck[1].shape.layoutXProperty().bind(neck[1].shape.widthProperty().map(w->pane_shape.getWidth()*3/4 - (double)w/2));
        body[1].shape.layoutYProperty().bind(body[1].shape.heightProperty().map(h->pane_shape.getHeight()/2 - (double)h/2));
        neck[1].shape.layoutYProperty().bind(neck[1].shape.heightProperty().map(h->pane_shape.getHeight()/2 - (double)h/2));
    }

    public void draw(double unit){
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

// 触发"绑定位置"
        neck[0].shape.setWidth(values[0]*unit);
        neck[0].shape.setHeight(values[2]*unit);
        body[0].shape.setWidth(values[3]*unit);
        body[0].shape.setHeight(values[5]*unit);

        neck[1].shape.setWidth(values[0]*unit);
        neck[1].shape.setHeight(values[1]*unit);
        body[1].shape.setWidth(values[3]*unit);
        body[1].shape.setHeight(values[4]*unit);

        neck[0].shape.setLayoutY(body[0].shape.getLayoutY() - neck[0].shape.getHeight());
    }
}
