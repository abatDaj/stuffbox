package com.stuffbox.view;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Feature;

public class NewFormularActivity  extends ActionBarActivity {
	
	private DynamicListView listFeaturesSelected;
	private ListView listFeaturesNotSelected;
	private FeatureArrayAdapter selectedfeaturesAdapter;
	private FeatureArrayAdapter selectedNotFeaturesAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.formular_new);
		
		initializeListFeaturesNotSelected();
		initializeListFeaturesSelected();         
	}
	
	/**
	 * Initializes the list with features selected (initial null)
	 */
	private void initializeListFeaturesNotSelected(){
		ArrayList<Feature> notSelectedfeatures = Controller.getFeatures(null);
		listFeaturesNotSelected = (ListView) findViewById( R.id.list_features_not_selected );
		listFeaturesNotSelected.setOnItemClickListener(new OnItemClickListener()
        {
	        @Override
	        public void onItemClick(AdapterView<?> l, View v, int position, long id)
	        {
	        	Feature choosenFeature = selectedNotFeaturesAdapter.getItem(position);
	        	selectedNotFeaturesAdapter.remove(choosenFeature);
	        	selectedfeaturesAdapter.add(choosenFeature);
	        }
        });
		
		selectedNotFeaturesAdapter = new FeatureArrayAdapter(this, R.layout.row_selection_feature, notSelectedfeatures, false);
		listFeaturesNotSelected.setAdapter( selectedNotFeaturesAdapter );	
	}

	private void initializeListFeaturesSelected(){
		ArrayList<Feature> selectedfeatures = new ArrayList<Feature>();

        listFeaturesSelected = (DynamicListView) findViewById(R.id.list_features_selected);
        listFeaturesSelected.setOnItemClickListener(new OnItemClickListener()
        {
	        @Override
	        public void onItemClick(AdapterView<?> l, View v, int position, long id)
	        {
	        	Feature choosenFeature = selectedfeaturesAdapter.getItem(position);
	        	selectedfeaturesAdapter.remove(choosenFeature);
	        	selectedNotFeaturesAdapter.add(choosenFeature);
	        }
        });
        
        selectedfeaturesAdapter = new FeatureArrayAdapter(this, R.layout.row_selection_feature, selectedfeatures, true);
        listFeaturesSelected.setItemList(selectedfeatures);
        listFeaturesSelected.setAdapter(selectedfeaturesAdapter);
        listFeaturesSelected.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.new_formlar, menu);
		return true;
	}
	/**
	 * 
	 * Ein neues Formular wird angelegt.
	 *
	 * @param view
	 */
	public void onSave(View view){

		//TODO
	}
	/**
	 * Vorgang wird abgebrochen - Daten werden verworfen
	 * @param view
	 */
	public void onCancel(View view){

		CharSequence text = "abbrechen - Miauuuuu, Miauuuuu";
		int duration = 7;
		Toast toast = Toast.makeText(this, text, duration);
		toast.show();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int itemId = item.getItemId();
	    switch (itemId) {
	        case R.id.menu_save:
	        	onSave(null);
	            return true;
	        case R.id.menu_abort:
	            onCancel(null);
	            return true;
	        case R.id.action_settings:
	        	//TODO do something
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
