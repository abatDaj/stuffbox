package com.stuffbox.model;

import java.util.ArrayList;

import com.stuffbox.controller.Controller;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataSourceCategory {

	public static String ROOT_CATEGORY = "ROOT";

	/**
	 * Erstellt die Tabelle Kategorie auf der Datenbank
	 * 
	 * @param database
	 */
	public void createCategorieTable(SQLiteDatabase db) {
		String CREATE_EIGENSCHAFT_TABLE = "CREATE TABLE "
				+ DatabaseHandler.TABLE_CATEGORY + "(" +
				// create column id
				DatabaseHandler.KEY_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
				// create column name
				DatabaseHandler.KEY_NAME + " TEXT," +
				// create column icon
				DatabaseHandler.KEY_ICON + " INTEGER," +
				// create column precategory
				DatabaseHandler.KEY_PRECATEGORY + " INTEGER," +
				// add foreign key to table category
				"FOREIGN KEY(" + DatabaseHandler.KEY_PRECATEGORY
				+ ") REFERENCES " + DatabaseHandler.TABLE_CATEGORY + "("
				+ DatabaseHandler.KEY_ID + ")" + ")";
		db.execSQL(CREATE_EIGENSCHAFT_TABLE);
	}

	/**
	 * Erstellt die Vernüpfungstabelle zwischen Kategorie und Oberkategorie
	 * 
	 * @param database
	 */
	public void createCategoryCategoryTable(SQLiteDatabase db) {
		String CREATE_CATEGORY_CATEGORY_TABLE = "CREATE TABLE "
				+ DatabaseHandler.CATEGORY_CATEGORY + "(" +
				// die ID der Kategorie
				DatabaseHandler.KEY_ID + " INTEGER NOT NULL," +
				// die ID der Vorgänger Kategorie
				DatabaseHandler.KEY_PRECATEGORY + " INTEGER NOT NULL," +
				// Beide als Schlüssel festlegen
				"PRIMARY KEY(" + DatabaseHandler.KEY_ID + ", " + 
				DatabaseHandler.KEY_PRECATEGORY + "))";
		db.execSQL(CREATE_CATEGORY_CATEGORY_TABLE);
	}

	/**
	 * Speichert eine neue Kategorie in der Tabelle Eigenschaft.
	 * 
	 * @param database
	 * @param name
	 * @param icon
	 *            Ein Icon oder null (Default-Icon wird gesetzt)
	 * @param Die
	 *            ID der Oberkategorie
	 */
	public void insertCategory(SQLiteDatabase database, String name, Icon icon,
			int precategory) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHandler.KEY_NAME, name);
		if (icon != null) {
			values.put(DatabaseHandler.KEY_ICON, icon.getId());
		}
		values.put(DatabaseHandler.KEY_PRECATEGORY, precategory);

		long rowid = DatabaseHandler.insertIntoDB(database,
				DatabaseHandler.TABLE_CATEGORY, values, name);

		if (rowid > -1) {
			
			String[] columns = {"MAX("+DatabaseHandler.KEY_ID+")",
								DatabaseHandler.KEY_ID,
								DatabaseHandler.KEY_PRECATEGORY};
			
			Cursor cursor = database.query(DatabaseHandler.TABLE_CATEGORY,columns , null, null, null, null, null);
			
			if (!cursor.moveToFirst())
				throw new RuntimeException("Something went horribly wrong :-O");
			
			//System.err.print("\n\n ABC: "+ cursor.getColumnNames().length + " \n\n");
			//for(String s : cursor.getColumnNames())
			//		System.err.print(" Ä:"+s);
			int newCategoryId = cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_ID));
			int newPreCategoryId = cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_PRECATEGORY));
			insertPreCategory(database, newCategoryId, newPreCategoryId);
			// TODO vll. boolean returnieren, falls ein insert nicht klappt,
			// also falls rowid == -1 ist ?
		}
	}

	/**
	 * Speichert Relation zwischen Ober- und Unterkategorie
	 * 
	 * @param database
	 * @param category
	 * @param precategory
	 * @return Die rowid
	 */
	private long insertPreCategory(SQLiteDatabase database, int category,
			int precategory) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHandler.KEY_ID, category);
		values.put(DatabaseHandler.KEY_PRECATEGORY, precategory);
		return DatabaseHandler.insertIntoDB(database,
				DatabaseHandler.CATEGORY_CATEGORY, values,
				"try to make a category, precatagory connection");
	}

	/**
	 * Returniert alle Unterkategorien einer Kategorie
	 * 
	 * @param database
	 * @param categoryId
	 * @return Die Unterkategorien oder null, falls keine Kategorien gefunden wurden
	 */
	public ArrayList<Category> getSubCategories(SQLiteDatabase database, int categoryId) {

		String whereStatment = DatabaseHandler.KEY_ID+"="+categoryId;
		Cursor cursor = database.query(DatabaseHandler.CATEGORY_CATEGORY, null, whereStatment, null, null, null, null);
		ArrayList<Long> subCategoryIds = new ArrayList<Long>();
		
		if (cursor.moveToFirst()) 
			do
				subCategoryIds.add(Long.valueOf(cursor.getColumnIndex(DatabaseHandler.KEY_PRECATEGORY)));
			while 
				(cursor.moveToNext());
		
		ArrayList<Category> subCategories = getCategories(database,subCategoryIds,Controller.getInstance().getIcons());
		return subCategories;
	}

	/**
	 * Gibt eine Liste aller Kategorien zurück, deren ids in der id Liste
	 * enthalten ist
	 * 
	 * @param database
	 * @param selectCategorieIds Liste aller zu selektierenden Ids (bei null werden alle geladen)
	 * @param types
	 * @param icons Liste aller Kategorie-Icons
 	 * 
	 * @return Liste der gefundenen Kategorien oder null, falls ein Fehler auftrat
	 */
	public ArrayList<Category> getCategories(SQLiteDatabase database, ArrayList<Long> selectCategorieIds, ArrayList<Icon> icons) {
		
		// erstelle where statement
		String whereStatment = DatabaseHandler.getWhereStatementFromIDList( selectCategorieIds, null);

		// select types from database
		Cursor cursor = database.query(DatabaseHandler.TABLE_CATEGORY, null, whereStatment, null, null, null, null);

		ArrayList<Category> categories = new ArrayList<Category>();

		// add all types to list
		if (cursor.moveToFirst()) {
			do {
				Icon icon = null;
				try {
					// NumberFormatException parseInt kann keine "null" verarbeiten 
					//(NumberFormatException), also wenn die Kategoruie kein Icon hat, was nicht vorkommen darf
					int iconid = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ICON)));
					for (Icon temp : icons) {
						if (iconid == temp.getId()) {
							icon = temp;
						}
					}
				} catch (NumberFormatException e) {
					return null;
				}

				Category category = new Category(
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
