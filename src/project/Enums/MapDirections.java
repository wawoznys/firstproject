package project.Enums;

import project.ElementsOnMap.Vector2d;

public enum MapDirections {
    NORTH,
    NORTHWEST,
    NORTHEAST,
    SOUTHWEST,
    SOUTHEAST,
    EAST,
    WEST,
    SOUTH;

    public String toString() {
        switch (this) {
            case NORTH:
                return "Północ";
            case SOUTH:
                return "Południe";
            case EAST:
                return "Wschód";
            case WEST:
                return "Zachód";
            case NORTHWEST:
                return "Północny wschód";
            case SOUTHWEST:
                return "Południowy zachód";
            case NORTHEAST:
                return "Północny wschód";
            case SOUTHEAST:
                return "Południowy zachód";
        }
        return null;
    }


    public MapDirections next() {
        switch (this) {
            case NORTH:
                return MapDirections.NORTHEAST;
            case SOUTH:
                return MapDirections.SOUTHWEST;
            case EAST:
                return MapDirections.SOUTHEAST;
            case WEST:
                return MapDirections.NORTHWEST;
            case NORTHWEST:
                return MapDirections.NORTH;
            case SOUTHWEST:
                return MapDirections.WEST;
            case NORTHEAST:
                return MapDirections.EAST;
            case SOUTHEAST:
                return MapDirections.SOUTH;
        }
        return null;
    }
    public MapDirections previous() {
        switch (this) {
            case NORTH:
                return MapDirections.NORTHWEST;
            case SOUTH:
                return MapDirections.SOUTHEAST;
            case EAST:
                return MapDirections.NORTHEAST;
            case WEST:
                return MapDirections.SOUTHWEST;
            case NORTHWEST:
                return MapDirections.WEST;
            case SOUTHWEST:
                return MapDirections.SOUTH;
            case NORTHEAST:
                return MapDirections.NORTH;
            case SOUTHEAST:
                return MapDirections.EAST;
        }
        return null;
    }

    public Vector2d toUnitVector() {
        switch (this) {
            case NORTH:
                return new Vector2d(0,1);
            case SOUTH:
                return new Vector2d(0,-1);
            case EAST:
                return new Vector2d(1,0);
            case WEST:
                return new Vector2d(-1,0);
            case NORTHWEST:
                return new Vector2d(-1,1);
            case SOUTHWEST:
                return new Vector2d(-1,-1);
            case NORTHEAST:
                return new Vector2d(1,1);
            case SOUTHEAST:
                return new Vector2d(1,-1);
            default:
                return new Vector2d(0,0);
        }
    }
}
