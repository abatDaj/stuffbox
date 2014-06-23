package com.stuffbox.model;

public class Item {
	private int id;
	private String name;
	private Formular formular;
	
	//TODO Prüfen ob hier auch noch die zugehörigen Kategorien gespeichert werden sollen
	
	public Item(int id, String name) {
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
}
