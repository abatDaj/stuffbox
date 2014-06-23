package com.stuffbox.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;

import com.stuffbox.R;

import android.content.ContentValues;
import android.content.Context;
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
        
        //Eintr�ge anlegen f�r alle Icons
        insertIcon(db, "icon_angry_minion", "W�tender Miniom");
    }
    
    /**
     * Speichert eine neues Icon in der Tabelle Icon. 
     * @param database
     * @param name Bezeichnung f�r das Icon
     * @param description Kompletter Pfad inklusive Name und Dateiendung 
     */
    private void insertIcon(SQLiteDatabase database, String name, String description){
    	ContentValues values = new ContentValues();
    	values.put(DatabaseHandler.KEY_NAME, name);
    	values.put(DatabaseHandler.KEY_DESCRIPTION, description);
    	DatabaseHandler.insertIntoDB(database, DatabaseHandler.TABLE_ICON, values);
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
    
    /**
     * temporär: Füllt ein paar die Tabelle mit ein paar Icons.
     * 
     */
    public void fillIconTableWithSomeIcons (Context context, SQLiteDatabase database)
    {
    	Field[] drawableFields = R.drawable.class.getFields();
		
		// holt alle Icons mit dem Prefix "category_icon_"
		for (int i = 0; i < drawableFields.length; i++)
			if (drawableFields[i].getName().contains( context.getResources().getText(R.string.prefix_icon_category)))  
				insertIcon(database,drawableFields[i].getName(),"egal");
    }
    
}
