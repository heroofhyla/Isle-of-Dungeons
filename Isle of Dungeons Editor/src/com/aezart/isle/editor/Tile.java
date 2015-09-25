package com.aezart.isle.editor;

public class Tile implements Comparable<Tile>{
	short autotilingType;
	Tile autotileRef;
	short paletteX;
	short paletteY;
	
	short tilesetPosition;
	boolean isAutotile;
	@Override
	public int compareTo(Tile t) {
		return this.autotilingType - t.autotilingType;
	}
	
	public Tile(){
	}
	
}
