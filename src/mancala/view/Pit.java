/**
 * Represents a single pit on the Mancala board, containing a collection of stones (rocks).
 * Handles rendering of the pit outline and its stones with random color variations.
 * 
 * <p>A Pit can represent either:
 * <ul>
 *   <li>A regular pit (one of 12 playable pits)</li>
 *   <li>A Mancala store (one of 2 scoring pits)</li>
 * </ul>
 * 
 * <p>Stones are rendered with random color variations based on a base color,
 * creating visual variety while maintaining style consistency.
 * 
 * @author CS151 Group Project
 * @version 1.0
 */
package mancala.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class Pit {
    private List<Rock> rocks;
    
    /**
     * Constructs a new Pit with the specified initial number of stones.
     * 
     * @param initialRocks The number of stones to initially place in this pit
     */
    public Pit(int initialRocks) {
        rocks = new ArrayList<>();
        addRocks(initialRocks);
    }

    /**
     * Draws the pit outline and all stones inside it.
     * The outline is drawn in the specified color, and stones are rendered
     * with color variations based on the same base color.
     * 
     * @param g2d Graphics context for drawing
     * @param x X position of the pit (top-left corner)
     * @param y Y position of the pit (top-left corner)
     * @param size Diameter of the circular pit
     * @param color Base color for the outline and stone color mixing
     */
    protected void draw(Graphics2D g2d, int x, int y, int size, Color color) {
        g2d.setColor(color);
        g2d.drawOval(x, y, size, size);
        drawRocks(g2d, x, y, size, color);
    }
    
    /**
     * Draws only the rocks/stones without the outline.
     * Used for Neon style where we want a different outline color than the stone base.
     * 
     * @param g2d Graphics context for drawing
     * @param x X position of the pit (top-left corner)
     * @param y Y position of the pit (top-left corner)
     * @param size Diameter of the circular pit
     * @param color Base color for stone color mixing
     */
    protected void drawRocks(Graphics2D g2d, int x, int y, int size, Color color) {
        int rockSize = size * 1 / 3;
        int xCenter = x + size / 2 - rockSize / 2;
        int yCenter = y + size / 2 - rockSize / 2;

        for (Rock rock : rocks) {
            rock.draw(g2d, xCenter, yCenter, rockSize, color);
        }
    }

    /**
     * Adds the specified number of stones to this pit.
     * Each stone is created with random color and position variations.
     * 
     * @param amount The number of stones to add
     */
    public void addRocks(int amount) {
        for (int i = 0; i < amount; ++i) {
            rocks.add(createRock());
        }
    }

    /**
     * Removes all stones from this pit.
     */
    public void clear() {
        rocks.clear();
    }
    
    /**
     * Sets the number of stones in this pit to match the model state.
     * Clears existing rocks and adds the specified amount.
     * 
     * @param stoneCount The number of stones to display
     */
    public void setStoneCount(int stoneCount) {
        clear();
        if (stoneCount > 0) {
            addRocks(stoneCount);
        }
    }
    
    /**
     * Gets the current number of stones (rocks) in this pit.
     * 
     * @return The number of rocks
     */
    public int getStoneCount() {
        return rocks.size();
    }

    /**
     * Creates a new Rock with random color and position variations.
     * Colors are randomized within a range, and positions have slight random offsets
     * to create visual variety while keeping stones centered in the pit.
     * 
     * @return A new Rock instance with randomized properties
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
