/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
	public void run() {
		// NOTE : recall that the 'final' keyword notes inmutability
		// even for local variables.

		// Frame in which components exist
		final JFrame frame = new JFrame("Space Invaders II");
		frame.setLocation(300, 300);
		// Create a start button
		final JButton start = new JButton("Start");
		// Make a menu panel
		final JPanel menu = new JPanel();
		// Add the menu to the frame and add the start button to the menu
		frame.add(menu);
		menu.add(start);
		// Create an image for the opening screen background
		ImageIcon image = new ImageIcon("Open.jpg");
		final JLabel imageLabel = new JLabel(image);
		// Add the image to the frame
		frame.add(imageLabel, BorderLayout.NORTH);
		// Create an Instructions button and add that to the menu
		final JButton Instructions = new JButton("Instructions");
		menu.add(Instructions);
		// Put the frame on the screen
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		// See if anyone clicked on the Instructions button
		Instructions.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// Get rid of components from previous screen
				frame.remove(menu);
				frame.remove(imageLabel);
				// Create image that displays instructions
				ImageIcon image = new ImageIcon("Instructions.png");
				JLabel imageLabel = new JLabel(image);
				// Add instructions image to the frame
				frame.add(imageLabel);
				// Put the frame on the screen
				frame.pack();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				// Create a menu button
				final JButton menu = new JButton("Menu");
				// Make an options jpanel
				final JPanel options = new JPanel();
				// Add options to the bottom of the frame
				frame.add(options, BorderLayout.SOUTH);
				// Add menu to options
				options.add(menu);
				// If someone clicks on the menu button, return to opening screen
				menu.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						frame.removeAll();
						frame.setVisible(false);
						run();
					}
				});
			}
		});
		start.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// Get rid of components from previous screens
				frame.remove(menu);
				frame.remove(imageLabel);
				// Create a status_panel
				final JPanel status_panel = new JPanel();
				// Add status_panel to the frame
				frame.add(status_panel, BorderLayout.SOUTH);
				final JLabel status = new JLabel("Running...");
				status_panel.add(status);

				// Main playing area
				final GameCourt court = new GameCourt(status);
				frame.add(court, BorderLayout.CENTER);

				// Reset button
				final JPanel control_panel = new JPanel();
				frame.add(control_panel, BorderLayout.NORTH);

				// Note here that when we add an action listener to the reset
				// button, we define it as an anonymous inner class that is
				// an instance of ActionListener with its actionPerformed()
				// method overridden. When the button is pressed,
				// actionPerformed() will be called.
				final JButton reset = new JButton("Reset");
				reset.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						court.reset();
					}
				});
				control_panel.add(reset);
				// Create a menu button
				final JButton menu = new JButton("Menu");
				// Add the menu button to the control_panel
				control_panel.add(menu);
				// If anyone clicks on the menu button, go back to opening screen
				menu.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						court.playing = false;
						frame.removeAll();
						frame.setVisible(false);
						run();
					}
				});
				// Put the frame on the screen
				frame.pack();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);

				// Start game
				court.reset();
			}
		});
	}

	/*
	 * Main method run to start and run the game Initializes the GUI elements
	 * specified in Game and runs it NOTE: Do NOT delete! You MUST include this
	 * in the final submission of your game.
	 */
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Game());
    }
}
