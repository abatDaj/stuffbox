package com.stuffbox.model;

import android.util.Log;

public enum FeatureType {
	Text(1l),
	Dezimalzahl(2l),
	Ganzzahl(3l),
	Datum(4l),
	Ranking(5l),
	Foto(6l),
	Wahrheitswert(7l);
	
	private Long id;
	
	FeatureType( Long id ){
		this.id = id;
	}
	public Long getId(){
		return this.id;
	}
	
	public static FeatureType getFeatureType(long id, String name) {
		for (FeatureType type : FeatureType.values()) {
			if(type.getId() == id && type.toString().equals(name)){
				return type;
			}
		}
		Log.e(FeatureType.class.getSimpleName(), "Type " + name + " mit ID " + id + " ist nicht vorgesehen");
		return null;
	}
}
