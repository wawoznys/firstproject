package project.JsonConfig;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;


public class JsonParser {
    private int width;
    private int height;
    private int moveEnergy;
    private int plantEnergy;
    private int startEnergy;
    private int numberOfBeginningAnimals;
    private double jungleRatio;

    public JsonParser(){
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader("C:\\Users\\użytkownik\\IdeaProjects\\project2\\src\\project\\JsonConfig\\parameters.json")) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            System.out.println(jsonObject);
            this.width = ((Number) jsonObject.get("width")).intValue();
            this.height = ((Number) jsonObject.get("height")).intValue();
            this.moveEnergy = ((Number) jsonObject.get("moveEnergy")).intValue();
            this.plantEnergy = ((Number) jsonObject.get("plantEnergy")).intValue();
            this.startEnergy = ((Number) jsonObject.get("startEnergy")).intValue();
            this.numberOfBeginningAnimals = ((Number) jsonObject.get("numberOfBeginningAnimals")).intValue();
            this.jungleRatio = ((Number) jsonObject.get("jungleRatio")).doubleValue();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMoveEnergy() {
        return moveEnergy;
    }

    public int getPlantEnergy() {
        return plantEnergy;
    }

    public int getStartEnergy() {
        return startEnergy;
    }

    public double getJungleRatio() {
        return jungleRatio;
    }

    public int getNumberOfBeginningAnimals() {
        return numberOfBeginningAnimals;
    }
}
