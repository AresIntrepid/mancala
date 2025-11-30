package mancala.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class Pit {
    private List<Rock> rocks;
    
    public Pit(int initialRocks) {
        rocks = new ArrayList<>();
        addRocks(initialRocks);
    }

    protected void draw(Graphics2D g2d, int x, int y, int size, Color color) {

        g2d.setColor(color);
        g2d.drawOval(x, y, size, size);

        int rockSize = size * 1 / 3;
        int xCenter = x + size / 2 - rockSize / 2;
        int yCenter = y + size / 2 - rockSize / 2;

        for (Rock rock : rocks) {
            rock.draw(g2d, xCenter, yCenter, rockSize, color);
        }
    }

    public void addRocks(int amount) {
        for (int i = 0; i < amount; ++i) {
            rocks.add(createRock());
        }
    }

    public void clear() {
        rocks.clear();
    }

    /**
     * Get rocks that are random in color with some randomness in position
     */
    private Rock createRock() {
        double r = Math.random() * 2 - 1;
        double g = Math.random() * 2 - 1;
        double b = Math.random() * 2 - 1;

        double RANDOMNESS_MAX = 0.5;

        double dx = Math.random() * 2 - 1;
        double dy = Math.random() * 2 - 1;

        dx = Math.clamp(dx, -RANDOMNESS_MAX, RANDOMNESS_MAX);
        dy = Math.clamp(dy, -RANDOMNESS_MAX, RANDOMNESS_MAX);
        
        return new Rock(dx, dy, r, g, b);
    }
}
