package com.aezart.isle.editor;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

public class MapSettingsPanel extends JPanel{
	JLabel nameLabel = new JLabel("Map name:");
	JTextField nameField = new JTextField();
	
	JLabel tileSizeLabel = new JLabel("Tile width (pixels):");
	JSpinner tileSizeSpinner = new JSpinner(new SpinnerNumberModel(16, 1, Integer.MAX_VALUE, 1));
	
	JLabel screen_xtilesLabel = new JLabel("Screen width (tiles):");
	JSpinner screen_xtilesSpinner = new JSpinner(new SpinnerNumberModel(20, 1, Integer.MAX_VALUE, 1));
	
	JLabel screen_ytilesLabel = new JLabel("Screen height (tiles):");
	JSpinner screen_ytilesSpinner = new JSpinner(new SpinnerNumberModel(15, 1, Integer.MAX_VALUE, 1));
	
	JLabel xscreensLabel = new JLabel ("Map width (screens):");
	JSpinner xscreensSpinner = new JSpinner(new SpinnerNumberModel(5, 1, Integer.MAX_VALUE, 1));
	
	JLabel yscreensLabel = new JLabel ("Map height (screens):");
	JSpinner yscreensSpinner = new JSpinner(new SpinnerNumberModel(5, 1, Integer.MAX_VALUE, 1));
	
	JLabel tilesetLabel = new JLabel("Tileset:");
	JPanel browsePanel = new JPanel(new BorderLayout());
	JButton browseButton = new JButton("Open");
	JTextField browseField = new JTextField();
	public MapSettingsPanel(){
		this.setLayout(new GridLayout(0, 2));
		this.add(nameLabel);
		this.add(nameField);
		this.add(tileSizeLabel);
		this.add(tileSizeSpinner);
		this.add(screen_xtilesLabel);
		this.add(screen_xtilesSpinner);
		this.add(screen_ytilesLabel);
		this.add(screen_ytilesSpinner);
		this.add(xscreensLabel);
		this.add(xscreensSpinner);
		this.add(yscreensLabel);
		this.add(yscreensSpinner);
		this.add(tilesetLabel);
		browsePanel.add(browseButton, BorderLayout.WEST);
		browsePanel.add(browseField, BorderLayout.CENTER);
		this.add(browsePanel);
	}
}
