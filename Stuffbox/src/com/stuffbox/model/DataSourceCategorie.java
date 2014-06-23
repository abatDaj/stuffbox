package com.stuffbox.model;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataSourceCategorie {
	
	public static String ROOT_CATEGORY = "ROOT";
	
    /**
     * Erstellt die Tabelle Kategorie auf der Datenbank
     * @param database
     */
    public void createCategorieTable(SQLiteDatabase db){
        String CREATE_EIGENSCHAFT_TABLE = "CREATE TABLE " + DatabaseHandler.TABLE_CATEGORY + "("+ 
        		//create column id
        		DatabaseHandler.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
        		//create column name
        		DatabaseHandler.KEY_NAME + " TEXT," + 
        		//create column icon
        		DatabaseHandler.KEY_ICON + " INTEGER" + ")";
        db.execSQL(CREATE_EIGENSCHAFT_TABLE);
        
        //insert rootcategory into the database
        insertCategory(db, ROOT_CATEGORY, null);
    }
    
    /**
     * Speichert eine neue Kategorie in der Tabelle Eigenschaft.
     * @param database
     * @param name
     */
    public void insertCategory(SQLiteDatabase database, String name, Icon icon){
    	ContentValues values = new ContentValues();
    	values.put(DatabaseHandler.KEY_NAME, name);
    	if (icon != null)
    		values.put(DatabaseHandler.KEY_ICON, icon.getId());
    	DatabaseHandler.insertIntoDB(database, DatabaseHandler.TABLE_CATEGORY, values);
    } 
    
    /**
     * Gibt eine Liste aller Kategorien zur�ck, deren ids in der id Liste enthalten ist
     * @param database
     * @param selectCategorieIds Liste aller zu selektierenden Ids (bei null werden alle geladen)
     * @param types
     * @return
     */
    public ArrayList<Category> getCategories(	SQLiteDatabase database, 
	    										ArrayList<Integer> selectCategorieIds, 
	    										ArrayList<Icon> icons) {  
    	//erstelle where statement
    	String whereStatment = DatabaseHandler.getWhereStatementFromIDList(selectCategorieIds);
    	
    	//select types from database
    	Cursor cursor = database.query(DatabaseHandler.TABLE_CATEGORY, null, whereStatment, null, null, null, null);
		
		ArrayList<Category> categories = new ArrayList<Category>();
		
		//add all types to list
		if (cursor.moveToFirst()) {
			do {
				Icon icon = null;
				try {
					// NumberFormatException parseInt kann keine "null" verarbeiten (NumberFormatException), also
					// wenn die Kategoruie kein Icon hat
					int iconid = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ICON)));
					for(Icon temp : icons){
						if(iconid == temp.getId()){
							icon = temp;
						}
					}
				}
				catch (NumberFormatException e) {
					//TODO Exception/Ausgabe
				}
				
				Category category = 
						new Category(
								Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID))),
							    cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_NAME)),
							    icon);

              // Adding type to list
				categories.add(category);
			} while (cursor.moveToNext());
		}
		 
		return categories;
    }
}
