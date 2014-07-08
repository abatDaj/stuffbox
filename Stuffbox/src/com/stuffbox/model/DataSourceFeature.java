package com.stuffbox.model;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataSourceFeature {
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
					insertOrUpdateFeature(db, new Feature(DatabaseHandler.INITIAL_ID, DEFAULT_FEATURES[i], type));
				}
			}
        }
    }
	
    /**
     * Speichert eine neue Eigenschaft in der Tabelle Eigenschaft.
     * @param database
     * @param name
     */
    public Feature insertOrUpdateFeature(SQLiteDatabase database, Feature feature){
    	ContentValues values = new ContentValues();
    	values.put(DatabaseHandler.KEY_NAME, feature.getName());
    	values.put(DatabaseHandler.TABLE_TYPE, feature.getType().getId());
		
    	long rowid;
	    if ( feature.getId() != DatabaseHandler.INITIAL_ID ) {
	    	//update
	    	ContentValues whereValues = new ContentValues();
	    	whereValues.put(DatabaseHandler.KEY_ID, feature.getId());
	        rowid = DatabaseHandler.updateEntryInDB(database, DatabaseHandler.TABLE_FEATURE, values, whereValues, feature.getName());
	        if (rowid > 0){
	        	return feature;
	        }
	    } else {	    	
	    	//insert
	        rowid = DatabaseHandler.insertIntoDB(database, DatabaseHandler.TABLE_FEATURE, values, feature.getName());
			if (rowid > DatabaseHandler.INITIAL_ID) {
				feature.setId(rowid);
				return feature;
			}
	    }
    	return null;
    } 
    
	/**
	 * Loescht eine Eigenschaft
	 * 
	 * @param feature
	 * @return Ob es erfolgreich geloescht wurde 
	 */
	public boolean deleteFeatures(SQLiteDatabase database, ArrayList<Feature> features) {
		ArrayList<Long> selectFeatureIds = new ArrayList<Long>();
		for (Feature feature : features) {
			selectFeatureIds.add(feature.getId());	
		}
		String whereStatement = DatabaseHandler.createWhereStatementFromIDList(selectFeatureIds,null);
	
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
		ArrayList<Feature> features = new ArrayList<Feature>();
    	if(selectFeatureIds != null && selectFeatureIds.isEmpty()){
			return features;
		}
    	
    	//erstelle where statement
    	String whereStatment = DatabaseHandler.createWhereStatementFromIDList(selectFeatureIds,null);
    	
    	//select types from database
    	Cursor cursor = database.query(DatabaseHandler.TABLE_FEATURE, null, whereStatment, null, null, null, null);
		
		//add all types to list
		if(cursor == null){
			return features;
		}
		
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
     * @param value
     * @param type
     * @return
     */
	public static String getDatabaseStringOfValue(Object value, FeatureType type){
		if(value == null){
			return "null";
		}
		return value.toString();
	}
	/**
	 * Erstellt aus dem uebergebenen String einen entsprechendes Objekt
	 * @param valueAsString
	 * @param type
	 * @return
	 */
	public static Object getValueFromDatabaseString(String valueAsString, FeatureType type){
		Object valueAsObject;
		try {
			switch (type) {
			case Wahrheitswert:
				valueAsObject = Boolean.parseBoolean(valueAsString);
				break;
			case Ranking:
				valueAsObject = Integer.parseInt(valueAsString);
				break;
			default:
				valueAsObject = valueAsString;
				break;
			}
		} catch (Exception e) {
			Log.e(DatabaseHandler.TAG, e.toString());
			valueAsObject = valueAsString;
		}

		return valueAsObject;
	}
}
