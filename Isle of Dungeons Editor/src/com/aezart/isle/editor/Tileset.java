package com.aezart.isle.editor;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Tileset {
	ArrayList<BufferedImage> tiles = new ArrayList<>();
	
	public BufferedImage processImage(String filePath, int tileSize){
		System.out.println("processImage called");
		BufferedImage image;
		try {
			image = ImageIO.read(this.getClass().getClassLoader().getResource(filePath));
			System.out.println("read image");
			tiles.clear();
			int xTiles = (int)Math.ceil(image.getWidth()/(double)tileSize);
			int yTiles = (int)Math.ceil(image.getHeight()/(double)tileSize);
			
			for (int i = 0; i < xTiles; ++i){
				for (int k = 0; k < yTiles; ++k){
					BufferedImage b = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_4BYTE_ABGR_PRE);
					Graphics g = b.getGraphics();
					int topLeftX = i * tileSize;
					int topLeftY = k * tileSize;
					g.drawImage(image, 0, 0, tileSize, tileSize, topLeftX, topLeftY, topLeftX+tileSize, topLeftY+tileSize, null);
					
					tiles.add(b);
				}
			}
			
			return image;

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
}
