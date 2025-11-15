package main.dimension;

import java.util.function.BiConsumer;

import javafx.beans.property.StringProperty;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class ShapeWithDimension<S extends Shape>{
    public S shape;
    public Dimension[] dimensions;
    private DimensionType[] dts;

    public ShapeWithDimension(S shape, DimensionType[] dts) {
        this.shape = shape;
        this.dts = dts;
        this.dimensions = new Dimension[dts.length];
        for (int i = 0; i < dts.length; i++){
            dimensions[i] = new Dimension(shape);
            setDimension(dimensions[i], i);
            final int index = i;
            shape.boundsInParentProperty().addListener((obs, oldVal, newVal)->{
                dimensions[index].getTransforms().setAll(shape.getTransforms());
                dts[index].setDimensionPoints.accept(shape, dimensions[index]);
            });
        }
    }
    private void setDimension(Dimension d, int i) {
        ((Pane)shape.getParent()).getChildren().add(d);
        d.layoutXProperty().bind(shape.layoutXProperty());
        d.layoutYProperty().bind(shape.layoutYProperty());
        d.setStroke(Color.RED);

        d.setTextPosition(dts[i].textPosition);
        d.setTranslateX(dts[i].dimensionTranslate[0]);
        d.setTranslateY(dts[i].dimensionTranslate[1]);
        if(dts[i].textBindTo instanceof StringProperty)
            d.textProperty().bind((StringProperty)dts[i].textBindTo);
        else if(dts[i].textBindTo instanceof String)
            d.setText((String)dts[i].textBindTo);
    }

    public void setDimensionTypeDefault(){
        for(int i = 0; i < dts.length; i++) dts[i] = dts[i].defaultDimensionType;
        for(int i = 0; i < dts.length; i++){
            dimensions[i].setTranslateX(dts[i].dimensionTranslate[0]);
            dimensions[i].setTranslateY(dts[i].dimensionTranslate[1]);
        }
    }

    public static class DimensionType{
        public static final DimensionType WIDTH = new DimensionType(
            (s, d)->d.setDimensionPoints(0, 0, ((Rectangle)s).getWidth(), 0),
            0.5f, 0, -5
        );
        public static final DimensionType HEIGHT = new DimensionType(
            (s, d)->d.setDimensionPoints(0, 0, 0, ((Rectangle)s).getHeight()),
            0.5f, -5, 0
        );

        private BiConsumer<Shape, Dimension> setDimensionPoints;
        private DimensionType defaultDimensionType;
        private float textPosition;
        private Object textBindTo;
        private double[] dimensionTranslate;

        private DimensionType(BiConsumer<Shape, Dimension> setDimensionPoints, float textPosition, double... translate){
            this.setDimensionPoints = setDimensionPoints;
            defaultDimensionType = this;
            this.textPosition = textPosition;
            this.dimensionTranslate = translate;
        }
        private DimensionType(BiConsumer<Shape, Dimension> setDimensionPoints, DimensionType defaultDimensionType, float textPosition, double... translate){
            this.setDimensionPoints = setDimensionPoints;
            this.defaultDimensionType = defaultDimensionType;
            this.textPosition = textPosition;
            this.dimensionTranslate = translate;
        }

        public DimensionType customTranslate(double... translate){
            if(this==defaultDimensionType) return new DimensionType(setDimensionPoints, defaultDimensionType, textPosition, translate);
            this.dimensionTranslate = translate;
            return this;
        }
        public DimensionType customTextPoisition(float textPosition){
            if(this==defaultDimensionType) return new DimensionType(setDimensionPoints, defaultDimensionType, textPosition, dimensionTranslate);
            this.textPosition = textPosition;
            return this;
        }
        public DimensionType customTextBindTo(StringProperty textBindTo){
            if(this==defaultDimensionType) return new DimensionType(setDimensionPoints, defaultDimensionType, textPosition, dimensionTranslate)
                .customTextBindTo(textBindTo);
            this.textBindTo = textBindTo;
            return this;
        }
        public DimensionType customTextBindTo(String textBindTo){
            if(this==defaultDimensionType) return new DimensionType(setDimensionPoints, defaultDimensionType, textPosition, dimensionTranslate)
                .customTextBindTo(textBindTo);
            this.textBindTo = textBindTo;
            return this;
        }
        public DimensionType custom(StringProperty textBindTo, float textPosition, double... translate){
            if(this==defaultDimensionType) return new DimensionType(setDimensionPoints, defaultDimensionType, textPosition, translate);
            this.textBindTo = textBindTo;
            this.textPosition = textPosition;
            this.dimensionTranslate = translate;
            return this;
        }
    }
}
