package project.World;

import project.JsonConfig.JsonParser;
import project.Visualization.RenderPanel;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

import static java.lang.System.out;

public class Simulation {
    public WorldMap map;
    public int currentDay;
    public int totalDays;
    public JFrame frame;
    private JsonParser config;


    public Simulation(int totalDays){
        this.config = new JsonParser();
        WorldMap map = new WorldMap(config.getWidth(), config.getHeight(), config.getPlantEnergy(), config.getMoveEnergy(), config.getStartEnergy(), config.getJungleRatio());
        this.map = map;
        for(int i=0; i < config.getNumberOfBeginningAnimals(); i++)
            map.addAnimalOnRandomField();
        frame = new JFrame("Evolution Simulation");
        this.totalDays = totalDays;
        currentDay = 1;
    }


    public void oneDaySimulation(){
        map.removeDeadAnimals();
        map.moveRandomAllAnimals();
        map.eating();
        map.breeding();
        map.dayAfterDay();
        map.setPlants();
        map.showAllGenotypes();
        map.showAllEnergies();
        map.showNumberOfAllPlants();
    }

    public void simulate() throws InterruptedException{
        out.println("Day: " + currentDay);
        out.println(map.toString());
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        RenderPanel renderPanel = new RenderPanel(map, frame);

        frame.setVisible(true);
        frame.setLayout(new GridLayout());
        frame.add(renderPanel);
        TimeUnit.SECONDS.sleep(1);

        for(int i = 1; i < totalDays; i++){
            this.oneDaySimulation();
            out.println("Day: " + currentDay++);
            out.println(map.toString());
            renderPanel.repaint();
            TimeUnit.MILLISECONDS.sleep(500);
        }
    }
}
