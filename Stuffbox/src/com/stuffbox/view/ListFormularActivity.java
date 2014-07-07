package com.stuffbox.view;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Formular;
import com.stuffbox.model.Icon;

public class ListFormularActivity extends ActionBarActivity{
	public static final long idNewFormularEntry = -1;
	public static final String PURPOSE_IS_CHOOSING_FOR_UPDATE = "IS_CHOOSING_FOR_UPDATE";
	
	
	private ListView mainListView ;
	private FormularArrayAdapter formularAdapter;
	private boolean isChoosingForUpdate = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		Controller.getInstance().setCurrentFormular(null);
		
		//als standard wird ein formular ausgewaehlt um ein item zu erstellen
		if(getIntent().getExtras() != null && getIntent().getExtras().get(PURPOSE_IS_CHOOSING_FOR_UPDATE) != null){
			isChoosingForUpdate = (Boolean) getIntent().getExtras().get(PURPOSE_IS_CHOOSING_FOR_UPDATE);
		}
		
		mainListView = (ListView) findViewById( R.id.listView );
        
        ArrayList<Formular> formulars = new ArrayList<Formular>();
        
        if(!isChoosingForUpdate){
			//Eintrag neues Formular einfuegen
			Formular newFormularEntry = new Formular(idNewFormularEntry, 
					getResources().getText(R.string.title_activity_new_formular).toString(), 
					null);
	        formulars.add(newFormularEntry);
        }
        
        //vorhandene Formulare an Liste anfuegen
        formulars.addAll(Controller.getInstance().getFormulars(null));
        
		formularAdapter = new FormularArrayAdapter (this, formulars);
	    mainListView.setAdapter( formularAdapter );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_formular, menu);
		
		Icon icon = Controller.getInstance().getCurrentCategory().getIcon();
		if (icon !=null)
			getSupportActionBar().setIcon(icon.getDrawableId());
		
		if(isChoosingForUpdate){
			 MenuItem saveItem = menu.findItem(R.id.menu_save_new_item);
			 saveItem.setIcon(R.drawable.ic_action_edit);
		}
	
		return true;
	}
	
	public void openCreatingNewFormular ()
	{
        Intent intent = new Intent();   
        intent.setClassName(getPackageName(), NewFormularActivity.class.getName());
        startActivity(intent);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	
		int itemId = item.getItemId();
	    switch (itemId) {
	    	case R.id.menu_save_new_item:
	    		onSave();
		        return true;
		    case R.id.menu_chancel_item:
		    	onBackPressed();
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
	 * Die Detail-Item-Aktivität wird gestartet und das ausgewaehlte Formular
	 * wird an diese verschickt.
	 *
	 */

	public void onSave(){
		Formular selectedFormular = formularAdapter.getCurrentFormular();
		Controller.getInstance().setCurrentFormular(selectedFormular);
		if(isChoosingForUpdate){
			Intent i = new Intent(this, NewFormularActivity.class);
			startActivity(i);
		}else{
			Intent i = new Intent(this, DetailItemActivity.class);
			startActivity(i);
		}
		finish();
	}
	/**
	 * 
	 * Zurueck zur aktuellen Kategorie
	 */	
	public void onBackPressed(){
        Intent intent = new Intent();   
        intent.setClassName(getPackageName(), ListCategoriesActivity.class.getName());
        startActivity(intent);				
		finish();
	}
	
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NewFormularActivity.REQUEST_NEW_FORMULAR) {
        	//fuegt neues formular an Auswahlliste an
        	Formular formular = Controller.getInstance().popLastInsertedFormular();
        	if(formular != null){
        		formularAdapter.add(formular);
        	}
        }
    }
	
    public void openFormularNewScreen(View view){    	
        Intent intent = new Intent();        
        intent.setClassName(getPackageName(), NewFormularActivity.class.getName());
        startActivity(intent);
    }		
}