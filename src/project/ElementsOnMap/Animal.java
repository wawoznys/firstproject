package project.ElementsOnMap;

import project.Enums.MapDirections;
import project.Enums.MoveDirections;
import project.Interfraces.IPositionChangeObserver;
import project.Interfraces.IWorldMapElement;
import project.World.WorldMap;

import java.awt.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Animal implements IWorldMapElement {
    public Genotype genotype;
    private Vector2d position;
    public int energy;
    private int startEnergy;
    private MapDirections direction;
    private Set<IPositionChangeObserver> observers = new HashSet<>();
    private WorldMap map;

    private Animal() {
        this.direction = MapDirections.NORTH;
        genotype = new Genotype();
        position = new Vector2d(2, 2);
    }

    private Animal(WorldMap map) {
        this();
        this.map = map;
    }

    private Animal(WorldMap map, Vector2d initialPosition) {
        this(map);
        this.position = initialPosition;
    }


    public Animal(Vector2d initialPosition, int startEnergy, WorldMap map) {
        this(map, initialPosition);
        this.energy = startEnergy;
        this.startEnergy = energy;
        genotype = new Genotype();
    }



    public Vector2d getPosition() {
        return new Vector2d(position.x, position.y);
    }

    public int getEnergy(){
        return energy;
    }

    public void changeEnergy(int changeEnergy){
        energy += changeEnergy;
        if(energy < 0)
            energy = 0;
    }


    public boolean isDead(){
        return energy <= 0;
    }

    public void move(MoveDirections toDirection) {
        Vector2d moved;
        switch (toDirection) {
            case LEFT:
                this.direction = this.direction.previous();
                break;
            case RIGHT:
                this.direction = this.direction.next();
                break;
            case FORWARD:
                moved = this.position.add(map.curvedPosition(this.direction.toUnitVector()));
                if(map.canMoveTo(moved)){
                    positionChanged(moved);
                    this.position = moved;
                }
                break;
            case BACKWARD:
                moved = this.position.subtract(map.curvedPosition(this.direction.toUnitVector()));
                if(map.canMoveTo(moved)) {
                    positionChanged(moved);
                    this.position = moved;
                    break;
                }
        }
    }


    public void turn() {
        double probabilityTab []=new double[8];
        for(int i=0; i<8; i++){
            probabilityTab[i]=0;
        }
        for(int i=0; i<32; i++){
            probabilityTab[genotype.getGenotype()[i]]+=1/32;
        }
        for(int i=1; i<8; i++) {
            probabilityTab[i]+=probabilityTab[i-1];
        }
        Random r = new Random();
        Double a = r.nextDouble();
        int numOfRotation = genotype.returnRandomGen();
        for(int i=1; i<8; i++) {
            if(probabilityTab[i]<=a && probabilityTab[i-1]>=a){
                numOfRotation=i;
            }
        }

        for (int i = 0; i < numOfRotation; i++) {
            this.move(MoveDirections.RIGHT);
        }
    }


    public Animal breed(Animal secondParent, Vector2d birthPosition) {
        int childEnergy = (int) (0.25 * (secondParent.energy + this.energy));
        secondParent.changeEnergy((int) - (0.25 * secondParent.energy));
        this.changeEnergy((int) -(this.energy * 0.25));
        Animal child = new Animal(birthPosition, childEnergy, map);
        child.genotype = new Genotype(this.genotype, secondParent.genotype);
        return child;
    }
    public boolean isAbleToBreed(){
        return this.energy >= this.startEnergy/2;
    }


    public void addObserver(IPositionChangeObserver observer){
        observers.add(observer);
    }


    public void removeObserver(IPositionChangeObserver observer){
        observers.remove(observer);
    }


    private void positionChanged(Vector2d newPosition){
        for(IPositionChangeObserver observer : observers)
            observer.positionChanged(this.getPosition(), newPosition, this);
    }

    @Override
    public String toString() {
        return "A";
    }


    public Color toColor() {
        if (energy == 0) return new Color(224, 6, 0);
        if (energy < 0.2 * startEnergy) return new Color(224, 49, 51);
        if (energy < 0.4 * startEnergy) return new Color(224, 66, 76);
        if (energy < 0.6 * startEnergy) return new Color(224, 74, 84);
        if (energy < 0.8 * startEnergy) return new Color(224, 88, 95);
        if (energy < startEnergy) return new Color(224, 105, 112);
        if (energy < 2 * startEnergy) return new Color(224, 123, 128);
        if (energy < 4 * startEnergy) return new Color(224, 148, 153);
        if (energy < 6 * startEnergy) return new Color(224, 168, 171);
        if (energy < 8 * startEnergy) return new Color(224, 188, 193);
        if (energy < 10 * startEnergy) return new Color(224, 199, 201);
        return new Color(255, 255, 255);
    }
}
