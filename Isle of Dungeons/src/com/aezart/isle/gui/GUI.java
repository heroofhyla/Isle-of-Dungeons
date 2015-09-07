package com.aezart.isle.gui;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.aezart.isle.Globals;

public class GUI {
	private JFrame frame;
	private Canvas canvas;
	private BufferStrategy bufstrat;
	private BufferedImage drawingSurface;
	public GUI(){
		frame = new JFrame(Globals.TITLEBAR);
		canvas = new Canvas();
		canvas.setSize(Globals.SURFACE_XPX * Globals.SCALE, Globals.SURFACE_YPX * Globals.SCALE);
		frame.add(canvas);
		drawingSurface = new BufferedImage(Globals.SURFACE_XPX, Globals.SURFACE_YPX, BufferedImage.TYPE_3BYTE_BGR);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		canvas.createBufferStrategy(2);
		bufstrat = canvas.getBufferStrategy();
	}
	
	public void blank(){
		Graphics g = drawingSurface.createGraphics();
		g.setColor(Globals.BLANK_COLOR);
		g.fillRect(0, 0, Globals.SURFACE_XPX,Globals.SURFACE_YPX);
	}
	
	public void show(){
		bufstrat.getDrawGraphics().drawImage(drawingSurface,0,0,Globals.SURFACE_XPX * Globals.SCALE, Globals.SURFACE_YPX * Globals.SCALE, null); 
		if (!bufstrat.contentsLost()){
			bufstrat.show();
		}
	}
	
	public Graphics2D getGraphics2D(){
		return drawingSurface.createGraphics();
	}

}
