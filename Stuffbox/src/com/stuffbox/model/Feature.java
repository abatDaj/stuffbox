package com.stuffbox.model;

import java.io.Serializable;

public class Feature implements Comparable<Feature>, Serializable{
	private long id;
	private Integer sortnumber; //TODO besserer name
	private String name;
	private FeatureType type;
	private Object value;
	
	public Feature(long id, String name, FeatureType type) {
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
	public long getId() {
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
	@Override
	public String toString(){
		return id + " " + name + " " + type;
	}
	@Override
	public int compareTo(Feature another) {
		sortnumber.compareTo(another.getSortnumber());
		return 0; // TODO ?!?
	}
}
