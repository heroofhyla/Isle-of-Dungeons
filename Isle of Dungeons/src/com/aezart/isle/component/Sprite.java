package com.aezart.isle.component;

import java.awt.image.BufferedImage;

public class Sprite extends Component{
	@Override
	public String toString(){
		return "sprite";
	}
	
	BufferedImage sprite;
	
	public Sprite(BufferedImage sprite){
		this.sprite = sprite;
	}
	
	public void setSprite(BufferedImage sprite){
		this.sprite = sprite;
	}
}
