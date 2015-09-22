package com.aezart.isle.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

public class MainWindow {
	public static final byte FORMAT_VERSION_NUMBER = 1;
	
	JFrame frame;
	JPanel mapPreview;
	VolatileImage mapPreviewImage;
	MapSettingsPanel mapSettingsPanel;
	JButton mapSettings;
	JButton saveButton;
	MapProperties properties;
	JToolBar toolbar = new JToolBar();
	JDialog paletteWindow;
	JPanel palettePanel;
	JScrollPane paletteScroll;
	Tileset tileset;
	JScrollPane scrollPane;
	BufferedImage tilesetImage;
	
	JComboBox<Integer> zoomLevel;
	JFileChooser fileChooser = new JFileChooser();
	TileRef selectedTile = null;
	int paletteXTile = 0;
	int paletteYTile = 0;
	int zoom = 1;
	boolean needsRedraw = false;
	public MainWindow(){
		properties = new MapProperties();
		tileset = new Tileset(properties);
		MapMouseListener mapListener = new MapMouseListener(this, properties);
		
		frame = new JFrame();
		paletteWindow = new JDialog(frame);
		frame.setLayout(new BorderLayout());
		JLabel zoomLabel = new JLabel("Zoom:");
		zoomLevel = new JComboBox<Integer>();
		zoomLevel.addItem(1);
		zoomLevel.addItem(2);
		zoomLevel.addItem(4);
		zoomLevel.setSelectedIndex(0);
		zoomLevel.setPrototypeDisplayValue(4);
		zoomLevel.addActionListener((ActionEvent arg0)->{
			reZoom();
			frame.repaint();
		});
		mapPreview = new JPanel(){
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
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
				
				g2D.setColor(Color.black);
				for (int x = 0; x < Math.min(mapPreview.getWidth(),mapPreviewImage.getWidth()*zoom); x += zoom * properties.tile_side){
					for (int y = 0; y < Math.min(mapPreview.getHeight(), mapPreviewImage.getHeight() * zoom); y += zoom*properties.tile_side){
						g.drawLine(x, 0, x, Math.min(mapPreview.getHeight(), mapPreviewImage.getHeight() * zoom));
						g.drawLine(0, y, Math.min(mapPreview.getWidth(), mapPreviewImage.getWidth() * zoom), y);
					}
				}
			}
		};
		
		mapPreview.addMouseListener(mapListener);
		mapPreview.addMouseMotionListener(mapListener);
		scrollPane = new JScrollPane(mapPreview);

		
		scrollPane.setPreferredSize(new Dimension(640,480));
		toolbar.add(zoomLabel);
		toolbar.add(zoomLevel);
		toolbar.add(new JButton(new SaveAction()));
		toolbar.add(new JButton(new LoadAction()));
		toolbar.add(new JButton(new SettingsAction()));
		toolbar.setFloatable(false);
		toolbar.setLayout(new FlowLayout());
		
		paletteWindow.setLayout(new BorderLayout());
		palettePanel = new JPanel(){
			@Override
			protected void paintComponent(Graphics g) {
				g.setColor(Color.white);
				g.fillRect(0, 0, palettePanel.getWidth(), palettePanel.getHeight());
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
		paletteWindow.setPreferredSize(new Dimension(200, 200));
		paletteWindow.pack();
		paletteWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		
		paletteWindow.setVisible(true);

		
	}
	
	public void updateMapDimensions(){
		tilesetImage = tileset.processImage(properties.tileset, properties.tile_side, frame);
		selectedTile = new TileRef(tileset.getTile(0, 0));
		palettePanel.setPreferredSize(new Dimension(tilesetImage.getWidth(), tilesetImage.getHeight()));
		properties.mapTiles = new TileRef[properties.yscreens * properties.screen_ytiles][properties.xscreens * properties.screen_xtiles];
		for (int i = 0; i < properties.mapTiles.length; ++i){
			for (int k = 0; k < properties.mapTiles[i].length; ++k){
				properties.mapTiles[i][k] = new TileRef(tileset.getTile(0, 0));
			}
		}
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
		int oldx = paletteXTile;
		int oldy = paletteYTile;
		paletteXTile = mousex/properties.tile_side;
		paletteYTile = mousey/properties.tile_side;
		if (tileset.getTile(paletteXTile, paletteYTile) != null){
			selectedTile = new TileRef(tileset.getTile(paletteXTile, paletteYTile));
		}else{
			paletteXTile = oldx;
			paletteYTile = oldy;
		}
		paletteWindow.repaint();
	}
	
	public void updateSelectedTile(TileRef t){
		paletteXTile = -1;
		paletteYTile = -1;
		
		selectedTile = t;
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
				int sx1 = (properties.mapTiles[y][x].tileID.paletteX) * properties.tile_side;
				int sy1 = (properties.mapTiles[y][x].tileID.paletteY) * properties.tile_side;
				
				//g.drawImage(tilesetP, dx1, dy1, dx1 + properties.tile_side, dy1 + properties.tile_side, sx1, sy1, sx1 + properties.tile_side, sy1 + properties.tile_side, null);
				//properties.updateAdjacency(x, y);
				tileset.drawTile(dx1, dy1, properties.mapTiles[y][x], g);
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
	
	class LoadAction extends AbstractAction{
		public LoadAction(){
			super("Open");
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			int result = fileChooser.showOpenDialog(frame);
			if (result == JFileChooser.APPROVE_OPTION){
				File f = fileChooser.getSelectedFile();
				
				try(DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(f)))){
					
					ByteBuffer headerBuffer = ByteBuffer.allocate(8);
					
					for (int i = 0; i < 8; ++i){
						headerBuffer.put(dis.readByte());
					}				
					
					boolean validHeader = true;
					headerBuffer.order(ByteOrder.LITTLE_ENDIAN);
					headerBuffer.position(0);
					
					if (12354 != headerBuffer.getShort()){
						validHeader = false;
					}
					if (12356 != headerBuffer.getShort()){
						validHeader = false;
					}
					if (12391 != headerBuffer.getShort()){
						validHeader = false;
					}
					if (12416 != headerBuffer.getShort()){
						validHeader = false;
					}
					if (!validHeader){
						JOptionPane.showMessageDialog(frame, "This file does not appear to be an Isle of Dungeons Editor map.");
						return;
					}
					
					byte versionNo = dis.readByte();
					
					properties.screen_xtiles = dis.readShort();
					properties.screen_ytiles = dis.readShort();
					properties.xscreens = dis.readShort();
					properties.yscreens = dis.readShort();
					
					updateMapDimensions();
					boolean eof = false;
					int count = 0;
					while (!eof){
						try{
						short tilesetPos = dis.readShort();
						byte adjacencies = dis.readByte();
						TileRef t = new TileRef(tileset.tiles.get(tilesetPos),adjacencies);
						//TODO: Adjacencies!
						
						properties.mapTiles[count/properties.mapTiles[0].length][count%properties.mapTiles[0].length] = t;

						++count;
						}catch (EOFException e){
							eof = true;
						}
								
					}
					initializeCanvas();
					mapPreviewImage.validate(frame.getGraphicsConfiguration());
					redrawCanvas();

					
					redrawCanvas();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	class SaveAction extends AbstractAction{

		public SaveAction(){
			super("Save");
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			/*
			 * Save file format:
			 * 	Header: 4 short values in sequence, written in little endian (so they are in the right order as UTF-8 chars): 12354 12356 12391 12416
			 * 			This is "あいでも" in utf-8
			 * 	Version number: 1 byte
			 * 	short: screen_xtiles
			 * 	short: screen_ytiles
			 * 	short: xscreens
			 * 	short: yscreens
			 * 	
			 * 	This leading information is followed by the rest of the map data, where each tile is indicated as follows:
			 * 	short: tilesetPosition
			 * 	byte: adjacency, where each bit represents adjacency on one face. 
			 * 			The least significant digit is the top left corner, and you go clockwise as you increase significance 
			 */
			int buttonResult = fileChooser.showSaveDialog(frame);
			if (buttonResult == JFileChooser.APPROVE_OPTION){
				File f = fileChooser.getSelectedFile();
				int overwrite = JOptionPane.OK_OPTION;
				if (f.exists()){
					overwrite = JOptionPane.showConfirmDialog(frame, "File already exists, overwrite?", "Overwrite File", JOptionPane.OK_CANCEL_OPTION);
				}
				if (overwrite == JOptionPane.OK_OPTION){
					try(DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(f)))){
						
						ByteBuffer headerBuffer = ByteBuffer.allocate(8);
						headerBuffer.order(ByteOrder.LITTLE_ENDIAN);
						headerBuffer.putShort((short)12354);
						headerBuffer.putShort((short)12356);
						headerBuffer.putShort((short)12391);
						headerBuffer.putShort((short)12416);
						
						headerBuffer.position(0);
						dos.write(headerBuffer.array());
						dos.writeByte(FORMAT_VERSION_NUMBER);
						dos.writeShort(properties.screen_xtiles);
						dos.writeShort(properties.screen_ytiles);
						dos.writeShort(properties.xscreens);
						dos.writeShort(properties.yscreens);
						for (short k = 0; k < properties.mapTiles.length; ++k){
							for (short i = 0; i < properties.mapTiles[k].length; ++i){
								TileRef t = properties.mapTiles[k][i];
								dos.writeShort(t.tileID.tilesetPosition);
								dos.writeByte(t.adjacency);
							}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
		}
		
	}
	
}
