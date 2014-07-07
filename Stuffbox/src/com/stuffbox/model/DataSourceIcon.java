package com.stuffbox.model;

import java.util.ArrayList;

import com.stuffbox.R;

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
    }
    
    /**
     * Speichert eine neues Icon in der Tabelle Icon. 
     * @param database
     * @param name Bezeichnung für das Icon
     * @param description Kompletter Pfad inklusive Name und Dateiendung 
     */
    public void insertIcon(SQLiteDatabase database, String name, String description){
    	ContentValues values = new ContentValues();
    	values.put(DatabaseHandler.KEY_NAME, name);
    	values.put(DatabaseHandler.KEY_DESCRIPTION, description);
    	DatabaseHandler.insertIntoDB(database, DatabaseHandler.TABLE_ICON, values, name);
    } 
    
    /**
     * Gibt eine List aller Icons zur�ck
     * @return
     */
    public ArrayList<Icon> getIcons(SQLiteDatabase database) {  
    	//select types from database
    	Cursor cursor = database.query(DatabaseHandler.TABLE_ICON, null, null, null, null, null, null);
		
		ArrayList<Icon> icons = new ArrayList<Icon>();
		
		//add all types to list
		if (cursor.moveToFirst()) {
			do {
				
				String iconName = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_NAME));
				Icon icon = 
						new Icon(
								cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_ID)),
								iconName,
							    cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_DESCRIPTION)));
				try {
	                int drawableId = R.drawable.class.getField(iconName).getInt(null);
	                icon.setDrawableId(drawableId);
                } catch (NoSuchFieldException e) { 
	                e.printStackTrace();
                }
				catch (IllegalAccessException e) { 
	                e.printStackTrace();
				}
				// Adding type to list
				icons.add(icon);
			} while (cursor.moveToNext());
		}
		return icons;
    } 
    
    /**
     * Holt+ sich ein Icon
     *  
     * @param id

     */
    public Icon getIcon(SQLiteDatabase database, int id){
    	String[] columns = {DatabaseHandler.KEY_ID};
    	Cursor cursor = database.query(DatabaseHandler.TABLE_ICON, columns, String.valueOf(id), null, null, null, null);
    	Icon icon = null;
		if (cursor.moveToFirst()) 
			icon = new Icon( Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID))),
							 cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_NAME)),
							 cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_DESCRIPTION)));
		return icon;
	}

}
