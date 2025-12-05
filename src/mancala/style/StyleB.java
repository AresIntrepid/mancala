// Concrete styles implementing BoardStyle for two distinct looks. 
// Decide the two visual themes (names/palette/shapes) and implement once the interface is locked.

/**
 * Concrete strategy class for the BoardStyle
 * 
 * This provides the neon style for the game
 * 
 * @author CS151 Group Project
 * @version 1.0
 */
package mancala.style;

import java.awt.Color;

public class StyleB implements BoardStyle {
    private static final String NAME = "Neon";
    private static final Color BACKGROUND = new Color(20, 20, 40); // Dark background
    
    // Palette indices for future board rendering:
    private static final int PIT_COLOR_INDEX = 0;      // Color for pit backgrounds
    private static final int STONE_COLOR_INDEX = 1;     // Color for stones/marbles
    private static final int BORDER_COLOR_INDEX = 2;    // Color for borders/outlines
    
    private static final Color[] PALETTE = {
        new Color(0, 255, 255),  // [0] PIT_COLOR - Cyan for pit backgrounds
        new Color(255, 0, 255),  // [1] STONE_COLOR - Magenta for stones/marbles
        new Color(255, 255, 0)   // [2] BORDER_COLOR - Yellow for borders/outlines
    };
    
    @Override
    public String getName() {
        return NAME;
    }
    
    @Override
    public Color getBackgroundColor() {
        return BACKGROUND;
    }
    
    @Override
    public Color[] getPalette() {
        return PALETTE.clone();
    }
    
    // Convenience methods for accessing specific palette colors
    /**
     * Returns the color for pit backgrounds
     */
    public Color getPitColor() {
        return PALETTE[PIT_COLOR_INDEX];
    }
    
    /**
     * Returns the color for stones/marbles
     */
    public Color getStoneColor() {
        return PALETTE[STONE_COLOR_INDEX];
    }
    
    /**
     * Returns the color for borders and outlines
     */
    public Color getBorderColor() {
        return PALETTE[BORDER_COLOR_INDEX];
    }
    
    @Override
    public int getBoardWidth() {
        return 800; // Placeholder width
    }
    
    @Override
    public int getBoardHeight() {
        return 400; // Placeholder height
    }
}
