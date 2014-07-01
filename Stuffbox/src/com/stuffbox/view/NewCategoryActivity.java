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

import android.support.v4.app.DialogFragment;
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

public class NewCategoryActivity extends ActionBarActivity implements DeleteDialogFragment.DeleteDialogListener  {
	
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
	public void onSaveCategory(){
		Spinner spinner = (Spinner) findViewById(R.id.spinner_new_category_icon);
		Icon selectedIcon = (Icon) spinner.getSelectedItem();
		String categoryName = ((TextView)findViewById(R.id.edit_category_name)).getText().toString();
		Category newCategory = Controller.getInstance().insertCategory(categoryName, selectedIcon, Controller.getInstance().getCurrentCategory().getId());
		Controller.getInstance().setCurrentCategory(newCategory);
        Intent intent = new Intent();   
        intent.setClassName(getPackageName(), ListCategoriesActivity.class.getName());
        startActivity(intent);
	}
	
	public void onUpdate(){
		Toast.makeText(this, "Update", 7).show();
	}

	/**
	 * 
	 * Zurück zur aktuellen Kategorie
	 */	
	public void onChancel(){
        Intent intent = new Intent();   
        intent.setClassName(getPackageName(), ListCategoriesActivity.class.getName());
        startActivity(intent);				
		this.finish();
	}
	
	/**
	 * 
	 * Löscht die Kategorie. Fragt aber vorher sicherheitshalber nochmal noch.
	 */	
	public void onDelete(){
		DialogFragment dialog = new DeleteDialogFragment();
        dialog.show(getSupportFragmentManager(), "DeleteDialogFragment");
	}
	
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
		Controller.getInstance().deleteCategory(categoryToEdit);
		//TODO Vll. einen Toast anziegen, der da lautet "Kategorie soundso mit soundso vielen Einträgen gelöscht"
		ListCategoriesActivity.navigateBack(this);  
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    	// Nirwana
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	
			int itemId = item.getItemId();
		    switch (itemId) {
		        case R.id.menu_save_new_category:
		        	onSaveCategory();
		            return true;
		        case R.id.menu_update_category:
		        	onUpdate();
		            return true;
		        case R.id.menu_delete_category:
		            onDelete();
		            return true;
		        case R.id.menu_chancel_category:
		            onChancel();
		            return true;
		        case R.id.action_settings:
		        	//TODO do something
		        	return true;
		        default:
		            return super.onOptionsItemSelected(item);
		    }		
	}
}
