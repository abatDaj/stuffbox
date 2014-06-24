package com.stuffbox.model;

public class Item {
	private int id;
	private String name;
	private Formular formular;
	
	public Item(int id, String name, Formular formular) {
		super();
		this.id = id;
		this.name = name;
		this.formular = formular;
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
	public Formular getFormular(){
		return formular;
	}
	public void setFormular(Formular formular){
		this.formular = formular;
	}
}
