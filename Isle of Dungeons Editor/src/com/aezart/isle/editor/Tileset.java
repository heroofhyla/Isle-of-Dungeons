package com.aezart.isle.editor;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Tileset {
	
	public static final int[] VERT_X_OFFSETS = {0,3,3,0};
	public static final int[] VERT_Y_OFFSETS = {3,3,4,4};
	
	public static final int[] HORIZ_X_OFFSETS = {1,2,2,1};
	public static final int[] HORIZ_Y_OFFSETS = {2,2,5,5};
	
	public static final int[] FULL_X_OFFSETS = {1,2,2,1};
	public static final int[] FULL_Y_OFFSETS = {3,3,4,4};
	
	public static final int[] OUTER_X_OFFSETS = {2,3,3,2};
	public static final int[] OUTER_Y_OFFSETS = {0,0,1,1};
	
	public static final int[] INNER_X_OFFSETS = {0,3,3,0};
	public static final int[] INNER_Y_OFFSETS = {2,2,5,5};
	
	public static final int[] DRAW_X_OFFSETS = {0,1,1,0};
	public static final int[] DRAW_Y_OFFSETS = {0,0,1,1};
	ArrayList<Tile> tiles = new ArrayList<>();
	int tilesetWidth;
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
			short nextId = 0;
			for (short k = 0; k < yTiles; ++k){
				for (short i = 0; i < xTiles; ++i){
					//if a tile is in the second column of the file, it is supplementary tiles for the autotile, so we don't need it
					if (i > 1 || (i == 0 && k%3 == 0)){
						Tile t = new Tile();
						t.autotilingType = nextId;
						//++nextId;
						t.paletteX = i;
						t.paletteY = k;
						
						//if a tile is in the first column of the file, it is an autotile
						if (i == 0){
							t.autotile = true;
							++nextId;
							t.autotilingType = nextId;

						}
						tiles.add(t);
					}
					
				}
			}
			Collections.sort(tiles);
			for (short i = 0; i < tiles.size(); ++i){
				tiles.get(i).tilesetPosition = i;
			}
			tilesetWidth = tiles.size()/nextId;
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
	
	public void drawTile(int xpx, int ypx, TileRef t, Graphics g){
		if (t.tileID.autotile){
			int qtile = properties.tile_side/2;
			
			for (int i = 0; i < 4; ++i){
				int corner = 2*i;
				boolean same = (byte)((t.adjacency >> corner) & 1) == 1;
				boolean horiz;
				boolean vert;
				if (i%2 == 0){
					horiz = (byte)((t.adjacency >> (corner-1)) & 1) == 1;
					vert = (byte)((t.adjacency >> (corner+1)) & 1) == 1;
				}else{
					horiz = (byte)((t.adjacency >> (corner+1)) & 1) == 1;
					vert = (byte)((t.adjacency >> (corner-1)) & 1) == 1;
				}
				int xoff = 0;
				int yoff = 0;

				if (horiz){
					if (same){
						if (vert){
							xoff = FULL_X_OFFSETS[i];
							yoff = FULL_Y_OFFSETS[i];
						}else { //!vert
							xoff = HORIZ_X_OFFSETS[i];
							yoff = HORIZ_Y_OFFSETS[i];

						}
					}else { //!same
						if (vert){
							xoff = OUTER_X_OFFSETS[i];
							yoff = OUTER_Y_OFFSETS[i];
						}else { //!vert
							xoff = HORIZ_X_OFFSETS[i];
							yoff = HORIZ_Y_OFFSETS[i];
						}
					}
				}else{ //!horiz
					if (same){
						if (vert){
								xoff = VERT_X_OFFSETS[i];
								yoff = VERT_Y_OFFSETS[i];
						}else { //!right
							xoff = INNER_X_OFFSETS[i];
							yoff = INNER_Y_OFFSETS[i];
						}
					}else { //!same
						if (vert){
								xoff = VERT_X_OFFSETS[i];
								yoff = VERT_Y_OFFSETS[i];
						}else { //!vert
							xoff = INNER_X_OFFSETS[i];
							yoff = INNER_Y_OFFSETS[i];
						}
					}
				}
				
				int sx = t.tileID.paletteX * properties.tile_side + xoff * qtile;
				int sy = t.tileID.paletteY * properties.tile_side + yoff * qtile;
				int dx = xpx + DRAW_X_OFFSETS[i]*qtile;
				int dy = ypx + DRAW_Y_OFFSETS[i]*qtile;
				g.drawImage(palette, dx, dy, dx+qtile, dy+qtile, sx, sy, sx + qtile, sy+qtile, null);

			}

		}else{
			g.drawImage(palette, xpx, ypx, xpx + properties.tile_side,ypx + properties.tile_side, 
				t.tileID.paletteX * properties.tile_side, t.tileID.paletteY * properties.tile_side, 
				(t.tileID.paletteX+1) * properties.tile_side, (t.tileID.paletteY+1) * properties.tile_side, null);
		}
		
	}
	
	
}
