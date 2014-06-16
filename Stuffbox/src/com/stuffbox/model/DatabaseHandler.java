package com.stuffbox.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper{

	private static final String TAG = DatabaseHandler.class.getSimpleName();
	
	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    private static final String UNDERLINE = "_";
    
    // Database Name
    private static final String DATABASE_NAME = "stuffbox";
    
    // item table name;
    private static final String TABLE_KATEGORIE = "kategorie";
    private static final String TABLE_EIGENSCHAFT = "eigenschaft";
    private static final String TABLE_ART = "art";
    private static final String TABLE_ITEM = "item";
    		
    // table columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ICON = "icon";
    private static final String KEY_ART = "art";
    private static final String KEY_EIGENSCHAFT = "eigenschaft";
    
    private SQLiteDatabase db;  
    private final Context context;  
    private String DB_PATH;  
    
    
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        //DB_PATH = "/data/data/" + context.getPackageName() + "/" + "";  
        
        //DB_PATH = context.getFilesDir().getPath() + "data/" + context.getPackageName() + "/";
    } 
	 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
    	createItemTable(db);
    	createArtTable(db);
    	createEigenschaftTable(db);
    }
 
    private void createItemTable(SQLiteDatabase db){
        String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_ITEM + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT" + ")";
        db.execSQL(CREATE_ITEM_TABLE);
    }
    
    private void createArtTable(SQLiteDatabase db){
        String CREATE_ART_TABLE = "CREATE TABLE " + TABLE_ART + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT" + ")";
        db.execSQL(CREATE_ART_TABLE);
    }
    
    private void createEigenschaftTable(SQLiteDatabase db){
        String CREATE_EIGENSCHAFT_TABLE = "CREATE TABLE " + TABLE_EIGENSCHAFT + "("+ 
        		//create column id
        		KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
        		//create column name
        		KEY_NAME + " TEXT," + 
        		//create column art
                KEY_ART + " INTEGER," + 
        		//add foreign key to table art
                "FOREIGN KEY(" + KEY_ART + ") REFERENCES " + TABLE_ART + "(" + KEY_ID + ")" + ")";
        db.execSQL(CREATE_EIGENSCHAFT_TABLE);
    }
    
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EIGENSCHAFT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KATEGORIE);
 
        // Create tables again
        onCreate(db);
    }
    
    public void instertItem(String name){
    	long rowID = -1;
    	
    	try{
	    	SQLiteDatabase db = getWritableDatabase();
	    	
	    	ContentValues values = new ContentValues();
	    	values.put(KEY_NAME, name);
	    	
	    	rowID = db.insert(TABLE_ITEM, null, values);
    	}catch(SQLiteException e){
    		Log.e(TAG, "insert()",e);
    	}finally{
    		Log.d(TAG, "insert(): rowId=" + rowID);
    	}
    }
	
}
