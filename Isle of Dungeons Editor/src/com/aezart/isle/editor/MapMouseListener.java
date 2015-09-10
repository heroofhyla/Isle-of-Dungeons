package com.aezart.isle.editor;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

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
		if (lastMouseX != Integer.MIN_VALUE){
			int mtx = tileFromMouse(e.getX(), properties.tile_side);
			int mty = tileFromMouse(e.getY(), properties.tile_side);
			int lmtx = tileFromMouse(lastMouseX, properties.tile_side);
			int lmty = tileFromMouse(lastMouseY, properties.tile_side);
			int xdist = mtx - lmtx;
			int ydist = mty - lmty;
			
			if (Math.max(Math.abs(xdist), Math.abs(ydist)) <= 1){
				paintTile(tileFromMouse(e.getX(), properties.tile_side), tileFromMouse(e.getY(), properties.tile_side), gui.paletteXTile, gui.paletteYTile);
			}else{
				double magnitude = Math.sqrt(xdist * xdist + ydist*ydist);
				System.out.println("distance: " + xdist + " " + ydist);
				System.out.println("magnitude: " + magnitude);
				double uxdist = xdist/magnitude;
				double uydist = ydist/magnitude;
				System.out.println("unit distance: " + uxdist + " " + uydist);
				double x = lmtx;
				double y = lmty;
				System.out.println("Entering loop");
				while (Math.abs(x - mtx) > 1 || Math.abs(y - mty) > 1){
					System.out.println(x + " " + y);
					x += uxdist;
					y += uydist;
					paintTile((int)x, (int)y, gui.paletteXTile, gui.paletteYTile);
				}
			}
		}
		lastMouseX = e.getX();
		lastMouseY = e.getY();
		paintTile(tileFromMouse(e.getX(), properties.tile_side), tileFromMouse(e.getY(), properties.tile_side), gui.paletteXTile, gui.paletteYTile);
		gui.frame.repaint();
	}
	
	public void paintTile(int dxt, int dyt, int sxt, int syt){
		int dxpx = dxt * properties.tile_side;
		int dypx = dyt * properties.tile_side;
		int sxpx = sxt * properties.tile_side;
		int sypx = syt * properties.tile_side;
		
		Graphics2D g = (Graphics2D)gui.mapPreviewImage.getGraphics();
		g.drawImage(gui.tilesetImage, dxpx, dypx, dxpx + properties.tile_side, dypx + properties.tile_side, sxpx, sypx, sxpx + properties.tile_side, sypx + properties.tile_side, null);

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
		paintTile(tileFromMouse(e.getX(), properties.tile_side), tileFromMouse(e.getY(), properties.tile_side), gui.paletteXTile, gui.paletteYTile);
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
