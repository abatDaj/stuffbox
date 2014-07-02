package com.stuffbox.controller;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ImageView;

import com.stuffbox.R;
import com.stuffbox.model.Category;
import com.stuffbox.model.DataSourceCategory;
import com.stuffbox.model.DatabaseHandler;
import com.stuffbox.model.Feature;
import com.stuffbox.model.FeatureType;
import com.stuffbox.model.Formular;
import com.stuffbox.model.Icon;
import com.stuffbox.model.Item;

public class Controller {
	
	public final static String EXTRA_EDIT_CATEGORY = "stuffbox.com.edit.category";
	
	private static Controller instance = null;
	private DatabaseHandler databaseHandler;
	private ArrayList<FeatureType> types = null;
	private ArrayList<Icon> icons;
	private Category currentCategory;
	private Feature newInsertedFeature;
	private Formular newInsertedFormular;
	private Context context = null;
	private Boolean init = false;
	
	private  Controller (Context context) {
		this.context = context;
		databaseHandler = new DatabaseHandler(context);		
	}
	
	public void init (){
		if (init)
			return;
		getTypes();
		//initialise database
        initializeDatabase(context);
		//initialize data
		getIcons();
		init = true;
	}
	
	//TODO
	public static Controller getInstance (Context context) {
		if (instance == null) 
			instance = new Controller (context);
		return instance;
	}
	
	public static Controller getInstance () {			
		return instance;
	}
	
    /**
     * Gibt eine Liste aller Arten zurueck
     * @return
     */
    public ArrayList<FeatureType> getTypes() {
    	if(types == null){
    		types = databaseHandler.getTypes();
    	}
    	return types;
    }
    
    /**
     * Gibt die Arten zurueck deren Name dem übergebenen entspricht
     * @param name
     * @return
     */
    public FeatureType getTypeWithName(String name) {
    	ArrayList<FeatureType> features = getTypes();
    	for (FeatureType featureType : features) {
			if(featureType.toString().equals(name)){
				return featureType;
			}
		}
    	return null;
    }
    
    /**
     * Gibt eine List aller Icons zurueck
     * @return
     */
    public ArrayList<Icon> getIcons() {
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
    public ArrayList<Feature> getFeatures(ArrayList<Long> selectFeatureIds) {
    	return databaseHandler.getFeatures(selectFeatureIds, types);
    }
    /**
     * Speichert eine neue Eigenschaft in der Tabelle Eigenschaft und
     * speichert die Eigenschaft als zuletzt angelegte im Controller
     * @param database
     * @param name
     */
    public Feature insertFeature(String name, FeatureType featureType){
    	newInsertedFeature = databaseHandler.insertFeature(name, featureType);
    	return newInsertedFeature;
    }
	/**
	 * Gibt die zuletzt angelegte Eigenschaft zur�ck und
	 * entfernt sie aus dem Controller
	 * @return
	 */
	public Feature popLastInsertedFeature(){
		Feature feature = newInsertedFeature;
		newInsertedFeature = null;
		return feature;
	}
    /**
     * Gibt eine Liste aller Formulare zurueck, deren ids in der id Liste enthalten ist
     * @param selectFormularIds
     * @return
     */
    public ArrayList<Formular> getFormulars(ArrayList<Long> selectFormularIds){
    	return databaseHandler.getFormulars(selectFormularIds);
    }
    
    /**
     * Speichert ein Formular in der Tabelle Formular und dessen zugeorndete
     * Eigenschaften in der Verknuepfungstabelle.
     * Speichert das Formular als zuletzt angelegte im Controller
     * @param name
     * @param features
     * @return
     */
    public Formular insertFormlar(String name, ArrayList<Feature> features){
    	newInsertedFormular = databaseHandler.insertFormlar(name, features);
    	return newInsertedFormular;
    }
	/**
	 * Gibt die zuletzt angelegtes Formular zur�ck und
	 * entfernt es aus dem Controller
	 * @return
	 */
	public Formular popLastInsertedFormular(){
		Formular formular = newInsertedFormular;
		newInsertedFormular = null;
		return formular;
	}
	
    /**
     * Speichert ein Item in der Tabelle Item und dessen zugeorndete
     * Eigenschaften, Formular und Kategorie in den Verknuepfungstabellen.
     * Speichert das Formular als zuletzt angelegte im Controller
     * @param name
     * @param formular
     * @return
     */
    public Item insertItem(String name, Formular formular){
    	return databaseHandler.insertItem(name, formular);
    }
	
    /**
     * Gibt eine Liste aller Kategorien zurueck, deren ids in der id Liste enthalten ist
     * @param selectFeatureIds Liste aller zu selektierenden Ids (bei null werden alle geladen)
     * @param types
     * @return
     */
    public ArrayList<Category> getCategories(ArrayList<Long> selectFeatureIds) {
    	return databaseHandler.getCategories(selectFeatureIds, icons);
    }
    
    /**
	 * Returniert alle Unterkategorien einer Kategorie
	 * 
	 * @param categoryID
	 * @return Die Unterkategorien oder null, falls ein Fehler auftrat
	 */
    public ArrayList<Category> getSubCategories(long categoryId) {
    	return databaseHandler.getSubCategories(categoryId);
    }
    
    /**
     * Speichert eine neue Kategorie in der Tabelle Kategorie.
     * @param name
     * @param icon
     * @param precategory
     * @return
     */
    public Category insertCategory(String name, Icon icon, long precategory){
    	return databaseHandler.insertOrUpdateCategory(name, icon, precategory);
    }
    
    /**
     * Speichert die Aenderungen einer Kategorie in der Tabelle Kategorie.
     * @param category
     * @return
     */
    public Category updateCategory(Category category){
    	return databaseHandler.updateCategory(category);
    }
    
    /**
     * Speichert eine neues Icon in der Tabelle Icon
     * @param name
     * @param description
     */
    public void insertIcon(String name, String description){
    	databaseHandler.insertIcon(name, description);
    }
    
    /**
     * Fuegt Debugeintraege in die Tabelle Eigenschaft in die Datenbank ein
     */
    public ArrayList<Feature> insertDebugFeatureEntries(){
    	ArrayList<Feature> createdFeatures = new ArrayList<Feature>();
        //Debugeintraege schreiben
//    	createdFeatures.add(insertFeature("Name", types.get(0)));
    	createdFeatures.add(insertFeature("Seriennummer", types.get(1)));
    	createdFeatures.add(insertFeature("Bewertung", types.get(2)));
    	createdFeatures.add(insertFeature("gekauft am", types.get(3)));
    	
    	return createdFeatures;
    }
    
    public ArrayList<Formular> insertDebugFormularEntries(ArrayList<Feature> features){   	
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
    	
    	//erstellen Formular Filmaufbau
    	ArrayList<Feature> featuresFormular3 = new ArrayList<Feature>();
    	features.get(0).setSortnumber(1);
    	featuresFormular3.add(features.get(0));
    	features.get(3).setSortnumber(3);
    	featuresFormular3.add(features.get(3));
    	createdFormulars.add(insertFormlar("Filmaufbau", featuresFormular3));
    	
    	//erstellen Formular Haustieraufbau
    	ArrayList<Feature> featuresFormular4 = new ArrayList<Feature>();
    	features.get(0).setSortnumber(1);
    	featuresFormular4.add(features.get(0));
    	features.get(3).setSortnumber(3);
    	featuresFormular4.add(features.get(3));
    	createdFormulars.add(insertFormlar("Haustieraufbau", featuresFormular4));
    	
    	//erstellen Formular Elektrogeraetaufbau
    	ArrayList<Feature> featuresFormular5 = new ArrayList<Feature>();
    	features.get(0).setSortnumber(1);
    	featuresFormular5.add(features.get(0));
    	features.get(3).setSortnumber(3);
    	featuresFormular5.add(features.get(3));
    	createdFormulars.add(insertFormlar("Elektrogerätaufbau", featuresFormular5));
    	
    	return createdFormulars;
    }
        
    /**
     * Fuegt Debugeintraege in die Tabelle Kategorie in die Datenbank ein
     */
    public void insertDebugCategoryEntries(){
        //Debugeintraege schreiben
		insertCategory("Buecher", icons.get(1), DatabaseHandler.INDEX_OF_ROOT_CATEGORY);
		insertCategory("Sportartikel", icons.get(6), DatabaseHandler.INDEX_OF_ROOT_CATEGORY);
		insertCategory("Musik", icons.get(7), DatabaseHandler.INDEX_OF_ROOT_CATEGORY);
		insertCategory("Suppen", icons.get(5), DatabaseHandler.INDEX_OF_ROOT_CATEGORY);
		insertCategory("Sonstiges", icons.get(3), DatabaseHandler.INDEX_OF_ROOT_CATEGORY);
    }
    
    /**
     * temporär: Füllt ein paar die Tabelle mit ein paar Icons.
     * 
     */
    public void fillIconTableWithSomeIcons (Context context)
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
    public void initializeDatabase(Context context){
    	databaseHandler.initializeDatabase();
    	insertDebugFeatureEntries();
    	ArrayList<Feature> features = getFeatures(null);
    	insertDebugFormularEntries(features);
    	fillIconTableWithSomeIcons(context);
    	//TODO Icons von fill verwenden
    	getIcons();
    	
		Category currentCategory = insertCategory(DataSourceCategory.ROOT_CATEGORY, icons.get(3), -1);    
		this.setCurrentCategory(currentCategory);
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
    public void setImageOnImageView(Context context, ImageView view, String imageName){
	    int resourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
	    view.setImageDrawable(context.getResources().getDrawable( resourceId ));
    }
    
    /**
     * Returniert die aktuelle Kategorie
     * 
     * @return Die aktuelle Kategorie
     */
    public Category getCurrentCategory() { 
    	return currentCategory;
    }
    
    public boolean deleteCategory(Category category) { 
    	return databaseHandler.deleteCategory(category);
    }
    
    /**
     * Überschreibt die aktuelle Kategorie
     * 
	 * @param newCurrentCategory
     */
    public void setCurrentCategory(Category newCurrentCategory) { 
    	currentCategory = newCurrentCategory;
    }
    
	/**
	 * Gibt Oberkategorie zurueck
	 * 
	 * @param database
	 * @param category
	 * @return Die Oberkategorien
	 */
	public Category getPreCategory(Category category) {
		return databaseHandler.getPreCategory(category);
	}	
    
	/**
	 * Returniert die neuste Reihe (Cursor) einer Tabelle mit den angegebenen Spalten.
	 * Es wird der SQL Befehl: "Select MAX(nameOfIdColumn), columns[0], columns[1]... from tabelName;"
	 * ausgeführt.
	 *  
	 * @param tabelName
	 * @param nameOfIdColumn Solle ein Primärschlüssel sein und AUTOINCREMENT.
	 * @param columns
	 *
	 * @return Den Cursor der letzten Reihe der Tabelle oder null, falls die Tabelle leer ist.
	 */
	public Cursor getNewestRow (String tabelName, String nameOfIdColumn, String[] columns) {
		return databaseHandler.getNewestRow(tabelName, nameOfIdColumn, columns);
	}
    
    public ArrayList<Object> getEntities(String tableName,
			String column,
			ArrayList<Object> selectValues,
			Class clas) { 
    	return databaseHandler.getEntities (tableName, column, selectValues, clas);
    }
    
}
