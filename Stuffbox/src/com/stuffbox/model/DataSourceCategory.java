package com.stuffbox.model;

import java.util.ArrayList;

import com.stuffbox.controller.Controller;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataSourceCategory {
	public static String	ROOT_CATEGORY	= "Stuffbox";

	/**
	 * Erstellt die Tabelle Kategorie auf der Datenbank
	 * 
	 * @param database
	 */
	public void createCategorieTable(SQLiteDatabase db) {
		String CREATE_EIGENSCHAFT_TABLE = "CREATE TABLE " + DatabaseHandler.TABLE_CATEGORY + "(" +
		// create column id
		        DatabaseHandler.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
		        // create column name
		        DatabaseHandler.KEY_NAME + " TEXT," +
		        // create column icon
		        DatabaseHandler.KEY_ICON + " INTEGER," +
		        // create column precategory
		        DatabaseHandler.KEY_PRECATEGORY + " INTEGER," +
		        // add foreign key to table category
		        "FOREIGN KEY(" + DatabaseHandler.KEY_PRECATEGORY + ") REFERENCES " 
		        	+ DatabaseHandler.TABLE_CATEGORY + "(" + DatabaseHandler.KEY_ID + ")" + " ON DELETE CASCADE " + ")";
		db.execSQL(CREATE_EIGENSCHAFT_TABLE);
	}

	/**
	 * Speichert eine neue Kategorie in der Tabelle Eigenschaft.
	 * 
	 * @param database
	 * @param name
	 * @param icon
	 *            Ein Icon oder null (Default-Icon wird gesetzt)
	 * @param precategory
	 *            Die ID der Oberkategorie
	 */
	public Category insertOrUpdateCategory(SQLiteDatabase database, Category category) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHandler.KEY_NAME, category.getName());
		if( !ROOT_CATEGORY.equals(category.getName())){
			values.put(DatabaseHandler.KEY_PRECATEGORY, category.getPreCategoryId());
		}else{
			values.put(DatabaseHandler.KEY_PRECATEGORY, DatabaseHandler.INDEX_OF_ROOT_CATEGORY);
		}
		
		if (category.getIcon() != null){
			values.put(DatabaseHandler.KEY_ICON, category.getIcon().getId());
		}
		
		long rowid;
		
	    if ( category.getId() != DatabaseHandler.INITIAL_ID ) {
	    	//update
	    	ContentValues whereValues = new ContentValues();
	    	whereValues.put(DatabaseHandler.KEY_ID, category.getId());
	        rowid = DatabaseHandler.updateEntryInDB(database, DatabaseHandler.TABLE_CATEGORY, values, whereValues, category.getName());
	        if (rowid > 0){
	        	return category;
	        }
	    } else {	    	
	    	//insert
	        rowid = DatabaseHandler.insertIntoDB(database, DatabaseHandler.TABLE_CATEGORY, values, category.getName());
			if (rowid > DatabaseHandler.INITIAL_ID) {
				category.setId(rowid);
				return category;
			}
	    }
		return null;
	}

	/**
	 * Loescht eine Kategorie
	 * 
	 * @param categoryId
	 * @return Ob es erfolgreich geloescht wurde 
	 */
	public boolean deleteCategory(SQLiteDatabase database, Category category) {
		ContentValues whereValues = new ContentValues();
		whereValues.put(DatabaseHandler.KEY_ID, category.getId());
		long delRows = DatabaseHandler.deletefromDB(database, DatabaseHandler.TABLE_CATEGORY, whereValues);
		return delRows == 1 ? true : false;
	}
	
	/**
	 * TODO: Funktioniert noch nicht.
	 * Loescht eine Kategorie und alle Unterkategorien und die jeweiligen Items.
	 * 
	 *
	 * @param database
	 * param category
	 * @return Ob alles erfolgreich geloescht wurde 
	 */
	public boolean deleteCategoryRecursively(SQLiteDatabase database, Category categoryToDeleteRecursively) {
				
		ArrayList<Category> allCategories = Controller.getInstance().getCategories(null);
		ArrayList<Category> allCategoriesToDelete = new ArrayList<Category>();
		
		for (Category category : allCategories) 
		{
			for( long idOfPreCategory = category.getPreCategoryId();;)
			{
				if (idOfPreCategory == categoryToDeleteRecursively.getId())
				{
					allCategoriesToDelete.add(category);
					break;
				} 
				else if (idOfPreCategory == DatabaseHandler.INDEX_OF_ROOT_CATEGORY) 
					break;
				//Category nextSubCategory = getCategory (database, idOfPreCategory)
				ArrayList<Long> l = new ArrayList<Long> ();
				l.add(idOfPreCategory);
				Category nextSubCategory = getCategories(database, l, Controller.getInstance().getIcons()).get(0);
				
				if (nextSubCategory == null) //TODO Darf eigentlich nie passieren
					break; 
				idOfPreCategory = nextSubCategory.getId();
			}						
		}
		boolean allItemsAndSubCategoriesWereDeleted = true;
		for (Category categoryToDelete : allCategoriesToDelete) 
		{
			if ( !Controller.getInstance().deleteItemsOfCategory(categoryToDelete.getId()) )
				allItemsAndSubCategoriesWereDeleted = false;
			if ( !Controller.getInstance().deleteCategory(categoryToDelete) )
				allItemsAndSubCategoriesWereDeleted = false;
		}
		return allItemsAndSubCategoriesWereDeleted;
	}

	/**
	 * Gibt alle Unterkategorien einer Kategorie zurueck
	 * 
	 * @param database
	 * @param categoryId
	 * @return Die Unterkategorien, gegebenenfalls eine leere Liste
	 */
	public ArrayList<Category> getSubCategories(SQLiteDatabase database, long categoryId) {
		String whereStatement = DatabaseHandler.KEY_PRECATEGORY + "=" + categoryId;
		Cursor cursor = database.query(DatabaseHandler.TABLE_CATEGORY, null, whereStatement, null, null, null, null);
		ArrayList<Long> subCategoryIds = new ArrayList<Long>();
		if (cursor.moveToFirst()){
			do{
				Long subCategoryId = Long.valueOf(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_ID)));
				//Root Kategorie kann keine Unterkategorie sein
				if(subCategoryId != DatabaseHandler.INDEX_OF_ROOT_CATEGORY){
					subCategoryIds.add(subCategoryId);
				}
			}
			while (cursor.moveToNext());
		}else{
			return new ArrayList<Category>();
		}
		return  getCategories(database, subCategoryIds, Controller.getInstance().getIcons());
	}
	
	/**
	 * Holt die Daten anhand der KategorieId von der Datenbank, isntanziert diese 
	 * Kategorie und returniert sie.
	 * 
	 * @param database
	 * @param categoryId
	 * @return Die gefundene Kategorie
	 */
	public Category getCategory(SQLiteDatabase database, long categoryId) {
		ArrayList<Long> selectCategorieIds = new ArrayList<Long>();
		selectCategorieIds.add(categoryId);
		return getCategories(database, selectCategorieIds, Controller.getInstance().getIcons()).get(0);	
	}
	
	/**
	 * Gibt Oberkategorie zurueck
	 * 
	 * @param database
	 * @param category
	 * @return Die Oberkategorie
	 */
	public Category getPreCategoryId(SQLiteDatabase database, Category category) {
		ArrayList<Long> preId = new ArrayList<Long>();
		preId.add(Long.valueOf(category.getPreCategoryId()));
		return getCategories(database, preId, Controller.getInstance().getIcons()).get(0);
	}

	/**
	 * Gibt eine Liste aller Kategorien zurueck, deren ids in der id Liste
	 * enthalten ist
	 * 
	 * @param database
	 * @param selectCategorieIds
	 *            Liste aller zu selektierenden Ids (bei null werden alle
	 *            geladen)
	 * @param types
	 * @param icons
	 *            Liste aller Kategorie-Icons
	 * 
	 * @return Liste der gefundenen Kategorien oder null, falls ein Fehler
	 *         auftrat
	 */
	public ArrayList<Category> getCategories(SQLiteDatabase database, ArrayList<Long> selectCategorieIds, ArrayList<Icon> icons) {
		// erstelle where statement
		String whereStatment = DatabaseHandler.createWhereStatementFromIDList(selectCategorieIds, null);
		// select types from database
		Cursor cursor = database.query(DatabaseHandler.TABLE_CATEGORY, null, whereStatment, null, null, null, null);
		ArrayList<Category> categories = new ArrayList<Category>();
		// add all types to list
		if (cursor.moveToFirst()) {
			do {
				Icon icon = null;
				try {
					// NumberFormatException parseInt kann keine "null" verarbeiten
					// (NumberFormatException), also wenn die Kategorie kein Icon hat, was nicht vorkommen darf
					int iconid = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ICON)));
					for (Icon temp : icons) {
						if (iconid == temp.getId()) {
							icon = temp;
						}
					}
				} catch (NumberFormatException e) {
					return null;
				}
				
				long pre;
				if(ROOT_CATEGORY.equals(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_NAME)))){
					pre = DatabaseHandler.INITIAL_ID;
				}else{
					pre = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_PRECATEGORY)));
				}
				
				Category category = new Category(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID))), cursor.getString(cursor
				        .getColumnIndex(DatabaseHandler.KEY_NAME)), icon, pre);
				// Adding type to list
				categories.add(category);
			} while (cursor.moveToNext());
		}
		return categories;
	}
}
