package com.aezart.isle.editor;

import java.awt.Color;
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
			int nextId = 0;
			for (int k = 0; k < yTiles; ++k){
				for (int i = 0; i < xTiles; ++i){
					//if a tile is in the second column of the file, it is supplementary tiles for the autotile, so we don't need it
					if (i > 1 || (i == 0 && k%3 == 0)){
						Tile t = new Tile();
						t.tileID = nextId;
						//++nextId;
						t.paletteX = i;
						t.paletteY = k;
						
						//if a tile is in the first column of the file, it is an autotile
						if (i == 0){
							t.autotile = true;
							++nextId;
							t.tileID = nextId;

						}
						tiles.add(t);
					}
					
				}
			}
			Collections.sort(tiles);
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
	
	public void drawTile(int xpx, int ypx, Tile t, Graphics g){
		if (t.autotile){ //abandon all hope, ye who enter here
			//basically, we need to divide the tile into 4 subtiles
			//each of those subtiles decides what it needs to draw based on the 3 relevant adjacent tiles
			//for instance the top-left subtile relies on the adjacent tiles west (ml), northwest (tl), and north (tm)
			//TODO: Surely there's a better way to do this
			int qtile = properties.tile_side/2;
			int xoff = 0;
			int yoff = 0;
			//TL
			if (t.ml){
				if (t.tl){
					if (t.tm){
						xoff = 1; //full
						yoff = 3;
					}else{ //!t.tm
						xoff = 1; //horz
						yoff = 2;
					}
				}else{ //!t.tl
					if (t.tm){
						xoff = 2; //outer
						yoff = 0;
					}else{ //!t.tm
						xoff = 1; //horz
						yoff = 2;
					}
				}
			}else{ //!t.ml
				if (t.tl){
					if (t.tm){
						xoff = 0; //vert
						yoff = 3;
					}else{ //!t.tm
						xoff = 0; //inner
						yoff = 2;
					}
				}else{ //!t.tl
					if (t.tm){
						xoff = 0; //vert
						yoff = 3;
					}else{ //!t.tm
						xoff = 0; //inner
						yoff = 2;
					}
				}
			}
			int sx = t.paletteX * properties.tile_side + xoff * qtile;
			int sy = t.paletteY * properties.tile_side + yoff * qtile;
			g.drawImage(palette, xpx, ypx, xpx+qtile, ypx+qtile, sx, sy, sx + qtile, sy+qtile, null);
			//TR
			if (t.tm){
				if (t.tr){
					if (t.mr){
						xoff = 1;//full
						yoff = 3;
					}else{ //!t.mr
						xoff = 2; //vert
						yoff = 3;
					}
				}else{ //!t.tr
					if (t.mr){
						xoff = 2;//outer
						yoff = 0;
					}else{ //!t.mr
						xoff = 2; //vert
						yoff = 3;
					}
				}
			}else{ //!t.tm
				if (t.tr){
					if (t.mr){
						xoff = 1;//horiz
						yoff = 2;
					}else{ //!t.mr
						xoff = 2;//inner
						yoff = 2;
					}
				}else{ //!t.tr
					if (t.mr){
						xoff = 1;//horiz
						yoff = 2;
					}else{ //!t.mr
						xoff = 2;//inner
						yoff = 2;
					}
				}
			}
			sx = t.paletteX * properties.tile_side + (xoff+1) * qtile;
			sy = t.paletteY * properties.tile_side + yoff * qtile;
			g.drawImage(palette, xpx+qtile, ypx, xpx+qtile + qtile, ypx+qtile, sx, sy, sx + qtile, sy+qtile, null);

			//BL
			if (t.ml){
				if (t.bl){
					if (t.bm){
						xoff = 1;//full
						yoff = 3;
					}else{ //!t.bm
						xoff = 1;//horiz
						yoff = 4;
					}
				}else { //!t.bl
					if (t.bm){
						xoff = 2;//outer
						yoff = 0;
					}else{ //!t.bm
						xoff = 1;//horiz
						yoff = 4;
					}
				}
			}else{ //!t.ml
				if (t.bl){
					if (t.bm){
						xoff = 0;//vert
						yoff = 3;
					}else{ //!t.bm
						xoff = 0;//inner
						yoff = 4;
					}
				}else { //!t.bl
					if (t.bm){
						xoff = 0;//vert
						yoff = 3;
					}else{ //!t.bm
						xoff = 0;//inner
						yoff = 4;
					}
				}
			}
			sx = t.paletteX * properties.tile_side + xoff * qtile;
			sy = t.paletteY * properties.tile_side + (yoff+1) * qtile;
			g.drawImage(palette, xpx, ypx+qtile, xpx+qtile, ypx+qtile+qtile, sx, sy, sx + qtile, sy+qtile, null);

			//BR
			if (t.mr){
				if (t.br){
					if (t.bm){
						xoff = 1;//full
						yoff = 3;
					}else{ //!t.bm
						xoff = 1;//horiz
						yoff = 4;
					}
				}else { //!t.br
					if (t.bm){
						xoff = 2;//outer
						yoff = 0;
					}else{ //!t.bm
						xoff = 1;//horiz
						yoff = 4;
					}
				}
			}else{ //!t.mr
				if (t.br){
					if (t.bm){
						xoff = 2;//vert
						yoff = 3;
					}else{ //!t.bm
						xoff = 2;//inner
						yoff = 4;
					}
				}else { //!t.br
					if (t.bm){
						xoff = 2;//vert
						yoff = 3;
					}else{ //!t.bm
						xoff = 2;//inner
						yoff = 4;
					}
				}
			}
			sx = t.paletteX * properties.tile_side + (xoff+1) * qtile;
			sy = t.paletteY * properties.tile_side + (yoff+1) * qtile;
			g.drawImage(palette, xpx+qtile, ypx+qtile, xpx+qtile+qtile, ypx+qtile+qtile, sx, sy, sx + qtile, sy+qtile, null);

			//g.setColor(Color.GREEN);
			//g.fillRect(xpx, ypx, properties.tile_side, properties.tile_side);
		}else{
		g.drawImage(palette, xpx, ypx, xpx + properties.tile_side,ypx + properties.tile_side, 
				t.paletteX * properties.tile_side, t.paletteY * properties.tile_side, 
				(t.paletteX+1) * properties.tile_side, (t.paletteY+1) * properties.tile_side, null);
		}
	}
	
	
}
