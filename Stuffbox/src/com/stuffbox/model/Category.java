package com.stuffbox.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable {
	private long id;
	private String name;
	private Icon icon;
	private ArrayList<Item> items;
	private long preCategoryId;
	
	public Category(long id, String name, Icon icon, long precategory) {
		super();
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.preCategoryId = precategory;
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
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public ArrayList<Item> getItems(){
		return items;
	}
	public boolean addItem(Item entry){
		return items.add(entry);
	}
	public long getPreCategoryId(){
		return preCategoryId;
	}
	public void setPreCategory(long precategory){
		this.preCategoryId = precategory;
	}
	@Override
	public String toString(){
		return id + " " + name;
	}
	
}
