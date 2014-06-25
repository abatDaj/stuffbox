package com.stuffbox.model;

import java.util.SortedSet;

public class Formular {
	private long id;
	private String name;
	private SortedSet<Feature> features;
	public Formular(long id, String name, SortedSet<Feature> features) {
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
	public SortedSet<Feature> getFeatures() {
		return features;
	}
	public void removeFeature(Feature feature){
		features.remove(feature);
	}
	public void addFeature(Feature feature){
		features.add(feature);
	}
}
