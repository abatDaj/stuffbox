package com.stuffbox.model;

public class Category {
	private int id;
	private String name;
	private Icon icon;
	
	public Category(int id, String name, Icon icon) {
		super();
		this.id = id;
		this.name = name;
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Icon getIcon() {
		return this.icon;
	}

	public void setIcon(Icon icon){
		this.icon = icon;
	}
	
	public int getId() {
		return id;
	}
	
	public String toString(){
		return id + " " + name;
	}
	
}
