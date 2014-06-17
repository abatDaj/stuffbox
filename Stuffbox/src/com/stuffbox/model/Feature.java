package com.stuffbox.model;

public class Feature {
	private int id;
	private String name;
	private FeatureType type;
	public Feature(int id, String name, FeatureType type) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public FeatureType getType() {
		return type;
	}
	public void setType(FeatureType type) {
		this.type = type;
	}
	public int getId() {
		return id;
	}
	
	public String toString(){
		return id + " " + name + " " + type;
	}
	
}
