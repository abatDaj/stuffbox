package com.stuffbox.model;

import java.util.ArrayList;

import com.stuffbox.controller.Controller;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataSourceFormular {
    /**
     * Erstellt die Tabelle Eigenschaft auf der Datenbank
     * @param database
     */
    public void createFormularTable(SQLiteDatabase db){
    	//Erstellt die Formular Tabelle
        String CREATE_FORMULAR_TABLE = "CREATE TABLE " + DatabaseHandler.TABLE_FORMULAR + "("+ 
        		//create column id
        		DatabaseHandler.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
        		//create column name
        		DatabaseHandler.KEY_NAME + " TEXT," + ")";
        db.execSQL(CREATE_FORMULAR_TABLE);
        
        //Erstellt die Item-Formular Verkn�pfungstabelle
        String CREATE_FORMULAR_ITEM_TABLE = "CREATE TABLE " + DatabaseHandler.TABLE_FORMULAR_ITEM + "("+ 
        		//create column formular
        		DatabaseHandler.TABLE_FORMULAR + " INTEGER," + 
        		//create column item
        		DatabaseHandler.TABLE_ITEM + " INTEGER," + 
        		"PRIMARY KEY(" + DatabaseHandler.TABLE_FORMULAR + "," + DatabaseHandler.TABLE_ITEM + ")," +
        		//add foreign key to table formular
                "FOREIGN KEY(" + DatabaseHandler.TABLE_FORMULAR + ") REFERENCES " 
        			+ DatabaseHandler.TABLE_FORMULAR + "(" + DatabaseHandler.KEY_ID + ")" +
        		//add foreign key to table item
                "FOREIGN KEY(" + DatabaseHandler.TABLE_ITEM + ") REFERENCES " 
        			+ DatabaseHandler.TABLE_ITEM + "(" + DatabaseHandler.KEY_ID + ")" +")";
        db.execSQL(CREATE_FORMULAR_ITEM_TABLE);
    }
	
    /**
     * Speichert eine neue Eigenschaft in der Tabelle Eigenschaft.
     * @param database
     * @param name
     */
    public void insertFeature(SQLiteDatabase database, String name, FeatureType featureType){
    	ContentValues values = new ContentValues();
    	values.put(DatabaseHandler.KEY_NAME, name);
    	values.put(DatabaseHandler.TABLE_TYPE, featureType.getId());
    	DatabaseHandler.insertIntoDB(database, DatabaseHandler.TABLE_FEATURE, values);
    } 
    
	
    /**
     * Gibt eine Liste aller Features zur�ck, deren ids in der id Liste enthalten ist
     * @param database
     * @param selectFeatureIds Liste aller zu selektierenden Ids (bei null werden alle geladen)
     * @param types
     * @return
     */
    public ArrayList<Feature> getFeatures(	SQLiteDatabase database, 
    										ArrayList<Integer> selectFeatureIds, 
    										ArrayList<FeatureType> types) {  
    	//erstelle where statement
    	String whereStatment = DatabaseHandler.getWhereStatementFromIDList(selectFeatureIds);
    	
    	//select types from database
    	Cursor cursor = database.query(DatabaseHandler.TABLE_FEATURE, null, whereStatment, null, null, null, null);
		
		ArrayList<Feature> features = new ArrayList<Feature>();
		
		//add all types to list
		if (cursor.moveToFirst()) {
			do {
				int typeid = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHandler.TABLE_TYPE)));
				FeatureType type = null;
				for(FeatureType temp : types){
					if(typeid == temp.getId()){
						type = temp;
					}
				}
				if(type == null){
					//TODO Exception/Ausgabe
				}
				Feature feature = 
						new Feature(
								Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID))),
							    cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_NAME)),
							    type);

              // Adding type to list
				features.add(feature);
			} while (cursor.moveToNext());
		}
		 
		return features;
    }
}
