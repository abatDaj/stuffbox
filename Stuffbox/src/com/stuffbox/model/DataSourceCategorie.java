package com.stuffbox.model;

import android.database.sqlite.SQLiteDatabase;

public class DataSourceCategorie {
    public void createCategorieTable(SQLiteDatabase db){
        String CREATE_EIGENSCHAFT_TABLE = "CREATE TABLE " + DatabaseHandler.TABLE_CATEGORY + "("+ 
        		//create column id
        		DatabaseHandler.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
        		//create column name
        		DatabaseHandler.KEY_NAME + " TEXT," + 
        		//create column icon
        		DatabaseHandler.KEY_ICON + " TEXT" + ")";
        db.execSQL(CREATE_EIGENSCHAFT_TABLE);
    }
    
    
}
