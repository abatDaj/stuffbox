package com.stuffbox.view;

import java.util.ArrayList;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Feature;
import com.stuffbox.model.FeatureType;
import com.stuffbox.view.DialogDecision.DialogDecisionListener;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class NewFeatureActivity extends ActionBarActivity implements DialogDecisionListener {

	public static final int REQUEST_NEW_FEATURE = 0;

	private boolean featureExits = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_feature);	    
	    
	    Controller.getInstance().popLastInsertedFeature();
	    
		ArrayList<FeatureType> types = Controller.getInstance().getTypes();
		
		Spinner spinner = (Spinner) findViewById(R.id.spinner_arten);
		
		ArrayAdapter<FeatureType> adapter = new ArrayAdapter<FeatureType>(this, android.R.layout.simple_spinner_item, types);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);

		//falls eine Eigenschaft geändert werden soll, uebernehm dessen Werte
	    if(Controller.getInstance().getCurrentFeature() != null){
	    	featureExits = true;
	    	EditText nameEditText = (EditText) findViewById(R.id.edit_name);
	    	nameEditText.setText(Controller.getInstance().getCurrentFeature().getName());
	    	int position = adapter.getPosition(Controller.getInstance().getCurrentFeature().getType());
	    	spinner.setSelection(position);	
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if(featureExits){
			getSupportActionBar().setTitle(this.getResources().getString(R.string.title_activity_feature_change));
			getMenuInflater().inflate(R.menu.edit, menu);
		}else{
			getMenuInflater().inflate(R.menu.change_menu, menu);
		}
		
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
	        case R.id.menu_chancel:
	            onCancel();
	            return true;
	        case R.id.menu_delete:
	            onDelete();
	            return true;	
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void onCancel(){
		Controller.getInstance().setCurrentFeature(null);
        Intent intent = new Intent();
		if(featureExits){      
			intent.setClassName(getPackageName(), ListFeatureActivity.class.getName());
		}else{
			//Zurueck zur Kateogorieansicht   
	        intent.setClassName(getPackageName(), ListCategoriesActivity.class.getName());
		}
        startActivity(intent);	
		finish();
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
		if(featureExits){
			Feature feature = Controller.getInstance().getCurrentFeature();
			feature.setName(name);
			feature.setType(type);
			Controller.getInstance().updateFeature(feature);
		}else{
			Controller.getInstance().insertFeature(name, type);	
		}
		
		Controller.getInstance().setCurrentFeature(null);
		
		//Zurueck zur anfragenden activity
        Intent intentMessage=new Intent();
        setResult(NewFeatureActivity.REQUEST_NEW_FEATURE,intentMessage);
		finish();
	}	
	
	/**
	 * 
	 * Loescht die Eigenschaft.
	 */	
	public void onDelete(){
		DialogDecision dd = new DialogDecision();
		String question = getResources().getText(R.string.delete_dialog_feature).toString();
		String yes = getResources().getText(R.string.btn_alert_de_ok).toString();
		String no = getResources().getText(R.string.btn_alert_de_no).toString();
		dd.initDialogAttributes(question, yes, no);
        dd.show(getSupportFragmentManager(), "DeleteDialogFragment");
	}
	
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
		ArrayList<Feature> selectedFeatures = new ArrayList<Feature>();
		selectedFeatures.add(Controller.getInstance().getCurrentFeature());
		Controller.getInstance().deleteFeatures(selectedFeatures);
		Controller.getInstance().setCurrentFeature(null);
		//zurueck zur anfragenden activity
        Intent intent = new Intent();                
        intent.setClassName(getPackageName(), ListCategoriesActivity.class.getName());
        startActivity(intent);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    	//nichts machen
    }
}
