package com.stuffbox.model;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataSourceType {
    
    /**
     * Erstellt die Tabelle Art auf der Datenbank und erzeugt zusätzlich 
     * die Einträge.
     * @param database
     */
    public void createTypeTable(SQLiteDatabase database){
        String CREATE_ART_TABLE = "CREATE TABLE " + DatabaseHandler.TABLE_TYPE + "("
                + DatabaseHandler.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + DatabaseHandler.KEY_NAME + " TEXT" + ")";
        database.execSQL(CREATE_ART_TABLE);
        
        //Einträge anlegen für jede Art
        for(FeatureType type : FeatureType.values()){
        	insertType(database, type.toString());
        }
    }
    
    /**
     * Speichert eine neue Art in der Tabelle Art.
     * @param database
     * @param name
     */
    private void insertType(SQLiteDatabase database, String name){
    	ContentValues values = new ContentValues();
    	values.put(DatabaseHandler.KEY_NAME, name);
    	DatabaseHandler.insertIntoDB(database, DatabaseHandler.TABLE_TYPE, values, name);
    } 
	
    /**
     * Gibt eine List aller Arten zurück
     * @return
     */
    public ArrayList<FeatureType> getTypes(SQLiteDatabase database) {  
    	//select types from database
    	Cursor cursor = database.query(DatabaseHandler.TABLE_TYPE, null, null, null, null, null, null);
		
		ArrayList<FeatureType> types = new ArrayList<FeatureType>();
		
		//add all types to list
		if (cursor.moveToFirst()) {
			do {
				FeatureType type = 
						FeatureType.getFeatureType(
								Long.parseLong(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID))),
							    cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_NAME)));

              // Adding type to list
				types.add(type);
			} while (cursor.moveToNext());
		}
		 
		return types;
    }  
}
