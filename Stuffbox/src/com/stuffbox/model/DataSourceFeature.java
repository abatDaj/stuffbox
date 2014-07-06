package com.stuffbox.model;

import java.util.ArrayList;

import com.stuffbox.controller.Controller;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataSourceFeature {
	//TODO Zuorndung Text und Foto koennte schoener sein
    public static final String[] DEFAULT_FEATURES 
    = {	"Name",
		"Bild"};
    public static final String[] DEFAULT_FEATURE_TYPES 
    = {	"Text",
		"Foto"};
    
    /**
     * Erstellt die Tabelle Eigenschaft auf der Datenbank
     * @param database
     */
    public void createFeatureTable(SQLiteDatabase db, ArrayList<FeatureType> types){
    	//Tabelle eigenschaft anlegen
        String CREATE_FEATURE_TABLE = "CREATE TABLE " + DatabaseHandler.TABLE_FEATURE + "("+ 
        		//Spalte Id erstellen
        		DatabaseHandler.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
        		//Spalte Name erstellen
        		DatabaseHandler.KEY_NAME + " TEXT," + 
        		//Spalte Typ erstellen
        		DatabaseHandler.KEY_TYPE + " INTEGER," + 
        		//Fremdschluessel zu Type Tabelle
                "FOREIGN KEY(" + DatabaseHandler.KEY_TYPE + ") REFERENCES " 
        			+ DatabaseHandler.TABLE_TYPE + "(" + DatabaseHandler.KEY_ID + ")" + ")";
        db.execSQL(CREATE_FEATURE_TABLE);
        
        //Eigenschaften anlegen fuer default features
        for (int i = 0; i < DEFAULT_FEATURES.length; i++) {
        	for (FeatureType type : types) {
				if(DEFAULT_FEATURE_TYPES[i].equals(type.toString())){
					insertFeature(db, DEFAULT_FEATURES[i], type);
				}
			}
        }
    }
	
    /**
     * Speichert eine neue Eigenschaft in der Tabelle Eigenschaft.
     * @param database
     * @param name
     */
    public Feature insertFeature(SQLiteDatabase database, String name, FeatureType featureType){
    	ContentValues values = new ContentValues();
    	values.put(DatabaseHandler.KEY_NAME, name);
    	values.put(DatabaseHandler.TABLE_TYPE, featureType.getId());
    	long id = DatabaseHandler.insertIntoDB(database, DatabaseHandler.TABLE_FEATURE, values, name);
    	return new Feature(id, name, featureType);
    } 
    
	/**
	 * Loescht eine Eigenschaft
	 * 
	 * @param feature
	 * @return Ob es erfolgreich geloescht wurde 
	 */
	public boolean deleteFeatures(SQLiteDatabase database, ArrayList<Feature> features) {
		ContentValues whereValues = new ContentValues();
		
		ArrayList<Long> selectFormularIds = new ArrayList<Long>();
		for (Feature feature : features) {
			selectFormularIds.add(feature.getId());	
		}
		String whereStatement = DatabaseHandler.createWhereStatementFromIDList(selectFormularIds,null);
	
		long delRows = DatabaseHandler.deletefromDB(database, DatabaseHandler.TABLE_FEATURE, whereStatement);		
		return delRows == 1 ? true : false;
	}
    
    /**
     * Gibt eine Liste aller Features zurueck, deren ids in der id Liste enthalten ist
     * @param database
     * @param selectFeatureIds Liste aller zu selektierenden Ids (bei null werden alle geladen)
     * @param types
     * @return
     */
    public ArrayList<Feature> getFeatures(	SQLiteDatabase database, 
    										ArrayList<Long> selectFeatureIds, 
    										ArrayList<FeatureType> types) {  
    	//erstelle where statement
    	String whereStatment = DatabaseHandler.createWhereStatementFromIDList(selectFeatureIds,null);
    	
    	//select types from database
    	Cursor cursor = database.query(DatabaseHandler.TABLE_FEATURE, null, whereStatment, null, null, null, null);
		
		ArrayList<Feature> features = new ArrayList<Feature>();
		
		//add all types to list
		if (cursor.moveToFirst()) {
			do {
				int typeid = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHandler.TABLE_TYPE)));
				FeatureType type = null;
				for(FeatureType temp : types){
					if(typeid == temp.getId()){
						type = temp;
					}
				}
				if(type == null){
					//TODO Exception/Ausgabe
				}
				Feature feature = 
						new Feature(
								Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID))),
							    cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_NAME)),
							    type);

              // Adding type to list
				features.add(feature);
			} while (cursor.moveToNext());
		}
		 
		return features;
    }
    
    /**
     * Erstellt aus dem uebergebenen Objekt einen speicherbaren String
     * TODO Type einbeziehen
     * @param value
     * @param type
     * @return
     */
	public static String getDatabaseStringOfValue(Object value, FeatureType type){
		return value.toString();
	}
	/**
	 * Erstellt aus dem uebergebenen String einen entsprechendes Objekt
	 * TODO Type einbeziehen
	 * @param valueAsString
	 * @param type
	 * @return
	 */
	public static Object getValueFromDatabaseString(String valueAsString, FeatureType type){
		Object valueAsObject;
		switch (type) {
		case Wahrheitswert:
			valueAsObject = Boolean.parseBoolean(valueAsString);
			break;
		case Ranking:
			valueAsObject = Integer.parseInt(valueAsString);
			break;
		default:
			valueAsObject = valueAsString.toString();
			break;
		}
		return valueAsObject;
	}
}
