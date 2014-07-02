package com.stuffbox.model;

import java.util.ArrayList;

public class Item {
	private long id;
	private String name;
	private Formular formular;
	private ArrayList<Category> categories;
	
	public Item(long id, String name, Formular formular, ArrayList<Category> categories) {
		super();
		this.id = id;
		this.name = name;
		this.formular = formular;
		this.categories = categories;
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
	
	public ArrayList<Category> getCategories(){
		return categories;
	}
	
	public void setCategories(ArrayList<Category> categories){
		this.categories = categories;
	}
}
