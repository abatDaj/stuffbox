package com.stuffbox.model;

public class Item {
	private long id;
	private String name;
	private Formular formular;
	
	public Item(long id, String name, Formular formular) {
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
	public long getId() {
		return id;
	}
	public Formular getFormular(){
		return formular;
	}
	public void setFormular(Formular formular){
		this.formular = formular;
	}
}
