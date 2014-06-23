package com.stuffbox.model;

public class Feature implements Comparable<Feature>{
	private int id;
	private Integer sortnumber; //TODO besserer name
	private String name;
	private FeatureType type;
	private Object value;
	
	public Feature(int id, String name, FeatureType type) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public FeatureType getType() {
		return type;
	}
	public void setType(FeatureType type) {
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public int getSortnumber(){
		return sortnumber;
	}
	public void setSortnumber(int sortnumber){
		this.sortnumber = sortnumber;
	}
	public Object getValue(){
		return value;
	}
	public void setValue(Object value){
		this.value = value;
	}
	public String toString(){
		return id + " " + name + " " + type;
	}
	@Override
	public int compareTo(Feature another) {
		sortnumber.compareTo(another.getSortnumber());
		return 0;
	}
	
}
