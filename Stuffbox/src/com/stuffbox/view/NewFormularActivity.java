package com.stuffbox.view;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Feature;
import com.stuffbox.model.FeatureType;
import com.stuffbox.model.Formular;
import com.stuffbox.view.DialogDecision.DialogDecisionListener;
import com.stuffbox.view.helper.DynamicListView;

public class NewFormularActivity  extends ActionBarActivity implements DialogDecisionListener {
	
	public static final int REQUEST_NEW_FORMULAR = 0;
	private static final long idNewFeatureEntry = -1;
	
	private ListView listFeaturesSelected;
	private ListView listFeaturesNotSelected;
	private FeatureDropDownArrayAdapter selectedfeaturesAdapter;
	private FeatureDropDownArrayAdapter selectedNotFeaturesAdapter;
	
	boolean formularExits = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_formular);
		
		//initial inserted formular
		Controller.getInstance().popLastInsertedFormular();

		//falls ein Formular geändert werden soll, uebernehm dessen Werte
	    if(Controller.getInstance().getCurrentFormular() != null){
	    	formularExits = true;
	    	EditText nameEditText = (EditText) findViewById(R.id.new_formular_name);
	    	nameEditText.setText(Controller.getInstance().getCurrentFormular().getName());
	    }
		
		//initialisiere Listen fuer selektierte und nicht selektierte Eigenschaften
		ArrayList<Feature> features = Controller.getInstance().getFeatures(null);
		ArrayList<Feature> selectedFeatures = new ArrayList<Feature>();
		ArrayList<Feature> notSelectedFeatures = new ArrayList<Feature>();
		
		//Eintrag neue Eigenschaft einfügen
		Feature newFeatureEntry = new Feature(idNewFeatureEntry, 
				getResources().getText(R.string.title_activity_feature).toString(), 
				FeatureType.Text);
		notSelectedFeatures.add(newFeatureEntry);
		
		if(formularExits){
			//Zuorndung der Eigenschaften zu ausgewaehlt und nicht ausgewaehlt
			for (Feature feature : features) {
				boolean wasFound = false;
				if(feature.getId() == Formular.idOfNameFeature ){
					selectedFeatures.add(feature);
					continue;
				}
				for(Feature featureOfFormular : Controller.getInstance().getCurrentFormular().getFeatures()){
					if(feature.getId() == featureOfFormular.getId() ){
						wasFound = true;
						feature.setSortnumber(featureOfFormular.getSortnumber());
						selectedFeatures.add(feature);
					}
				}
				if(!wasFound){
					notSelectedFeatures.add(feature);
				}
			}
			Collections.sort(selectedFeatures);
		}else{
			//Zuorndung der Eigenschaften zu ausgewaehlt und nicht ausgewaehlt
			for (Feature feature : features) {
				if(feature.getId() == Formular.idOfNameFeature ){
					selectedFeatures.add(feature);
				}else{
					notSelectedFeatures.add(feature);
				}
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
	        		//neue Eigenschaft soll angelegt werden
	        		
	        		//TODO bisherige Eingabe speichern
	                Intent intent = new Intent();        
	                intent.setClassName(getPackageName(), NewFeatureActivity.class.getName());
	                startActivityForResult(intent, NewFeatureActivity.REQUEST_NEW_FEATURE);
	        	}else{
	        		//verschiebe Eigenschaft in Auswahlliste
		        	selectedNotFeaturesAdapter.remove(choosenFeature);
		        	selectedfeaturesAdapter.add(choosenFeature);
	        	}
	        }
        });
		
		selectedNotFeaturesAdapter = new FeatureDropDownArrayAdapter(this, R.layout.row_selection_feature, features, false);
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
        
        selectedfeaturesAdapter = new FeatureDropDownArrayAdapter(this, R.layout.row_selection_feature, features, true);
        if(listFeaturesSelected instanceof DynamicListView){
            ((DynamicListView) listFeaturesSelected).setItemList(features);
        }
        listFeaturesSelected.setAdapter(selectedfeaturesAdapter);
        listFeaturesSelected.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
	}
	
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NewFeatureActivity.REQUEST_NEW_FEATURE) {
        	//fuegt neue Eigenschaft an selektierte Eigenschaften an
        	Feature feature = Controller.getInstance().popLastInsertedFeature();
        	if(feature != null){
        		selectedfeaturesAdapter.add(feature);
        	}
        }
    }

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(formularExits){
			getSupportActionBar().setTitle(this.getResources().getString(R.string.title_activity_formulars_change));
			getMenuInflater().inflate(R.menu.edit, menu);
		}else{
			getMenuInflater().inflate(R.menu.change_menu, menu);
		}
		return true;
	}
	/**
	 * 
	 * Ein neues Formular wird angelegt.
	 *
	 * @param view
	 */
	public void onSave(){
		//speicher Formular auf der Datenbank
		String formularName = ((TextView)findViewById(R.id.new_formular_name)).getText().toString();
		ArrayList<Feature> features = selectedfeaturesAdapter.getFeatures();
		if(formularExits){
			Formular formular = Controller.getInstance().getCurrentFormular();
			Controller.getInstance().setCurrentFormular(null);
			formular.setName(formularName);
			formular.setFeatures(features);
			Controller.getInstance().updateFormlar(formular);
			//Zurueck zur Kateogorieansicht
	        Intent intent = new Intent();   
	        intent.setClassName(getPackageName(), ListCategoriesActivity.class.getName());
	        startActivity(intent);				
			finish();
		}else{
			Controller.getInstance().insertFormlar(formularName, features);
			//Zurueck zur anfragenden activity
	        Intent intentMessage=new Intent();
	        setResult(NewFormularActivity.REQUEST_NEW_FORMULAR,intentMessage);
			finish();
		}
	}
	/**
	 * Vorgang wird abgebrochen - Daten werden verworfen
	 * @param view
	 */
	public void onBackPressed(){	
		Controller.getInstance().setCurrentFormular(null);
        Intent intent = new Intent();
		if(formularExits){      
			intent.setClassName(getPackageName(), ListFormularActivity.class.getName());
			intent.putExtra(ListFormularActivity.PURPOSE_IS_CHOOSING_FOR_UPDATE, true);
		}else{
			//Zurueck zur Kateogorieansicht   
	        intent.setClassName(getPackageName(), ListCategoriesActivity.class.getName());
		}
        startActivity(intent);	
		finish();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	
		int itemId = item.getItemId();
	    switch (itemId) {
	        case R.id.menu_save:
	        	onSave();
	            return true;
	        case R.id.menu_update:
	        	onSave();
	            return true;
	        case R.id.menu_chancel:	            
	        case R.id.menu_abort:
	        	onBackPressed();
	            return true;
	        case R.id.menu_delete:
	            onDelete();
	            return true;	
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/**
	 * 
	 * Loescht das Formular.
	 */	
	public void onDelete(){
		DialogDecision dd = new DialogDecision();
		String question = getResources().getText(R.string.delete_dialog_formular).toString();
		String yes = getResources().getText(R.string.btn_alert_de_ok).toString();
		String no = getResources().getText(R.string.btn_alert_de_no).toString();
		dd.initDialogAttributes(question, yes, no);
        dd.show(getSupportFragmentManager(), "DeleteDialogFragment");
	}
	
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
		ArrayList<Formular> selectedFormulars = new ArrayList<Formular>();
		selectedFormulars.add(Controller.getInstance().getCurrentFormular());
		Controller.getInstance().deleteFormulars(selectedFormulars);
		Controller.getInstance().setCurrentFormular(null);
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
