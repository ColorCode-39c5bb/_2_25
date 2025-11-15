package main.dimension;

import javafx.scene.shape.Shape;

// public sealed interface Dimensionable permits ShapeWithDimension {
public interface Dimensionable {
    public Shape getShape();
    public Dimension getDimension();
}
