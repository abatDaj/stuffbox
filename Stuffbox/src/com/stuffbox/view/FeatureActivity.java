package com.stuffbox.view;

import java.util.ArrayList;

import com.stuffbox.R;
import com.stuffbox.R.id;
import com.stuffbox.R.layout;
import com.stuffbox.R.menu;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.DatabaseHandler;
import com.stuffbox.model.FeatureType;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.os.Build;

public class FeatureActivity extends ActionBarActivity {

	public static final int REQUEST_NEW_FEATURE = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feature);	    
	    
		ArrayList<FeatureType> types = Controller.getTypes();
		
		Spinner spinner = (Spinner) findViewById(R.id.spinner_arten);
		// Create an ArrayAdapter using the string array and a default spinner layout
		
		/*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.planets_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		*/
		
		ArrayAdapter<FeatureType> adapter = new ArrayAdapter<FeatureType>(this, android.R.layout.simple_spinner_item, types);
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.feature, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
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
	
	public void onCancel(View view){
		//return to mainactivity
		startNextActivity(CategoryActivity.class.getName());
	}
	
	public void onSave(View view){
		//TODO Pruefen, ob Name gef�llt ist und wenn nicht ausgabe
		
		//get set name
		EditText textview_name = (EditText) findViewById(R.id.edit_name);
		String name = textview_name.getText().toString();
		//get set type
		Spinner spinner_type = (Spinner) findViewById(R.id.spinner_arten);
		FeatureType type = (FeatureType) spinner_type.getSelectedItem();
		//insert feature into db
		Controller.insertFeature(name, type);
		
		//return to activity
        Intent intentMessage=new Intent();
        setResult(FeatureActivity.REQUEST_NEW_FEATURE,intentMessage);
		finish();
	}
	/**
	 * Startet die �bergebene Activity
	 * @param activity
	 */
	private void startNextActivity(String activity){
        Intent intent = new Intent();   
        intent.setClassName(getPackageName(), activity);
        startActivity(intent);
	}
	
}
