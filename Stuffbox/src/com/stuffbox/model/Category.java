package com.stuffbox.model;

import java.util.ArrayList;

public class Category {
	private int id;
	private String name;
	private Icon icon;
	private ArrayList<Item> items;
	private int precategory;
	
	public Category(int id, String name, Icon icon, int precategory) {
		super();
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.precategory = precategory;
	}
	
	public Category() {
		super();
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
	
	public void setId(int id) {
		this.id = id;
	}
	
	public ArrayList<Item> getItems(){
		return items;
	}
	public boolean addItem(Item entry){
		return items.add(entry);
	}
	public int getPreCategory(){
		return precategory;
	}
	public void setPreCategory(int precategory){
		this.precategory = precategory;
	}
	public String toString(){
		return id + " " + name;
	}
	
}
