package com.stuffbox.model;

import java.util.ArrayList;

import com.stuffbox.controller.Controller;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataSourceItem {
    /**
     * Erstellt die Tabelle Item auf der Datenbank und die Verknüpfungstabelle Item-Eigenschaft
     * @param database
     */
    public void createItemTable(SQLiteDatabase db){
    	//Erstellt die Item Tabelle
        String CREATE_ITEM_TABLE = "CREATE TABLE " + DatabaseHandler.TABLE_ITEM + "("
                + DatabaseHandler.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + DatabaseHandler.KEY_NAME + " TEXT" + ")";
        db.execSQL(CREATE_ITEM_TABLE);
        
        //Erstellt die Item-Eigenschaft-Wert Verknüpfungstabelle
        String CREATE_FORMULAR_ITEM_TABLE = "CREATE TABLE " + DatabaseHandler.TABLE_FEATURE_ITEM + "("+ 
        		//create column formular
        		DatabaseHandler.TABLE_FEATURE + " INTEGER," + 
        		//create column item
        		DatabaseHandler.TABLE_ITEM + " INTEGER," + 
        		//create column wert
        		DatabaseHandler.KEY_VALUE + " STRING," + 
        		"PRIMARY KEY(" + DatabaseHandler.TABLE_FEATURE + "," + DatabaseHandler.TABLE_ITEM + ")," +
        		//add foreign key to table formular
                "FOREIGN KEY(" + DatabaseHandler.TABLE_FEATURE + ") REFERENCES " 
        			+ DatabaseHandler.TABLE_FEATURE + "(" + DatabaseHandler.KEY_ID + ")" +
        		//add foreign key to table item
                "FOREIGN KEY(" + DatabaseHandler.TABLE_ITEM + ") REFERENCES " 
        			+ DatabaseHandler.TABLE_ITEM + "(" + DatabaseHandler.KEY_ID + ")" +")";
        db.execSQL(CREATE_FORMULAR_ITEM_TABLE);
    }
	
    /**
     * Fügt ein Item der Datenbank hinzu.
     * TODO Kategoriezuordnung speichern
     * @param name
     */
    public void insertItem(Item item, SQLiteDatabase database){
    	ContentValues values = new ContentValues();
    	values.put(DatabaseHandler.KEY_NAME, item.getName());
    	
    	DatabaseHandler.insertIntoDB(database, DatabaseHandler.TABLE_ITEM, values);
    	
    	//TODO ID erhalten
    	
    	Formular formular = item.getFormular();
    	for (Feature feature : formular.getFeatures()) {
    		insertFeatureOfItem(database, feature, item);
		}
    	
    	//TODO Kategorie Zuordnung speichern
    }
    
    /**
     * Fügt den Wert einer Eigenschaft eines Items in der Datenbank hinzu

     * @param database
     * @param feature
     * @param item
     */
    public void insertFeatureOfItem(SQLiteDatabase database, Feature feature, Item item){
    	ContentValues values = new ContentValues();
    	values.put(DatabaseHandler.TABLE_FEATURE, feature.getId());
    	values.put(DatabaseHandler.KEY_VALUE, DataSourceFeature.getDatabaseStringOfValue(feature.getValue(), feature.getType()));
    	values.put(DatabaseHandler.TABLE_ITEM, item.getId());
    	
    	DatabaseHandler.insertIntoDB(database, DatabaseHandler.TABLE_FEATURE_ITEM, values);
    }
    
    /**
     * Selektiert die Werte der Eigenschaften eines Eintrages aus der Datenbank.
     * @param database
     * @param itemid
     * @param features
     */
    public void selectValuesOfFeatures( SQLiteDatabase database,
    								 	 String itemid,
    								 	 ArrayList<Feature> features ){
    	//erstelle where statement
    	StringBuilder whereStatement = new StringBuilder();
		whereStatement.append(" ");
		whereStatement.append(DatabaseHandler.TABLE_ITEM);
		whereStatement.append(" = ");
		whereStatement.append(itemid);
		whereStatement.append(" ");
		whereStatement.append(DatabaseHandler.SQL_AND);
		
    	ArrayList<Long> selectFeatureIds = new ArrayList<Long>();
    	for(Feature feature : features){
    		selectFeatureIds.add(feature.getId());
    	}
		whereStatement.append("(");
		whereStatement.append(DatabaseHandler.getWhereStatementFromIDList(selectFeatureIds,DatabaseHandler.TABLE_FEATURE));
		whereStatement.append(")");
    	
    	
    	//select types from database
    	Cursor cursor = database.query(DatabaseHandler.TABLE_FEATURE_ITEM, null, whereStatement.toString(), null, null, null, null);
    	
		//Werte in Feature speichern
		if (cursor.moveToFirst()) {
			do {
				int featureid = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHandler.TABLE_FEATURE)));
				String value = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_VALUE));
				if(value == null){
					//TODO Exception/Ausgabe
				}
				
				boolean featureWasFound = false;
				for (Feature feature : features) {
					if(feature.getId() == featureid){
						feature.setValue(value);
						featureWasFound = true;
					}
				}
				
				if (!featureWasFound) {
					//TODO Exception/Ausgabe
				}
			} while (cursor.moveToNext());
		}
    }
}
