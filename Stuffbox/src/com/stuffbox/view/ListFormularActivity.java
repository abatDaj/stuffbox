package com.stuffbox.view;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Formular;

public class ListFormularActivity extends ActionBarActivity {
	public static final long idNewFormularEntry = -1;
	
	private ListView mainListView ;
	private FormularArrayAdapter formularAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.formular_list);
		
		mainListView = (ListView) findViewById( R.id.formularListView );
        
        ArrayList<Formular> formulars = new ArrayList<Formular>();
        
		//Eintrag neues Formular einfuegen
		Formular newFormularEntry = new Formular(idNewFormularEntry, 
				getResources().getText(R.string.title_activity_new_formular).toString(), 
				null);
        formulars.add(newFormularEntry);
        
        //vorhandene Formulare an Liste anfuegen
        formulars.addAll(Controller.getInstance().getFormulars(null));
        
		formularAdapter = new FormularArrayAdapter (this, formulars);
	    mainListView.setAdapter( formularAdapter );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.feature_list, menu);
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
	 * Die Detail-Item-Aktivität wird gestartet und das ausgewählte Formular
	 * wird an diese verschickt.
	 *
	 */

	public void onSave(){
		Formular selectedFormular = formularAdapter.getCurrentFormular();
		//TODO Ausgabe ordentlich (Toasts sind eher schlecht, Text kann so bleiben ;)
		if (selectedFormular != null){
			Intent i = new Intent(this, DetailItemActivity.class);
			i.putExtra(Controller.EXTRA_FORMULAR_FOR_NEW_ITEM, selectedFormular);
			startActivity(i);
			Toast.makeText(getApplicationContext(), selectedFormular.getName(), 7).show();
		}else{
			Toast.makeText(getApplicationContext(), "You should select at least one formular you moron.", 7).show();
		}
	}
	
	/**
	 * 
	 * Zurueck zur aktuellen Kategorie
	 */	
	public void onChancel(){
        Intent intent = new Intent();   
        intent.setClassName(getPackageName(), ListCategoriesActivity.class.getName());
        startActivity(intent);				
		this.finish();
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