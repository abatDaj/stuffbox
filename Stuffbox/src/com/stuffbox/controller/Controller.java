package com.stuffbox.controller;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
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
	private static boolean wasInitialized = false;
	
	/**
	 * Initialisiert Controller falls er nicht bereits initialisiert wurde
	 * @param context
	 */
	public static void initialize(Context context) {
		if(!wasInitialized){
			//initialize database handler
			databaseHandler = new DatabaseHandler(context);
			//initialize static data
			getTypes();
			getIcons();
			//initialise database
	        initializeDatabase(context);
			//initialize static data
			getTypes();
			getIcons();
			
			wasInitialized = true;
		}
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
     * Gibt eine Liste aller Features zurueck, deren ids in der id Liste enthalten ist
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
     * Gibt eine Liste aller Kategorien zurueck, deren ids in der id Liste enthalten ist
     * @param selectFeatureIds Liste aller zu selektierenden Ids (bei null werden alle geladen)
     * @param types
     * @return
     */
    public static ArrayList<Category> getCategories(ArrayList<Integer> selectFeatureIds) {
    	return databaseHandler.getCategories(selectFeatureIds, icons);
    }
    /**
     * Fuegt Debugeintraege in die Tabelle Eigenschaft in die Datenbank ein
     */
    public static void insertDebugFeatureEntries(){
        //Debugeintraege schreiben
		insertFeature("Name", types.get(0));
		insertFeature("Seriennummer", types.get(1));
		insertFeature("Bewertung", types.get(2));
		insertFeature("gekauft am", types.get(3));
    }
    /**
     * Fuegt Debugeintraege in die Tabelle Kategorie in die Datenbank ein
     */
    public static void insertDebugCategoryEntries(){
        //Debugeintraege schreiben
		insertCategory("Buecher", icons.get(1));
		insertCategory("Technik", icons.get(4));
		insertCategory("Sport", icons.get(6));
    }
    
    /**
     * temporär: Füllt ein paar die Tabelle mit ein paar Icons.
     * 
     */
    public static void fillIconTableWithSomeIcons (Context context)
    {
    	Field[] drawableFields = R.drawable.class.getFields();
		
		// holt alle Icons mit dem Prefix "category_icon_"
		for (int i = 0; i < drawableFields.length; i++)
			if (drawableFields[i].getName().contains( context.getResources().getText(R.string.prefix_icon_category)))  
				databaseHandler.insertIcon(drawableFields[i].getName(),"egal");
    }
    
    /**
     * Setzt die Datenbank neu auf
     */
    public static void initializeDatabase(Context context){
    	databaseHandler.initializeDatabase();
    	insertDebugCategoryEntries();
    	insertDebugFeatureEntries();
    	fillIconTableWithSomeIcons(context);
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
     * Speichert eine neues Icon in der Tabelle Icon
     * @param name
     * @param description
     */
    public static void insertIcon(String name, String description){
    	databaseHandler.insertIcon(name, description);
    }
    
    /**
     * Setzt das Bild mit dem �bergebenen Namen auf den �bergebenen Imageview
     * 	//Beispielcode um Image auf ImageView zu setzen
     * 	//    ImageView img = (ImageView) findViewById(R.id.testimage);
     * 	//    Icon icon = Controller.getIcons().get(0);
     * 	//    Controller.setImageOnImageView(this, img, icon.getName());
     * @param context
     * @param view
     * @param imageName
     */
    public static void setImageOnImageView(Context context, ImageView view, String imageName){
	    int resourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
	    view.setImageDrawable(context.getResources().getDrawable( resourceId ));
    }
    
}
