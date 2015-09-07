package com.aezart.isle.editor;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JViewport;

public class MainWindow {
	JFrame frame;
	JPanel mapPreview;
	BufferedImage mapPreviewImage;
	MapSettingsPanel mapSettingsPanel;
	JButton mapSettings;
	MapProperties properties;
	JToolBar toolbar = new JToolBar();
	JScrollPane scrollPane;
	public MainWindow(){
		properties = new MapProperties();
		frame = new JFrame();
		frame.setLayout(new BorderLayout());
		mapPreview = new JPanel(){
			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(mapPreviewImage, 0, 0, null);
			}
		};
		scrollPane = new JScrollPane(mapPreview);
		updateMapDimensions();

		
		scrollPane.setPreferredSize(new Dimension(640,480));
		
		toolbar.add(new JButton(new SettingsAction()));
		toolbar.setFloatable(false);
		
		mapSettingsPanel = new MapSettingsPanel();
		mapSettings = new JButton(new SettingsAction());
		
		frame.add(toolbar, BorderLayout.NORTH);
		frame.add(scrollPane, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
	
	public void updateMapDimensions(){
		mapPreviewImage = frame.getGraphicsConfiguration().createCompatibleImage(properties.xscreens * properties.screen_xtiles * properties.tile_side, 
				properties.yscreens * properties.screen_ytiles * properties.tile_side);
		mapPreview.setPreferredSize(new Dimension(mapPreviewImage.getWidth(), mapPreviewImage.getHeight()));
		mapPreview.revalidate();
		scrollPane.repaint();

	}
	class SettingsAction extends AbstractAction{
		
		public SettingsAction() {
			super("Map Settings");
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			mapSettingsPanel.nameField.setText(properties.name);
			mapSettingsPanel.tileSizeSpinner.setValue(properties.tile_side);
			mapSettingsPanel.screen_xtilesSpinner.setValue(properties.screen_xtiles);
			mapSettingsPanel.screen_ytilesSpinner.setValue(properties.screen_ytiles);
			mapSettingsPanel.xscreensSpinner.setValue(properties.xscreens);
			mapSettingsPanel.yscreensSpinner.setValue(properties.yscreens);
			mapSettingsPanel.tilesetField.setText(properties.tileset);
			int result = JOptionPane.showConfirmDialog(frame, mapSettingsPanel, "Map Settings", JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION){
				properties.name = mapSettingsPanel.nameField.getText();
				properties.tile_side = (Integer)mapSettingsPanel.tileSizeSpinner.getValue();
				properties.screen_xtiles = (Integer)mapSettingsPanel.screen_xtilesSpinner.getValue();
				properties.screen_ytiles = (Integer)mapSettingsPanel.screen_ytilesSpinner.getValue();
				properties.xscreens = (Integer)mapSettingsPanel.xscreensSpinner.getValue();
				properties.yscreens = (Integer)mapSettingsPanel.yscreensSpinner.getValue();
				properties.tileset = mapSettingsPanel.tilesetField.getText();
				
				updateMapDimensions();
			}
		}	
	}
}
