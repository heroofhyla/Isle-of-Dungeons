package com.aezart.isle;

import java.awt.Color;
import java.io.InputStream;
import java.util.Properties;

public class Globals {
	public final static int TILE_SIDE;
	public final static int SURFACE_XTILES;
	public final static int SURFACE_YTILES;
	public final static int SURFACE_XPX;
	public final static int SURFACE_YPX;
	
	public final static int SCALE;
	public final static String TITLEBAR;
	public final static Color BLANK_COLOR;
	
	public final static long NANOTIMESTEP;
	
	static {
		int tile_side = 16;
		int surface_xtiles = 20;
		int surface_ytiles = 15;
		int surface_xpx = tile_side * surface_xtiles;
		int surface_ypx = tile_side * surface_ytiles;
		int scale = 3;
		String titlebar = "Isle of Dungeons";
		Color blank_color = Color.black;
		long nanotimestep = 50 * 1_000_000;
		
		Properties props = new Properties();
		String fname = "settings/isle.properties";
		InputStream pstream = Globals.class.getClassLoader().getResourceAsStream(fname);
		try {
			props.load(pstream);
			tile_side = Integer.parseInt(props.getProperty("TILE_SIDE"));
			surface_xtiles = Integer.parseInt(props.getProperty("SURFACE_XTILES"));
			surface_ytiles = Integer.parseInt(props.getProperty("SURFACE_YTILES"));
			surface_xpx = tile_side * surface_xtiles;
			surface_ypx = tile_side * surface_ytiles;
			scale = Integer.parseInt(props.getProperty("SCALE"));
			titlebar = props.getProperty("TITLEBAR");
			blank_color = new Color(Integer.parseInt(props.getProperty("BLANK_COLOR"), 16));
			nanotimestep = Long.parseLong(props.getProperty("NANOTIMESTEP"));
			}catch (Exception e){
				e.printStackTrace();
				System.out.println("Couldn't load isle.properties");
			}finally{
				TILE_SIDE = tile_side;
				SURFACE_XTILES = surface_xtiles;
				SURFACE_YTILES = surface_ytiles;
				SURFACE_XPX = surface_xpx;
				SURFACE_YPX = surface_ypx;
				SCALE = scale;
				TITLEBAR = titlebar;
				BLANK_COLOR = blank_color;
				NANOTIMESTEP = nanotimestep;
			}

		}
}
