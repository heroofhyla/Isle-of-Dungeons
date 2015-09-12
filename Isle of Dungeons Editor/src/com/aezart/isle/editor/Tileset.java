package com.aezart.isle.editor;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Tileset {
	ArrayList<BufferedImage> tiles = new ArrayList<>();
	
	public BufferedImage processImage(String filePath, int tileSize, JFrame frame){
		BufferedImage image;
		try {
			image = ImageIO.read(this.getClass().getClassLoader().getResource(filePath));
			BufferedImage b = frame.getGraphicsConfiguration().createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.OPAQUE);
			b.getGraphics().drawImage(image, 0, 0, null);
			return b;

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
}
