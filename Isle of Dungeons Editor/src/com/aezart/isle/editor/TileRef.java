package com.aezart.isle.editor;

public class TileRef {
	public static final byte TOP_LEFT = 0;
	public static final byte TOP_MID = 1;
	public static final byte TOP_RIGHT = 2;
	public static final byte MID_RIGHT = 3;
	public static final byte BOT_RIGHT = 4;
	public static final byte BOT_MID = 5;
	public static final byte BOT_LEFT = 6;
	public static final byte MID_LEFT = 7; 
	Tile tileID;
	byte adjacency;
	
	public void setAdjacent(byte corner, boolean adjacent){
		
		byte bit = (byte) ((1) << corner);
		adjacency = (byte) (adjacent?(adjacency|bit):(adjacency&(~bit)));
	}
	
	public boolean isAdjacent(byte corner){
		return ((adjacency >> corner) & 1) == 1;	 
	}
	
	public TileRef(){
		tileID = null;
		adjacency = (byte) 0xFF;
	}
	
	public TileRef(Tile tileID){
		this.tileID = tileID;
		adjacency = (byte) 0xFF;
	}
	
	
	public TileRef(Tile tileID, byte adjacency){
		this.tileID = tileID;
		this.adjacency = adjacency;
	}
	
}
