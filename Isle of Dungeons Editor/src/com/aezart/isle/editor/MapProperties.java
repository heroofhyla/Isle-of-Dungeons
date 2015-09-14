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
}
