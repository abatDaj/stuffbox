package com.stuffbox.model;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataSourceIcon {
    /**
     * Erstellt die Tabelle Icon auf der Datenbank
     * @param database
     */
    public void createIconTable(SQLiteDatabase db){
        String CREATE_ICON_TABLE = "CREATE TABLE " + DatabaseHandler.TABLE_ICON + "("+ 
        		//create column id
        		DatabaseHandler.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
        		//create column name
        		DatabaseHandler.KEY_NAME + " TEXT," +
        		DatabaseHandler.KEY_DESCRIPTION + " TEXT " + ")";
        db.execSQL(CREATE_ICON_TABLE);
        
        //Einträge anlegen für alle Icons
        insertIcon(db, "icon_angry_minion", "Wütender Miniom");
    }
    
    /**
     * Speichert eine neues Icon in der Tabelle Icon. 
     * @param database
     * @param name Bezeichnung für das Icon
     * @param description Kompletter Pfad inklusive Name und Dateiendung 
     */
    private void insertIcon(SQLiteDatabase database, String name, String description){
    	ContentValues values = new ContentValues();
    	values.put(DatabaseHandler.KEY_NAME, name);
    	values.put(DatabaseHandler.KEY_DESCRIPTION, description);
    	DatabaseHandler.insertIntoDB(database, DatabaseHandler.TABLE_ICON, values);
    } 
    
    /**
     * Gibt eine List aller Icons zurück
     * @return
     */
    public ArrayList<Icon> getIcons(SQLiteDatabase database) {  
    	//select types from database
    	Cursor cursor = database.query(DatabaseHandler.TABLE_ICON, null, null, null, null, null, null);
		
		ArrayList<Icon> icons = new ArrayList<Icon>();
		
		//add all types to list
		if (cursor.moveToFirst()) {
			do {
				Icon icon = 
						new Icon(
								Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID))),
							    cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_NAME)),
							    cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_DESCRIPTION)));

              // Adding type to list
				icons.add(icon);
			} while (cursor.moveToNext());
		}
		 
		return icons;
    } 
}
