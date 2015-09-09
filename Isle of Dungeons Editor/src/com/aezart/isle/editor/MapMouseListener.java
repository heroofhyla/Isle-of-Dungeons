package com.aezart.isle.editor;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MapMouseListener implements MouseListener, MouseMotionListener{
	
	MainWindow gui;
	MapProperties properties;
	
	public MapMouseListener(MainWindow gui, MapProperties properties){
		this.gui = gui;
		this.properties = properties;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		System.out.println("drag!");
		properties.tileIDs[selectedTile(e.getX(), properties.tile_side)][selectedTile(e.getY(), properties.tile_side)] = gui.selectedTile;
		Graphics2D g = (Graphics2D)gui.mapPreviewImage.getGraphics();
		int dx1 = selectedTile(e.getX(),properties.tile_side) * properties.tile_side;
		int dy1 = selectedTile(e.getY(),properties.tile_side) * properties.tile_side;
		int sx1 = gui.selectedXTile;
		int sy1 = gui.selectedYTile;
		long startTime = System.nanoTime();
		g.drawImage(gui.tilesetImage, dx1, dy1, dx1 + properties.tile_side, dy1 + properties.tile_side, sx1, sy1, sx1 + properties.tile_side, sy1 + properties.tile_side, null);
		System.out.println("draw to image took " + (System.nanoTime() - startTime)/1_000_000 + " ms");
		gui.frame.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("drag!");
		properties.tileIDs[selectedTile(e.getX(), properties.tile_side)][selectedTile(e.getY(), properties.tile_side)] = gui.selectedTile;
		Graphics2D g = (Graphics2D)gui.mapPreviewImage.getGraphics();
		int dx1 = selectedTile(e.getX(),properties.tile_side) * properties.tile_side;
		int dy1 = selectedTile(e.getY(),properties.tile_side) * properties.tile_side;
		int sx1 = gui.selectedXTile;
		int sy1 = gui.selectedYTile;
		long startTime = System.nanoTime();
		g.drawImage(gui.tilesetImage, dx1, dy1, dx1 + properties.tile_side, dy1 + properties.tile_side, sx1, sy1, sx1 + properties.tile_side, sy1 + properties.tile_side, null);
		System.out.println("draw to image took " + (System.nanoTime() - startTime)/1_000_000 + " ms");
		gui.frame.repaint();
		}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	int selectedTile(int x, int tileSide){
		return x/tileSide;
	}

}
