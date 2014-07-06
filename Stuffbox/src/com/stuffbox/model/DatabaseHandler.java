package com.stuffbox.model;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import com.stuffbox.controller.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper{

	private static final String TAG = DatabaseHandler.class.getSimpleName();
	
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
    public static final int INDEX_OF_ROOT_CATEGORY = 1;
    public static final long INITIAL_ID = -1; 
    
    private final Context context; 
    private SQLiteDatabase database;
    
    private DataSourceType dataSourceType;
    private DataSourceFeature dataSourceFeature;
    private DataSourceFormular dataSourceFormular;
    private DataSourceIcon dataSourceIcon;
    private DataSourceCategory dataSourceCategory;
    private DataSourceItem dataSourceItem;
    
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
    	dataSourceFeature.createFeatureTable(db, getTypes());
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
    	
        // Create tables again
        onCreate(database);
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
     * Speichert eine neue Eigenschaft in der Tabelle Eigenschaft.
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
    	return dataSourceFormular.insertFormlar(database, name, features);
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
    
    /**
     * Speichert ein Item in der Tabelle Item und dessen zugeorndete
     * Eigenschaften, Formular und Kategorie in den Verknuepfungstabellen.
     * Speichert das Formular als zuletzt angelegte im Controller
     * @param name
     * @param formular
     * @return
     */
    public Item insertItem(String name, Formular formular, ArrayList<Category> categories){
    	return dataSourceItem.insertItem(database, name, formular, categories);
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
    	return dataSourceCategory.insertOrUpdateCategory(database, category);
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
    	long rowID = -1;
    	
    	try{
    	    rowID = database.insert(table, null, values);
	    	
    	}catch(SQLiteException e){
    		Log.e(TAG, "insert " + table + " " + logString, e);
    	}finally{
    		Log.d(TAG, "insert " + table + " rowId=" + rowID + " " + logString);
    	}
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
    	long rowID = -1;
    	
    	String whereClause = createWhereStatementFromContentValues(whereValues); 
    	
    	try{
    		rowID = database.update(table, values, whereClause, null);
    	}catch(SQLiteException e){
    		Log.e(TAG, "update " + table + " " + logString, e);
    	}finally{
    		Log.d(TAG, "update " + table + " rowId=" + rowID + " " + logString);
    	}
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
    	String whereClause = createWhereStatementFromContentValues(whereValues); 	
    	return deletefromDB(database, table, whereClause);
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
    	long delRows = 0;	
    	try{
    		delRows = database.delete(table, whereStatement, null);
    	}catch(SQLiteException e){
    		Log.e(TAG, "delete " + table + " " + table, e);
    	}finally{
    		Log.d(TAG, "delete " + table + " deleted Rows=" + delRows + " " + table);
    	}
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
    
    public Cursor getWordMatches(String query, String[] columns) {
        String selection = KEY_NAME + " MATCH ?";
        String[] selectionArgs = new String[] {query+"*"};

        return query(selection, selectionArgs, columns);
    }

    private Cursor query(String selection, String[] selectionArgs, String[] columns) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(TABLE_ITEM);

        Cursor cursor = builder.query(this.getReadableDatabase(),
                columns, selection, selectionArgs, null, null, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }
}
