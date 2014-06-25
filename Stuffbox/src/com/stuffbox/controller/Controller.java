package com.stuffbox.controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ImageView;

import com.stuffbox.R;
import com.stuffbox.model.Category;
import com.stuffbox.model.DatabaseHandler;
import com.stuffbox.model.Feature;
import com.stuffbox.model.FeatureType;
import com.stuffbox.model.Formular;
import com.stuffbox.model.Icon;

public class Controller {
	
	private static Controller instance;
	private static DatabaseHandler databaseHandler;
	private static ArrayList<FeatureType> types;
	private static ArrayList<Icon> icons;
	
	private Controller (Context context) {
		databaseHandler = new DatabaseHandler(context);
		//initialize static data
		getTypes();
		//initialise database
        initializeDatabase(context);
		//initialize static data
		getTypes();
		getIcons();
	}

	public static Controller getInstance (Context context) {
		if (instance == null) {
	      instance = new Controller (context);
	    }
	    return instance;
	}
	
    /**
     * Gibt eine List aller Arten zurueck
     * @return
     */
    public static ArrayList<FeatureType> getTypes() {
    	if(types == null){
    		types = databaseHandler.getTypes();
    	}
    	return types;
    }
    /**
     * Gibt eine List aller Icons zurueck
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
    public static ArrayList<Feature> getFeatures(ArrayList<Long> selectFeatureIds) {
    	return databaseHandler.getFeatures(selectFeatureIds, types);
    }
    /**
     * Speichert eine neue Eigenschaft in der Tabelle Eigenschaft.
     * @param database
     * @param name
     */
    public static Feature insertFeature(String name, FeatureType featureType){
    	return databaseHandler.insertFeature(name, featureType);
    }
    /**
     * Gibt eine Liste aller Formulare zurueck, deren ids in der id Liste enthalten ist
     * @param selectFormularIds
     * @return
     */
    public static ArrayList<Formular> getFormulars(ArrayList<Long> selectFormularIds){
    	return databaseHandler.getFormulars(selectFormularIds);
    }
    
    /**
     * Speichert ein Formular in der Tabelle Formular und dessen zugeorndete
     * Eigenschaften in der Verkn�pfungstabelle.
     * @param name
     * @param features
     * @return
     */
    public static Formular insertFormlar(String name, ArrayList<Feature> features){
    	return databaseHandler.insertFormlar(name, features);
    }
    /**
     * Gibt eine Liste aller Kategorien zurueck, deren ids in der id Liste enthalten ist
     * @param selectFeatureIds Liste aller zu selektierenden Ids (bei null werden alle geladen)
     * @param types
     * @return
     */
    public static ArrayList<Category> getCategories(ArrayList<Long> selectFeatureIds) {
    	return databaseHandler.getCategories(selectFeatureIds, icons);
    }
    /**
     * Speichert eine neue Kategorie in der Tabelle Eigenschaft.
     * @param database
     * @param name
     */
    public static void insertCategory(String name, Icon icon, int precategory){
    	databaseHandler.insertCategory(name, icon, precategory);
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
     * Fuegt Debugeintraege in die Tabelle Eigenschaft in die Datenbank ein
     */
    public static ArrayList<Feature> insertDebugFeatureEntries(){
    	ArrayList<Feature> createdFeatures = new ArrayList<Feature>();
        //Debugeintraege schreiben
    	createdFeatures.add(insertFeature("Name", types.get(0)));
    	createdFeatures.add(insertFeature("Seriennummer", types.get(1)));
    	createdFeatures.add(insertFeature("Bewertung", types.get(2)));
    	createdFeatures.add(insertFeature("gekauft am", types.get(3)));
    	
    	return createdFeatures;
    }
    
    public static ArrayList<Formular> insertDebugFormularEntries(ArrayList<Feature> features){   	
    	ArrayList<Formular> createdFormulars = new ArrayList<Formular>();
        //Debugeintraege schreiben
    	//erstellen Formular Buecheraufbau
    	ArrayList<Feature> featuresFormular1 = new ArrayList<Feature>();
    	features.get(0).setSortnumber(1);
    	featuresFormular1.add(features.get(0));
    	features.get(2).setSortnumber(3);
    	featuresFormular1.add(features.get(2));
    	features.get(3).setSortnumber(2);
    	featuresFormular1.add(features.get(3));
    	createdFormulars.add(insertFormlar("Buecheraufbau", featuresFormular1));
    	
    	//erstellen Formular Musikaufbau
    	ArrayList<Feature> featuresFormular2 = new ArrayList<Feature>();
    	features.get(0).setSortnumber(1);
    	featuresFormular2.add(features.get(0));
    	features.get(3).setSortnumber(3);
    	featuresFormular2.add(features.get(3));
    	createdFormulars.add(insertFormlar("Musikaufbau", featuresFormular2));
    	
    	return createdFormulars;
    }
    
    /**
     * Fuegt Debugeintraege in die Tabelle Kategorie in die Datenbank ein
     */
    public static void insertDebugCategoryEntries(){
        //Debugeintraege schreiben
//		insertCategory("Buecher", icons.get(1), 0);
//		insertCategory("Technik", icons.get(4), 0);
//		insertCategory("Sport", icons.get(6), 0);
		insertCategory("Buecher", null, 0);
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
    	ArrayList<Feature> features = insertDebugFeatureEntries();
    	insertDebugFormularEntries(features);
    	
    	fillIconTableWithSomeIcons(context);
    	//TODO Icons von fill verwenden
    	getIcons();
    	insertDebugCategoryEntries();
    	
    	//ArrayList<Formular> formulars = getFormulars(null);    	
    }
    
    /**
     * Setzt das Bild mit dem uebergebenen Namen auf den �bergebenen Imageview
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
