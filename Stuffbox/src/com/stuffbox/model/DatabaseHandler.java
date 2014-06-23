package com.stuffbox.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.renderscript.Type;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper{

	private static final String TAG = DatabaseHandler.class.getSimpleName();
	
	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 3;
 
    public static final String UNDERLINE = "_";
    
    // Database Name
    public static final String DATABASE_NAME = "stuffbox";
    
    // item table name;
    public static final String TABLE_CATEGORY = "kategorie";
    public static final String TABLE_FEATURE = "eigenschaft";
    public static final String TABLE_TYPE = "art";
    public static final String TABLE_ITEM = "item";
    public static final String TABLE_ICON = "icon";
    		
    // table columns names
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_ICON = "icon";
    public static final String KEY_TYPE = "art";
    public static final String KEY_FEATURE = "eigenschaft";
    public static final String KEY_DESCRIPTION = "beschreibung";
      
    public static final String SQL_OR = "OR";
    
    private String DB_PATH;
    
    private final Context context; 
    private SQLiteDatabase database;
    
    private DataSourceType dataSourceType;
    private DataSourceFeature dataSourceFeature;
    private DataSourceIcon dataSourceIcon;
    private DataSourceCategorie dataSourceCategorie;
    private DataSourceItem dataSourceItem;
    
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        File filesDir = context.getFilesDir();
        DB_PATH = filesDir.getPath().substring(0, filesDir.getPath().length() - filesDir.getName().length());
        
        //instance datasources
        dataSourceType = new DataSourceType();
        dataSourceFeature = new DataSourceFeature();
        dataSourceIcon = new DataSourceIcon();
        dataSourceCategorie = new DataSourceCategorie();
        dataSourceItem = new DataSourceItem();
        
        database = getWritableDatabase();
    } 
    /**
     * Gibt eine List aller Arten zur�ck
     * @return
     */
    public ArrayList<FeatureType> getTypes() { 
    	return dataSourceType.getTypes(database);
    }  
    /**
     * Gibt eine List aller Icons zur�ck
     * @return
     */
    public ArrayList<Icon> getIcons(){
    	return dataSourceIcon.getIcons(database);
    }
    /**
     * Gibt eine Liste aller Features zur�ck, deren ids in der id Liste enthalten ist
     * @param selectFeatureIds Liste aller zu selektierenden Ids (bei null werden alle geladen)
     * @param types
     * @return
     */
    public ArrayList<Feature> getFeatures(ArrayList<Integer> selectFeatureIds, ArrayList<FeatureType> types) {
    	return dataSourceFeature.getFeatures(database, selectFeatureIds, types);
    }
    /**
     * Speichert eine neue Eigenschaft in der Tabelle Eigenschaft.
     * @param database
     * @param name
     */
    public void insertFeature(String name, FeatureType featureType){
    	dataSourceFeature.insertFeature(database, name, featureType);
    }
    
    /**
     * Gibt eine Liste aller Kategorien zur�ck, deren ids in der id Liste enthalten ist
     * @param selectFeatureIds Liste aller zu selektierenden Ids (bei null werden alle geladen)
     * @param types
     * @return
     */
    public ArrayList<Category> getCategories(ArrayList<Integer> selectFeatureIds, ArrayList<Icon> icons) {
    	return dataSourceCategorie.getCategories(database, selectFeatureIds, icons);
    }
    /**
     * Speichert eine neue Eigenschaft in der Tabelle Eigenschaft.
     * @param database
     * @param name
     */
    public void insertCategory(String name, Icon icon){
    	dataSourceCategorie.insertCategory(database, name, icon);
    }
    
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
    	dataSourceType.createTypeTable(db);
    	dataSourceFeature.createFeatureTable(db);
    	dataSourceIcon.createIconTable(db);
    	dataSourceItem.createItemTable(db);
    	dataSourceCategorie.createCategorieTable(db);
    }
    
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	database = db;
    	initializeDatabase();
    }
	
    public void initializeDatabase(){
        // Drop older table if existed
    	database.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPE);
    	database.execSQL("DROP TABLE IF EXISTS " + TABLE_FEATURE);
    	database.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
    	database.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
    	database.execSQL("DROP TABLE IF EXISTS " + TABLE_ICON);
 
        // Create tables again
        onCreate(database);
    }
    
    /**
     * Inserts one entry into the database
     * @param database
     * @param table
     * @param values
     */
    public static void insertIntoDB(SQLiteDatabase database, String table, ContentValues values){
    	long rowID = -1;
    	
    	try{
	    	rowID = database.insert(table, null, values);
    	}catch(SQLiteException e){
    		Log.e(TAG, "insert " + table,e);
    	}finally{
    		Log.d(TAG, "insert + table: rowId=" + rowID);
    	}
    }
    /**
     * Returns the Where Statement created from the passed list
     * @param selectIds
     * @return
     */
	public static String getWhereStatementFromIDList(ArrayList<Integer> selectIds) {
		if(selectIds == null){
			return null;
		}
		StringBuilder whereStatement = new StringBuilder();
		for(Integer id : selectIds){
			whereStatement.append(" ");
			whereStatement.append(KEY_ID);
			whereStatement.append(" = ");
			whereStatement.append(id);
			whereStatement.append(" ");
			whereStatement.append(SQL_OR);
		}
		
		return whereStatement.substring(0, whereStatement.length()-SQL_OR.length());
	} 
	
	public void fillIconTableWithSomeIcons (Context context)
	{
		dataSourceIcon.fillIconTableWithSomeIcons(context, database);
	}
	
}
