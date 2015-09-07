package com.aezart.isle;

import com.aezart.isle.gui.GUI;

public class Game {
	public static void main(String[] args) throws InterruptedException{
		GUI gui = new GUI();
		
		long timeBehind = 0;
		long startTime = 0;
		while(true){
			 startTime= System.nanoTime();
			while (timeBehind >= Globals.NANOTIMESTEP){
				tick();
				timeBehind -= Globals.NANOTIMESTEP;
			}
			redraw(gui);
			timeBehind += System.nanoTime() - startTime;
		}
	}
	
	public static void tick(){
		
	}
	
	public static void redraw(GUI gui){
		gui.blank();
		gui.show();
	}
}
