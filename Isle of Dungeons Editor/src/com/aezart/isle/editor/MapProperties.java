package com.aezart.isle.editor;

public class MapProperties {
	String name = "New Map";
	String tileset = "res/exterior03.png";
	public int tile_side = 16;
	public int screen_xtiles = 20;
	public int screen_ytiles = 15;
	public int xscreens = 5;
	public int yscreens = 5;
	
	Tile[][] mapTiles = new Tile[screen_ytiles * yscreens][screen_xtiles * xscreens];
	
	public void updateAdjacency(int xt, int yt){
		if (xt < 0 || xt >= mapTiles[yt].length - 1){
			System.out.println("NULL");
			return;
		}
		if (yt < 0 || yt >= mapTiles.length - 1){
			System.out.println("NULL");
			return;
		}
		Tile t = mapTiles[yt][xt];
		if (xt == 0){
			t.tl = true;
			t.ml = true;
			t.bl = true;
		}else{
			t.ml = (mapTiles[yt][xt-1].tileID == t.tileID);
		}
		if (xt == mapTiles[yt].length - 1){
			t.tr = true;
			t.mr = true;
			t.br = true;
		}else{
			t.mr = (mapTiles[yt][xt+1].tileID == t.tileID);
		}
		if (yt == 0){
			t.tl = true;
			t.tm = true;
			t.tr = true;
		}else{
			t.tm = (mapTiles[yt-1][xt].tileID == t.tileID);
		}
		if (yt == mapTiles.length){
			t.bl = true;
			t.bm = true;
			t.br = true;
		}else{
			t.bm = (mapTiles[yt+1][xt].tileID == t.tileID);
		}
		
		if (xt > 0 && yt > 0){
			t.tl = mapTiles[yt-1][xt-1].tileID == t.tileID;
		}
		
		if (xt > 0 && yt < mapTiles.length){
			t.bl = mapTiles[yt+1][xt-1].tileID == t.tileID;
		}
		
		if (xt < mapTiles[yt].length && yt > 0){
			t.tr = mapTiles[yt-1][xt+1].tileID == t.tileID;
		}
		
		if (xt < mapTiles[yt].length && yt < mapTiles.length){
			t.br = mapTiles[yt+1][xt+1].tileID == t.tileID;
		}
	}
}
