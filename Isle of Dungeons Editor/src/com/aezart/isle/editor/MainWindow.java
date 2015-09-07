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
	JPanel mapSettingsPanel;
	JButton mapSettings;
	MapProperties properties;
	JToolBar toolbar = new JToolBar();
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
		
		mapPreviewImage = frame.getGraphicsConfiguration().createCompatibleImage(properties.xscreens * properties.screen_xtiles * properties.tile_side, 
				properties.yscreens * properties.screen_ytiles * properties.tile_side);
		mapPreview.setPreferredSize(new Dimension(mapPreviewImage.getWidth(), mapPreviewImage.getHeight()));
		JScrollPane scrollPane = new JScrollPane(mapPreview);
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
	
	class SettingsAction extends AbstractAction{
		
		public SettingsAction() {
			super("Map Settings");
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			JOptionPane.showConfirmDialog(frame, mapSettingsPanel, "Map Settings", JOptionPane.OK_CANCEL_OPTION);
		}
		
	}
}
