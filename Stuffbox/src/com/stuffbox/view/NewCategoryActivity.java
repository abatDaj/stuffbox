package com.stuffbox.view;

import java.util.ArrayList;
import java.util.LinkedList;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Category;
import com.stuffbox.model.DataSourceCategory;
import com.stuffbox.model.DatabaseHandler;
import com.stuffbox.model.FeatureType;
import com.stuffbox.model.Icon;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NewCategoryActivity extends ActionBarActivity {
	
	Category categoryToEdit = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_new);

		Spinner spinner = (Spinner) findViewById(R.id.spinner_new_category_icon);
		//Controller.fillIconTableWithSomeIcons(this);
		ArrayList<Icon> allicons = Controller.getInstance().getIcons();
		LinkedList<Icon> list = new LinkedList<Icon>();

		for (Icon i : allicons)
			if (i.getName().contains(getResources().getText(R.string.prefix_icon_category))) 
				list.add(i);
		
		Icon[] icons = new Icon[list.size()];
		
		for (int i = 0 ;i < list.size();i++)
			icons[i] = list.get(i);
		
		IconArrayAdapter adapter = new IconArrayAdapter(this, icons);
		spinner.setAdapter(adapter);
		
		Category serializedCategory = (Category) getIntent().getSerializableExtra(Controller.EXTRA_EDIT_CATEGORY);
		if (serializedCategory != null) {
			if (!serializedCategory.getName().equals(DataSourceCategory.ROOT_CATEGORY)) {
				categoryToEdit = serializedCategory;
				// setzt den aktuellen Icon
				for (int i = 0; i < icons.length; i++)
					if (icons[i].getId() == serializedCategory.getIcon().getId()) {
						spinner.setSelection(i);
						break;
					}
				EditText editTextName = (EditText) findViewById(R.id.edit_category_name);
				editTextName.setText(categoryToEdit.getName());
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (categoryToEdit != null) {
			getSupportActionBar().setTitle(this.getResources().getString(R.string.actionbartitle_edit_category));
			getMenuInflater().inflate(R.menu.edit_category, menu);
		}
		else
			getMenuInflater().inflate(R.menu.new_category, menu);

		Icon icon = Controller.getInstance().getCurrentCategory().getIcon();
		if (icon !=null) 
			getSupportActionBar().setIcon(icon.getDrawableId());
		
		return true;
	}
	/**
	 * 
	 * Eine neue Kategorie wird angelegt.
	 *
	 * @param view
	 */
	public void onSave(View view){

		Spinner spinner = (Spinner) findViewById(R.id.spinner_new_category_icon);

		Icon selectedIcon = (Icon) spinner.getSelectedItem();
		String categoryName = ((TextView)findViewById(R.id.edit_category_name)).getText().toString();
		Controller.getInstance().insertCategory(categoryName, selectedIcon, Controller.getInstance().getCurrentCategory().getId());
		
        Intent intent = new Intent();   
        intent.setClassName(getPackageName(), ListCategoriesActivity.class.getName());
        startActivity(intent);
	}
	
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
