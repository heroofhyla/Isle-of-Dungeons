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
	private JLabel nameLabel = new JLabel("Map name:");
	public JTextField nameField = new JTextField();
	
	private JLabel tileSizeLabel = new JLabel("Tile width (pixels):");
	public JSpinner tileSizeSpinner = new JSpinner(new SpinnerNumberModel(16, 1, Integer.MAX_VALUE, 1));
	
	private JLabel screen_xtilesLabel = new JLabel("Screen width (tiles):");
	public JSpinner screen_xtilesSpinner = new JSpinner(new SpinnerNumberModel(20, 1, Integer.MAX_VALUE, 1));
	
	private JLabel screen_ytilesLabel = new JLabel("Screen height (tiles):");
	public JSpinner screen_ytilesSpinner = new JSpinner(new SpinnerNumberModel(15, 1, Integer.MAX_VALUE, 1));
	
	private JLabel xscreensLabel = new JLabel ("Map width (screens):");
	public JSpinner xscreensSpinner = new JSpinner(new SpinnerNumberModel(5, 1, Integer.MAX_VALUE, 1));
	
	private JLabel yscreensLabel = new JLabel ("Map height (screens):");
	public JSpinner yscreensSpinner = new JSpinner(new SpinnerNumberModel(5, 1, Integer.MAX_VALUE, 1));
	
	private JLabel tilesetLabel = new JLabel("Tileset:");
	private JPanel tilesetPanel = new JPanel(new BorderLayout());
	private JButton tilesetButton = new JButton("Open");
	public JTextField tilesetField = new JTextField();
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
		tilesetPanel.add(tilesetButton, BorderLayout.WEST);
		tilesetPanel.add(tilesetField, BorderLayout.CENTER);
		this.add(tilesetPanel);
	}
}
