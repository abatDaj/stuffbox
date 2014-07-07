package com.stuffbox.view;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.controller.Messenger;
import com.stuffbox.model.Category;
import com.stuffbox.model.Feature;

public class ChooseFeaturesActivity extends ActionBarActivity {

	public static final int REQUEST_CHOOSE_CATEGORIES = 0;
	
	private FeatureChooseArrayAdapter featureAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		ListView mainListView = (ListView) findViewById( R.id.listView );
        ArrayList<Feature> features = Controller.getInstance().getFeatures(null);
        //name darf nicht geloescht werden
        features.remove(0);
        
        featureAdapter = new FeatureChooseArrayAdapter (this, features);
	    mainListView.setAdapter( featureAdapter );	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.delete_features, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	
		int itemId = item.getItemId();
	    switch (itemId) {
	        case R.id.menu_delete:
	        	onDelete(null);
	            return true;
	        case R.id.menu_abort:
	        	onCancel(null);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/**
	 * 
	 * Kategorien wurden ausgewaehlt
	 *
	 * @param view
	 */
	public void onDelete(View view){
		//TODO Dialog der nachfraget
		
		ArrayList<Feature> selectedFeatures = featureAdapter.getSelectedFeatures();
		Controller.getInstance().deleteFeatures(selectedFeatures);
		
		//zurueck zur anfragenden activity
        Intent intent = new Intent();                
        intent.setClassName(getPackageName(), ListCategoriesActivity.class.getName());
        startActivity(intent);	
	}
	/**
	 * Vorgang wird abgebrochen - Daten werden verworfen
	 * @param view
	 */
	public void onCancel(View view){			
		this.finish();
	}
}
