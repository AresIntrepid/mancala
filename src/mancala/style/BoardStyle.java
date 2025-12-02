/**
 * Strategy interface for board visual styles.
 * Defines the contract for different visual themes (e.g., "Wood", "Neon").
 * Implementations provide colors, fonts, sizes, and other visual properties
 * that the view uses for rendering the game board.
 * 
 * <p>This interface follows the Strategy pattern, allowing the view to
 * switch between different visual styles without modifying rendering code.
 * 
 * @author CS151 Group Project
 * @version 1.0
 */
package mancala.style;

import java.awt.Color;

public interface BoardStyle {
    /**
     * Returns the name of this style (e.g., "Wood", "Neon")
     */
    String getName();
    
    /**
     * Returns the background color for the board
     */
    Color getBackgroundColor();
    
    /**
     * Returns the primary color palette (for future use)
     */
    Color[] getPalette();
    
    /**
     * Returns board metrics (width, height) for layout calculations
     */
    int getBoardWidth();
    int getBoardHeight();
    
    /**
     * Returns the color for pit backgrounds
     */
    Color getPitColor();
    
    /**
     * Returns the color for stones/marbles
     */
    Color getStoneColor();
    
    /**
     * Returns the color for borders and outlines
     */
    Color getBorderColor();
}
