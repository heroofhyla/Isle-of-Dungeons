package com.aezart.isle.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.ArrayDeque;

import javafx.scene.control.ScrollBar;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

public class MainWindow {
	JFrame frame;
	JPanel mapPreview;
	VolatileImage mapPreviewImage;
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
	
	JComboBox<Integer> zoomLevel; 
	int selectedTile = 0;
	int paletteXTile = 0;
	int paletteYTile = 0;
	int zoom = 1;
	boolean needsRedraw = false;
	ArrayDeque<int[]> tilesToUpdate = new ArrayDeque<>();
	public MainWindow(){
		properties = new MapProperties();
		
		MapMouseListener mapListener = new MapMouseListener(this, properties);
		
		frame = new JFrame();
		paletteWindow = new JDialog(frame);
		frame.setLayout(new BorderLayout());
		JLabel zoomLabel = new JLabel("Zoom:");
		zoomLevel = new JComboBox<Integer>();
		zoomLevel.addItem(1);
		zoomLevel.addItem(2);
		zoomLevel.addItem(4);
		zoomLevel.setPrototypeDisplayValue(4);
		zoomLevel.addActionListener((ActionEvent arg0)->{
			reZoom();
			frame.repaint();
		});
		mapPreview = new JPanel(){
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Long startTime = System.nanoTime();

				Graphics2D g2D = (Graphics2D) g;
				int zoom = (Integer)zoomLevel.getSelectedItem();
				int x1 = -mapPreview.getX();
				int x2 = x1 + scrollPane.getVisibleRect().width;
				int y1 = -mapPreview.getY();
				int y2 = y1 + scrollPane.getVisibleRect().height;
				//if (mapPreviewImage.validate(frame.getGraphicsConfiguration()) != VolatileImage.IMAGE_OK || mapPreviewImage.validate(frame.getGraphicsConfiguration()) != VolatileImage.IMAGE_RESTORED){
				if (mapPreviewImage.validate(frame.getGraphicsConfiguration()) == VolatileImage.IMAGE_INCOMPATIBLE){
					initializeCanvas();
					mapPreviewImage.validate(frame.getGraphicsConfiguration());
					redrawCanvas();
				}

				g2D.drawImage(mapPreviewImage, x1, y1, x2, y2, x1/zoom, y1/zoom, x2/zoom, y2/zoom, null);
				System.out.println(System.nanoTime() - startTime);
			}
		};
		
		mapPreview.addMouseListener(mapListener);
		mapPreview.addMouseMotionListener(mapListener);
		scrollPane = new JScrollPane(mapPreview);

		
		scrollPane.setPreferredSize(new Dimension(640,480));
		toolbar.add(zoomLabel);
		toolbar.add(zoomLevel);
		toolbar.add(new JButton(new SettingsAction()));
		toolbar.setFloatable(false);
		toolbar.setLayout(new FlowLayout());
		
		paletteWindow.setLayout(new BorderLayout());
		palettePanel = new JPanel(){
			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(tilesetImage,0,0,null);
				g.setColor(Color.black);
				g.drawRect(paletteXTile * properties.tile_side, paletteYTile * properties.tile_side, properties.tile_side, properties.tile_side);
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
		
		initializeCanvas();
		mapPreviewImage.validate(frame.getGraphicsConfiguration());
		redrawCanvas();

		paletteWindow.setLocation(0,0);
		paletteWindow.setPreferredSize(new Dimension(200,400));
		paletteWindow.pack();
		paletteWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		
		paletteWindow.setVisible(true);

		
	}
	
	public void updateMapDimensions(){
		tilesetImage = tileset.processImage(properties.tileset, properties.tile_side, frame);
		palettePanel.setPreferredSize(new Dimension(tilesetImage.getWidth(), tilesetImage.getHeight()));
		properties.tileIDs = new int[properties.yscreens * properties.screen_ytiles][properties.xscreens * properties.screen_xtiles];
		paletteWindow.repaint();
		initializeCanvas();
		mapPreview.setPreferredSize(new Dimension(mapPreviewImage.getWidth(), mapPreviewImage.getHeight()));
		mapPreview.revalidate();
		scrollPane.repaint();

		Graphics g = mapPreviewImage.getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, mapPreviewImage.getWidth(), mapPreviewImage.getHeight());

	}
	
	public void updateSelectedTile(int mousex, int mousey){
		paletteXTile = mousex/properties.tile_side;
		paletteYTile = mousey/properties.tile_side;
		int tilesetWidth = (int)Math.ceil((double)tilesetImage.getWidth()/properties.tile_side);
		selectedTile = paletteYTile * tilesetWidth + paletteXTile;
		paletteWindow.repaint();
	}
	
	public void reZoom(){
		double scaleFactor = (Integer)zoomLevel.getSelectedItem()/(double)zoom;
		JScrollBar xScrollBar = scrollPane.getHorizontalScrollBar();
		JScrollBar yScrollBar = scrollPane.getVerticalScrollBar();
		
		int oldCenterX = xScrollBar.getValue() + xScrollBar.getVisibleAmount()/2;
		int oldCenterY = yScrollBar.getValue() + yScrollBar.getVisibleAmount()/2;
		int targetCenterX = (int)(oldCenterX * scaleFactor);
		int targetCenterY = (int)(oldCenterY * scaleFactor);
		
		int dCenterX = targetCenterX - oldCenterX;
		int dCenterY = targetCenterY - oldCenterY;
		int oldLeftX = xScrollBar.getValue();
		int oldTopY = yScrollBar.getValue();
		mapPreview.setPreferredSize(new Dimension(mapPreviewImage.getWidth() * (Integer)zoomLevel.getSelectedItem(), mapPreviewImage.getHeight() * (Integer)zoomLevel.getSelectedItem()));
		mapPreview.setSize(mapPreview.getPreferredSize());
		mapPreview.validate();
		xScrollBar.setMaximum(mapPreview.getWidth());
		yScrollBar.setMaximum(mapPreview.getHeight());
		int targetLeftX = oldLeftX + dCenterX;
		int targetTopY = oldTopY + dCenterY;
		xScrollBar.setValue(targetLeftX);
		yScrollBar.setValue(targetTopY);
		
		zoom = (Integer)zoomLevel.getSelectedItem();

	}
	
	public void initializeCanvas(){
		mapPreviewImage = frame.getGraphicsConfiguration().createCompatibleVolatileImage(properties.xscreens * properties.screen_xtiles * properties.tile_side, 
				properties.yscreens * properties.screen_ytiles * properties.tile_side, Transparency.OPAQUE);
		needsRedraw = true;
	}
	
	public void redrawCanvas(){
		Graphics2D g = mapPreviewImage.createGraphics();
		for (int y = 0; y < mapPreviewImage.getHeight()/properties.tile_side; ++y){
			for (int x = 0; x < mapPreviewImage.getWidth()/properties.tile_side; ++x){
				//System.out.println("drawing at tile:" + x + " " + y + " with palette ID: " + properties.tileIDs[y][x]);
				int dx1 = x * properties.tile_side;
				int dy1 = y * properties.tile_side;
				int tilesetWidth = (int)Math.ceil((double)tilesetImage.getWidth()/properties.tile_side);
				int sx1 = (properties.tileIDs[y][x] % tilesetWidth) * properties.tile_side;
				int sy1 = (properties.tileIDs[y][x] / tilesetWidth) * properties.tile_side;
				
				g.drawImage(tilesetImage, dx1, dy1, dx1 + properties.tile_side, dy1 + properties.tile_side, sx1, sy1, sx1 + properties.tile_side, sy1 + properties.tile_side, null);   
			}
		}
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
