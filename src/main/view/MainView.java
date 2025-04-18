<<<<<<< Updated upstream
package main.view;

public class MainView {

}
=======
/**
 * Project Name: Gradebook
 * File Name: MainView.java
 * Course: CSC 335 Spring 2025
 * Purpose: Root view container that manages all application scenes and handles 
 *          transitions between different views. Maintains a registry of scenes
 *          and coordinates with MainController for navigation.
 */
package view;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class MainView extends JFrame{
	
	
	private JPanel mainPanel;
	private CardLayout layout;
	private JPanel panel1, panel2;
	private JTextField textfield;
	private JLabel label;
	
	public MainView() {
		this.setTitle("Main Page");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(500,500);
		
		this.layout = new CardLayout();
		this.mainPanel = new JPanel(layout);
		
		this.panel1 = new JPanel();
		this.textfield = new JTextField(20);
		JButton button1 = new JButton("Go to Panel 2");
		panel1.add(new JLabel("Username:"));
		panel1.add(textfield);
		panel1.add(button1);
		
		this.panel2 = new JPanel();
		this.label = new JLabel(" ");
		JButton button2 = new JButton("Go to Panel 1");
		panel2.add(label);
		panel2.add(button2);
		
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				label.setText(textfield.getText());
				layout.show(mainPanel, "panel2");
			}
		});
		
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				layout.show(mainPanel, "panel1");
			}
		});
		
		mainPanel.add(panel1,"panel1");
		mainPanel.add(panel2,"panel2");
		
		add(mainPanel);
		setVisible(true);
	}
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new MainView();
			}
		});
	}
}
>>>>>>> Stashed changes
