package com.stuffbox.view;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import com.stuffbox.R;
import com.stuffbox.R.id;
import com.stuffbox.R.layout;
import com.stuffbox.R.menu;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.FeatureType;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Build;

public class NewCategoryActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_new);
		// Intent intent = getIntent();
		// ((TextView)(findViewById(R.id.categoryText))).setText("Es wurde "+intent.getStringExtra(MainActivity.EXTRA_KATEGORIE_NAME)+
		// " gewählt!");

		Spinner spinner = (Spinner) findViewById(R.id.spinner_new_category_icon);
		
		Field[] drawableFields = R.drawable.class.getFields();
		LinkedList<String> list = new LinkedList<String>();
		R.drawable drawableResources = new R.drawable();
		
		// holt alle Icons mit dem Prefix "category_icon_"
		for (int i = 0; i < drawableFields.length; i++)
			try {
				if (drawableFields[i].getName().contains( this.getResources().getText(R.string.prefix_icon_category))) 
					list.add("" + drawableFields[i].getInt(drawableResources));
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		Object[] os = list.toArray();
		String[] icons = Arrays.asList(os).toArray(new String[os.length]);

		IconArrayAdapter adapter = new IconArrayAdapter(this, icons);
		spinner.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Intent intent = getIntent();
		// String message =
		// intent.getStringExtra(MainActivity.EXTRA_KATEGORIE_NAME);
		// getSupportActionBar().setTitle(message);

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_category, menu);
		return true;
	}
	
	public void onSave(View view){

		CharSequence text = "fertig - IIHHAAHH IIHHAAAA";
		int duration = 7;
		Toast toast = Toast.makeText(this, text, duration);
		toast.show();
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
