package main.dimension;

import java.util.Arrays;
import java.util.function.Function;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Bounds;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Shape;

public non-sealed class ShapeWithDimension<S extends Shape> implements Dimensionable {
    public S shape;
    private Polyline dimension;
    public ObjectProperty<byte[]> DimensionTypeProperty = new SimpleObjectProperty<byte[]>();

    public ShapeWithDimension(S shape, Function<Bounds, Double[]> setPointsDimension) {// TODO double[]竟然不行？
        this.shape = shape;
        dimension = new Polyline();
        shape.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
            dimension.getPoints().setAll( Arrays.asList(setPointsDimension.apply(observable.getValue())) );
        });
    }

    @Override
    public ObjectProperty<byte[]> getDimensionTypeProperty() {
        return DimensionTypeProperty;
    }
    @Override
    public void setDimensionType(byte[] types) {  
        DimensionTypeProperty.set(types);
    }
}
