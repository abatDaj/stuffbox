package com.stuffbox.model;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper{

	private static final String TAG = DatabaseHandler.class.getSimpleName();
	
	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 4;
 
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
    public static final String CATEGORY_ITEM = DatabaseHandler.TABLE_CATEGORY 
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
    } 
    
    /**
     * Tabellen erstellen
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
    	dataSourceType.createTypeTable(db);
    	dataSourceFeature.createFeatureTable(db, getTypes());
    	dataSourceFormular.createFormularTable(db);;
    	dataSourceIcon.createIconTable(db);
    	dataSourceItem.createItemTable(db);
    	dataSourceCategory.createCategorieTable(db);
    }   
    
    public void initializeDatabase(){
        // Drop older table if existed
    	database.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPE);
    	database.execSQL("DROP TABLE IF EXISTS " + TABLE_FEATURE);
    	database.execSQL("DROP TABLE IF EXISTS " + TABLE_FORMULAR);
    	database.execSQL("DROP TABLE IF EXISTS " + TABLE_FORMULAR_FEATURE);
    	database.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
    	database.execSQL("DROP TABLE IF EXISTS " + TABLE_FEATURE_ITEM);
    	database.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
    	database.execSQL("DROP TABLE IF EXISTS " + TABLE_ICON);
    	
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
    public Feature insertFeature(String name, FeatureType featureType){
    	return dataSourceFeature.insertFeature(database, name, featureType);
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
     * Eigenschaften in der Verkn�pfungstabelle.
     * @param name
     * @param features
     * @return
     */
    public Formular insertFormlar(String name, ArrayList<Feature> features){
    	return dataSourceFormular.insertFormlar(database, name, features);
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
    	return dataSourceItem.insertItem(database, name, formular);
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
    public Category insertOrUpdateCategory(String name, Icon icon, long precategory){
    	return dataSourceCategory.insertOrUpdateCategory(database, INITIAL_ID, name, icon, precategory);
    }
    
    /**
     * Speichert die Aenderungen einer Kategorie in der Tabelle Kategorie.
     * @param category
     * @return
     */
    public Category updateCategory(Category category){
    	return dataSourceCategory.insertOrUpdateCategory(database, 
    			category.getId(), 
    			category.getName(), 
    			category.getIcon(), 
    			category.getPreCategory());
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
	public Category getPreCategory(Category category) {
		return dataSourceCategory.getPreCategory(database, category);
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
    	long delRows = 0;
    	String whereClause = createWhereStatementFromContentValues(whereValues); 	
    	try{
    		delRows = database.delete(table, whereClause, null);
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
	public static String getWhereStatementFromIDList(ArrayList<Long> selectIds, String idName) {
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
			String[] columnsWithMax = new String[columns.length + 1];
			columnsWithMax[0] = "MAX("+ nameOfIdColumn +")";
			System.arraycopy(columns, 0, columnsWithMax, 1, columns.length);
			Cursor cursor = database.query(tabelName, columnsWithMax , null, null, null, null, null);
			
			// Sicherstellung der Constraints dieser Methode. 1 Reihe sollte wiedergegeben werden
			// und nameOfIdColumn sollte ein Schlüssel sein.
			if (cursor.getCount() != 1 && !cursor.getExtras().containsKey(nameOfIdColumn))
				return null; 
			
			return cursor;	
	}
	
	  /**
     * Gibt eine Liste aller Features zurück, deren ids in der id Liste enthalten ist
     * @param database
     * @param selectFeatureIds Liste aller zu selektierenden Ids (bei null werden alle geladen)
     * @param types
     * @return
     */
    public ArrayList<Object> getEntities(	String tableName,
    										String column,
    										ArrayList<Object> selectValues,
    										Class clas) {  
    	String whereStatment = DatabaseHandler.getWhereStatementFromSomeList(column, selectValues);
    	String[] columns = {column};
    	Cursor cursor = database.query(tableName, null, whereStatment, null, null, null, null);
		ArrayList<Object> entities = new ArrayList<Object>();
		Constructor constructor = null;
		try {
			constructor = clas.getConstructor();
		} catch (NoSuchMethodException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		if (cursor.moveToFirst()) {
			do {				
				Object entity = 0;
				try {
					entity = constructor.newInstance();
					Method[] methods = clas.getMethods();
				
					for (Method m : methods) {
						String methodName = m.getName();
						if (methodName.startsWith("set"))
						{
							String curColumn = methodName.substring(3).toLowerCase();
							int pos = cursor.getColumnIndex(curColumn);
							if (pos > -1) {
								String curValue = cursor.getString(cursor.getColumnIndex(curColumn));
								try { //TODO
									m.invoke(entity,  curColumn.equals(DatabaseHandler.KEY_ID) ? Integer.parseInt(curValue.toString()) : curValue );
								}catch(IllegalArgumentException e) {}
							}
						}	
					}
				} catch (InstantiationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvocationTargetException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				entities.add(entity);
			} while (cursor.moveToNext());
		}
		return entities;
    }	
	

/**
 * Stellt ein where-Statement zusammen.
 * @param column Spalte
 * @param selectValues Werte
 * @return
 */
public static String getWhereStatementFromSomeList(String column, ArrayList<Object> selectValues) {
	if(selectValues == null || selectValues.isEmpty() || column == null)
		return null;
	String whereStatement = "";
	for(Object value : selectValues)
		whereStatement += " "   + column + " = "  + value + " " + SQL_OR;	
	return whereStatement.substring(0,whereStatement.length()-SQL_OR.length());
} 
	
	
}
