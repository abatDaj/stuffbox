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
		        "FOREIGN KEY(" + DatabaseHandler.KEY_PRECATEGORY + ") REFERENCES " + DatabaseHandler.TABLE_CATEGORY + "(" + DatabaseHandler.KEY_ID + ")" + ")";
		db.execSQL(CREATE_EIGENSCHAFT_TABLE);
	}

	/**
	 * Erstellt die Vernüpfungstabelle zwischen Kategorie und Oberkategorie
	 * 
	 * @param database
	 */
	public void createCategoryCategoryTable(SQLiteDatabase db) {
		String CREATE_CATEGORY_CATEGORY_TABLE = "CREATE TABLE " + DatabaseHandler.CATEGORY_CATEGORY + "(" +
		// die ID der Kategorie
		        DatabaseHandler.KEY_ID + " INTEGER NOT NULL," +
		        // die ID der Vorgaenger Kategorie
		        DatabaseHandler.KEY_PRECATEGORY + " INTEGER NOT NULL," +
		        // Beide als Schluessel festlegen
		        "PRIMARY KEY(" + DatabaseHandler.KEY_ID + ", " + DatabaseHandler.KEY_PRECATEGORY + "))";
		db.execSQL(CREATE_CATEGORY_CATEGORY_TABLE);
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
	public Category insertCategory(SQLiteDatabase database, String name, Icon icon, int precategoryId) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHandler.KEY_NAME, name);
		if (icon != null){
			values.put(DatabaseHandler.KEY_ICON, icon.getId());
		}
		values.put(DatabaseHandler.KEY_PRECATEGORY, precategoryId);
		
		long rowid = DatabaseHandler.insertIntoDB(database, DatabaseHandler.TABLE_CATEGORY, values, name);
		
		if (rowid > -1) {
			
			Cursor cursor = Controller.getInstance().getNewestRow(DatabaseHandler.TABLE_CATEGORY, DatabaseHandler.KEY_ID,
			        new String[] { DatabaseHandler.KEY_ID });
			if (!cursor.moveToFirst())
				return null;
			
			int newCategoryId = cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_ID));
			// Der Root-Kategorie wird -1 als precategoryId uebergeben und die
			// braucht keine Oberkategorie
			// if (precategoryId > -1)
			insertPreCategory(database, newCategoryId, precategoryId);
			return new Category(newCategoryId, name, icon, precategoryId);
		}
		return null;
	}

	/**
	 * Speichert Relation zwischen Ober- und Unterkategorie
	 * 
	 * @param database
	 * @param category
	 * @param precategory
	 * @return Die rowid
	 */
	private long insertPreCategory(SQLiteDatabase database, int category, int precategory) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHandler.KEY_ID, category);
		values.put(DatabaseHandler.KEY_PRECATEGORY, precategory);
		return DatabaseHandler.insertIntoDB(database, DatabaseHandler.CATEGORY_CATEGORY, values, "try to make a category, precatagory connection");
	}
	

	/**
	 * Löscht eine Kategorie
	 * 
	 * @param categoryId
	 * @return Ob es erfolgreich gelöscht wurde 
	 */
	public boolean deleteCategory(SQLiteDatabase database, Category category) {
		ContentValues whereValues = new ContentValues();
		whereValues.put(DatabaseHandler.KEY_ID, category.getId());
		long delRows = DatabaseHandler.deletefromDB(database, DatabaseHandler.TABLE_CATEGORY, whereValues);
		return delRows == 1 ? true : false;
	}

	/**
	 * Gibt alle Unterkategorien einer Kategorie zurueck
	 * 
	 * @param database
	 * @param categoryId
	 * @return Die Unterkategorien, gegebenenfalls eine leere Liste
	 */
	public ArrayList<Category> getSubCategories(SQLiteDatabase database, int categoryId) {
		String whereStatement = DatabaseHandler.KEY_PRECATEGORY + "=" + categoryId;
		Cursor cursor = database.query(DatabaseHandler.CATEGORY_CATEGORY, null, whereStatement, null, null, null, null);
		ArrayList<Long> subCategoryIds = new ArrayList<Long>();
		if (cursor.moveToFirst()){
			do{
				subCategoryIds.add(Long.valueOf(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_ID))));
			}
			while (cursor.moveToNext());
		}else{
			return new ArrayList<Category>();
		}
		return  getCategories(database, subCategoryIds, Controller.getInstance().getIcons());
	}
	
	/**
	 * Gibt Oberkategorie zurueck
	 * 
	 * @param database
	 * @param category
	 * @return Die Oberkategorien
	 */
	public Category getPreCategory(SQLiteDatabase database, Category category) {
		ArrayList<Long> preId = new ArrayList<Long>();
		preId.add(Long.valueOf(category.getPreCategory()));
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
		String whereStatment = DatabaseHandler.getWhereStatementFromIDList(selectCategorieIds, null);
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
				Category category = new Category(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID))), cursor.getString(cursor
				        .getColumnIndex(DatabaseHandler.KEY_NAME)), icon, Integer.parseInt(cursor.getString(cursor
				        .getColumnIndex(DatabaseHandler.KEY_PRECATEGORY))));
				// Adding type to list
				categories.add(category);
			} while (cursor.moveToNext());
		}
		return categories;
	}
}
