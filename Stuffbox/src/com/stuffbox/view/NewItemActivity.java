package com.stuffbox.view;

import com.stuffbox.R;
import com.stuffbox.R.id;
import com.stuffbox.R.layout;
import com.stuffbox.R.menu;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Category;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class NewItemActivity extends ActionBarActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_item, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	
		int itemId = item.getItemId();
	    switch (itemId) {
	    	case R.id.menu_save_new_item:
	    		onSaveItem();
		        return true;
		    case R.id.menu_chancel_item:
		    	onChancel();
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
	 * Eine neue Kategorie wird angelegt.
	 *
	 * @param view
	 */
	public void onSaveItem(){
		Toast.makeText(getApplicationContext(), "Speichern !!!", 7).show();
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
}
