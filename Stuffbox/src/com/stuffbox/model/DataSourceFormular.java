package com.stuffbox.model;

import java.util.ArrayList;

import com.stuffbox.controller.Controller;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataSourceFormular {
    /**
     * Erstellt die Tabelle Eigenschaft auf der Datenbank
     * @param database
     */
    public void createFormularTable(SQLiteDatabase db){
    	//Erstellt die Formular Tabelle
        String CREATE_FORMULAR_TABLE = "CREATE TABLE " + DatabaseHandler.TABLE_FORMULAR + "("+ 
        		//Erstellt Spalte id
        		DatabaseHandler.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
        		//Erstellt Spalte Name
        		DatabaseHandler.KEY_NAME + " TEXT" + ")";
        db.execSQL(CREATE_FORMULAR_TABLE);
        
        //Erstellt die Formular-Eigenschaft Verkn�pfungstabelle
        String CREATE_FORMULAR_FEATURE_TABLE = "CREATE TABLE " + DatabaseHandler.TABLE_FORMULAR_FEATURE + "("+ 
        		//Erstellt Spalte Formular
        		DatabaseHandler.TABLE_FORMULAR + " INTEGER," + 
        		//Erstellt Spalte Item
        		DatabaseHandler.TABLE_FEATURE + " INTEGER," + 
        		//Erstellt Spalte Sortiernummer
        		DatabaseHandler.KEY_SORTNUMBER + " INTEGER," + 
        		//Erstellt Primearschluessel
        		"PRIMARY KEY(" + DatabaseHandler.TABLE_FORMULAR + "," + DatabaseHandler.TABLE_FEATURE + ")," +
        		//Erstellt Fremdschluessel zu Formulartabelle
                "FOREIGN KEY(" + DatabaseHandler.TABLE_FORMULAR + ") REFERENCES " 
        			+ DatabaseHandler.TABLE_FORMULAR + "(" + DatabaseHandler.KEY_ID + ")"  + " ON DELETE CASCADE " +
        		//Erstellt Fremdschluessel zu Eigenschaftentabelle
                "FOREIGN KEY(" + DatabaseHandler.TABLE_FEATURE + ") REFERENCES " 
        			+ DatabaseHandler.TABLE_FEATURE + "(" + DatabaseHandler.KEY_ID + ")" + " ON DELETE CASCADE " +")";
        db.execSQL(CREATE_FORMULAR_FEATURE_TABLE);
    }
	
    /**
     * Speichert eine neue Eigenschaft in der Tabelle Eigenschaft.
     * @param database
     * @param name
     */
    public Formular insertOrUpdateFormlar(SQLiteDatabase database, Formular formular){
    	//Formular in Datenbank einfuegen
    	ContentValues values = new ContentValues();
    	values.put(DatabaseHandler.KEY_NAME, formular.getName());

    	long rowid;
	    if ( formular.getId() != DatabaseHandler.INITIAL_ID ) {
	    	ArrayList<Long> selectFormularIds = new ArrayList<Long>();
	    	selectFormularIds.add(formular.getId());
	    	Formular oldFormular = getFormulars(database, selectFormularIds).get(0);
	    	
	    	//update
	    	ContentValues whereValues = new ContentValues();
	    	whereValues.put(DatabaseHandler.KEY_ID, formular.getId());
	        rowid = DatabaseHandler.updateEntryInDB(database, DatabaseHandler.TABLE_FORMULAR, values, whereValues, formular.getName());
	        
	        //update features
	        for(Feature feature : formular.getFeatures()){
	        	Feature featureFound = null;
	        	//suche in alten features nach den aktuellen
	        	for(Feature oldFeature : oldFormular.getFeatures()){
		        	if(feature.getId() == oldFeature.getId()){
		        		featureFound = oldFeature;
		        		break;
		        	}
	        	}
	        	if(featureFound != null){
	        		//wenn gefunden loesche es aus der liste
	        		oldFormular.removeFeature(featureFound);
	        		//TODO update
	        	}else{
	        		//wenn nicht gefunden fuege es auf der Datenbank hinzu
	        		insertFeatureOfFormular(database, formular, feature);
	        	}
	        }
	        //loesche verbleibende Features aus der Liste
	        ArrayList<Long> deleteFeatureIds = new ArrayList<Long>();
	        for(Feature oldFeature : oldFormular.getFeatures()){
	        	selectFormularIds.add(oldFeature.getId());	
	        }
	        if(!selectFormularIds.isEmpty()){
				String whereStatement = DatabaseHandler.createWhereStatementFromIDList(deleteFeatureIds, DatabaseHandler.TABLE_FEATURE);
				DatabaseHandler.deletefromDB(database, DatabaseHandler.TABLE_FORMULAR_FEATURE, whereStatement);		
	        }
	        
	        if (rowid > 0){
	        	return formular;
	        }
	    } else {	    	
	    	//insert
	    	long formularId = DatabaseHandler.insertIntoDB(database, DatabaseHandler.TABLE_FORMULAR, values, formular.getName());
	    	formular.setId(formularId);
	    	
	    	//Eigenschaften in Datenbank einfuegen
	    	for (Feature feature : formular.getFeatures()) {
	    		insertFeatureOfFormular(database, formular, feature);
			}
			if (formularId > DatabaseHandler.INITIAL_ID) {
				return formular;
			}
	    }
    	
    	
    	return null;
    } 
    
    private void insertFeatureOfFormular(SQLiteDatabase database, Formular formular, Feature feature){
    	ContentValues featurevalues = new ContentValues();
    	featurevalues.put(DatabaseHandler.TABLE_FORMULAR, formular.getId());
    	featurevalues.put(DatabaseHandler.TABLE_FEATURE, feature.getId());
    	featurevalues.put(DatabaseHandler.KEY_SORTNUMBER, feature.getSortnumber());
    	String logString = DatabaseHandler.TABLE_FORMULAR + " " + formular.getName()  + "(" + formular.getId() + ")" + " - " 
    					   + DatabaseHandler.TABLE_FEATURE + " " + feature.getName()  + "(" + feature.getId() + ")";
    	DatabaseHandler.insertIntoDB(database, DatabaseHandler.TABLE_FORMULAR_FEATURE, featurevalues, logString);
    }
    
	/**
	 * Loescht ein Formular
	 * 
	 * @param formulars
	 * @return Ob es erfolgreich geloescht wurde 
	 */
	public boolean deleteFormulars(SQLiteDatabase database, ArrayList<Formular> formulars) {
		ArrayList<Long> selectFormularIds = new ArrayList<Long>();
		for (Formular formular : formulars) {
			selectFormularIds.add(formular.getId());	
		}
		String whereStatement = DatabaseHandler.createWhereStatementFromIDList(selectFormularIds,null);
	
		long delRows = DatabaseHandler.deletefromDB(database, DatabaseHandler.TABLE_FORMULAR, whereStatement);		
		return delRows == 1 ? true : false;
	}
    
    /**
     * Gibt eine Liste aller Features zurueck, deren ids in der id Liste enthalten ist
     * @param database
     * @param selectFormularIds Liste aller zu selektierenden Ids (bei null werden alle geladen)
     * @param types
     * @return
     */
    public ArrayList<Formular> getFormulars( SQLiteDatabase database, 
    										 		ArrayList<Long> selectFormularIds) {  
    	
    	ArrayList<Formular> formulars = new ArrayList<Formular>();
		if(selectFormularIds != null && selectFormularIds.isEmpty()){
			return formulars;
		}
    	
    	//erstelle where statement
    	String whereStatement = DatabaseHandler.createWhereStatementFromIDList(selectFormularIds,null);
    	
    	//select types from database
    	Cursor cursor = database.query(DatabaseHandler.TABLE_FORMULAR, null, whereStatement, null, null, null, null);
		
		if(cursor == null){
			return formulars;
		}
		
		//add all types to list
		if (cursor.moveToFirst()) {
			do {
				long formularId = Long.parseLong(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID)));
				String formularName = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_NAME));
				
				//erhalte ids verknuepfter eigeschaften aus der verknuepfungstabelle
				ArrayList<Long> selectedFeatureIds = DatabaseHandler.getEntriesOfConjunctionTable(database, 
																			 formularId, 
																			 DatabaseHandler.TABLE_FORMULAR, 
																			 DatabaseHandler.TABLE_FEATURE, 
																			 DatabaseHandler.TABLE_FORMULAR_FEATURE);
				//erhalte daten der eigenschaften aller verknuepften eigenschaften aus der eigenschaftentabelle
				ArrayList<Feature> features = Controller.getInstance().getFeatures(selectedFeatureIds);
				
				//Formular erstellen
				Formular formular = 
						new Formular(
								formularId,
							    formularName,
							    features);

              // Adding type to list
				formulars.add(formular);
			} while (cursor.moveToNext());
		}
		 
		return formulars;
    }
}
