package com.stuffbox.controller;

import java.util.ArrayList;

import android.content.Context;

import com.stuffbox.model.DatabaseHandler;
import com.stuffbox.model.Feature;
import com.stuffbox.model.FeatureType;

public class Controller {
	private static DatabaseHandler databaseHandler;
	private static ArrayList<FeatureType> types;
	
	public static void initialize(Context context) {
		//initialize database handler
		databaseHandler = new DatabaseHandler(context);
		//initialize types
		getTypes();
	}
	
    /**
     * Gibt eine List aller Arten zurück
     * @return
     */
    public static ArrayList<FeatureType> getTypes() {
    	if(types == null){
    		types = databaseHandler.getTypes();
    	}
    	return types;
    }
    /**
     * Gibt eine Liste aller Features zurück, deren ids in der id Liste enthalten ist
     * @param selectFeatureIds Liste aller zu selektierenden Ids (bei null werden alle geladen)
     * @param types
     * @return
     */
    public static ArrayList<Feature> getFeatures(ArrayList<Integer> selectFeatureIds) {
    	return databaseHandler.getFeatures(selectFeatureIds, types);
    }
    /**
     * Speichert eine neue Eigenschaft in der Tabelle Eigenschaft.
     * @param database
     * @param name
     */
    public static void insertFeature(String name, FeatureType featureType){
    	databaseHandler.insertFeature(name, featureType);
    }
}
