package com.stuffbox.view;

import java.util.ArrayList;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.FeatureType;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class NewFeatureActivity extends ActionBarActivity {

	public static final int REQUEST_NEW_FEATURE = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_feature);	    
	    Controller.getInstance();
		ArrayList<FeatureType> types = Controller.getInstance().getTypes();
		
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
		getMenuInflater().inflate(R.menu.new_feature, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
		int itemId = item.getItemId();
	    switch (itemId) {
	        case R.id.menu_save:
	        	onSave();
	            return true;
	        case R.id.menu_abort:
	            onCancel();
	            return true;
	        case R.id.action_settings:
	        	//TODO do something
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void onCancel(){
		this.finish();
	}
	
	public void onSave(){
		//TODO Pruefen, ob Name gefuellt ist und wenn nicht ausgabe
		
		//erhalte gesetzten Namen
		EditText textview_name = (EditText) findViewById(R.id.edit_name);
		String name = textview_name.getText().toString();
		//erhalte gesetzte Art
		Spinner spinner_type = (Spinner) findViewById(R.id.spinner_arten);
		FeatureType type = (FeatureType) spinner_type.getSelectedItem();
		//einfuegen der Eigenschaft in die Datenbank
		Controller.getInstance().insertFeature(name, type);
		
		//Zurueck zur anfragenden activity
        Intent intentMessage=new Intent();
        setResult(NewFeatureActivity.REQUEST_NEW_FEATURE,intentMessage);
		finish();
	}	
}
