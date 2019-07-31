package model.randompath;

import javafx.scene.shape.Rectangle;

public class Path2D implements Path2DSettings {
    Rectangle source;
    Rectangle destination;
    int numOfShapes;
    int numOfPoints;
    int pixelPathInertia;
    int pixelPathSpan;
    int pixelPathColorChange;
    boolean reflectFromBorders;

}
