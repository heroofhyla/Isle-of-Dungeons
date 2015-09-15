package com.aezart.isle.editor;

public class Tile implements Comparable<Tile>{
	int tileID;
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
		/*if (this.autotile && ! t.autotile){
			return -1;
		}else if (!this.autotile && t.autotile){
			return 1;
		}else{
			return this.tileID - t.tileID;
		}*/
		return this.tileID - t.tileID;
	}
	
	public Tile(){
	}
	/**
	 * @param Source tile to copy tileID, paletteX, and paletteY from
	 * @param copyAutoTiling If true, keep the same autotiling configuration as the source tile
	 */
	public Tile(Tile t, boolean copyAutoTiling){
		this.tileID = t.tileID;
		this.paletteX = t.paletteX;
		this.paletteY = t.paletteY;
		this.autotile = t.autotile;
		if (copyAutoTiling){
			tl = t.tl;
			tm = t.tm;
			tr = t.tr;
			mr = t.mr;
			br = t.br;
			bm = t.bm;
			bl = t.bl;
			ml = t.ml;
		}
	}
}
