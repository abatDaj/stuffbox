package com.stuffbox.model;

public class Category {
	private int id;
	private String name;
	
	public Category(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}
	
	public String toString(){
		return id + " " + name;
	}
	
}
