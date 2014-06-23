package com.stuffbox.controller;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ImageView;

import com.stuffbox.R;
import com.stuffbox.model.Category;
import com.stuffbox.model.DatabaseHandler;
import com.stuffbox.model.Feature;
import com.stuffbox.model.FeatureType;
import com.stuffbox.model.Icon;

public class Controller {
	private static DatabaseHandler databaseHandler;
	private static ArrayList<FeatureType> types;
	private static ArrayList<Icon> icons;
	
	//TODO eventuell Flag setzen und vor jeder Methode pr�fen ob bereits initialisiert wurde
	public static void initialize(Context context) {
		//initialize database handler
		databaseHandler = new DatabaseHandler(context);
		//initialize static data
		getTypes();
		getIcons();
		
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
    	if(icons == null)
    		icons = databaseHandler.getIcons();
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
     * Gibt eine Liste aller Kategorien zur�ck, deren ids in der id Liste enthalten ist
     * @param selectFeatureIds Liste aller zu selektierenden Ids (bei null werden alle geladen)
     * @param types
     * @return
     */
    public static ArrayList<Category> getCategories(ArrayList<Integer> selectFeatureIds) {
    	return databaseHandler.getCategories(selectFeatureIds, icons);
    }
    /**
     * F�gt Debugeintr�ge in die Tabelle Eigenschaft in die Datenbank ein
     */
    public static void insertDebugFeatureEntries(){
        //Debugeintr�ge schreiben
		insertFeature("Name", types.get(0));
		insertFeature("Seriennummer", types.get(1));
		insertFeature("Bewertung", types.get(2));
		insertFeature("gekauft am", types.get(3));
    }
    /**
     * F�gt Debugeintr�ge in die Tabelle Kategorie in die Datenbank ein
     */
    public static void insertDebugCategoryEntries(){
        //Debugeintr�ge schreiben
		insertCategory("B�cher", icons.get(0));
		insertCategory("Technik", icons.get(0));
		insertCategory("Musik", icons.get(0));
    }
    /**
     * Setzt die Datenbank neu auf
     */
    public static void initializeDatabase(){
    	databaseHandler.initializeDatabase();
    }
    
    /**
     * Speichert eine neue Kategorie in der Tabelle Eigenschaft.
     * @param database
     * @param name
     */
    public static void insertCategory(String name, Icon icon){
    	databaseHandler.insertCategory(name, icon);
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
    
    public static void fillIconTableWithSomeIcons(Context context) {
    	databaseHandler.fillIconTableWithSomeIcons(context);
    } 
}
