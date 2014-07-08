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

public class ChooseCategoriesActivity extends ActionBarActivity {

	public static final int REQUEST_CHOOSE_CATEGORIES = 0;
	
	private CategoryChooseArrayAdapter categoryAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		ListView mainListView = (ListView) findViewById( R.id.listView );
        ArrayList<Category> categories = Controller.getInstance().getCategories(null);
        
        categoryAdapter = new CategoryChooseArrayAdapter (this, categories);
	    mainListView.setAdapter( categoryAdapter );	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.change_menu_choose_categories, menu);
		return true;
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
	public void onSave(View view){
		
		ArrayList<Category> selectedCategories= categoryAdapter.getSelectedCategories();
		
		//Meldung, wenn keine Kategorie ausgewaehlt wurde
		if(selectedCategories.isEmpty()){
			new Messenger(this).showAlertMessage(
					getResources().getString(R.string.error), 
					getResources().getString(R.string.msg_alert_de_choose_category));
			return;
		}
		
		Controller.getInstance().setSelectedCategoriesInItem(selectedCategories);
		
		//zurueck zur anfragenden activity
		Intent intentMessage=new Intent();
		setResult(NewFormularActivity.REQUEST_NEW_FORMULAR,intentMessage);
		finish(); 
	}
	/**
	 * Vorgang wird abgebrochen - Daten werden verworfen
	 * @param view
	 */
	public void onCancel(View view){			
		this.finish();
	}
}
