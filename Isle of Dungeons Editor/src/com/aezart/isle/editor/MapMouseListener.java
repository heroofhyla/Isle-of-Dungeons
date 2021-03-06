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
	boolean shiftdragging;
	public MapMouseListener(MainWindow gui, MapProperties properties){
		this.gui = gui;
		this.properties = properties;
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		int zoom = (Integer)gui.zoomLevel.getSelectedItem();
		if (lastMouseX != Integer.MIN_VALUE){
			int mtx = tileFromMouse(e.getX(), properties.tile_side * zoom);
			int mty = tileFromMouse(e.getY(), properties.tile_side * zoom);
			int lmtx = tileFromMouse(lastMouseX, properties.tile_side * zoom);
			int lmty = tileFromMouse(lastMouseY, properties.tile_side * zoom);
			int xdist = mtx - lmtx;
			int ydist = mty - lmty;
			
			if (Math.max(Math.abs(xdist), Math.abs(ydist)) <= 1){
				if (shiftdragging){
					placeTile(tileFromMouse(e.getX(), properties.tile_side * zoom), tileFromMouse(e.getY(), properties.tile_side * zoom), true);
				}else{
					placeTile(tileFromMouse(e.getX(), properties.tile_side * zoom), tileFromMouse(e.getY(), properties.tile_side * zoom), false);
				}
			}else{
				double magnitude = Math.sqrt(xdist * xdist + ydist*ydist);
				double uxdist = xdist/magnitude;
				double uydist = ydist/magnitude;
				double x = lmtx;
				double y = lmty;
				while (Math.abs(x - mtx) > 1 || Math.abs(y - mty) > 1){
					x += uxdist;
					y += uydist;
					if(shiftdragging){
						placeTile((int)x, (int)y, true);
					}else{
						placeTile((int)x, (int)y, false);
					}
					
				}
			}
		}
		if (shiftdragging){
			placeTile(tileFromMouse(e.getX(), properties.tile_side * zoom), tileFromMouse(e.getY(), properties.tile_side * zoom), true);
		}else{
			placeTile(tileFromMouse(e.getX(), properties.tile_side * zoom), tileFromMouse(e.getY(), properties.tile_side * zoom), false);
		}
		gui.frame.repaint();
		
		lastMouseX = e.getX();
		lastMouseY = e.getY();

	}
	
	public void placeTile(int dxt, int dyt, boolean maintainAdjacency){
		
		TileRef t = gui.selectedTile;
		if (maintainAdjacency){
			properties.mapTiles[dyt][dxt] = new TileRef(t.tileID, t.adjacency);
		}else{
			properties.mapTiles[dyt][dxt] = new TileRef(t.tileID);
		}

		Graphics2D g = (Graphics2D)gui.mapPreviewImage.getGraphics();
		for (int i = dxt - 1; i <= dxt +1; ++i){
			for (int k = dyt -1; k <= dyt + 1; ++k){
				if (k < properties.mapTiles.length && k >= 0 && i >= 0 && i < properties.mapTiles[k].length){
					if (!maintainAdjacency){
						properties.updateAdjacency(i, k);
					}
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
		shiftdragging = ((e.getModifiersEx() & MouseEvent.SHIFT_DOWN_MASK) != 0);
		int zoom = (Integer)gui.zoomLevel.getSelectedItem();
	
		if (e.getButton() == MouseEvent.BUTTON1){
			if (shiftdragging){
				placeTile(tileFromMouse(e.getX(), properties.tile_side * zoom), tileFromMouse(e.getY(), properties.tile_side * zoom), true);

			}else{
				placeTile(tileFromMouse(e.getX(), properties.tile_side * zoom), tileFromMouse(e.getY(), properties.tile_side * zoom), false);

			}
			gui.frame.repaint();
		}
		if (e.getButton() == MouseEvent.BUTTON3){
			TileRef t;
			if (shiftdragging){				
				t = new TileRef(properties.mapTiles[tileFromMouse(e.getY(), properties.tile_side * zoom)][tileFromMouse(e.getX(), properties.tile_side * zoom)].tileID, properties.mapTiles[tileFromMouse(e.getY(), properties.tile_side * zoom)][tileFromMouse(e.getX(), properties.tile_side * zoom)].adjacency);
			}else{
				t = new TileRef(properties.mapTiles[tileFromMouse(e.getY(), properties.tile_side * zoom)][tileFromMouse(e.getX(), properties.tile_side * zoom)].tileID);
			}
			
			gui.updateSelectedTile(t);
			gui.palettePanel.repaint();
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		lastMouseX = Integer.MIN_VALUE;
		lastMouseY = Integer.MIN_VALUE;
		gui.redrawCanvas();
		
	}
	
	int tileFromMouse(int x, int tileSide){
		return x/tileSide;
	}

}
