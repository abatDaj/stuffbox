package com.stuffbox.model;

import java.util.ArrayList;

public class Category {
	private int id;
	private String name;
	private Icon icon;
	private ArrayList<Item> items;
	private ArrayList<Category> categories;
	
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
	public ArrayList<Item> getItems(){
		return items;
	}
	public boolean addItem(Item entry){
		return items.add(entry);
	}
	public ArrayList<Category> getCategories(){
		return categories;
	}
	public boolean addCategory(Category category){
		return categories.add(category);
	}
	public String toString(){
		return id + " " + name;
	}
	
}
