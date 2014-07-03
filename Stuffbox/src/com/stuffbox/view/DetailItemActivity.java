package com.stuffbox.view;

import java.util.ArrayList;
import java.util.LinkedList;

import com.stuffbox.R;
import com.stuffbox.R.id;
import com.stuffbox.R.layout;
import com.stuffbox.R.menu;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Category;
import com.stuffbox.model.Feature;
import com.stuffbox.model.Formular;
import com.stuffbox.model.Icon;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class DetailItemActivity extends ActionBarActivity {
	
	private ListView mainListView ;

	private FeatureArrayAdapterForDetailItem featureAdapter;
	private Formular formular;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_item);
		
		formular = (Formular) getIntent().getSerializableExtra(Controller.EXTRA_FORMULAR_FOR_NEW_ITEM);
		if (formular != null) {
			
			//Controller.fillIconTableWithSomeIcons(this);
			ArrayList<Category> allcats = Controller.getInstance().getCategories(null);
			Spinner spinner = (Spinner) findViewById(R.id.spinner_categories_in_item);
			CategoryChooseArrayAdapter categoryChooseAdapter = new CategoryChooseArrayAdapter(this, allcats);
			spinner.setAdapter(categoryChooseAdapter);

			
			// Alle Eigenschaften des Formulars anzeigen
			mainListView = (ListView) findViewById( R.id.featureListViewInDetailItem);	        		

	        ArrayList<Feature> features = formular.getFeatures();
	        featureAdapter = new FeatureArrayAdapterForDetailItem (this, features);
		    mainListView.setAdapter( featureAdapter );
		    // Größe der Liste anhand der Anzahl der Eigenschaften berechnen.

	        Utility.setListViewHeightBasedOnChildren(mainListView, 100);
		}
		else // TODO
			 new RuntimeException ("Something went horribly wrong :-0");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		Category currentCategory = Controller.getInstance().getCurrentCategory();
		Icon icon = currentCategory.getIcon();
		if (icon !=null)
			getSupportActionBar().setIcon(icon.getDrawableId());
		
		getMenuInflater().inflate(R.menu.change_menu, menu);
		return true;
	}

	/**
	 * 0
	 * Ein neues Formular wird angelegt.
	 *
	 * @param view
	 */
	public void onSave(View view){
		//speicher item auf der Datenbank
		String itemName = ((TextView)findViewById(R.id.editNameFeature)).getText().toString();
        
		Spinner spinner = (Spinner) findViewById(R.id.spinner_categories_in_item);
		ArrayList<Category> selectedCategories = new ArrayList<Category>();
		selectedCategories.add((Category) spinner.getSelectedItem());
				
		//TODO erhalte gesetzte Werte des Formlars
		
		Controller.getInstance().insertItem(itemName, formular, selectedCategories);
		
//        Intent intent = new Intent();        
//        intent.setClassName(getPackageName(), ListCategoriesActivity.class.getName());
//        startActivity(intent);
	}
	/**
	 * Vorgang wird abgebrochen - Daten werden verworfen
	 * @param view
	 */
	public void onCancel(View view){			
		this.finish();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	
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
