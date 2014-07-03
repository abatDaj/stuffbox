package com.stuffbox.view;

import java.util.ArrayList;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Category;
import com.stuffbox.model.DataSourceCategory;
import com.stuffbox.model.DatabaseHandler;
import com.stuffbox.model.Feature;
import com.stuffbox.model.Icon;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.support.v4.app.DialogFragment; //api 8 ! 
import android.support.v4.app.FragmentManager;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class ChooseCategoriesActivity extends ActionBarActivity {

	private CategoryChooseArrayAdapter categoryAdapter;
	//private ArrayList<Category> selectedCatgegories = new ArrayList<Category>();
	
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
	        	//TODO do something
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
		Controller.getInstance().sayIt(String.valueOf(selectedCategories.size()));
		Controller.getInstance().setSelectedCategoriesInItem(selectedCategories);
		// finish() beendet die Kategorie und man wird automatisch in die letzte (mit dem gleichen Zustand) versetzt.
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
