package com.stuffbox.model;

import java.util.ArrayList;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper{

	static final String TAG = DatabaseHandler.class.getSimpleName();

	public static final String PREFS_NAME = "STUFFBOX_DATABASE";
	
	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 5;
 
    public static final String SQL_INSERT_OR_REPLACE = "__sql_insert_or_replace__";
    
    public static final String UNDERLINE = "_";
    
    // Database Name
    public static final String DATABASE_NAME = "stuffbox";
    
    // item table name;
    public static final String TABLE_CATEGORY = "kategorie";
    public static final String TABLE_FEATURE = "eigenschaft";
    public static final String TABLE_TYPE = "art";
    public static final String TABLE_ITEM = "item";
    public static final String TABLE_ICON = "icon";
    public static final String TABLE_FORMULAR = "formular";
    public static final String TABLE_FORMULAR_FEATURE = DatabaseHandler.TABLE_FORMULAR 
    												 + DatabaseHandler.UNDERLINE 
    												 + DatabaseHandler.TABLE_FEATURE;
    public static final String TABLE_FEATURE_ITEM = DatabaseHandler.TABLE_FEATURE 
													 + DatabaseHandler.UNDERLINE 
													 + DatabaseHandler.TABLE_ITEM;
    public static final String TABLE_CATEGORY_ITEM = DatabaseHandler.TABLE_CATEGORY 
													 + DatabaseHandler.UNDERLINE 
													 + DatabaseHandler.TABLE_ITEM;
    
    // table columns names
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_ICON = "icon";
    public static final String KEY_TYPE = "art";
    public static final String KEY_FEATURE = "eigenschaft";
    public static final String KEY_DESCRIPTION = "beschreibung";
    public static final String KEY_VALUE = "wert";
    public static final String KEY_SORTNUMBER = "sortiernummer";
    public static final String KEY_PRECATEGORY = "pre";
      
    public static final String SQL_OR = "OR";
    public static final String SQL_AND = "AND";
    public static final String PREFIX_ICON_CATEGORY = "category_icon_";
    public static long INDEX_OF_ROOT_CATEGORY = -1;
    public static final long INITIAL_ID = -1; 
    
    private final Context context; 
    private SQLiteDatabase database;
    
    private DataSourceType dataSourceType;
    private DataSourceFeature dataSourceFeature;
    private DataSourceFormular dataSourceFormular;
    private DataSourceIcon dataSourceIcon;
    private DataSourceCategory dataSourceCategory;
    private DataSourceItem dataSourceItem;
    
    private static DatabaseHandler sInstance;
    
    public static DatabaseHandler getInstance(Context context) {

        // Use the application context, which will ensure that you 
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
          sInstance = new DatabaseHandler(context.getApplicationContext());
        }
        return sInstance;
      }
    
    private DatabaseHandler(Context context) {
    	//TODO rausnehmen für richtige Anwendung
//        super(context, "/mnt/sdcard/" + DATABASE_NAME, null, DATABASE_VERSION);
        super(context, "/mnt/sdcard/Stuffbox/" + DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        
        //instance datasources
        dataSourceType = new DataSourceType();
        dataSourceFeature = new DataSourceFeature();
        dataSourceFormular = new DataSourceFormular();
        dataSourceIcon = new DataSourceIcon();
        dataSourceCategory = new DataSourceCategory();
        dataSourceItem = new DataSourceItem();
        
        database = getWritableDatabase();
        if (!database.isReadOnly()) {
            // Enable foreign key constraints
        	database.execSQL("PRAGMA foreign_keys=ON;");
        }
    } 

	/**
     * Tabellen erstellen
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
    	dataSourceIcon.createIconTable(db);
    	dataSourceType.createTypeTable(db);
    	ArrayList<FeatureType> types = dataSourceType.getTypes(db);
    	dataSourceFeature.createFeatureTable(db, types);
    	dataSourceFormular.createFormularTable(db);;
    	dataSourceCategory.createCategorieTable(db);
    	dataSourceItem.createItemTable(db);
    }   
    
    public void initializeDatabase(){
        // Drop older table if existed
    	database.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY_ITEM);
    	database.execSQL("DROP TABLE IF EXISTS " + TABLE_FEATURE_ITEM);
    	database.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
    	database.execSQL("DROP TABLE IF EXISTS " + TABLE_FORMULAR_FEATURE);
    	database.execSQL("DROP TABLE IF EXISTS " + TABLE_FORMULAR);
    	database.execSQL("DROP TABLE IF EXISTS " + TABLE_FEATURE);
    	database.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
    	database.execSQL("DROP TABLE IF EXISTS " + TABLE_ICON);
    	database.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPE);
    	DatabaseHandler.INDEX_OF_ROOT_CATEGORY = INITIAL_ID;
    	
        // Create tables again
        onCreate(database);
    }   
        
    public void setRootCategoryID(){
    	dataSourceCategory.setRootCategoryID(database);
    }
    
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	database = db;
    	initializeDatabase();
    }     
    
    /**
     * Gibt eine List aller Arten zurueck
     * @return
     */
    public ArrayList<FeatureType> getTypes() { 
    	return dataSourceType.getTypes(database);
    }  
    /**
     * Gibt eine List aller Icons zurueck
     * @return
     */
    public ArrayList<Icon> getIcons(){
    	return dataSourceIcon.getIcons(database);
    }
    /**
     * Gibt eine Liste aller Features zurueck, deren ids in der id Liste enthalten ist
     * @param selectFeatureIds Liste aller zu selektierenden Ids (bei null werden alle geladen)
     * @param types
     * @return
     */
    public ArrayList<Feature> getFeatures(ArrayList<Long> selectFeatureIds, ArrayList<FeatureType> types) {
    	return dataSourceFeature.getFeatures(database, selectFeatureIds, types);
    }
    /**
     * Speichert eine neue Eigenschaft in der Tabelle Eigenschaft.
     * @param name
     * @param featureType
     */
    public Feature insertFeature(String name, FeatureType featuretype){
    	return dataSourceFeature.insertOrUpdateFeature(database, new Feature(INITIAL_ID, name, featuretype));
    }
    /**
     * Speichert die Aenderungen an einer Eigenschaft in der Tabelle Eigenschaft.
     * @param name
     * @param featureType
     */
    public Feature updateFeature(Feature feature){
    	return dataSourceFeature.insertOrUpdateFeature(database, feature);
    }
    /**
     * Loescht Eigenschaften von der Datenbank
     * @param features
     */
    public boolean deleteFeatures(ArrayList<Feature> features){
    	return dataSourceFeature.deleteFeatures(database, features);
    }
    
    /**
     * Gibt eine Liste aller Formulare zurueck, deren ids in der id Liste enthalten ist
     * @param selectFormularIds
     * @return
     */
    public ArrayList<Formular> getFormulars(ArrayList<Long> selectFormularIds){
    	return dataSourceFormular.getFormulars(database, selectFormularIds);
    }
    
    /**
     * Speichert ein Formular in der Tabelle Formular und dessen zugeorndete
     * Eigenschaften in der Verknuepfungstabelle.
     * @param name
     * @param features
     * @return
     */
    public Formular insertFormlar(String name, ArrayList<Feature> features){
    	return dataSourceFormular.insertOrUpdateFormlar(database, new Formular(INITIAL_ID, name, features));
    }
    
    /**
     * Speichert die Aenderungen an einem Formular in der Tabelle Formular und 
     * dessen zugeorndete Eigenschaften in der Verknuepfungstabelle.
     * @param formular
     * @return
     */
    public Formular updateFormlar(Formular formular){
    	return dataSourceFormular.insertOrUpdateFormlar(database, formular);
    }
    
    /**
     * Loescht Formulare von der Datenbank
     * @param formulare
     */
    public boolean deleteFormulars(ArrayList<Formular> formulars){
    	return dataSourceFormular.deleteFormulars(database, formulars);
    }
    /**
     * Gibt eine Liste aller Items zurueck, , deren ids in der id Liste enthalten ist
     * @param database
     * @param selectIds Liste aller zu selektierenden Ids (bei null werden alle geladen)
     * @return
     */
    public ArrayList<Item> getItems(ArrayList<Long> selectIds){
    	return dataSourceItem.getItems(database, selectIds);
    }
    
    /**
     * Gibt eine Liste aller Items zurueck, die einer Kategorie enthalten sind.
     * @param database
     * @param categoryID 
     * @return Die Items in der spezifizierten Kategorie
     */
    public ArrayList<Item> getItemsOfACategory(long categoryID) {
    	return dataSourceItem.getItemsOfACategory(database, categoryID);
    }   
    
    public ArrayList<Item> getItemsFromWordMatches(String query, String[] columns){
    	return dataSourceItem.getItemsFromWordMatches(database, query, columns);
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
    	return dataSourceItem.insertOrUpdateItem(database, new Item(INITIAL_ID, name, formular, categories));
    }
    /**
     * Speichert die Aenderungen an einem Item in der Tabelle Item und dessen zugeorndete
     * Eigenschaften, Formular und Kategorie in den Verknuepfungstabellen.
     * Speichert das Formular als zuletzt angelegte im Controller
     * @param name
     * @param formular
     * @return
     */
    public Item updateItem(Item item){
    	return dataSourceItem.insertOrUpdateItem(database, item);
    }
	/**
	 * Loescht mehrere Items
	 *
	 * @param item
	 * @return Ob es erfolgreich geloescht wurde 
	 */
	public boolean deleteItems(ArrayList<Item> items) {
		return dataSourceItem.deleteItems(database, items);
	}
    /**
     * Gibt eine Liste aller Kategorien zurueck, deren ids in der id Liste enthalten ist
     * @param selectFeatureIds Liste aller zu selektierenden Ids (bei null werden alle geladen)
     * @param types
     * @return
     */
    public ArrayList<Category> getCategories(ArrayList<Long> selectFeatureIds, ArrayList<Icon> icons) {
    	return dataSourceCategory.getCategories(database, selectFeatureIds, icons);
    }
    
	/**
	 * Returniert alle Unterkategorien einer Kategorie
	 * 
	 * @param categoryID
	 * @return Die Unterkategorien
	 */
    public ArrayList<Category> getSubCategories(long categoryID) {
    	return dataSourceCategory.getSubCategories(database, categoryID);
    }
    
    /**
     * Speichert eine neue Eigenschaft in der Tabelle Eigenschaft.
     * @param database
     * @param name
     */
    public Category insertOrUpdateCategory(Category category){
    	Category cat = dataSourceCategory.insertOrUpdateCategory(database, category);
    	return cat;
    }
    /**
     * Speichert eine neues Icon in der Tabelle Icon
     * @param name
     * @param icon
     */
    public void insertIcon(String name, String description){
    	dataSourceIcon.insertIcon(database, name, description);
    }    
    
	/**
	 * Gibt Oberkategorie zurueck
	 * 
	 * @param database
	 * @param category
	 * @return Die Oberkategorien
	 */
	public Category getPreCategoryId(Category category) {
		return dataSourceCategory.getPreCategoryId(database, category);
	}	
	    
    /**
     * Inserts one entry into the database
     * @param database
     * @param table
     * @param values
     */
    public static long insertIntoDB(SQLiteDatabase database, String table, ContentValues values, String logString){
    	database.beginTransaction();
    	long rowID = -1;
    	
    	try{
    	    rowID = database.insert(table, null, values);
	    	
    	}catch(SQLiteException e){
    		Log.e(TAG, "insert " + table + " " + logString, e);
    	}finally{
    		Log.d(TAG, "insert " + table + " rowId=" + rowID + " " + logString);
    	}
    	database.setTransactionSuccessful();
    	database.endTransaction();
    	return rowID;
    }
    
    /**
     * Inserts one entry into the database
     * @param database
     * @param table
     * @param values
     * @param whereValues
     * @param logString
     * @return number of updated rows
     */
    public static long updateEntryInDB(SQLiteDatabase database, 
    		String table, 
    		ContentValues values, 
    		ContentValues whereValues, 
    		String logString){
    	database.beginTransaction();
    	
    	String whereClause = createWhereStatementFromContentValues(whereValues); 
    	
    	return updateEntryInDB(database, table, values, whereClause, logString);
    }    
	
    
    /**
     * Inserts one entry into the database
     * @param database
     * @param table
     * @param values
     * @param whereValues
     * @param logString
     * @return number of updated rows
     */
    public static long updateEntryInDB(SQLiteDatabase database, 
    		String table, 
    		ContentValues values, 
    		String whereClause, 
    		String logString){
    	
    	database.beginTransaction();
    	long rowID = -1; 
    	
    	try{
    		rowID = database.update(table, values, whereClause, null);
//    		rowID = database.update(table, values, KEY_ID + "=?", new String[]{2+""});
    	}catch(SQLiteException e){
    		Log.e(TAG, "update " + table + " " + logString, e);
    	}finally{
    		Log.d(TAG, "update " + table + " geänderte Zeilen: " + rowID + " " + logString);
    	}

    	database.setTransactionSuccessful();
    	database.endTransaction();
    	
    	return rowID;
    } 
    
    /**
     * Loescht einen Tupel
     * @param table
     * @param whereValues
     * @param logString
     * 
     * @return Die Anzahl der geloeschten Zeilen
     */
    public static long deletefromDB(SQLiteDatabase database, String table, ContentValues whereValues){
    	database.beginTransaction();
    	String whereClause = createWhereStatementFromContentValues(whereValues); 	
    	long deletedRows = deletefromDB(database, table, whereClause);
    	database.setTransactionSuccessful();
    	database.endTransaction();
    	return deletedRows;
    }
    
    /**
     * Loescht einen Tupel
     * @param table
     * @param whereValues
     * @param logString
     * 
     * @return Die Anzahl der geloeschten Zeilen
     */
    public static long deletefromDB(SQLiteDatabase database, String table, String whereStatement){
    	database.beginTransaction();
    	long delRows = 0;	
    	try{
    		delRows = database.delete(table, whereStatement, null);
    	}catch(SQLiteException e){
    		Log.e(TAG, "delete " + table + " " + table, e);
    	}finally{
    		Log.d(TAG, "delete " + table + " deleted Rows=" + delRows + " " + table);
    	}
    	database.setTransactionSuccessful();
    	database.endTransaction();
    	return delRows;
    }
    
	public boolean deleteCategory(Category category) {
		return dataSourceCategory.deleteCategory(database, category);
	}
	
	/**
	 * Loescht alle Items einer Kategorie
	 * 
	 * @param categoryID
	 * @return Ob es erfolgreich geloescht wurde 
	 */
	public boolean deleteItemsOfCategory(long categoryID) {
		return dataSourceItem.deleteItemsOfCategory(database, categoryID);
	}
	
	/**
	 * Loescht eine Kategorie und alle Unterkategorien und die jeweiligen Items.
	 * 
	 * @param database
	 * param category
	 * @return Ob alles erfolgreich geloescht wurde 
	 */
	public boolean deleteCategoryRecursively(Category categoryToDeleteRecursively) {
		return dataSourceCategory.deleteCategoryRecursively(database, categoryToDeleteRecursively);
	}
    
    /**
     * Erstellt einen where-Statement aus ContentValues
     * 
     * @param whereValues
     * @return the where Clause
     */
	public static String createWhereStatementFromContentValues(ContentValues whereValues) {
		String whereClause = "";
		for (Map.Entry<String,Object> e : whereValues.valueSet())
			whereClause += e.getKey() + "=" + e.getValue().toString() + " " + SQL_OR+ " "; 
		return whereClause.substring(0, whereClause.length() - SQL_OR.length() - 1);
	}     
    
    /**
     * Returns the Where Statement created from the passed list
     * @param selectIds
     * @param idName name of id table (if null then default is id)
     * @return
     */
	public static String createWhereStatementFromIDList(ArrayList<Long> selectIds, String idName) {
		if(selectIds == null || selectIds.isEmpty()){
			return null;
		}
		if(idName == null){
			idName = KEY_ID;
		}
		StringBuilder whereStatement = new StringBuilder();
		for(Long id : selectIds){
			whereStatement.append(" ");
			whereStatement.append(idName);
			whereStatement.append(" = ");
			whereStatement.append(id);
			whereStatement.append(" ");
			whereStatement.append(SQL_OR);
		}
		
		String whereStatementFromIDList = whereStatement.toString();
		if(selectIds != null){
			whereStatementFromIDList = whereStatementFromIDList.substring(0, whereStatementFromIDList.length()-SQL_OR.length());
		}
		
		return whereStatementFromIDList;
	} 
	
    /**
     * Alle Eintraege die in der Tabelle tableName in der Spalte keyGivenIdName 
     * den Wert id haben, werden selektiert und als Ergebnis wird eine Liste mit 
     * den Werten der Spalte keySearchedIdName aller gefundener Eintraege 
     * zurueckgegeben.
	 * @param database
	 * @param id 
	 * @param keyGivenIdName
	 * @param keySearchedIdName
	 * @param tableName
	 * @return
	 */
    public static ArrayList<Long> getEntriesOfConjunctionTable(
    		SQLiteDatabase database,
			Long id,
			String keyGivenIdName,
			String keySearchedIdName,
			String tableName){
    	//erstelle where statement
    	StringBuilder whereStatement = new StringBuilder();
		whereStatement.append(" ");
		whereStatement.append(keyGivenIdName);
		whereStatement.append(" = ");
		whereStatement.append(id);
		whereStatement.append(" ");
    	 	
    	//select types from database
    	Cursor cursor = database.query(tableName, null, whereStatement.toString(), null, null, null, null);
    	
    	ArrayList<Long> selectFeatureIds = new ArrayList<Long>();
    	
		//Werte in Feature speichern
		if (cursor.moveToFirst()) {
			do {
				selectFeatureIds.add(Long.parseLong(cursor.getString(cursor.getColumnIndex(keySearchedIdName))));
			} while (cursor.moveToNext());
		}
		
		return selectFeatureIds;
    }
}
