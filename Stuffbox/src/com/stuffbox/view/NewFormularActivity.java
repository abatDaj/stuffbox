package com.stuffbox.view;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Feature;
import com.stuffbox.model.FeatureType;
import com.stuffbox.model.Formular;

public class NewFormularActivity  extends ActionBarActivity {
	
	private static final long idNewFeatureEntry = -1;
	
	private ListView listFeaturesSelected;
	private ListView listFeaturesNotSelected;
	private FeatureArrayAdapter selectedfeaturesAdapter;
	private FeatureArrayAdapter selectedNotFeaturesAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.formular_new);
		
		//initialisiere Listen fuer selektierte und nicht selektierte Eigenschaften
		ArrayList<Feature> features = Controller.getInstance().getFeatures(null);
		ArrayList<Feature> selectedFeatures = new ArrayList<Feature>();
		ArrayList<Feature> notSelectedFeatures = new ArrayList<Feature>();
		//Eintrag neue Eigenschaft einfügen
		Feature newFeatureEntry = new Feature(idNewFeatureEntry, 
				getResources().getText(R.string.title_activity_feature).toString(), 
				FeatureType.Text);
		notSelectedFeatures.add(newFeatureEntry);
		//Zuorndung der Eigenschaften zu ausgewaehlt und nicht ausgewaehlt
		for (Feature feature : features) {
			if(feature.getId() == Formular.idOfNameFeature ){
				selectedFeatures.add(feature);
			}else{
				notSelectedFeatures.add(feature);
			}
		}
		//Listen erstellen
		initializeListFeaturesNotSelected(notSelectedFeatures);
		initializeListFeaturesSelected(selectedFeatures);         
	}
	
	/**
	 * Initializes the list with features selected (initial null)
	 */
	private void initializeListFeaturesNotSelected(ArrayList<Feature> features){
		listFeaturesNotSelected = (ListView) findViewById( R.id.list_features_not_selected );
		listFeaturesNotSelected.setOnItemClickListener(new OnItemClickListener()
        {
	        @Override
	        public void onItemClick(AdapterView<?> l, View v, int position, long id)
	        {
	        	Feature choosenFeature = selectedNotFeaturesAdapter.getItem(position);
	        	if(choosenFeature.getId() == idNewFeatureEntry){
	        		//neuer Eintrag soll angelegt werden
	        		
	        		//TODO bisherige Eingabe speichern
	                Intent intent = new Intent();        
	                intent.setClassName(getPackageName(), FeatureActivity.class.getName());
	                startActivityForResult(intent, FeatureActivity.REQUEST_NEW_FEATURE);
	        	}else{
	        		//verschiebe Eigenschaft in Auswahlliste
		        	selectedNotFeaturesAdapter.remove(choosenFeature);
		        	selectedfeaturesAdapter.add(choosenFeature);
	        	}
	        }
        });
		
		selectedNotFeaturesAdapter = new FeatureArrayAdapter(this, R.layout.row_selection_feature, features, false);
		listFeaturesNotSelected.setAdapter( selectedNotFeaturesAdapter );	
	}
	
	private void initializeListFeaturesSelected(ArrayList<Feature> features){
		//pruefen der sdk version von android um dynamischen listview ein- bzw. abzuschalten
		if(Integer.valueOf(android.os.Build.VERSION.SDK_INT) >= 11){
			LinearLayout layout = (LinearLayout) findViewById(R.id.linearlayout_features_selected_dynamiclistview);
			listFeaturesSelected = new DynamicListView(this);
			layout.addView(listFeaturesSelected);
			findViewById(R.id.linearlayout_features_selected_listview).setVisibility(LinearLayout.GONE);
		}else{
			listFeaturesSelected = (ListView) findViewById(R.id.list_features_selected_listview);
			findViewById(R.id.linearlayout_features_selected_dynamiclistview).setVisibility(LinearLayout.GONE);
		}
		
        listFeaturesSelected.setOnItemClickListener(new OnItemClickListener()
        {
	        @Override
	        public void onItemClick(AdapterView<?> l, View v, int position, long id)
	        {
	        	Feature choosenFeature = selectedfeaturesAdapter.getItem(position);
	        	if(choosenFeature.getId() != Formular.idOfNameFeature ){
	        		//entferne Eigenschaft aus Auswahlliste wenn es sich nicht um den Namen handelt
		        	selectedfeaturesAdapter.remove(choosenFeature);
		        	selectedNotFeaturesAdapter.add(choosenFeature);
	        	}
	        }
        });
        
        selectedfeaturesAdapter = new FeatureArrayAdapter(this, R.layout.row_selection_feature, features, true);
        if(listFeaturesSelected instanceof DynamicListView){
            ((DynamicListView) listFeaturesSelected).setItemList(features);
        }
        listFeaturesSelected.setAdapter(selectedfeaturesAdapter);
        listFeaturesSelected.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
	}
	
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FeatureActivity.REQUEST_NEW_FEATURE) {
        	//fuegt neue Eigenschaft an selektierte Eigenschaften an
            selectedfeaturesAdapter.add(Controller.getInstance().popLastInsertedFeature());;
        }
    }

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.new_formlar, menu);
		return true;
	}
	/**
	 * 
	 * Ein neues Formular wird angelegt.
	 *
	 * @param view
	 */
	public void onSave(View view){
		//speicher Formular auf der Datenbank
		String formularName = ((TextView)findViewById(R.id.new_formular_name)).getText().toString();
		ArrayList<Feature> features = selectedfeaturesAdapter.getFeatures();
		
		Controller.getInstance().insertFormlar(formularName, features);
		
		//starte naechsten Screen
        Intent intent = new Intent();        
        intent.setClassName(getPackageName(), MainActivity.class.getName());
        startActivity(intent);
	}
	/**
	 * Vorgang wird abgebrochen - Daten werden verworfen
	 * @param view
	 */
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
