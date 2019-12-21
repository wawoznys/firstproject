package project.Interfraces;

import project.ElementsOnMap.Vector2d;

public interface IWorldMapElement {

    Vector2d getPosition();

    void addObserver(IPositionChangeObserver observer);

}