// Shows two style buttons and gathers stones-per-pit (3 or 4), then signals "start game." 
// Decide the IDs/names of the initial styles and how we collect/validate the stones input.
package mancala.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class StyleSelectPanel extends JPanel {
    private JTextField stonesField;
    private JButton woodButton;
    private JButton neonButton;
    private JLabel statusLabel;
    private ActionListener styleButtonListener;
    
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
    
    public void setStyleButtonListener(ActionListener listener) {
        this.styleButtonListener = listener;
    }
    
    public void disableStonesField() {
        stonesField.setEnabled(false);
        stonesField.setBackground(Color.LIGHT_GRAY);
    }
    
    public int getStonesPerPit() {
        try {
            return Integer.parseInt(stonesField.getText().trim());
        } catch (NumberFormatException e) {
            return 3; // Default
        }
    }
    
    public void enableStonesField() {
        stonesField.setEnabled(true);
        stonesField.setBackground(Color.WHITE);
        stonesField.setText(""); // Clear the previous value
    }
}
