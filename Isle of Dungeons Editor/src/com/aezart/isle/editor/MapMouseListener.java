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
				paintTile(tileFromMouse(e.getX(), properties.tile_side * zoom), tileFromMouse(e.getY(), properties.tile_side * zoom), gui.paletteXTile, gui.paletteYTile);
			}else{
				double magnitude = Math.sqrt(xdist * xdist + ydist*ydist);
				double uxdist = xdist/magnitude;
				double uydist = ydist/magnitude;
				double x = lmtx;
				double y = lmty;
				while (Math.abs(x - mtx) > 1 || Math.abs(y - mty) > 1){
					x += uxdist;
					y += uydist;
					paintTile((int)x, (int)y, gui.paletteXTile, gui.paletteYTile);
					
				}
			}
		}
		paintTile(tileFromMouse(e.getX(), properties.tile_side * zoom), tileFromMouse(e.getY(), properties.tile_side * zoom), gui.paletteXTile, gui.paletteYTile);
		gui.frame.repaint();
		
		lastMouseX = e.getX();
		lastMouseY = e.getY();
		System.out.println("dragevent finished in " + (System.nanoTime() - startTime)/1000000);

	}
	
	public void paintTile(int dxt, int dyt, int sxt, int syt){
		System.out.print(properties.mapTiles[dyt][dxt] + " -> ");

		properties.mapTiles[dyt][dxt] = gui.selectedTile;
		System.out.println(properties.mapTiles[dyt][dxt]);
		int dxpx = dxt * properties.tile_side;
		int dypx = dyt * properties.tile_side;
		int sxpx = sxt * properties.tile_side;
		int sypx = syt * properties.tile_side;
		
		Graphics2D g = (Graphics2D)gui.mapPreviewImage.getGraphics();
		//Graphics2D g = (Graphics2D)gui.mapPreview.getGraphics();
		g.drawImage(gui.tilesetImage, dxpx, dypx, dxpx + properties.tile_side, dypx + properties.tile_side, sxpx, sypx, sxpx + properties.tile_side, sypx + properties.tile_side, null);
		//gui.mapPreview.repaint(dxpx, dypx, dxpx + properties.tile_side, dypx + properties.tile_side);
		int[] vals = {dxt, dyt, sxt, syt};
		gui.tilesToUpdate.push(vals);

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

		paintTile(tileFromMouse(e.getX(), properties.tile_side * zoom), tileFromMouse(e.getY(), properties.tile_side * zoom), gui.paletteXTile, gui.paletteYTile);
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
