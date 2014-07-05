package com.stuffbox.view;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Category;
import com.stuffbox.model.Feature;
import com.stuffbox.model.Formular;
import com.stuffbox.model.Icon;
import com.stuffbox.model.Item;
import com.stuffbox.view.helper.ActivityWithATimePickerEditText;
import com.stuffbox.view.helper.DatePickerFragment;
import com.stuffbox.view.helper.EditTextDatePicker;
import com.stuffbox.view.helper.Utility;

public class DetailItemActivity extends ActionBarActivity implements ActivityWithATimePickerEditText {
	
	private ListView mainListView;

	private FeatureArrayAdapterForDetailItem featureAdapter;
	private Formular formular;
	private boolean changeMode = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_item);
		
		formular = (Formular) getIntent().getSerializableExtra(Controller.EXTRA_FORMULAR_FOR_NEW_ITEM);
		
		if (Controller.getInstance().getCurrentItem() != null){
			changeMode = false;
		}else if (formular != null) {
			changeMode = true;
		}else{ 
			//TODO
			new RuntimeException ("Something went horribly wrong :-0");
		}
		
		Item currentItem = Controller.getInstance().getCurrentItem();
		Controller.getInstance().setCurrentItem(null);
		
		//Setze Namensansicht
		if(!changeMode){
			EditText name = (EditText) findViewById(R.id.editNameFeature);
			name.setText(currentItem.getName());
			name.setEnabled(changeMode);	
		}
		
		//setze Werte für Kategorieanzeige
		if(changeMode){
			//initial ist aktuelle Kategorie gesetzt
			ArrayList<Category> initCategories = new ArrayList<Category>();
			initCategories.add(Controller.getInstance().getCurrentCategory());
			Controller.getInstance().setSelectedCategoriesInItem(initCategories);	
		}else{
			Controller.getInstance().setSelectedCategoriesInItem(currentItem.getCategories());		
		}
		showCategoryText();

		// Alle Eigenschaften des Formulars anzeigen
		mainListView = (ListView) findViewById( R.id.featureListViewInDetailItem);	        		

		ArrayList<Feature> features = null;
		if(changeMode){
			features = formular.getFeatures();
		}else{
			features = currentItem.getFormular().getFeatures();
		}
        featureAdapter = new FeatureArrayAdapterForDetailItem (this, features, this);
        

        featureAdapter.setEditable(changeMode);
	    mainListView.setAdapter( featureAdapter );
	    
	    // Groesse der Liste anhand der Anzahl der Eigenschaften berechnen.
        Utility.setListViewHeightBasedOnChildren(mainListView, 100);
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
	 * Ein neues Formular wird angelegt.
	 *
	 * @param view
	 */
	public void onSave(View view){
		//speicher item auf der Datenbank
		String itemName = ((TextView)findViewById(R.id.editNameFeature)).getText().toString();
		ArrayList<Category> selectedCategories = Controller.getInstance().getSelectedCategoriesInItem();
		
		for(int i = 0; i < mainListView.getChildCount(); i++)
		{
			View lView = (View) mainListView.getChildAt(i);
			// TODO Callback-Methoden werden aber nur aktiviert, 
			// wenn das gleiche mit den Kindern und Enkeln von lView passiert ?!?
			lView.clearFocus(); 
		} 
		
		Controller.getInstance().insertItem(itemName, formular, selectedCategories);
		Controller.getInstance().setCurrentItem(Controller.getInstance().popLastInsertedItem());

		Controller.getInstance().setSelectedCategoriesInItem(null);

		Intent intent = getIntent();
		finish();
		startActivity(intent);
	}
	/**
	 * Vorgang wird abgebrochen - Daten werden verworfen
	 * @param view
	 */
	public void onCancel(View view){	
		Controller.getInstance().setSelectedCategoriesInItem(null);

		this.finish();
	}
	
	public void openCategoryChooser (View view) {
        Intent intent = new Intent();        
        intent.setClassName(getPackageName(), ChooseCategoriesActivity.class.getName());
        startActivityForResult(intent, ChooseCategoriesActivity.REQUEST_CHOOSE_CATEGORIES);
	}
	
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ChooseCategoriesActivity.REQUEST_CHOOSE_CATEGORIES) {
        	//aktualisiere Kategorieanzeige
        	showCategoryText();
        }
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

	@Override
	public void showTimePickerDialog(EditTextDatePicker editTextDatePicker) {
		DatePickerFragment newFragment = new DatePickerFragment();
		newFragment.setEditTextPicker(editTextDatePicker);
	    newFragment.show(getSupportFragmentManager(), "datePicker");
	}
	
	private void showCategoryText(){
		TextView textView = (TextView) findViewById(R.id.detail_item_category_text);
		
		ArrayList<Category> categories = Controller.getInstance().getSelectedCategoriesInItem();
		//erstelle Text für zugeordnete Kategorien
		StringBuilder stringBuilder = new StringBuilder();
		for (int index = 0; index < categories.size(); index++) {
			Category category = categories.get(index);
			stringBuilder.append(category);
			if(index != categories.size()-1){
				stringBuilder.append(", ");
			}
		}		

		textView.setText(stringBuilder.toString());	
	}
}
