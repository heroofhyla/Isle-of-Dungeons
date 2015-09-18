package com.aezart.isle.editor;

public class MapProperties {
	String name = "New Map";
	String tileset = "res/exterior05.png";
	public int tile_side = 16;
	public int screen_xtiles = 20;
	public int screen_ytiles = 15;
	public int xscreens = 5;
	public int yscreens = 5;
	
	Tile[][] mapTiles = new Tile[screen_ytiles * yscreens][screen_xtiles * xscreens];
	
	public void updateAdjacency(int xt, int yt){
		if (yt < 0 || yt >= mapTiles.length){
			System.out.println("NULL");
			return;
		}
		if (xt < 0 || xt >= mapTiles[yt].length){
			System.out.println("NULL");
			return;
		}
		
		Tile t = mapTiles[yt][xt];
		if (xt == 0){
			t.tl = true;
			t.ml = true;
			t.bl = true;
		}else{
			t.ml = (mapTiles[yt][xt-1].autotilingType == t.autotilingType);
		}
		if (xt == mapTiles[yt].length - 1){
			System.out.println("right edge");
			t.tr = true;
			t.mr = true;
			t.br = true;
		}else{
			t.mr = (mapTiles[yt][xt+1].autotilingType == t.autotilingType);
		}
		if (yt == 0){
			t.tl = true;
			t.tm = true;
			t.tr = true;
		}else{
			t.tm = (mapTiles[yt-1][xt].autotilingType == t.autotilingType);
		}
		if (yt == mapTiles.length - 1){
			t.bl = true;
			t.bm = true;
			t.br = true;
		}else{
			t.bm = (mapTiles[yt+1][xt].autotilingType == t.autotilingType);
		}
		
		if (xt > 0 && yt > 0){
			t.tl = mapTiles[yt-1][xt-1].autotilingType == t.autotilingType;
		}
		
		if (xt > 0 && yt < mapTiles.length - 1){
			t.bl = mapTiles[yt+1][xt-1].autotilingType == t.autotilingType;
		}
		
		if (xt < mapTiles[yt].length -1 && yt > 0){
			t.tr = mapTiles[yt-1][xt+1].autotilingType == t.autotilingType;
		}
		
		if (xt < mapTiles[yt].length-1 && yt < mapTiles.length-1){
			t.br = mapTiles[yt+1][xt+1].autotilingType == t.autotilingType;
		}
	}
}
