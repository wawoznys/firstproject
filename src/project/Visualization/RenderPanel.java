package project.Visualization;

import project.ElementsOnMap.Animal;
import project.ElementsOnMap.Plant;
import project.World.WorldMap;

import javax.swing.*;
import java.awt.*;

public class RenderPanel extends JPanel {
    public WorldMap map;
    public JFrame frame;
    private int width;
    private int height;
    private int heightScale;
    private int widthScale;

    public RenderPanel(WorldMap map, JFrame frame) {
        this.map = map;
        this.frame = frame;
    }


    @Override
    protected void paintComponent(Graphics gg){
        Graphics2D g = (Graphics2D) gg;
        super.paintComponent(g);
        this.width = this.getWidth();
        this.height = this.getHeight();
        this.widthScale = width / map.width;
        this.heightScale = height / map.height;
        this.setSize(frame.getWidth(), frame.getHeight() - 38);
        g.setColor(new Color(210, 224, 103));
        g.fillRect(0, 0, this.width, this.height);

        g.setColor(new Color(132, 160, 0));
        g.fillRect(map.jungleLowerLeft.x * widthScale,
                map.jungleLowerLeft.y * heightScale,
                map.jungleWidth * widthScale,
                map.jungleHeight * heightScale);

        for (Plant grass : map.getGrassList()) {
            g.setColor(grass.toColor());
            int y = map.curvedPosition(grass.getPosition()).y * heightScale;
            int x = map.curvedPosition(grass.getPosition()).x * widthScale;
            g.fillRect(x, y, widthScale, heightScale);
        }

        for (Animal a : map.getAnimalsList()) {
            g.setColor(a.toColor());
            int y = map.curvedPosition(a.getPosition()).y * heightScale;
            int x = map.curvedPosition(a.getPosition()).x * widthScale;
            g.fillOval(x, y, widthScale, heightScale);
        }
    }
}
