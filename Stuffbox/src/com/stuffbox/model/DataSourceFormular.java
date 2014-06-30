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
        		DatabaseHandler.KEY_NAME + " TEXT" + ")";
        db.execSQL(CREATE_FORMULAR_TABLE);
        
        //Erstellt die Formular-Eigenschaft Verkn�pfungstabelle
        String CREATE_FORMULAR_FEATURE_TABLE = "CREATE TABLE " + DatabaseHandler.TABLE_FORMULAR_FEATURE + "("+ 
        		//create column formular
        		DatabaseHandler.TABLE_FORMULAR + " INTEGER," + 
        		//create column item
        		DatabaseHandler.TABLE_FEATURE + " INTEGER," + 
        		//create column sortnumber
        		DatabaseHandler.KEY_SORTNUMBER + " INTEGER," + 
        		//create primary key
        		"PRIMARY KEY(" + DatabaseHandler.TABLE_FORMULAR + "," + DatabaseHandler.TABLE_FEATURE + ")," +
        		//add foreign key to table formular
                "FOREIGN KEY(" + DatabaseHandler.TABLE_FORMULAR + ") REFERENCES " 
        			+ DatabaseHandler.TABLE_FORMULAR + "(" + DatabaseHandler.KEY_ID + ")" +
        		//add foreign key to table item
                "FOREIGN KEY(" + DatabaseHandler.TABLE_FEATURE + ") REFERENCES " 
        			+ DatabaseHandler.TABLE_FEATURE + "(" + DatabaseHandler.KEY_ID + ")" +")";
        db.execSQL(CREATE_FORMULAR_FEATURE_TABLE);
    }
	
    /**
     * Speichert eine neue Eigenschaft in der Tabelle Eigenschaft.
     * @param database
     * @param name
     */
    public Formular insertFormlar(SQLiteDatabase database, String name, ArrayList<Feature> features){
    	//Formular in Datenbank einfuegen
    	ContentValues values = new ContentValues();
    	values.put(DatabaseHandler.KEY_NAME, name);
    	long formularId = DatabaseHandler.insertIntoDB(database, DatabaseHandler.TABLE_FORMULAR, values, name);
    	
    	//Eigenschaften in Datenbank einfuegen
    	for (Feature feature : features) {
        	ContentValues featurevalues = new ContentValues();
        	featurevalues.put(DatabaseHandler.TABLE_FORMULAR, formularId);
        	featurevalues.put(DatabaseHandler.TABLE_FEATURE, feature.getId());
        	featurevalues.put(DatabaseHandler.KEY_SORTNUMBER, feature.getSortnumber());
        	String logString = DatabaseHandler.TABLE_FORMULAR + " " + name  + "(" + formularId + ")" + " - " 
        					   + DatabaseHandler.TABLE_FEATURE + " " + feature.getName()  + "(" + feature.getId() + ")";
        	DatabaseHandler.insertIntoDB(database, DatabaseHandler.TABLE_FORMULAR_FEATURE, featurevalues, logString);
		}
    	
    	return new Formular(formularId, name, features);
    } 
    
    /**
     * Gibt eine Liste aller Features zurueck, deren ids in der id Liste enthalten ist
     * @param database
     * @param selectFormularIds Liste aller zu selektierenden Ids (bei null werden alle geladen)
     * @param types
     * @return
     */
    public static ArrayList<Formular> getFormulars( SQLiteDatabase database, 
    										 		ArrayList<Long> selectFormularIds) {  
    	//erstelle where statement
    	String whereStatement = DatabaseHandler.getWhereStatementFromIDList(selectFormularIds,null);
    	
    	//select types from database
    	Cursor cursor = database.query(DatabaseHandler.TABLE_FORMULAR, null, whereStatement, null, null, null, null);
		
		ArrayList<Formular> formulars = new ArrayList<Formular>();
		
		//add all types to list
		if (cursor.moveToFirst()) {
			do {
				long formularId = Long.parseLong(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID)));
				String formularName = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_NAME));
				
				ArrayList<Feature> features;
				features = DataSourceFeature.getFeaturesOfConjunctionTable(database, 
														 formularId, 
														 DatabaseHandler.TABLE_FORMULAR, 
														 DatabaseHandler.TABLE_FORMULAR_FEATURE);
				
				//Formular erstellen
				Formular formular = 
						new Formular(
								formularId,
							    formularName,
							    features);

              // Adding type to list
				formulars.add(formular);
			} while (cursor.moveToNext());
		}
		 
		return formulars;
    }
}
