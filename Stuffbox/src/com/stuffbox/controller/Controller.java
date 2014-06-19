package com.stuffbox.controller;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ImageView;

import com.stuffbox.R;
import com.stuffbox.model.DatabaseHandler;
import com.stuffbox.model.Feature;
import com.stuffbox.model.FeatureType;
import com.stuffbox.model.Icon;

public class Controller {
	private static DatabaseHandler databaseHandler;
	private static ArrayList<FeatureType> types;
	private static ArrayList<Icon> icons;
	
	public static void initialize(Context context) {
		//initialize database handler
		databaseHandler = new DatabaseHandler(context);
		//initialize static data
		getTypes();
		getIcons();
        //Debugeintr�ge schreiben
		//insertFeature("Test", types.get(0));
		
	}
	
    /**
     * Gibt eine List aller Arten zur�ck
     * @return
     */
    public static ArrayList<FeatureType> getTypes() {
    	if(types == null){
    		types = databaseHandler.getTypes();
    	}
    	return types;
    }
    /**
     * Gibt eine List aller Icons zur�ck
     * @return
     */
    public static ArrayList<Icon> getIcons() {
    	if(icons == null){
    		icons = databaseHandler.getIcons();
    	}
    	return icons;
    }
    /**
     * Gibt eine Liste aller Features zur�ck, deren ids in der id Liste enthalten ist
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
    /**
     * Setzt das Bild mit dem �bergebenen Namen auf den �bergebenen Imageview
     * @param context
     * @param view
     * @param imageName
     */
    public static void setImageOnImageView(Context context, ImageView view, String imageName){
	    int resourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
	    view.setImageDrawable(context.getResources().getDrawable( resourceId ));
    }
}
