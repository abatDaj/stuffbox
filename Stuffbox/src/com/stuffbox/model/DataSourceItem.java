package com.stuffbox.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class DataSourceItem {
    /**
     * Erstellt die Tabelle Item auf der Datenbank
     * @param database
     */
    public void createItemTable(SQLiteDatabase db){
        String CREATE_ITEM_TABLE = "CREATE TABLE " + DatabaseHandler.TABLE_ITEM + "("
                + DatabaseHandler.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + DatabaseHandler.KEY_NAME + " TEXT" + ")";
        db.execSQL(CREATE_ITEM_TABLE);
    }
	
    /**
     * Fügt ein Item der Datenbank hinzu.
     * TODO Daten des Items speichern
     * @param name
     */
    public void insertItem(String name, SQLiteDatabase database){
    	ContentValues values = new ContentValues();
    	values.put(DatabaseHandler.KEY_NAME, name);
    	
    	DatabaseHandler.insertIntoDB(database, DatabaseHandler.TABLE_ITEM, values);
    }
}
