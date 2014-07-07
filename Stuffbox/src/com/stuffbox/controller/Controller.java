package com.stuffbox.controller;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

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
	public final static String EXTRA_FORMULAR_FOR_NEW_ITEM="stuffbox.com.new.formular.for.item";
	
	public final static String DIALOG_ARGUMENT_YES="stuffbox.com.dialog_yes";
	public final static String DIALOG_ARGUMENT_NO="stuffbox.com.dialog_no";
	public final static String DIALOG_ARGUMENT_YES_NO_QUESTION ="stuffbox.com.dialog_question";
	public final static String DIALOG_ARGUMENT_NOTICE_TEXT ="stuffbox.com.dialog.notice.text";

	public final static int NUMBER_STARS_OF_RANKING = 9; 
	public final static int NUMBER_CHARS_OF_MOST_EDIT_TEXTS_IN_ICON_SCREEN = 7; 
	public final static int NUMBER_CHARS_OF_LARGER_EDIT_TEXTS_IN_ICON_SCREEN = 12; 
	public final static int BADGE_ICON_SIZE = 70; 

	
	
	public final static int DEFAULT_RANKING_VALUE = 5;
	
	// Default-Wert, die Items für Bilder bekommen, wenns keins
	// geschossen wurde.
	public final static String DEFAULT_ICON_VALUE_FOR_PICTURE = "stuffbox.item.without.picture";

	
	// Für die Bild-Galerie Funktion
	public static final int REQUEST_CAMERA = 42;
	public static final int SELECT_FILE = 1;

	public static final int  REQUEST_CODE_CHOOSE_ICON = 77;
	
	// Java-CSS
	public static final int CSS_TEXT_SIZE_LABELS = 16;
	

	private static Controller instance = null;
	private DatabaseHandler databaseHandler;

	private ArrayList<FeatureType> types = null;
	private ArrayList<Icon> icons;
	private ArrayList<Category> selectedCategoriesInItem = null; 
	private Category currentCategory;
	private Feature currentFeature;
	private Formular currentFormular;
	private Item currentItem;
	private Feature newInsertedFeature;
	private Formular newInsertedFormular;
	private Item newInsertedItem;
	private Context context = null;
	private Boolean init = false;
	
	private  Controller (Context context) {
		this.context = context;
		databaseHandler = new DatabaseHandler(context);		
	}
	
	public void init (){
		if (init){
			return;
		}
		//initialise database
        initializeDatabase(context);
		init = true;
	}
	
	//TODO
	public static Controller getInstance (Context context) {
		if (instance == null){
			instance = new Controller (context);
		}
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
     * Speichert eine neue Eigenschaft in der Tabelle Eigenschaft und
     * speichert die Eigenschaft als zuletzt angelegte im Controller
     * @param database
     * @param name
     */
    public Feature updateFeature(Feature feature){
    	newInsertedFeature = databaseHandler.updateFeature(feature);
    	return newInsertedFeature;
    }
    /**
     * Loescht Eigenschaften von der Datenbank
     * @param features
     */
    public boolean deleteFeatures(ArrayList<Feature> features){
    	return databaseHandler.deleteFeatures(features);
    }
	/**
	 * Gibt die zuletzt angelegte Eigenschaft zurueck und
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
     * Speichert die Aenderungen an einem Formular in der Tabelle Formular und 
     * dessen zugeorndete Eigenschaften in der Verknuepfungstabelle.
     * @param formular
     * @return
     */
    public Formular updateFormlar(Formular formular){
    	return databaseHandler.updateFormlar(formular);
    }
    /**
     * Loescht Formulare von der Datenbank
     * @param formulare
     */
    public boolean deleteFormulars(ArrayList<Formular> formulars){
    	return databaseHandler.deleteFormulars(formulars);
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
    public Item insertItem(String name, Formular formular, ArrayList<Category> categories){
    	newInsertedItem = databaseHandler.insertItem(name, formular, categories);
    	return newInsertedItem;
    }
	
	/**
	 * Gibt die zuletzt angelegtes Item zurueck und
	 * entfernt es aus dem Controller
	 * @return
	 */
	public Item popLastInsertedItem(){
		Item formular = newInsertedItem;
		newInsertedItem = null;
		return formular;
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
    
    public Category getRootCategory(){
    	ArrayList<Long> ids = new ArrayList<Long>();
    	ids.add((long) DatabaseHandler.INDEX_OF_ROOT_CATEGORY);
    	return databaseHandler.getCategories(ids, icons).get(0);
    }
    
    /**
     * Speichert eine neue Kategorie in der Tabelle Kategorie.
     * @param name
     * @param icon
     * @param precategory
     * @return
     */
    public Category insertCategory(String name, Icon icon, long precategory){
    	return databaseHandler.insertOrUpdateCategory(new Category(DatabaseHandler.INITIAL_ID, name, icon, precategory));
    }
    
    /**
     * Speichert die Aenderungen einer Kategorie in der Tabelle Kategorie.
     * @param category
     * @return
     */
    public Category updateCategory(Category category){
    	return databaseHandler.insertOrUpdateCategory(category);
    }
    
    public boolean deleteCategory(Category category) { 
    	return databaseHandler.deleteCategory(category);
    }
    
	/**
	 * Loescht alle Items einer Kategorie
	 * 
	 * @param categoryID
	 * @return Ob es erfolgreich geloescht wurde 
	 */
	public boolean deleteItemsOfCategory(long categoryID) {
		return databaseHandler.deleteItemsOfCategory(categoryID);
	}
	
	/**
	 * Loescht eine Kategorie und alle Unterkategorien und die jeweiligen Items.
	 * 
	 * @param database
	 * param category
	 * @return Ob alles erfolgreich geloescht wurde 
	 */
	public boolean deleteCategoryRecursively(Category categoryToDeleteRecursively) {
		return databaseHandler.deleteCategoryRecursively(categoryToDeleteRecursively);
	}
    
    /**
     * Gibt eine Liste aller Items zurueck, , deren ids in der id Liste enthalten ist
     * @param selectIds Liste aller zu selektierenden Ids (bei null werden alle geladen)
     * @return
     */
    public ArrayList<Item> getItems(ArrayList<Long> selectIds){
    	return databaseHandler.getItems(selectIds);
    }
    
    /**
     * Gibt eine Liste aller Items zurueck, die einer Kategorie enthalten sind.
     * @param database
     * @param categoryID 
     * @return Die Items in der spezifizierten Kategorie
     */
    public ArrayList<Item> getItemsOfACategory(long categoryID) {
    	return databaseHandler.getItemsOfACategory(categoryID);
    }    
    
    public ArrayList<Item> getItemsFromWordMatches(String query, String[] columns){
    	return databaseHandler.getItemsFromWordMatches(query, columns);
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
    	createdFeatures.add(insertFeature("Seriennummer", types.get(1)));
    	createdFeatures.add(insertFeature("Bewertung", types.get(2)));
    	createdFeatures.add(insertFeature("gekauft am", types.get(3)));
    	return createdFeatures;
    }
    
    
    /**
     * Fuegt Debugeintraege in die Tabelle Formular und Verknuepfungstabelle
     * in die Datenbank ein
     * @param features
     * @return
     */
    public ArrayList<Formular> insertDebugFormularEntries(ArrayList<Feature> features){   	
    	ArrayList<Formular> createdFormulars = new ArrayList<Formular>();
        //Debugeintraege schreiben
    	
    	//erstelle Formular Buecheraufbau
    	Feature feature1 = insertFeature("Foto", types.get(5));
    	Feature feature2 = insertFeature("Autor", types.get(0));
    	Feature feature3 = insertFeature("Ranking", types.get(4));
    	
    	ArrayList<Feature> bookFeatures = new ArrayList<Feature>();
    	bookFeatures.add(feature1);
    	bookFeatures.get(0).setSortnumber(1);
    	bookFeatures.add(feature2);
    	bookFeatures.get(1).setSortnumber(2);
    	bookFeatures.add(feature3);
    	bookFeatures.get(2).setSortnumber(3);
    	createdFormulars.add(insertFormlar("Buecheraufbau", bookFeatures));
    	
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
    	
    	
    	
    	
      	ArrayList<Feature> someFeatures = new ArrayList<Feature>();
        //Debugeintraege schreiben
      	someFeatures.add(insertFeature("Text", types.get(0)));
      	someFeatures.add(insertFeature("Dezimalzahl", types.get(1)));
      	someFeatures.add(insertFeature("Ganzzahl", types.get(2)));
      	someFeatures.add(insertFeature("Datum", types.get(3)));
      	someFeatures.add(insertFeature("Ranking", types.get(4)));
      	someFeatures.add(insertFeature("Foto", types.get(5)));
      	someFeatures.add(insertFeature("Wahrheitswert", types.get(6)));

       	ArrayList<Feature> featuresFormular7 = new ArrayList<Feature>();
       	someFeatures.get(0).setSortnumber(1);
    	featuresFormular7.add(someFeatures.get(0));
       	someFeatures.get(1).setSortnumber(2);
    	featuresFormular7.add(someFeatures.get(1));
       	someFeatures.get(2).setSortnumber(3);
    	featuresFormular7.add(someFeatures.get(2));
       	someFeatures.get(3).setSortnumber(4);
    	featuresFormular7.add(someFeatures.get(3));
       	someFeatures.get(4).setSortnumber(5);
    	featuresFormular7.add(someFeatures.get(4));
       	someFeatures.get(5).setSortnumber(6);
    	featuresFormular7.add(someFeatures.get(5));
       	someFeatures.get(6).setSortnumber(7);
    	featuresFormular7.add(someFeatures.get(6));
    	createdFormulars.add(insertFormlar("Testaufbau", featuresFormular7));
      	
    	    	
    	return createdFormulars;
    }
        
    /**
     * Fuegt Debugeintraege in die Tabelle Kategorie in die Datenbank ein
     */
    public void insertDebugCategoryEntries(){
        //Debugeintraege schreiben
    	    	
    	for (Icon icon : getIcons())
    	{
    		if (icon.getName().equals("category_icon_books"))
    			insertCategory("Buecher",icon, DatabaseHandler.INDEX_OF_ROOT_CATEGORY);
    		if (icon.getName().equals("category_icon_games"))
    			insertCategory("Sportartikel", icon, DatabaseHandler.INDEX_OF_ROOT_CATEGORY);
    		if (icon.getName().equals("category_icon_music"))
    			insertCategory("Musik", icon, DatabaseHandler.INDEX_OF_ROOT_CATEGORY);
    		if (icon.getName().equals("category_icon_food"))
    			insertCategory("Suppen",icon, DatabaseHandler.INDEX_OF_ROOT_CATEGORY);
        	if (icon.getName().equals("category_icon_default"))
        		insertCategory("Sonstiges",icon, DatabaseHandler.INDEX_OF_ROOT_CATEGORY);
    	}
    }
    /**
     * Fuegt Debugeintraege in die Tabelle Item und Verknuepfungstabellen 
     * in die Datenbank ein
     */
    public void insertDebugItemEntries(ArrayList<Formular> formulars, ArrayList<Category> categories){
    	ArrayList<Item> items = new ArrayList<Item>();
    	
    	Formular formular = formulars.get(1);
    	formular.getFeatures().get(0).setValue("Test");
    	formular.getFeatures().get(1).setValue("haha");
    	ArrayList<Category> itemcategories = new ArrayList<Category>();
    	itemcategories.add(categories.get(1));
    	itemcategories.add(categories.get(3));
    	items.add(insertItem("Song1", formular, itemcategories));

    	
      	ArrayList<Feature> someFeatures = new ArrayList<Feature>();
        //Debugeintraege schreiben
      	someFeatures.add(insertFeature("Text", types.get(0)));
      	someFeatures.add(insertFeature("Dezimalzahl", types.get(1)));
      	someFeatures.add(insertFeature("Ganzzahl", types.get(2)));
      	someFeatures.add(insertFeature("Datum", types.get(3)));
      	someFeatures.add(insertFeature("Ranking", types.get(4)));
      	someFeatures.add(insertFeature("Foto", types.get(5)));
      	someFeatures.add(insertFeature("Wahrheitswert", types.get(6)));
    	
      	
    	
    	Formular formularForMyItem = formulars.get(5);
    	formularForMyItem.getFeatures().get(0).setValue("Text.Value");
    	formularForMyItem.getFeatures().get(1).setValue("7.7");
    	formularForMyItem.getFeatures().get(2).setValue("7");
    	formularForMyItem.getFeatures().get(3).setValue("12.12.1977");
    	formularForMyItem.getFeatures().get(4).setValue("5");
    	formularForMyItem.getFeatures().get(5).setValue("icon_angry_minion");
    	formularForMyItem.getFeatures().get(6).setValue("true");

    	ArrayList<Category> categoriesForMyItem = new ArrayList<Category>();
    	categoriesForMyItem.add(categories.get(0));  	
    	//insertItem("Item_mit_allen_Features", formularForMyItem, categoriesForMyItem);
    	
    	Formular formularForMyItem2 = formulars.get(5);
    	formularForMyItem2.getFeatures().get(0).setValue("Ich bin ein Text");
    	formularForMyItem2.getFeatures().get(0).setSortnumber(1);
    	formularForMyItem2.getFeatures().get(1).setValue("7.7");
    	formularForMyItem2.getFeatures().get(1).setSortnumber(2);
    	formularForMyItem2.getFeatures().get(2).setValue("7");
    	formularForMyItem2.getFeatures().get(2).setSortnumber(3);
    	formularForMyItem2.getFeatures().get(3).setValue("12.12.1977");
    	formularForMyItem2.getFeatures().get(3).setSortnumber(4);
    	formularForMyItem2.getFeatures().get(4).setValue("5");
    	formularForMyItem2.getFeatures().get(4).setSortnumber(5);
    	formularForMyItem2.getFeatures().get(5).setValue("category_icon_pets");
    	formularForMyItem2.getFeatures().get(5).setSortnumber(6);
    	formularForMyItem2.getFeatures().get(6).setValue("true");
    	formularForMyItem2.getFeatures().get(6).setSortnumber(7);

    	ArrayList<Category> categoriesForMyItem2 = new ArrayList<Category>();
    	categoriesForMyItem2.add(categories.get(0));  	
    	//insertItem("Katze_mit_allen_Features", formularForMyItem2, categoriesForMyItem2);
    	
    	
     	Formular formularForMyItem4 = formulars.get(1);
    	formularForMyItem4.getFeatures().get(0).setValue("Ich bin ein Text");
    	formularForMyItem4.getFeatures().get(0).setSortnumber(1);
    	formularForMyItem4.getFeatures().get(1).setValue("Hahahaahaha");
    	formularForMyItem4.getFeatures().get(1).setSortnumber(2);

    	ArrayList<Category> categoriesForMyItem4 = new ArrayList<Category>();
    	categoriesForMyItem4.add(categories.get(0));
    	insertItem("Item_ohne_Bild", formularForMyItem4, categoriesForMyItem4);
    }
    
    /**
     * temporaer: Fuellt ein paars die Tabelle mit ein paar Icons.
     * TODO richtig machen
     */
    public void fillIconTableWithIcons (Context context)
    {
    	Field[] drawableFields = R.drawable.class.getFields();
		
		// holt alle Icons mit dem Prefix "category_icon_"
		for (int i = 0; i < drawableFields.length; i++)
			if (drawableFields[i].getName().startsWith( (String) context.getResources().getText(R.string.prefix_icon_category)))  
				databaseHandler.insertIcon(drawableFields[i].getName(),"egal");
    }
    
    /**
     * Setzt die Datenbank neu auf
     */
    public void initializeDatabase(Context context){
    	//databaseHandler.initializeDatabase();
    	
    	
    	databaseHandler.setRootCategoryID();
    	
    	getTypes();
//    	
//    	insertDebugFeatureEntries();
//    	ArrayList<Feature> features = getFeatures(null);
//    	insertDebugFormularEntries(features);
    	if(DatabaseHandler.INDEX_OF_ROOT_CATEGORY == DatabaseHandler.INITIAL_ID){
    		//Nur hinzufuegen, wenn neue initialisierte DB
    		fillIconTableWithIcons(context);
    	}
    	getIcons();
    	//Root Kategorie einfuegen
		Category currentCategory = insertCategory(DataSourceCategory.ROOT_CATEGORY, icons.get(5), -1);    
		this.setCurrentCategory(currentCategory);
//    	insertDebugCategoryEntries();
//
//    	ArrayList<Formular> formulars = getFormulars(null); 
//    	ArrayList<Category> categories = getCategories(null); 
//    	insertDebugItemEntries(formulars, categories);
//    	
//    	getItems(null);    	
    	
    }
    
    public void insertDebugEntries(Context context){
    	databaseHandler.initializeDatabase();
    	getTypes();
    	
    	insertDebugFeatureEntries();
    	ArrayList<Feature> features = getFeatures(null);
    	insertDebugFormularEntries(features);
    	
    	fillIconTableWithIcons(context);
    	getIcons();
    	
    	//Root Kategorie einfuegen
		Category currentCategory = insertCategory(DataSourceCategory.ROOT_CATEGORY, icons.get(5), -1);    
		this.setCurrentCategory(currentCategory);
    	insertDebugCategoryEntries();

    	ArrayList<Formular> formulars = getFormulars(null); 
    	ArrayList<Category> categories = getCategories(null); 
    	insertDebugItemEntries(formulars, categories);
    	
    	getItems(null); 
    }
    
    /**
     * Setzt das Bild mit dem uebergebenen Namen auf den uebergebenen Imageview
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
     * Gibt die aktuelle Kategorie zurueck
     * 
     * @return Die aktuelle Kategorie
     */
    public Category getCurrentCategory() { 
    	return currentCategory;
    }
    /**
     * Ueberschreibt die aktuelle Kategorie
     * 
	 * @param newCurrentCategory
     */
    public void setCurrentCategory(Category newCurrentCategory) { 
    	currentCategory = newCurrentCategory;
    } 
    /**
     * Gibt die aktuelle Eigenschaft zurueck
     * 
     * @return Die aktuelle Eigenschaft
     */
    public Feature getCurrentFeature() { 
    	return currentFeature;
    }
    /**
     * Ueberschreibt die aktuelle Eigenschaft
     * 
	 * @param newCurrentFeature
     */
    public void setCurrentFeature(Feature newCurrentFeature ) { 
    	currentFeature = newCurrentFeature ;
    }  
    /**
     * Gibt die aktuelle Formular zurueck
     * 
     * @return Die aktuelle Formular
     */
    public Formular getCurrentFormular() { 
    	return currentFormular;
    }
    /**
     * Ueberschreibt die aktuelle Formular
     * 
	 * @param newCurrentFormular
     */
    public void setCurrentFormular(Formular newCurrentFormular ) { 
    	currentFormular = newCurrentFormular ;
    }
    /**
     * Gibt die aktuelle Item zurueck
     * 
     * @return Die aktuelle Item
     */
    public Item getCurrentItem() { 
    	return currentItem;
    }
    /**
     * Ueberschreibt die aktuelle Item
     * 
	 * @param newCurrentItem
     */
    public void setCurrentItem(Item newCurrentItem) { 
    	currentItem = newCurrentItem;
    }    
	/**
	 * Gibt Oberkategorie zurueck
	 * 
	 * @param database
	 * @param category
	 * @return Die Oberkategorien
	 */
	public Category getPreCategoryId(Category category) {
		return databaseHandler.getPreCategoryId(category);
	}	
	
	public ArrayList<Category> getSelectedCategoriesInItem() {
		return selectedCategoriesInItem;
	}
	
	public void setSelectedCategoriesInItem(
			ArrayList<Category> selectedCategoriesInItem) {
		this.selectedCategoriesInItem = selectedCategoriesInItem;
	}
    
    // LÖSCHE MICH DEMNÄCHST, aber nicht während der Entwicklung.
    public void sayIt(String message) {
    	Toast.makeText(context, message, Integer.valueOf(20)).show();
    }
}
