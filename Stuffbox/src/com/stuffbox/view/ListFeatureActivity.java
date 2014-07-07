package com.stuffbox.view;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Feature;

public class ListFeatureActivity extends ActionBarActivity {
	public static final long idNewFormularEntry = -1;
	
	private ListView mainListView ;
	private FeatureRadioArrayAdapter featureAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		mainListView = (ListView) findViewById( R.id.listView );
        
        ArrayList<Feature> features = new ArrayList<Feature>();
        features.addAll(Controller.getInstance().getFeatures(null));
        //name darf nicht geaendert werden
        features.remove(0);
        
        if(features.isEmpty()){
        	//TODO ausgabe dass keine features zum aendern verf�gbar sind
        	onChancel();
        }
        
        featureAdapter = new FeatureRadioArrayAdapter (this, features);
	    mainListView.setAdapter( featureAdapter );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_feature, menu);
		
		return true;
	}
	
	public void openCreatingNewFormular ()
	{
        Intent intent = new Intent();   
        intent.setClassName(getPackageName(), NewFormularActivity.class.getName());
        startActivity(intent);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	
		int itemId = item.getItemId();
	    switch (itemId) {
	    	case R.id.menu_save_new_item:
	    		onChoose();
		        return true;
		    case R.id.menu_chancel_item:
		    	onChancel();
		    case R.id.action_delete_features:
	            Intent intentChooseFeatures = new Intent();        
	            intentChooseFeatures.setClassName(getPackageName(), ChooseFeaturesActivity.class.getName());
	            startActivity(intentChooseFeatures);
		    default:
		    	return super.onOptionsItemSelected(item);
	    }		
	}
	
	/**
	 * 
	 * Die Detail-Item-Aktivität wird gestartet und das ausgewählte Formular
	 * wird an diese verschickt.
	 *
	 */

	public void onChoose(){
		Feature selectedFeature = featureAdapter.getCurrentFeature();
		Controller.getInstance().setCurrentFeature(selectedFeature);
        Intent intent = new Intent();   
        intent.setClassName(getPackageName(), NewFeatureActivity.class.getName());
        startActivity(intent);				
		this.finish();
	}
	
	/**
	 * 
	 * Zurueck zur aktuellen Kategorie
	 */	
	public void onChancel(){
        Intent intent = new Intent();   
        intent.setClassName(getPackageName(), ListCategoriesActivity.class.getName());
        startActivity(intent);				
		this.finish();
	}	
}