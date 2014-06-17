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
 
    private static final String UNDERLINE = "_";
    
    // Database Name
    private static final String DATABASE_NAME = "stuffbox";
    
    // item table name;
    private static final String TABLE_CATEGORY = "kategorie";
    private static final String TABLE_FEATURE = "eigenschaft";
    private static final String TABLE_TYPE = "art";
    private static final String TABLE_ITEM = "item";
    		
    // table columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ICON = "icon";
    private static final String KEY_TYPE = "art";
    private static final String KEY_FEATURE = "eigenschaft";
    
    private static final String[] TYPES = {		"Text",
    											"Dezimalzahl",
    											"Ganzzahl",
    											"Datum",
    											"Ranking",
    											"Foto",
    											"Wahrheitswert"};
      
    private final Context context; 
    
    
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    } 
	 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
    	createItemTable(db);
    	createTypeTable(db);
    	createEigenschaftTable(db);
    }
 
    private void createItemTable(SQLiteDatabase db){
        String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_ITEM + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT" + ")";
        db.execSQL(CREATE_ITEM_TABLE);
    }
    
    private void createEigenschaftTable(SQLiteDatabase db){
        String CREATE_EIGENSCHAFT_TABLE = "CREATE TABLE " + TABLE_FEATURE + "("+ 
        		//create column id
        		KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
        		//create column name
        		KEY_NAME + " TEXT," + 
        		//create column art
                KEY_TYPE + " INTEGER," + 
        		//add foreign key to table art
                "FOREIGN KEY(" + KEY_TYPE + ") REFERENCES " + TABLE_TYPE + "(" + KEY_ID + ")" + ")";
        db.execSQL(CREATE_EIGENSCHAFT_TABLE);
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
     * Erstellt die Tabelle Art auf der Datenbank und erzeugt zusätzlich 
     * die Einträge.
     * @param database
     */
    private static void createTypeTable(SQLiteDatabase database){
        String CREATE_ART_TABLE = "CREATE TABLE " + TABLE_TYPE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT" + ")";
        database.execSQL(CREATE_ART_TABLE);
        
        //create entry for each feature
        for(String name : TYPES){
        	instertType(database, name);
        }
    }
    
    /**
     * Speichert eine neue Art in der Tabelle Art.
     * @param database
     * @param name
     */
    private static void instertType(SQLiteDatabase database, String name){
    	ContentValues values = new ContentValues();
    	values.put(KEY_NAME, name);
    	insertIntoDB(database, TABLE_TYPE, values);
    } 
    /**
     * Inserts one entry into the database
     * @param database
     * @param table
     * @param values
     */
    private static void insertIntoDB(SQLiteDatabase database, String table, ContentValues values){
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
     * Returns a cursor to typequery
     * @return
     */
    public ArrayList<FeatureType> getTypes() {  
    	//select types from database
		SQLiteDatabase database = getReadableDatabase();
		Cursor cursor = database.query(TABLE_TYPE, null, null, null, null, null, null);
		
		ArrayList<FeatureType> types = new ArrayList<FeatureType>();
		
		//add all types to list
		if (cursor.moveToFirst()) {
			do {
				FeatureType type = 
						new FeatureType(
								Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))),
							    cursor.getString(cursor.getColumnIndex(KEY_NAME)));

              // Adding type to list
				types.add(type);
			} while (cursor.moveToNext());
		}
		 
		return types;
    }  
}
