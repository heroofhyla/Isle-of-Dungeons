package com.aezart.isle.editor;

public class Tile implements Comparable<Tile>{
	short autotilingType;
	short paletteX;
	short paletteY;
	
	short tilesetPosition;
	boolean autotile;
	@Override
	public int compareTo(Tile t) {
		return this.autotilingType - t.autotilingType;
	}
	
	public Tile(){
	}
	
}
