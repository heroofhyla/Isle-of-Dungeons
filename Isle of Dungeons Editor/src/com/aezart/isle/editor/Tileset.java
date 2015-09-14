package com.aezart.isle.editor;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Tileset {
	ArrayList<Tile> tiles = new ArrayList<>();
	int tilesetWidth = 10;
	BufferedImage palette;
	MapProperties properties;
	
	public Tileset(MapProperties properties){
		this.properties = properties;
	}
	public BufferedImage processImage(String filePath, int tileSize, JFrame frame){
		tiles.clear();
		try {
			palette = ImageIO.read(this.getClass().getClassLoader().getResource(filePath));
			
			int xTiles = (int)Math.ceil(palette.getWidth()/(double)tileSize);
			int yTiles = (int)Math.ceil(palette.getHeight()/(double)tileSize);
			int nextId = 0;
			for (int k = 0; k < yTiles; ++k){
				for (int i = 0; i < xTiles; ++i){
					//if a tile is in the second column of the file, it is supplementary tiles for the autotile, so we don't need it
					if (i > 1 || (i == 0 && k%3 == 0)){
						Tile t = new Tile();
						t.tileID = nextId;
						++nextId;
						t.paletteX = i;
						t.paletteY = k;
						
						//if a tile is in the first column of the file, it is an autotile
						if (i == 0){
							t.autotile = true;
						}
						tiles.add(t);
					}
					
				}
				Collections.sort(tiles);
			}
			int tilesetHeight = (int)Math.ceil(tiles.size()/(double)tilesetWidth);
			BufferedImage b = frame.getGraphicsConfiguration().createCompatibleImage(tilesetWidth * tileSize, tilesetHeight * tileSize, Transparency.OPAQUE);
			Graphics2D g = b.createGraphics();
			int nextTile = 0;
			for (int k = 0; k < tilesetHeight; ++k){
				for (int i = 0; i < tilesetWidth; ++i){
					if (tiles.size() > nextTile){
						int sx = tiles.get(nextTile).paletteX * tileSize;
						int sy = tiles.get(nextTile).paletteY * tileSize;
						
						int dx = i * tileSize;
						int dy = k * tileSize;
						
						g.drawImage(palette, dx, dy, dx+tileSize, dy+tileSize, sx, sy, sx+tileSize, sy+tileSize,null);
						++nextTile;
					}
					
				}
			}
			g.dispose();
			return b;

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public Tile getTile(int tilesetX, int tilesetY){
		int id = tilesetX + tilesetY*tilesetWidth;
		if (id < tiles.size()){
		return tiles.get(id);
		}else{
			return null;
		}
	}
	
	public void drawTile(int xpx, int ypx, Tile t, Graphics g){
		g.drawImage(palette, xpx, ypx, xpx + properties.tile_side,ypx + properties.tile_side, 
				t.paletteX * properties.tile_side, t.paletteY * properties.tile_side, 
				(t.paletteX+1) * properties.tile_side, (t.paletteY+1) * properties.tile_side, null);
	}
}
