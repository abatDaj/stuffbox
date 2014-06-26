package com.stuffbox.model;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataSourceCategory {
	
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
        		DatabaseHandler.KEY_ICON + " INTEGER," +
        		//create column precategory
        		DatabaseHandler.KEY_PRECATEGORY + " INTEGER," + 
        		//add foreign key to table category
                "FOREIGN KEY(" + DatabaseHandler.KEY_PRECATEGORY + ") REFERENCES " 
        			+ DatabaseHandler.TABLE_CATEGORY + "(" + DatabaseHandler.KEY_ID + ")"+ ")";
        db.execSQL(CREATE_EIGENSCHAFT_TABLE);
        
        //insert rootcategory into the database
        insertCategory(db, ROOT_CATEGORY, null, -1);

    }
    
    /**
     * Speichert eine neue Kategorie in der Tabelle Eigenschaft.
     * @param database
     * @param name
     */
    public void insertCategory(SQLiteDatabase database, String name, Icon icon, int precategory){
    	ContentValues values = new ContentValues();
    	values.put(DatabaseHandler.KEY_NAME, name);
    	if (icon != null){
    		values.put(DatabaseHandler.KEY_ICON, icon.getId());
    	}
    	values.put(DatabaseHandler.KEY_PRECATEGORY, precategory);
    	
    	DatabaseHandler.insertIntoDB(database, DatabaseHandler.TABLE_CATEGORY, values, name);
    } 
    
    /**
     * Gibt eine Liste aller Kategorien zurück, deren ids in der id Liste enthalten ist
     * @param database
     * @param selectCategorieIds Liste aller zu selektierenden Ids (bei null werden alle geladen)
     * @param types
     * 
     * @return
     */
    public ArrayList<Category> getCategories(	SQLiteDatabase database, 
	    										ArrayList<Long> selectCategorieIds, 
	    										ArrayList<Icon> icons) {  
    	//erstelle where statement
    	String whereStatment = DatabaseHandler.getWhereStatementFromIDList(selectCategorieIds,
																		   null);
    	
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
							    icon,
							    Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_PRECATEGORY))));

              // Adding type to list
				categories.add(category);
			} while (cursor.moveToNext());
		}
		 
		return categories;
    }
}