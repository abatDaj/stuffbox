package com.stuffbox.view;

import java.util.ArrayList;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Category;
import com.stuffbox.model.Feature;
import com.stuffbox.model.Formular;
import com.stuffbox.model.Icon;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

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
			
			// TODO beim speicher oder abbrechen des Item-Erstellungsvorgangs sollte im Controller
			// selectedCategoriesInItem auf null gesetzt werden.
			Controller.getInstance().setSelectedCategoriesInItem(null);
									
			ImageButton imageButton = (ImageButton) findViewById(R.id.detail_item_category_icon);
			imageButton.setImageResource(Controller.getInstance().getCurrentCategory().getIcon().getDrawableId());
			TextView textView = (TextView) findViewById(R.id.detail_item_category_text);
			textView.setText(Controller.getInstance().getCurrentCategory().getName());

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
        
		//Spinner spinner = (Spinner) findViewById(R.id.spinner_categories_in_item);
		//ArrayList<Category> selectedCategories = new ArrayList<Category>();
		//selectedCategories.add((Category) spinner.getSelectedItem());
				
		//TODO erhalte gesetzte Werte des Formlars
		
		/*Controller.getInstance().insertItem(itemName, formular, selectedCategories);
		

        Intent intent = new Intent();        
        intent.setClassName(getPackageName(), ListCategoriesActivity.class.getName());
        startActivity(intent);*/
		Controller.getInstance().sayIt("onSave()");

	}
	/**
	 * Vorgang wird abgebrochen - Daten werden verworfen
	 * @param view
	 */
	public void onCancel(View view){			
		this.finish();
	}
	
	public void openCategoryChooser (View view) {
        Intent intent = new Intent();        
        intent.setClassName(getPackageName(), ChooseCategoriesActivity.class.getName());
        startActivity(intent);
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
