package com.aezart.isle.editor;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.JWindow;
import javax.swing.Scrollable;

public class MainWindow {
	JFrame frame;
	JPanel mapPreview;
	BufferedImage mapPreviewImage;
	MapSettingsPanel mapSettingsPanel;
	JButton mapSettings;
	MapProperties properties;
	JToolBar toolbar = new JToolBar();
	JDialog paletteWindow;
	JPanel palettePanel;
	JScrollPane paletteScroll;
	Tileset tileset = new Tileset();
	JScrollPane scrollPane;
	BufferedImage tilesetImage;
	int selectedTile = 0;
	int selectedXTile = 0;
	int selectedYTile = 0;
	public MainWindow(){
		properties = new MapProperties();
		MapMouseListener mapListener = new MapMouseListener(this, properties);
		frame = new JFrame();
		paletteWindow = new JDialog(frame);
		frame.setLayout(new BorderLayout());
		
		mapPreview = new JPanel(){
			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(mapPreviewImage, 0, 0, null);
			}
		};
		mapPreview.addMouseListener(mapListener);
		mapPreview.addMouseMotionListener(mapListener);
		scrollPane = new JScrollPane(mapPreview);

		
		scrollPane.setPreferredSize(new Dimension(640,480));
		
		toolbar.add(new JButton(new SettingsAction()));
		toolbar.setFloatable(false);
		
		paletteWindow.setLayout(new BorderLayout());
		palettePanel = new JPanel(){
			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(tilesetImage,0,0,null);
				g.setColor(Color.black);
				g.drawRect(selectedXTile * properties.tile_side, selectedYTile * properties.tile_side, properties.tile_side, properties.tile_side);
			}
		};		
		
		palettePanel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				updateSelectedTile(e.getX(), e.getY());
			}
		});
		
		paletteScroll = new JScrollPane(palettePanel);
		paletteWindow.add(paletteScroll, BorderLayout.CENTER);
		mapSettingsPanel = new MapSettingsPanel();
		mapSettings = new JButton(new SettingsAction());
		
		updateMapDimensions();

		frame.add(toolbar, BorderLayout.NORTH);
		frame.add(scrollPane, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		paletteWindow.setLocation(20,20);
		paletteWindow.pack();
		paletteWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		
		paletteWindow.setVisible(true);

		
	}
	
	public void updateMapDimensions(){
		mapPreviewImage = frame.getGraphicsConfiguration().createCompatibleImage(properties.xscreens * properties.screen_xtiles * properties.tile_side, 
				properties.yscreens * properties.screen_ytiles * properties.tile_side);
		mapPreview.setPreferredSize(new Dimension(mapPreviewImage.getWidth(), mapPreviewImage.getHeight()));
		mapPreview.revalidate();
		scrollPane.repaint();
		tilesetImage = tileset.processImage(properties.tileset, properties.tile_side);
		palettePanel.setPreferredSize(new Dimension(tilesetImage.getWidth(), tilesetImage.getHeight()));
		properties.tileIDs = new int[properties.xscreens * properties.screen_xtiles][properties.yscreens * properties.screen_ytiles];
		paletteWindow.repaint();

	}
	
	public void updateSelectedTile(int mousex, int mousey){
		selectedXTile = mousex/properties.tile_side;
		System.out.println("xtile " + selectedXTile);
		selectedYTile = mousey/properties.tile_side;
		System.out.println("ytile " + selectedYTile);
		int tilesetWidth = (int)Math.ceil((double)tilesetImage.getWidth()/properties.tile_side);
		selectedTile = selectedYTile * tilesetWidth + selectedXTile;
		System.out.println("selected tile " + selectedTile);
		paletteWindow.repaint();
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
