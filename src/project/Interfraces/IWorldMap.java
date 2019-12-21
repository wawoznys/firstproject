package project.Interfraces;

import project.ElementsOnMap.Vector2d;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo
 *
 */
public interface IWorldMap {

    Object objectAt(Vector2d position);
    boolean isOccupied(Vector2d currentPosition);

    Vector2d curvedPosition(Vector2d toUnitVector);
}