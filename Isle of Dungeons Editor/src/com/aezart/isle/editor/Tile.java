package com.aezart.isle.editor;

public class Tile implements Comparable<Tile>{
	int tileID;
	int mapx;
	int mapy;
	int paletteX;
	int paletteY;
	
	boolean autotile;
	boolean tl;
	boolean tm;
	boolean tr;
	boolean mr;
	boolean br;
	boolean bm;
	boolean bl;
	boolean ml;
	@Override
	public int compareTo(Tile t) {
		if (this.autotile && ! t.autotile){
			return -1;
		}else if (!this.autotile && t.autotile){
			return 1;
		}else{
			return this.tileID - t.tileID;
		}
	}
}
