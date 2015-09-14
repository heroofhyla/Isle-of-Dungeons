package com.aezart.isle.editor;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingUtilities;

public class MapMouseListener implements MouseListener, MouseMotionListener{
	
	MainWindow gui;
	MapProperties properties;
	int lastMouseX = Integer.MIN_VALUE;
	int lastMouseY = Integer.MIN_VALUE;

	public MapMouseListener(MainWindow gui, MapProperties properties){
		this.gui = gui;
		this.properties = properties;
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		System.out.println("received dragevent");
		long startTime = System.nanoTime();
		int zoom = (Integer)gui.zoomLevel.getSelectedItem();
		if (lastMouseX != Integer.MIN_VALUE){
			int mtx = tileFromMouse(e.getX(), properties.tile_side * zoom);
			int mty = tileFromMouse(e.getY(), properties.tile_side * zoom);
			int lmtx = tileFromMouse(lastMouseX, properties.tile_side * zoom);
			int lmty = tileFromMouse(lastMouseY, properties.tile_side * zoom);
			int xdist = mtx - lmtx;
			int ydist = mty - lmty;
			
			if (Math.max(Math.abs(xdist), Math.abs(ydist)) <= 1){
				placeTile(tileFromMouse(e.getX(), properties.tile_side * zoom), tileFromMouse(e.getY(), properties.tile_side * zoom), gui.paletteXTile, gui.paletteYTile);
			}else{
				double magnitude = Math.sqrt(xdist * xdist + ydist*ydist);
				double uxdist = xdist/magnitude;
				double uydist = ydist/magnitude;
				double x = lmtx;
				double y = lmty;
				while (Math.abs(x - mtx) > 1 || Math.abs(y - mty) > 1){
					x += uxdist;
					y += uydist;
					placeTile((int)x, (int)y, gui.paletteXTile, gui.paletteYTile);
					
				}
			}
		}
		placeTile(tileFromMouse(e.getX(), properties.tile_side * zoom), tileFromMouse(e.getY(), properties.tile_side * zoom), gui.paletteXTile, gui.paletteYTile);
		gui.frame.repaint();
		
		lastMouseX = e.getX();
		lastMouseY = e.getY();
		System.out.println("dragevent finished in " + (System.nanoTime() - startTime)/1000000);

	}
	
	public void placeTile(int dxt, int dyt, int sxt, int syt){
		System.out.print(properties.mapTiles[dyt][dxt] + " -> ");
		Tile t = new Tile(gui.selectedTile, false);
		
		properties.mapTiles[dyt][dxt] = new Tile(t, false);
		
		Graphics2D g = (Graphics2D)gui.mapPreviewImage.getGraphics();
		for (int i = dxt - 1; i <= dxt +1; ++i){
			for (int k = dyt -1; k <= dyt + 1; ++k){
				if (k < properties.mapTiles.length && k >= 0 && i >= 0 && i < properties.mapTiles[k].length){
					properties.updateAdjacency(i, k);
					int dxpx = i * properties.tile_side;
					int dypx = k * properties.tile_side;
					gui.tileset.drawTile(dxpx, dypx, properties.mapTiles[k][i], g);
					gui.frame.repaint();
				}
			}
		}
		
		

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
		int zoom = (Integer)gui.zoomLevel.getSelectedItem();

		placeTile(tileFromMouse(e.getX(), properties.tile_side * zoom), tileFromMouse(e.getY(), properties.tile_side * zoom), gui.paletteXTile, gui.paletteYTile);
		gui.frame.repaint();

		}

	@Override
	public void mouseReleased(MouseEvent e) {
		lastMouseX = Integer.MIN_VALUE;
		lastMouseY = Integer.MIN_VALUE;
		
	}
	
	int tileFromMouse(int x, int tileSide){
		return x/tileSide;
	}

}
