package com.stuffbox.model;

import java.util.ArrayList;

import com.stuffbox.controller.Controller;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataSourceItem {
    /**
     * Erstellt die Tabelle Item auf der Datenbank und die Verkn�pfungstabelle Item-Eigenschaft
     * @param database
     */
    public void createItemTable(SQLiteDatabase db){
    	//Erstellt die Item Tabelle
        String CREATE_ITEM_TABLE = "CREATE TABLE " + DatabaseHandler.TABLE_ITEM + "("
                + DatabaseHandler.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
        		+ DatabaseHandler.KEY_NAME + " TEXT," 
                + DatabaseHandler.TABLE_FORMULAR + " INTEGER" + ")";
        db.execSQL(CREATE_ITEM_TABLE);
        
        //Erstellt die Item-Eigenschaft-Wert Verkn�pfungstabelle
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
        
        //Erstellt die Item-Kategorie-Wert Verkn�pfungstabelle
        String CREATE_CATEGORY_ITEM_TABLE = "CREATE TABLE " + DatabaseHandler.TABLE_CATEGORY_ITEM + "("+ 
        		//create column kategorie
        		DatabaseHandler.TABLE_CATEGORY + " INTEGER," + 
        		//create column item
        		DatabaseHandler.TABLE_ITEM + " INTEGER," +
        		"PRIMARY KEY(" + DatabaseHandler.TABLE_CATEGORY + "," + DatabaseHandler.TABLE_ITEM + ")," +
        		//add foreign key to table kategorie
                "FOREIGN KEY(" + DatabaseHandler.TABLE_CATEGORY + ") REFERENCES " 
        			+ DatabaseHandler.TABLE_CATEGORY + "(" + DatabaseHandler.KEY_ID + ")" +
        		//add foreign key to table item
                "FOREIGN KEY(" + DatabaseHandler.TABLE_ITEM + ") REFERENCES " 
        			+ DatabaseHandler.TABLE_ITEM + "(" + DatabaseHandler.KEY_ID + ")" +")";
        db.execSQL(CREATE_CATEGORY_ITEM_TABLE); 
    }
	
    /**
     * F�gt ein Item der Datenbank hinzu.
     * TODO Kategoriezuordnung speichern
     * @param name
     */
    public Item insertItem(SQLiteDatabase database, String name, Formular formular, ArrayList<Category> categories){
    	ContentValues values = new ContentValues();
    	values.put(DatabaseHandler.KEY_NAME, name);
    	values.put(DatabaseHandler.TABLE_FORMULAR, formular.getId());
    	
    	long id = DatabaseHandler.insertIntoDB(database, DatabaseHandler.TABLE_ITEM, values, name);
    	
    	Item item = new Item(id, name, formular, categories);
    	
    	for (Feature feature : formular.getFeatures()) {
    		insertFeatureOfItem(database, feature, item);
		}
    	
    	for(Category category : categories){
    		insertCategoryOfItem(database, category, item);
    	}
    	
    	return item;
    }
    
    /**
     * F�gt den Wert einer Eigenschaft eines Items in der Verknuepfungstabelle auf der Datenbank hinzu

     * @param database
     * @param feature
     * @param item
     */
    public void insertFeatureOfItem(SQLiteDatabase database, Feature feature, Item item){
    	ContentValues values = new ContentValues();
    	values.put(DatabaseHandler.TABLE_FEATURE, feature.getId());
    	values.put(DatabaseHandler.KEY_VALUE, DataSourceFeature.getDatabaseStringOfValue(feature.getValue(), feature.getType()));
    	values.put(DatabaseHandler.TABLE_ITEM, item.getId());
    	
    	DatabaseHandler.insertIntoDB(database, DatabaseHandler.TABLE_FEATURE_ITEM, values, item.getName());
    }
    /**
     * F�gt eine Kategorie der ein Item zuegehoert in der Verknuepfungstabelle auf der Datenbank hinzu

     * @param database
     * @param feature
     * @param item
     */
    public void insertCategoryOfItem(SQLiteDatabase database, Category category, Item item){
    	ContentValues values = new ContentValues();
    	values.put(DatabaseHandler.TABLE_CATEGORY, category.getId());
    	values.put(DatabaseHandler.TABLE_ITEM, item.getId());
    	
    	DatabaseHandler.insertIntoDB(database, DatabaseHandler.TABLE_CATEGORY_ITEM, values, item.getName());
    }
    /**
     * Gibt eine Liste aller Items zurueck, , deren ids in der id Liste enthalten ist
     * @param database
     * @param selectIds Liste aller zu selektierenden Ids (bei null werden alle geladen)
     * @return
     */
    public ArrayList<Item> getItems( SQLiteDatabase database, 
			 			  ArrayList<Long> selectIds){
    	//erstelle where statement
    	String whereStatement = DatabaseHandler.getWhereStatementFromIDList(selectIds,null);
    	
    	//select types from database
    	Cursor cursor = database.query(DatabaseHandler.TABLE_ITEM, null, whereStatement, null, null, null, null);
		
		ArrayList<Item> items = new ArrayList<Item>();
		
		//add all types to list
		if (cursor.moveToFirst()) {
			do {
				long itemId = Long.parseLong(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID)));
				String itemName = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_NAME));
				long formularId = Long.parseLong(cursor.getString(cursor.getColumnIndex(DatabaseHandler.TABLE_FORMULAR)));
				
				//selektiere Formular f�r item
				ArrayList<Long> selectFormularId = new ArrayList<Long>();
				selectFormularId.add(formularId);
				Formular formular = Controller.getInstance().getFormulars(selectFormularId).get(0);
				setValuesOfFeatures(database, itemId, formular.getFeatures());
				
				//erhalte ids verknuepfter kategorien aus der verknuepfungstabelle
				ArrayList<Long> selectedFeatureIds = DatabaseHandler.getEntriesOfConjunctionTable(database, 
						 itemId, 
						 DatabaseHandler.TABLE_ITEM, 
						 DatabaseHandler.TABLE_CATEGORY,
						 DatabaseHandler.TABLE_CATEGORY_ITEM);
				//erhalte daten der kategrien aller verknuepften kategorien aus der kategorietabelle
				ArrayList<Category> categories =Controller.getInstance().getCategories(selectedFeatureIds);
				
				//Item erstellen
				Item item = new Item(itemId, itemName, formular, categories);
				
				// Adding type to list
				items.add(item);
			} while (cursor.moveToNext());
		}
		 
		return items;
    }
    
    /**
     * Selektiert die Werte der Eigenschaften eines Eintrages aus der Datenbank.
     * @param database
     * @param itemid
     * @param features
     */
    public void setValuesOfFeatures( SQLiteDatabase database,
    								 	 long itemid,
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
						Object valueAsObject = DataSourceFeature.getValueFromDatabaseString(value, feature.getType());
						feature.setValue(valueAsObject);
						featureWasFound = true;
					}
				}
				
				if (!featureWasFound) {
					//TODO Exception/Ausgabe
				}
				
				//TODO kategorie
			} while (cursor.moveToNext());
		}
    }
    
    /**
     * Gibt eine Liste aller Items zurueck, die einer Kategorie enthalten sind.
     * @param database
     * @param categoryID 
     * @return Die Items in der spezifizierten Kategorie
     */
    public ArrayList<Item> getItemsOfACategory( SQLiteDatabase database, long categoryID) {
    	    	
    	ContentValues whereValues = new ContentValues();
    	whereValues.put(DatabaseHandler.TABLE_CATEGORY, categoryID);
    	
    	String whereStatement = DatabaseHandler.createWhereStatementFromContentValues(whereValues);
    	
    	//select types from database
    	Cursor cursor = database.query(DatabaseHandler.TABLE_CATEGORY_ITEM, null, whereStatement, null, null, null, null);
		
		ArrayList<Item> items = new ArrayList<Item>();
		
		//add all types to list
		if (cursor.moveToFirst()) {
			do {
				long itemId = cursor.getInt(cursor.getColumnIndex(DatabaseHandler.TABLE_ITEM));
				ArrayList<Long> anIdinAList = new ArrayList<Long>();
				anIdinAList.add(itemId);
				Item anFoundItem = this.getItems(database, anIdinAList).get(0);
				items.add(anFoundItem);
			} while (cursor.moveToNext());
		}
		return items;
    }
}
