package main.dimension;

import javafx.beans.property.ObjectProperty;

public sealed interface Dimensionable permits ShapeWithDimension {
    public ObjectProperty<byte[]> getDimensionTypeProperty();
    public void setDimensionType(byte[] types);

    // public double[] getPointsDimension();
}
