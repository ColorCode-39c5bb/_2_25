package main.dimension;

import java.util.Arrays;

import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

public class Dimension extends Polyline{
    private Shape shapeAttached;
    private float textPosition = 0.5f;
    private Label label;

    public Dimension(Shape shapeAttached){
        this.shapeAttached = shapeAttached;
        this.label = new Label();
        ((Pane)shapeAttached.getParent()).getChildren().add(label);
        setLabel();
    }
    private void setLabel(){
        label.setStyle("-fx-text-fill: red;");
        label.layoutXProperty().bind(this.layoutXProperty());
        label.layoutYProperty().bind(this.layoutYProperty());  
    }
    public void setText(String text){
        label.setText(text);
    }
    public StringProperty textProperty() {
        return label.textProperty();
    }
    public void setTextPosition(float textPosition) {
        this.textPosition = textPosition;
        side = textPosition>=0?0:1;
        radio = Math.abs(textPosition);
    }
    
    private int side = 0;
    private float radio = 0.5f;
    public void setDimensionPoints(double... points) {
        super.getPoints().setAll(Arrays.stream(points).boxed().toArray(Double[]::new));
        double angle = Math.atan2(points[3]-points[1], points[2]-points[0]);
        label.getTransforms().setAll(this.getTransforms());
        label.getTransforms().add(new Rotate(Math.toDegrees(angle), 0, 0));
        label.setTranslateX(this.getTranslateX() + radio*(points[2]-points[0]) - label.getWidth()/2*Math.cos(angle) + side*label.getHeight()*Math.sin(angle));
        label.setTranslateY(this.getTranslateY() + radio*(points[3]-points[1]) - label.getWidth()/2*Math.sin(angle) - side*label.getHeight()*Math.cos(angle));
    }
}
