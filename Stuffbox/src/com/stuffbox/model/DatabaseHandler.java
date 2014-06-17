package com.stuffbox.model;

import java.io.File;
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
    private static final int DATABASE_VERSION = 2;
 
    public static final String UNDERLINE = "_";
    
    // Database Name
    public static final String DATABASE_NAME = "stuffbox";
    
    // item table name;
    public static final String TABLE_CATEGORY = "kategorie";
    public static final String TABLE_FEATURE = "eigenschaft";
    public static final String TABLE_TYPE = "art";
    public static final String TABLE_ITEM = "item";
    		
    // table columns names
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_ICON = "icon";
    public static final String KEY_TYPE = "art";
    public static final String KEY_FEATURE = "eigenschaft";
      
    public static final String SQL_OR = "OR";
    
    private final Context context; 
    private SQLiteDatabase database;
    
    private DataSourceType dataSourceType;
    private DataSourceFeature dataSourceFeature;
    
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        
        dataSourceType = new DataSourceType();
        dataSourceFeature = new DataSourceFeature();
        
        database = getWritableDatabase();
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
     * Gibt eine List aller Arten zurück
     * @return
     */
    public ArrayList<FeatureType> getTypes() { 
    	return dataSourceType.getTypes(database);
    }
    /**
     * Gibt eine Liste aller Features zurück, deren ids in der id Liste enthalten ist
     * @param selectFeatureIds Liste aller zu selektierenden Ids (bei null werden alle geladen)
     * @param types
     * @return
     */
    public ArrayList<Feature> getFeatures(ArrayList<Integer> selectFeatureIds, ArrayList<FeatureType> types) {
    	return dataSourceFeature.getFeatures(database, selectFeatureIds, types);
    }
    
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
    	createItemTable(db);
    	dataSourceType.createTypeTable(db);
    	dataSourceFeature.createFeatureTable(db);
    }
 
    private void createItemTable(SQLiteDatabase db){
        String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_ITEM + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT" + ")";
        db.execSQL(CREATE_ITEM_TABLE);
    }
    
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEATURE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
 
        // Create tables again
        onCreate(db);
    }
    
    /**
     * Fügt ein Item der Datenbank hinzu.
     * TODO Daten des Items speichern
     * @param name
     */
    public void instertItem(String name){
    	ContentValues values = new ContentValues();
    	values.put(KEY_NAME, name);
    	
    	SQLiteDatabase database = getWritableDatabase();
    	insertIntoDB(database, TABLE_ITEM, values);
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
     * @param selectFeatureIds
     * @return
     */
	public static String getWhereStatementFromIDList(ArrayList<Integer> selectFeatureIds) {
		StringBuilder whereStatement = new StringBuilder();
		for(Integer id : selectFeatureIds){
			whereStatement.append(" ");
			whereStatement.append(KEY_ID);
			whereStatement.append(" = ");
			whereStatement.append(id);
			whereStatement.append(" ");
			whereStatement.append(SQL_OR);
		}
		
		return whereStatement.substring(0, whereStatement.length()-SQL_OR.length());
	} 
}
