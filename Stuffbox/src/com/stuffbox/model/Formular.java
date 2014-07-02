package com.stuffbox.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Formular implements Serializable {
	//TODO mach das Sinn die hier zu speichern
	public static final long idOfNameFeature = 1;
	public static final long idOfPictureFeature = 2;
	
	private long id;
	private String name;
	private ArrayList<Feature> features;
	public Formular(long id, String name, ArrayList<Feature> features) {
		super();
		this.id = id;
		this.name = name;
		this.features = features;
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
	public ArrayList<Feature> getFeatures() {
		return features;
	}
	public void removeFeature(Feature feature){
		features.remove(feature);
	}
	public void addFeature(Feature feature){
		features.add(feature);
	}
}
