package mancala.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Panel for selecting game style and initial stone count.
 * Displays style buttons (Wood/Neon) and a text field for stones per pit (3 or 4).
 * 
 * @author CS151 Group Project
 * @version 1.0
 */
public class StyleSelectPanel extends JPanel {
    private JTextField stonesField;
    private JButton woodButton;
    private JButton neonButton;
    private JLabel statusLabel;
    private ActionListener styleButtonListener;
    
    /**
     * Constructs a new StyleSelectPanel with style buttons and stones input field.
     */
    public StyleSelectPanel() {
        setLayout(new BorderLayout());
        
        // Bottom bar
        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // WEST: Undo (disabled/hidden)
        JButton undoButton = new JButton("Undo");
        undoButton.setEnabled(false);
        undoButton.setVisible(false); // Hidden as per spec
        bottomBar.add(undoButton, BorderLayout.WEST);
        
        // CENTER: Stones input
        JPanel centerPanel = new JPanel(new FlowLayout());
        centerPanel.add(new JLabel("Stones per pit:"));
        stonesField = new JTextField(5);
        stonesField.setToolTipText("Enter 3 or 4");
        centerPanel.add(stonesField);
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        centerPanel.add(statusLabel);
        bottomBar.add(centerPanel, BorderLayout.CENTER);
        
        // EAST: Style buttons
        JPanel styleButtonsPanel = new JPanel(new FlowLayout());
        woodButton = new JButton("Wood");
        neonButton = new JButton("Neon");
        styleButtonsPanel.add(woodButton);
        styleButtonsPanel.add(neonButton);
        bottomBar.add(styleButtonsPanel, BorderLayout.EAST);
        
        add(bottomBar, BorderLayout.SOUTH);
        
        // Add action listeners to style buttons
        woodButton.addActionListener(e -> handleStyleSelection("Wood"));
        neonButton.addActionListener(e -> handleStyleSelection("Neon"));
    }
    
    /**
     * Handles style button clicks and validates stones input.
     * Notifies the listener with format "styleName:stones" if valid.
     * 
     * @param styleName The name of the selected style ("Wood" or "Neon")
     */
    private void handleStyleSelection(String styleName) {
        String stonesText = stonesField.getText().trim();
        int stones;
        
        try {
            stones = Integer.parseInt(stonesText);
            if (stones != 3 && stones != 4) {
                statusLabel.setText("Please enter 3 or 4");
                return;
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Please enter 3 or 4");
            return;
        }
        
        // Clear status and notify listener with format "styleName:stones"
        statusLabel.setText(" ");
        if (styleButtonListener != null) {
            styleButtonListener.actionPerformed(
                new java.awt.event.ActionEvent(this, 
                    java.awt.event.ActionEvent.ACTION_PERFORMED, 
                    styleName + ":" + stones)
            );
        }
    }
    
    /**
     * Sets the action listener for style button clicks.
     * 
     * @param listener The ActionListener to notify when a style is selected
     */
    public void setStyleButtonListener(ActionListener listener) {
        this.styleButtonListener = listener;
    }
    
    /**
     * Disables the stones input field (after game starts).
     */
    public void disableStonesField() {
        stonesField.setEnabled(false);
        stonesField.setBackground(Color.LIGHT_GRAY);
    }
    
    /**
     * Gets the number of stones per pit from the input field.
     * 
     * @return The number of stones (3 or 4), or 3 as default if invalid
     */
    public int getStonesPerPit() {
        try {
            return Integer.parseInt(stonesField.getText().trim());
        } catch (NumberFormatException e) {
            return 3; // Default
        }
    }
    
    /**
     * Enables the stones input field and clears its value (for new game).
     */
    public void enableStonesField() {
        stonesField.setEnabled(true);
        stonesField.setBackground(Color.WHITE);
        stonesField.setText(""); // Clear the previous value
    }
}
