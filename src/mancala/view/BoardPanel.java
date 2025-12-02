/**
 * Panel that renders the Mancala game board, including pits, Mancalas, stones, and labels.
 * Handles mouse interactions (clicks and hover) and maps them to pit indices.
 * 
 * <p>Responsibilities:
 * <ul>
 *   <li>Renders the game board with pits, Mancalas, and stones</li>
 *   <li>Handles mouse click events and notifies the controller</li>
 *   <li>Provides visual feedback on hover (thicker borders)</li>
 *   <li>Updates display based on model state changes</li>
 *   <li>Supports different visual styles (Wood, Neon)</li>
 * </ul>
 * 
 * <p>Layout:
 * <ul>
 *   <li>Mancalas are positioned outside the board border (left and right)</li>
 *   <li>Regular pits are arranged in two rows inside a black rounded rectangle border</li>
 *   <li>Bottom row: Player A (indices 0-5)</li>
 *   <li>Top row: Player B (indices 7-12)</li>
 * </ul>
 * 
 * @author CS151 Group Project
 * @version 1.0
 */
package mancala.view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import mancala.style.BoardStyle;

public class BoardPanel extends JPanel {
    private BoardStyle style;
    private int pitsPerSide;
    private Pit[][] pits; // Regular pits: pits[0] = Player A (bottom), pits[1] = Player B (top)
    private Pit playerAMancala; // Left side (index 6)
    private Pit playerBMancala; // Right side (index 13)
    
    // Model state (set via setBoardState)
    private int[] boardState;
    private int currentPlayer;
    private boolean gameOver;
    
    // Hit-testing and interaction
    private PitClickListener pitClickListener;
    private int hoveredPitIndex = -1; // -1 means no pit hovered
    
    // Layout calculations (cached for hit-testing)
    private int mancalaWidth;
    private int mancalaHeight;
    private int pitSize;
    private int pitWidth;
    private int pitHeight;
    private int centerAreaX;
    private int centerAreaY;
    private int centerAreaWidth;
    private int centerAreaHeight;
    private int leftMancalaX;
    private int rightMancalaX;
    private int mancalaY;
    private int bottomRowY;
    private int topRowY;
    private int[] pitXPositions; // X positions for each pit in the row
    // Board rectangle (the black border area)
    private int boardRectX;
    private int boardRectY;
    private int boardRectWidth;
    private int boardRectHeight;
    
    /**
     * Constructs a new BoardPanel with default settings.
     * Initializes pits, sets up mouse listeners, and prepares the panel for rendering.
     */
    public BoardPanel() {
        pitsPerSide = 6;
        initializePits();
        setPreferredSize(new Dimension(800, 400));
        setBackground(Color.LIGHT_GRAY);
        
        // Initialize state
        boardState = null;
        currentPlayer = 1;
        gameOver = false;
        
        // Initialize board rectangle to default values
        boardRectX = 100;
        boardRectY = 100;
        boardRectWidth = 600;
        boardRectHeight = 200;
        
        // Set up mouse listener
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });
        
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                handleMouseMove(e.getX(), e.getY());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                hoveredPitIndex = -1;
                repaint();
            }
        });
    }
    
    /**
     * Sets the visual style for the board panel.
     * Updates the background color and triggers a repaint.
     * 
     * @param style The BoardStyle to apply (e.g., StyleA for "Wood", StyleB for "Neon")
     */
    public void setStyle(BoardStyle style) {
        this.style = style;
        if (style != null) {
            setBackground(style.getBackgroundColor());
        }
        repaint();
    }
    
    /**
     * Gets the currently active board style.
     * 
     * @return The current BoardStyle, or null if no style has been set
     */
    public BoardStyle getStyle() {
        return style;
    }
    
    /**
     * Sets the board state from the model.
     * Called by the controller whenever the model state changes.
     * 
     * @param boardState Array of 14 integers representing the board state
     * @param currentPlayer The current player (1 or 2)
     * @param gameOver Whether the game is over
     */
    public void setBoardState(int[] boardState, int currentPlayer, boolean gameOver) {
        this.boardState = boardState;
        this.currentPlayer = currentPlayer;
        this.gameOver = gameOver;
        
        if (boardState != null && boardState.length == 14) {
            // Update regular pits
            // Player A (bottom row): indices 0-5
            for (int j = 0; j < pitsPerSide; j++) {
                pits[0][j].setStoneCount(boardState[j]);
            }
            // Player B (top row): indices 7-12
            for (int j = 0; j < pitsPerSide; j++) {
                pits[1][j].setStoneCount(boardState[7 + j]);
            }
            
            // Update Mancalas
            playerAMancala.setStoneCount(boardState[6]);  // Index 6
            playerBMancala.setStoneCount(boardState[13]); // Index 13
        }
        
        repaint();
    }
    
    /**
     * Sets the listener for pit click events.
     * 
     * @param listener The PitClickListener to notify when a pit is clicked
     */
    public void setPitClickListener(PitClickListener listener) {
        this.pitClickListener = listener;
    }
    
    /**
     * Maps mouse coordinates to a pit index.
     * Returns -1 if click is not on a valid pit.
     * 
     * @param x Mouse X coordinate
     * @param y Mouse Y coordinate
     * @return Pit index (0-13) or -1 if not on a pit
     */
    private int getPitIndexAt(int x, int y) {
        // Ensure layout is calculated (pitXPositions must be initialized)
        if (pitXPositions == null || pitXPositions.length != pitsPerSide) {
            return -1;
        }
        
        // Mancalas are not clickable, so we ignore clicks on them
        // Check if click is in the center area (regular pits)
        if (x >= centerAreaX && x < centerAreaX + centerAreaWidth &&
            y >= centerAreaY && y < centerAreaY + centerAreaHeight) {
            
            // Use actual pit size (circle diameter) for hit-testing - matches drawPit()
            int actualPitSize = Math.min(pitWidth, pitHeight);
            // Make radius smaller to prevent overlap - use 85% to create buffer
            int radius = (int)((actualPitSize / 2) * 0.85);
            int radiusSquared = radius * radius;
            
            int bestPitIndex = -1;
            int minDistanceSquared = Integer.MAX_VALUE;
            
            // Check bottom row (Player A, indices 0-5)
            if (y >= bottomRowY && y < bottomRowY + pitHeight) {
                for (int j = 0; j < pitsPerSide; j++) {
                    // Calculate center exactly as drawPit() does
                    int pitX = pitXPositions[j];
                    int centerX = pitX + pitWidth / 2;
                    int centerY = bottomRowY + pitHeight / 2;
                    
                    // Check if point is within the circular pit area
                    int dx = x - centerX;
                    int dy = y - centerY;
                    int distanceSquared = dx * dx + dy * dy;
                    
                    // Only consider pits where mouse is actually inside the circle
                    if (distanceSquared <= radiusSquared) {
                        // Find the closest pit (in case multiple match)
                        if (distanceSquared < minDistanceSquared) {
                            minDistanceSquared = distanceSquared;
                            bestPitIndex = j; // Index 0-5
                        }
                    }
                }
                if (bestPitIndex >= 0) {
                    return bestPitIndex;
                }
            }
            
            // Check top row (Player B, indices 7-12)
            if (y >= topRowY && y < topRowY + pitHeight) {
                bestPitIndex = -1;
                minDistanceSquared = Integer.MAX_VALUE;
                
                for (int j = 0; j < pitsPerSide; j++) {
                    // Calculate center exactly as drawPit() does
                    int pitX = pitXPositions[j];
                    int centerX = pitX + pitWidth / 2;
                    int centerY = topRowY + pitHeight / 2;
                    
                    // Check if point is within the circular pit area
                    int dx = x - centerX;
                    int dy = y - centerY;
                    int distanceSquared = dx * dx + dy * dy;
                    
                    // Only consider pits where mouse is actually inside the circle
                    if (distanceSquared <= radiusSquared) {
                        // Find the closest pit (in case multiple match)
                        if (distanceSquared < minDistanceSquared) {
                            minDistanceSquared = distanceSquared;
                            bestPitIndex = 7 + j; // Index 7-12
                        }
                    }
                }
                if (bestPitIndex >= 0) {
                    return bestPitIndex;
                }
            }
        }
        
        return -1; // Not on a clickable pit
    }
    
    /**
     * Handles mouse click events on the board.
     * Determines which pit was clicked and notifies the listener if valid.
     * 
     * @param x Mouse X coordinate
     * @param y Mouse Y coordinate
     */
    private void handleMouseClick(int x, int y) {
        if (gameOver || pitClickListener == null) {
            return;
        }
        
        int pitIndex = getPitIndexAt(x, y);
        if (pitIndex >= 0) {
            pitClickListener.onPitClicked(pitIndex);
        }
    }
    
    /**
     * Handles mouse movement events for hover highlighting.
     * Updates the hovered pit index and repaints if it changed.
     * 
     * @param x Mouse X coordinate
     * @param y Mouse Y coordinate
     */
    private void handleMouseMove(int x, int y) {
        int newHoveredPit = getPitIndexAt(x, y);
        if (newHoveredPit != hoveredPitIndex) {
            hoveredPitIndex = newHoveredPit;
            repaint();
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // Don't draw if panel hasn't been sized yet
        if (width <= 0 || height <= 0) {
            return;
        }
        
        // If no style is set, use default colors (shouldn't happen in normal flow)
        Color pitColor, stoneColor, borderColor;
        boolean isNeonStyle = false;
        Color stoneBaseColor; // Color used as base for stone rendering
        if (style == null) {
            pitColor = Color.LIGHT_GRAY;
            stoneColor = Color.DARK_GRAY;
            borderColor = Color.BLACK;
            stoneBaseColor = Color.LIGHT_GRAY;
        } else {
            // For Neon style, use background color for pit fills instead of pit color
            isNeonStyle = "Neon".equals(style.getName());
            if (isNeonStyle) {
                pitColor = style.getBackgroundColor(); // Use background color for Neon style pit fills
                stoneBaseColor = style.getPitColor(); // Use bright cyan for stone base (lighter/more visible)
            } else {
                pitColor = style.getPitColor();
                stoneBaseColor = pitColor; // Use pit color for stone base in other styles
            }
            stoneColor = style.getStoneColor();
            borderColor = style.getBorderColor();
        }
        
        // Calculate layout dimensions
        calculateLayout(width, height);
        
        // Draw the board area border (rounded rectangle) - matches original design
        g2d.setColor(getBackground().darker());
        g2d.fillRoundRect(boardRectX, boardRectY, boardRectWidth, boardRectHeight, 20, 20);
        
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(boardRectX, boardRectY, boardRectWidth, boardRectHeight, 20, 20);
        
        // Draw Mancala stores on left and right (OUTSIDE the board border)
        // Left Mancala (Player A, index 6) - to the left of the board border
        drawMancala(g2d, leftMancalaX, mancalaY, mancalaWidth, mancalaHeight, 
                   playerAMancala, pitColor, stoneColor, borderColor, "A", 
                   6 == hoveredPitIndex, isNeonStyle, stoneBaseColor);
        
        // Right Mancala (Player B, index 13) - to the right of the board border
        drawMancala(g2d, rightMancalaX, mancalaY, mancalaWidth, mancalaHeight, 
                   playerBMancala, pitColor, stoneColor, borderColor, "B", 
                   13 == hoveredPitIndex, isNeonStyle, stoneBaseColor);
        
        // Draw regular pits INSIDE the board area (within the black border)
        // pitXPositions is already calculated in calculateLayout() above
        
        // Bottom row: Player A (indices 0-5)
        for (int j = 0; j < pitsPerSide; j++) {
            int pitIndex = j;
            int x = pitXPositions[j];
            int y = bottomRowY;
            boolean isCurrentPlayer = (currentPlayer == 1);
            boolean isHovered = (pitIndex == hoveredPitIndex);
            drawPit(g2d, x, y, pitWidth, pitHeight, pits[0][j], pitColor, stoneColor, 
                   borderColor, "A" + (j + 1), isCurrentPlayer, isHovered, false, isNeonStyle, stoneBaseColor);
        }
        
        // Top row: Player B (indices 7-12)
        for (int j = 0; j < pitsPerSide; j++) {
            int pitIndex = 7 + j;
            int x = pitXPositions[j];
            int y = topRowY;
            boolean isCurrentPlayer = (currentPlayer == 2);
            boolean isHovered = (pitIndex == hoveredPitIndex);
            drawPit(g2d, x, y, pitWidth, pitHeight, pits[1][j], pitColor, stoneColor, 
                   borderColor, "B" + (j + 1), isCurrentPlayer, isHovered, true, isNeonStyle, stoneBaseColor);
        }
    }
    
    /**
     * Calculates and caches layout dimensions for rendering and hit-testing.
     * Called whenever the panel is resized or needs to recalculate positions.
     * 
     * <p>Calculates:
     * <ul>
     *   <li>Board rectangle (black border area) position and size</li>
     *   <li>Mancala positions (outside board border, left and right)</li>
     *   <li>Pit positions within the board rectangle</li>
     *   <li>Row positions for top and bottom pit rows</li>
     * </ul>
     * 
     * @param width Panel width
     * @param height Panel height
     */
    private void calculateLayout(int width, int height) {
        // Ensure minimum dimensions
        if (width < 100 || height < 100) {
            width = 800;
            height = 400;
        }
        
        // Calculate board rectangle (rounded rectangle border) - matches original
        boardRectX = width / 8;
        boardRectY = height / 4;
        boardRectWidth = width * 3 / 4;
        boardRectHeight = height / 2;
        
        // Mancala dimensions: 2x width, 1.5x height of regular pit
        int basePitSize = Math.max(30, Math.min(width / 10, height / 6));
        pitSize = basePitSize;
        pitWidth = basePitSize;
        pitHeight = basePitSize;
        
        mancalaWidth = pitWidth * 2;
        mancalaHeight = (int)(pitHeight * 1.5);
        
        // Position Mancalas OUTSIDE the board rectangle but within window bounds
        // Left Mancala: to the left of the board border
        leftMancalaX = Math.max(10, boardRectX - mancalaWidth - 15); // 15px spacing, but at least 10px from left edge
        
        // Right Mancala: to the right of the board border (move it more to the right)
        rightMancalaX = Math.min(width - mancalaWidth - 10, boardRectX + boardRectWidth + 15); // 15px spacing, but at least 10px from right edge
        
        // Center area for regular pits (INSIDE the board rectangle)
        int padding = Math.max(5, width / 64);
        int pitSpacing = Math.max(5, width / 64);
        
        centerAreaX = boardRectX + padding;
        centerAreaWidth = boardRectWidth - padding * 2;
        centerAreaHeight = boardRectHeight - padding * 2;
        centerAreaY = boardRectY + padding;
        
        // Calculate pit positions in center area
        int totalPitWidth = pitWidth * pitsPerSide;
        int totalSpacing = pitSpacing * (pitsPerSide - 1);
        int availableWidth = centerAreaWidth - totalPitWidth - totalSpacing;
        int xStart = centerAreaX + Math.max(0, availableWidth / 2);
        
        pitXPositions = new int[pitsPerSide];
        for (int j = 0; j < pitsPerSide; j++) {
            pitXPositions[j] = xStart + j * (pitWidth + pitSpacing);
        }
        
        // Calculate row Y positions
        int totalRowHeight = pitHeight * 2 + centerAreaHeight / 8; // Space between rows
        int rowStartY = centerAreaY + (centerAreaHeight - totalRowHeight) / 2;
        topRowY = rowStartY;
        bottomRowY = rowStartY + pitHeight + centerAreaHeight / 8;
        
        // Mancala Y position (centered vertically, matching board rectangle)
        mancalaY = boardRectY + (boardRectHeight - mancalaHeight) / 2;
    }
    
    /**
     * Draws a single pit with its stones, border, and label.
     * Handles special rendering for Neon style (dark blue outline, bright cyan stones).
     * 
     * @param g2d Graphics context for drawing
     * @param x X position of the pit rectangle
     * @param y Y position of the pit rectangle
     * @param width Width of the pit rectangle
     * @param height Height of the pit rectangle
     * @param pit The Pit object containing stones to draw
     * @param pitColor Color for pit fill
     * @param stoneColor Color for stones (unused, kept for compatibility)
     * @param borderColor Color for borders (unused, kept for compatibility)
     * @param label Text label for the pit (e.g., "A1", "B3")
     * @param isCurrentPlayer Whether this pit belongs to the current player
     * @param isHovered Whether the mouse is hovering over this pit
     * @param isTopRow Whether this pit is in the top row (Player B)
     * @param isNeonStyle Whether Neon style is active
     * @param stoneBaseColor Base color for stones (bright cyan for Neon, pitColor for others)
     */
    private void drawPit(Graphics2D g2d, int x, int y, int width, int height, Pit pit,
    		Color pitColor, Color stoneColor, Color borderColor, 
            String label, boolean isCurrentPlayer, boolean isHovered, 
            boolean isTopRow, boolean isNeonStyle, Color stoneBaseColor) {
        int size = Math.min(width, height);
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        int drawX = centerX - size / 2;
        int drawY = centerY - size / 2;
        
        // Highlight current player's pits slightly
        Color fillColor = pitColor;
        if (isCurrentPlayer && !gameOver) {
            fillColor = new Color(
                Math.min(255, pitColor.getRed() + 20),
                Math.min(255, pitColor.getGreen() + 20),
                Math.min(255, pitColor.getBlue() + 20)
            );
        }
        
        // Draw pit background (filled)
        g2d.setColor(fillColor);
        g2d.fillOval(drawX, drawY, size, size);
        
        // Save the current stroke and clip to prevent bleeding into adjacent pits
        Stroke originalStroke = g2d.getStroke();
        Shape originalClip = g2d.getClip();
        
        // Determine border width for clipping (need extra space for border stroke)
        int borderWidth = isHovered && !gameOver ? 3 : 1;
        int clipPadding = borderWidth; // Extra padding to accommodate border stroke
        
        // Clip to slightly larger than circle bounds to accommodate border stroke
        // This prevents bleeding while allowing full border visibility
        java.awt.geom.Ellipse2D clipCircle = new java.awt.geom.Ellipse2D.Double(
            drawX - clipPadding, drawY - clipPadding, 
            size + clipPadding * 2, size + clipPadding * 2);
        g2d.setClip(clipCircle);
        
        // Draw stones using Pit's draw method
        // Note: Pit.draw() draws the pit outline first, then draws rocks inside
        // For Neon style: draw outline in dark blue first, then draw stones with bright cyan
        // For other styles: use pitColor for both outline and stones
        if (isNeonStyle) {
            // Draw outline in dark blue to match pit fill (not bright neon)
            g2d.setColor(pitColor);
            g2d.setStroke(new BasicStroke(1));
            g2d.drawOval(drawX, drawY, size, size);
            // Draw rocks with bright cyan base for visibility
            pit.drawRocks(g2d, drawX, drawY, size, stoneBaseColor);
        } else {
            // For non-Neon styles, use standard drawing
            pit.draw(g2d, drawX, drawY, size, pitColor);
        }
        
        // Draw border OVER the outline from Pit.draw() (thicker on hover for valid moves)
        g2d.setStroke(new BasicStroke(borderWidth));
        // Use black border when hovered for clear visibility
        // For Neon style, use black for non-hovered borders too (not bright neon colors)
        // For other styles, use pitColor to match original
        g2d.setColor(isHovered && !gameOver ? Color.BLACK : (isNeonStyle ? Color.BLACK : pitColor));
        g2d.drawOval(drawX, drawY, size, size);
        
        // Restore original clip and stroke
        g2d.setClip(originalClip);
        g2d.setStroke(originalStroke);
        
        // Draw label - above for top row, below for bottom row
        g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        FontMetrics fm = g2d.getFontMetrics();
        int labelX = centerX - fm.stringWidth(label) / 2;
        int labelY;
        if (isTopRow) {
            labelY = y - 2; // Above pit for top row
        } else {
            labelY = y + height + fm.getAscent() + 2; // Below pit for bottom row
        }
        // Use white for Neon style, black otherwise
        g2d.setColor(isNeonStyle ? Color.WHITE : Color.BLACK);
        g2d.drawString(label, labelX, labelY);
        
     // Draw stone count in the center of the pit
        g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        g2d.setColor(Color.WHITE);
        String countText = String.valueOf(pit.getStoneCount());
        FontMetrics countFm = g2d.getFontMetrics();
        int countX = centerX - countFm.stringWidth(countText) / 2;
        int countY = centerY + countFm.getAscent() / 2;
        g2d.drawString(countText, countX, countY);
    }
    
    /**
     * Draws a Mancala store (large pit on the side) with its stones, border, and label.
     * Handles special rendering for Neon style (dark blue outline, bright cyan stones).
     * 
     * @param g2d Graphics context for drawing
     * @param x X position of the Mancala rectangle
     * @param y Y position of the Mancala rectangle
     * @param width Width of the Mancala rectangle
     * @param height Height of the Mancala rectangle
     * @param mancala The Pit object containing stones to draw
     * @param pitColor Color for Mancala fill
     * @param stoneColor Color for stones (unused, kept for compatibility)
     * @param borderColor Color for borders (unused, kept for compatibility)
     * @param label Text label for the Mancala ("A" or "B")
     * @param isHovered Whether the mouse is hovering over this Mancala
     * @param isNeonStyle Whether Neon style is active
     * @param stoneBaseColor Base color for stones (bright cyan for Neon, pitColor for others)
     */
    private void drawMancala(Graphics2D g2d, int x, int y, int width, int height, Pit mancala,
                             Color pitColor, Color stoneColor, Color borderColor, 
                             String label, boolean isHovered, boolean isNeonStyle, Color stoneBaseColor) {
        int size = Math.min(width, height);
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        int drawX = centerX - size / 2;
        int drawY = centerY - size / 2;
        
        // Draw Mancala background - same color as regular pits
        g2d.setColor(pitColor);
        g2d.fillOval(drawX, drawY, size, size);
        
        // Draw stones using Pit's draw method
        // For Neon style: draw outline in dark blue, then draw rocks with bright cyan
        // For other styles: use standard drawing
        if (isNeonStyle) {
            // Draw outline in dark blue to match pit fill
            g2d.setColor(pitColor);
            g2d.setStroke(new BasicStroke(1));
            g2d.drawOval(drawX, drawY, size, size);
            // Draw rocks with bright cyan base for visibility
            mancala.drawRocks(g2d, drawX, drawY, size, stoneBaseColor);
        } else {
            // For non-Neon styles, use standard drawing
            mancala.draw(g2d, drawX, drawY, size, pitColor);
        }
        
        // Draw border OVER the outline from Pit.draw()
        g2d.setStroke(new BasicStroke(2));
        // Use black border for Neon style, black for others too (consistent)
        g2d.setColor(Color.BLACK);
        g2d.drawOval(drawX, drawY, size, size);
        
        // Draw label below Mancala
        g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        FontMetrics fm = g2d.getFontMetrics();
        int labelX = centerX - fm.stringWidth(label) / 2;
        int labelY = y + height + fm.getAscent() + 2;
        // Use white for Neon style, black otherwise
        g2d.setColor(isNeonStyle ? Color.WHITE : Color.BLACK);
        g2d.drawString(label, labelX, labelY);
        
     // Draw stone count in the center of the Mancala
        g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        g2d.setColor(Color.WHITE);
        String countText = String.valueOf(mancala.getStoneCount());
        FontMetrics countFm = g2d.getFontMetrics();
        int countX = centerX - countFm.stringWidth(countText) / 2;
        int countY = centerY + countFm.getAscent() / 2;
        g2d.drawString(countText, countX, countY);
    }

    /**
     * Initializes all pit objects (regular pits and Mancalas).
     * Creates Pit instances for both players' pits and their Mancala stores.
     * All pits start with 0 stones and will be updated via setBoardState().
     */
    private void initializePits() {
        pits = new Pit[2][pitsPerSide];
        
        // Initialize regular pits
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < pitsPerSide; j++) {
                pits[i][j] = new Pit(0); // Start with 0, will be set by setBoardState
            }
        }
        
        // Initialize Mancalas
        playerAMancala = new Pit(0);
        playerBMancala = new Pit(0);
    }
}
