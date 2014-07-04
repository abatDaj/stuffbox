package com.stuffbox.model;

public class Badge {
	private String catName;
	private Icon icon1;
	private Icon icon2;
	private Icon icon3;
	private Icon icon4;
	private Icon icon5;
	
	public Badge(String catName){
		this.catName = catName;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}
	
	
}
