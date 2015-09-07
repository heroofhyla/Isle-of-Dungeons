package com.aezart.isle;

public class Vector2D {
	public double x;
	public double y;
	
	public Vector2D(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	
	
	public double magnitude(){
		return Math.sqrt(x*x + y*y);
	}
	
	public double unitX(){
		return x/magnitude();
	}
	
	public double unitY(){
		return y/magnitude();
	}
}
