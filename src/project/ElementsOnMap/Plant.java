package project.ElementsOnMap;
import project.Interfraces.IPositionChangeObserver;
import project.Interfraces.IWorldMapElement;

import java.awt.*;

public class Plant implements IWorldMapElement {
    private Vector2d position;
    public Vector2d getPosition(){
        return this.position;
    }

    public Plant(Vector2d initialPosition){
        position = initialPosition;
    }

    @Override
    public String toString(){
        return "*";
    }

    public void addObserver(IPositionChangeObserver observer) {
        return;
    }

    public Color toColor() {
        return new Color(0, 82, 8);
    }
}
