package com.aezart.isle.editor;

public class MapProperties {
	String name = "New Map";
	String tileset = "res/tiletest2.png";
	public int tile_side = 32;
	public int screen_xtiles = 19;
	public int screen_ytiles = 15;
	public int xscreens = 1;
	public int yscreens = 1;
	
	TileRef[][] mapTiles = new TileRef[screen_ytiles * yscreens][screen_xtiles * xscreens];
	
	public void updateAdjacency(int xt, int yt){
		if (yt < 0 || yt >= mapTiles.length){
			System.out.println("NULL");
			return;
		}
		if (xt < 0 || xt >= mapTiles[yt].length){
			System.out.println("NULL");
			return;
		}
		
		TileRef t = mapTiles[yt][xt];
		if (xt == 0){
			t.setAdjacent(TileRef.TOP_LEFT, true);
			t.setAdjacent(TileRef.MID_LEFT, true);
			t.setAdjacent(TileRef.BOT_LEFT, true);
		}else{
			t.setAdjacent(TileRef.MID_LEFT,  (mapTiles[yt][xt-1].tileID.autotilingType == t.tileID.autotilingType));
		}
		
		if (xt == mapTiles[yt].length - 1){
			t.setAdjacent(TileRef.TOP_RIGHT, true);
			t.setAdjacent(TileRef.MID_RIGHT, true);
			t.setAdjacent(TileRef.BOT_RIGHT, true);
		}else{
			t.setAdjacent(TileRef.MID_RIGHT,  (mapTiles[yt][xt+1].tileID.autotilingType == t.tileID.autotilingType));
			
			
		}
		
		if (yt == 0){
			t.setAdjacent(TileRef.TOP_LEFT, true);
			t.setAdjacent(TileRef.TOP_MID, true);
			t.setAdjacent(TileRef.TOP_RIGHT, true);
		}else{
			t.setAdjacent(TileRef.TOP_MID,  (mapTiles[yt-1][xt].tileID.autotilingType == t.tileID.autotilingType));
		}
		
		if (yt == mapTiles.length - 1){
			t.setAdjacent(TileRef.BOT_LEFT, true);
			t.setAdjacent(TileRef.BOT_MID, true);
			t.setAdjacent(TileRef.BOT_RIGHT, true);
		}else{
			t.setAdjacent(TileRef.BOT_MID,  (mapTiles[yt+1][xt].tileID.autotilingType == t.tileID.autotilingType));
		}
		
		if (xt > 0 && yt > 0){
			t.setAdjacent(TileRef.TOP_LEFT,  (mapTiles[yt-1][xt-1].tileID.autotilingType == t.tileID.autotilingType));
		}
		
		if (xt > 0 && yt < mapTiles.length - 1){
			t.setAdjacent(TileRef.BOT_LEFT,  (mapTiles[yt+1][xt-1].tileID.autotilingType == t.tileID.autotilingType));
		}
		
		if (xt < mapTiles[yt].length -1 && yt > 0){
			t.setAdjacent(TileRef.TOP_RIGHT,  (mapTiles[yt-1][xt+1].tileID.autotilingType == t.tileID.autotilingType));
		}
		
		if (xt < mapTiles[yt].length-1 && yt < mapTiles.length-1){
			t.setAdjacent(TileRef.BOT_RIGHT,  (mapTiles[yt+1][xt+1].tileID.autotilingType == t.tileID.autotilingType));
		}
		
	}
}
