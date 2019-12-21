package project.World;

import project.ElementsOnMap.Animal;
import project.ElementsOnMap.Plant;
import project.ElementsOnMap.Vector2d;
import project.Enums.MapDirections;
import project.Enums.MoveDirections;
import project.Interfraces.IPositionChangeObserver;
import project.Interfraces.IWorldMap;
import project.Visualization.MapVisualizer;

import java.util.*;

public class WorldMap implements IWorldMap, IPositionChangeObserver {
    public final Vector2d upperRight;
    public final Vector2d lowerLeft;
    public final Vector2d jungleUpperRight;
    public final Vector2d jungleLowerLeft;
    public final int jungleWidth;
    public final int jungleHeight;
    public final int width;
    public final int height;
    public final double jungleRatio;
    private int plantEnergy;
    private int moveEnergy;
    private int startEnergy;
    private HashMap<Vector2d, Plant> plants = new HashMap<>();
    private HashMap<Vector2d, List<Animal>> animals = new HashMap<>();
    private LinkedList<Animal> animalsList;


    public WorldMap(int width, int height, int plantEnergy, int moveEnergy, int startEnergy, double jungleRatio){
        this.width = width;
        this.height = height;
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width - 1, height - 1);
        this.plantEnergy = plantEnergy;
        this.moveEnergy = (-1) * moveEnergy;
        this.startEnergy = startEnergy;
        this.animalsList = new LinkedList<>();
        if (jungleRatio > 1.0 || jungleRatio <= 0) {
            throw new IllegalArgumentException("jungle ratio cannot be more than 1 or must be more than 0");
        }
        this.jungleRatio=jungleRatio;
        jungleWidth = (int)(width * jungleRatio);
        jungleHeight = (int)(height * jungleRatio);
        jungleLowerLeft = new Vector2d((int)((width - jungleWidth)/2), (int)((height - jungleHeight)/2));
        jungleUpperRight = new Vector2d(lowerLeft.x + jungleWidth, lowerLeft.y + jungleHeight);
    }

    public void moveRandomAllAnimals() {
        List<Animal> currentAnimalsOnMap = getAnimalsList();
        for (int i = 0; i < currentAnimalsOnMap.size(); i++) {
            currentAnimalsOnMap.get(i).turn();
            currentAnimalsOnMap.get(i).move(MoveDirections.FORWARD);
        }
    }
    public void setPlants() {
            Vector2d newPlantJunglePosition=getRandomPositionInJungle();
            if (plants.get(newPlantJunglePosition) == null && canBePlaced(newPlantJunglePosition))
                place(new Plant(newPlantJunglePosition));

            Vector2d newPlantSteppePosition=getRandomPositionInSteppe();
            if (plants.get(newPlantSteppePosition) == null && canBePlaced(newPlantSteppePosition))
                place(new Plant(newPlantSteppePosition));

    }

    public void breeding() {
        for (List<Animal> animalsWantToBreed : animals.values()) {
            if (animalsWantToBreed != null) {
                if (animalsWantToBreed.size() >= 2) {
                    animalsWantToBreed.sort(Comparator.comparing(Animal::getEnergy));
                    Animal firstParent = animalsWantToBreed.get(0);
                    Animal secondParent = animalsWantToBreed.get(1);
                    if (firstParent.isAbleToBreed() && secondParent.isAbleToBreed()){
                        Animal child = secondParent.breed(firstParent, curvedPosition((firstParent.getPosition())));
                        place(child);
                        System.out.println("Let's have a Baby!");
                    }
                }
            }
        }
    }
    public Vector2d placeToBirth(Vector2d position){
        for(MapDirections direction : MapDirections.values()){
            Vector2d birthPos = curvedPosition(direction.toUnitVector().add(position));
            if(canBePlaced(birthPos));
            return birthPos;
        }
        return curvedPosition(position);
    }

    public void dayAfterDay() {
        for (List<Animal> animalsOnMap : animals.values()) {
            if (animalsOnMap != null) {
                if (animalsOnMap.size() > 0) {
                    for (Animal animal : animalsOnMap) {
                        animal.changeEnergy(moveEnergy);
                    }
                }
            }
        }
    }

    public Vector2d curvedPosition(Vector2d position) { //"connects" opposite edges of the map
        int xAfterMove = position.x;
        int yAfterMove = position.y;
        while(xAfterMove < 0) xAfterMove+=this.width;
        while(yAfterMove < 0) yAfterMove+=this.height;
        xAfterMove %= this.width;
        yAfterMove %= this.height;

        return new Vector2d(xAfterMove, yAfterMove);
    }

    public void eating() {
        List<Plant> plantsWillBeEaten = new LinkedList<>();
        for (Plant plant : plants.values()) {
            List<Animal> animalsWantToEat = animals.get(plant.getPosition());
            if (animalsWantToEat != null) {
                if (animalsWantToEat.size() > 0) {
                    for (Animal animal : animalsWantToEat) {
                        animal.changeEnergy(plantEnergy / animalsWantToEat.size());
                        plantsWillBeEaten.add(plant);
                    }
                }
            }
        }

        for (Plant plant : plantsWillBeEaten) {
            plants.remove(plant.getPosition());
        }
    }

    public void removeDeadAnimals() {
        LinkedList<Animal> currentAnimalsOnMap = animalsList;
        for (int i = 0; i < currentAnimalsOnMap.size(); i++) {
            if (currentAnimalsOnMap.get(i).isDead()) {
                removeAnimal(currentAnimalsOnMap.get(i), currentAnimalsOnMap.get(i).getPosition());
                currentAnimalsOnMap.get(i).removeObserver(this);
                animalsList.remove(currentAnimalsOnMap.get(i));
            }
        }
    }

    private boolean canBePlaced(Vector2d position) {
        Vector2d mapPosition = curvedPosition(position);
        if (animals.get(mapPosition) == null)
            return true;
        return animals.get(mapPosition).size() < 3;
    }


    public boolean canMoveTo(Vector2d position) {
        Vector2d mapPosition = curvedPosition(position);
        if (animals.get(mapPosition) == null)
            return true;
        return animals.get(mapPosition).size() < 2;
    }


    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    public boolean addAnimalOnRandomField() {
        boolean a = false;
        while (!a) {
            Vector2d position = curvedPosition(new Vector2d((int) (Math.random() * (this.width)), (int) (Math.random() * (this.height))));
            if (canBePlaced(position)) {
                place(new Animal(position, this.startEnergy, this));
                a=true;
            }
        }
        return a;
    }
    public void place(Animal animal) {
        Vector2d position = curvedPosition(animal.getPosition());
        if (canBePlaced(position)) {
            addAnimal(animal, position);
            animalsList.add(animal);
            animal.addObserver(this);
        }
    }

    public void place(Plant plant) {
        if (!isOccupied(plant.getPosition())) {
            plants.put(plant.getPosition(), plant);
        }
    }
    private void addAnimal(Animal animal, Vector2d position) {
        if (animal == null)
            throw new IllegalArgumentException("Animal does not exist");
        Vector2d mapPosition = curvedPosition(position);
        List<Animal> animalsOnThatPosition = animals.get(mapPosition);
        if (animalsOnThatPosition == null) {
            List<Animal> listOfOneAnimal = new LinkedList<>();
            listOfOneAnimal.add(animal);
            animals.put(mapPosition, listOfOneAnimal);
        }
        else {
            animalsOnThatPosition.add(animal);
        }
    }

    private void removeAnimal(Animal animal, Vector2d position) {
        Vector2d mapPosition = curvedPosition(position);
        List<Animal> animalsOnThisPosition = animals.get(mapPosition);
        if (animalsOnThisPosition  == null || animalsOnThisPosition .size() == 0)
            throw new IllegalArgumentException("Animal" + animal.getPosition() + "does not belong to this place");
        else {
            animalsOnThisPosition .remove(animal);
            if (animalsOnThisPosition .size() == 0)
                animals.remove(mapPosition);
        }
    }


    public Object objectAt(Vector2d position) {
        Vector2d mapPosition = curvedPosition(position);
        List<Animal> animalsOnThisPosition = animals.get(mapPosition);
        if (animalsOnThisPosition == null || animalsOnThisPosition.size() == 0)
            return plants.get(mapPosition);
        else return animalsOnThisPosition.get(0);
    }

    public boolean positionChanged(Vector2d oldPosition2, Vector2d newPosition2, Object entity) {

        Vector2d oldPosition = curvedPosition(oldPosition2);
        Vector2d newPosition = curvedPosition(newPosition2);

        if (canMoveTo(newPosition)) {

            removeAnimal((Animal) entity, oldPosition);
            addAnimal((Animal) entity, newPosition);
            return true;
        }
        return false;
    }

    public void showAllGenotypes() {
        LinkedList<Animal> currentAnimalsOnMap = animalsList;
        System.out.println("Liczba zwierzaczkow: " + currentAnimalsOnMap.size());
        for(int i=0; i<currentAnimalsOnMap.size(); i++) {
            int x = i + 1;
            System.out.println("Genotyp zwierzaka nr." + x);
            for (int j = 0; j < 32; j++)
                System.out.print(currentAnimalsOnMap.get(i).genotype.getGenotype()[j] + " ");
            System.out.println(" ");
        }
    }
    public void showAllEnergies() {
        LinkedList<Animal> currentAnimalsOnMap = animalsList;
        for (int i = 0; i < currentAnimalsOnMap.size(); i++) {
            int j = i + 1;
            System.out.println("Energia zwierzaka nr." + j);
            System.out.println(currentAnimalsOnMap.get(i).getEnergy());
        }
    }

    public void showNumberOfAllPlants() {
        Collection<Plant> currentPlantsOnMap = this.plants.values();
        System.out.println("Aktualna liczba roślin na mapie: " + currentPlantsOnMap.size());
    }

    @Override
    public String toString() {
        MapVisualizer mapVisualization = new MapVisualizer(this);
        return mapVisualization.draw(lowerLeft, upperRight);
    }


    public Collection<Plant> getGrassList() {
        return this.plants.values();
    }


    public LinkedList<Animal> getAnimalsList() {
        return this.animalsList;
    }

    public Vector2d getRandomPositionInJungle(){
        return new Vector2d((int) (Math.random() * (jungleWidth) + jungleLowerLeft.x), (int) (Math.random() * (jungleHeight) + jungleLowerLeft.y));
    }
    public Vector2d getRandomPositionInSteppe() {
        Vector2d steppePosition = new Vector2d((int) (Math.random() * (width) + lowerLeft.x), (int) (Math.random() * (height) + lowerLeft.y));
        while (steppePosition.follows(jungleLowerLeft) && steppePosition.precedes(jungleUpperRight)) {
            steppePosition = new Vector2d((int) (Math.random() * (width) + lowerLeft.x), (int) (Math.random() * (height) + lowerLeft.y));
        }
        return steppePosition;
    }
}

