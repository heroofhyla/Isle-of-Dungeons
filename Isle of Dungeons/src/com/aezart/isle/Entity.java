package com.aezart.isle;

import java.util.HashMap;

import com.aezart.isle.component.Component;

public class Entity {
	HashMap<String, Component> components = new HashMap<String, Component>();
	
	public void addComponent(Component c){
		components.put(c.toString(), c);
	}
	
	public boolean hasComponent(String s){
		return components.containsKey(s);
	}
	
	public Component getComponent(String s){
		return components.get(s);
	}
}
