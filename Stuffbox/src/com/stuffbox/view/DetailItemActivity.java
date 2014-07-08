package com.stuffbox.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Category;
import com.stuffbox.model.Feature;
import com.stuffbox.model.Formular;
import com.stuffbox.model.Icon;
import com.stuffbox.model.Item;
import com.stuffbox.view.DialogDecision.DialogDecisionListener;
import com.stuffbox.view.helper.ActivityWithATimePickerEditText;
import com.stuffbox.view.helper.DatePickerFragment;
import com.stuffbox.view.helper.EditTextDatePicker;
import com.stuffbox.view.helper.ImageViewPhoto;
import com.stuffbox.view.helper.Utility;

public class DetailItemActivity extends ActionBarActivity implements ActivityWithATimePickerEditText, DialogDecisionListener {

	public static final String PURPOSE_IS_UPDATE = "UPDATE";
	
	private ListView mainListView;

	private FeatureArrayAdapterForDetailItem featureAdapter;
	private Formular formular;
	private boolean changeMode = false;
	private boolean itemExits = false;
	private ImageViewPhoto photoImageView;
	private String lastFileNameOfPhoto;
	private Item item;
	
	private static final String TAG = DetailItemActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_item);		
		
		formular = Controller.getInstance().getCurrentFormular();
		
		if (Controller.getInstance().getCurrentItem() != null){
			itemExits = true;
			//als standard wird ein item nur angezeigt
			if(getIntent().getExtras() != null && getIntent().getExtras().get(PURPOSE_IS_UPDATE) != null){
				changeMode = (Boolean) getIntent().getExtras().get(PURPOSE_IS_UPDATE);
			}else{
				changeMode = false;
			}
		}else {
			//ein neues Item wird immer in Aenderungsmodus gezeigt
			itemExits = false;
			changeMode = true;
		}
		
		item = Controller.getInstance().getCurrentItem();
		Controller.getInstance().setCurrentItem(null);
		
		//Setze Namensansicht
		if(itemExits){
			EditText name = (EditText) findViewById(R.id.editNameFeature);
			name.setText(item.getName());
			name.setEnabled(changeMode);	
		}
		
		//setze Werte fuer Kategorieanzeige
		if(!itemExits){
			//initial ist aktuelle Kategorie gesetzt
			ArrayList<Category> initCategories = new ArrayList<Category>();
			initCategories.add(Controller.getInstance().getCurrentCategory());
			Controller.getInstance().setSelectedCategoriesInItem(initCategories);	
		}else{
			Controller.getInstance().setSelectedCategoriesInItem(item.getCategories());		
		}
		showCategoryText();
		
		LinearLayout linearlayoutCategoryChooserForItem = (LinearLayout) findViewById(R.id.linearlayout_categoryChooserForItem);
		if(changeMode){
			linearlayoutCategoryChooserForItem.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					openCategoryChooser(v);
				}
			});
		}else{
			linearlayoutCategoryChooserForItem.setClickable(false);
		}

		// Alle Eigenschaften des Formulars anzeigen
		mainListView = (ListView) findViewById( R.id.featureListViewInDetailItem);	        		

		ArrayList<Feature> features = null;
		if(itemExits){
			formular = item.getFormular();
		}
			
		features = formular.getFeatures();
		
		Collections.sort(features);
		
		//Eigenschaften anzeigen
        featureAdapter = new FeatureArrayAdapterForDetailItem (this, features, this);

        featureAdapter.setEditable(changeMode);
	    mainListView.setAdapter( featureAdapter );
	    
	    // Groesse der Liste anhand der Anzahl der Eigenschaften berechnen.
        Utility.setListViewHeightBasedOnChildren(mainListView, 100);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Category currentCategory = Controller.getInstance().getCurrentCategory();
		Icon icon = currentCategory.getIcon();
		if (icon !=null)
			getSupportActionBar().setIcon(icon.getDrawableId());
		
		int actionbarmenu;
		if (!changeMode){
			actionbarmenu = R.menu.change_menu_edit_item;
		}else if(!itemExits){
			actionbarmenu = R.menu.change_menu;
		}else{
			actionbarmenu = R.menu.edit;
		}
		getMenuInflater().inflate(actionbarmenu, menu);
		return true;
	}
	
	/**
	 * Item wird gespeichert
	 *
	 * @param view
	 */
	public void onSave(View view){
		//speicher item auf der Datenbank
		String itemName = "";
		CharSequence chars = ((TextView)findViewById(R.id.editNameFeature)).getText();
		if (chars.length() < 1) 
		{
			DialogNotice dn = new DialogNotice();
			String noticeText = getResources().getText(R.string.dialog_notice_item_name_missing).toString();
			String noticeOk = getResources().getText(R.string.btn_alert_de_ok).toString();
			dn.initDialogAttributes(noticeText, noticeOk);
	        dn.show(getSupportFragmentManager(), TAG);
		}
		else {
			if(photoImageView != null){

				if (this.lastFileNameOfPhoto != null) {// String darf nicht null sein !
					photoImageView.setTag(this.lastFileNameOfPhoto);
					Controller.getInstance().sayIt("NAME: " + lastFileNameOfPhoto);
				}
				// Kleiner Hack (beide Anweiseungen): "normaler" ClickListener wird unsschädlich gemacht
				// und die else Block speichert dann den Dateiname des geschossenen Fotos (falls vorhanden)
				photoImageView.setCallOnCklick(false);
				photoImageView.performClick();
			}
			itemName = chars.toString();		
			ArrayList<Category> selectedCategories = Controller.getInstance().getSelectedCategoriesInItem();
			
			for(int i = 0; i < mainListView.getChildCount(); i++)
			{
				View lView = (View) mainListView.getChildAt(i);
				lView.clearFocus(); 
			} 

			
			for(Feature feature : formular.getFeatures()){
				if(feature.getId() == Formular.idOfNameFeature ){
					feature.setValue(itemName);
				}
			}
			
			if(!itemExits){	
				//neues Item anlegen
				Controller.getInstance().insertItem(itemName, formular, selectedCategories);
				//TODO Diese Anweisung fuehrt dazu, das die Rueckspruenge nicht mehr so gut funktionieren.
				Controller.getInstance().setCurrentItem(Controller.getInstance().popLastInsertedItem());
				Controller.getInstance().setSelectedCategoriesInItem(null);
				Intent intent = getIntent();
				startActivity(intent);
			}else{
				//vorhandenes Item updaten
				item.setName(itemName);
				item.setFormular(formular);
				item.setCategories(selectedCategories);
				Controller.getInstance().updateItem(item);
				Controller.getInstance().setCurrentItem(item);
				Controller.getInstance().setSelectedCategoriesInItem(null);

				Intent intent = new Intent(); 
				intent.putExtra(DetailItemActivity.PURPOSE_IS_UPDATE, false);
		        intent.setClassName(getPackageName(), DetailItemActivity.class.getName());
		        startActivity(intent);	
			}
		}
	}
	/**
	 * Vorgang wird abgebrochen - Daten werden verworfen
	 * @param view
	 */
	public void onCancel(View view){	
		Controller.getInstance().setSelectedCategoriesInItem(null);
		this.finish();
	}
	
	public void openCategoryChooser (View view) {
	        Intent intent = new Intent();        
	        intent.setClassName(getPackageName(), ChooseCategoriesActivity.class.getName());
	        startActivityForResult(intent, ChooseCategoriesActivity.REQUEST_CHOOSE_CATEGORIES);
	}

	@Override
	public void setPhotoImageView(ImageViewPhoto imageView) {
		this.photoImageView = imageView;
		
	}
	
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ChooseCategoriesActivity.REQUEST_CHOOSE_CATEGORIES) {
        	//aktualisiere Kategorieanzeige
        	showCategoryText();
        }
        if (resultCode == RESULT_OK)
            if (requestCode == Controller.REQUEST_CAMERA)
            	showFinallyRealCapturedPhoto(lastFileNameOfPhoto);
    }
    
	@Override
	public void showFinallyRealCapturedPhoto(String filePath) {
		Utility.replaceImageViewWithPhoto(filePath, photoImageView, 300);

	}
    
    @Override
	public void onClickOfImageViewPhoto (final String fileNameOfPhoto) {
		final CharSequence[] items = { "Foto machen", "Galerie", "Abbrechen" };
		lastFileNameOfPhoto = fileNameOfPhoto;
		//final String finalStringBecauseOfJava = fileNameOfPhoto;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Bild hinzufuegen");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Foto machen")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), fileNameOfPhoto);
                    Uri uri = Uri.fromFile(f);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, Controller.REQUEST_CAMERA);
                } else if (items[item].equals("Galerie")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            Controller.SELECT_FILE);
                } else if (items[item].equals("Abbrechen")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	
		int itemId = item.getItemId();
	    switch (itemId) {
	        case R.id.menu_save:
	        case R.id.menu_update:
	        	onSave(null);
	            return true;
	        case R.id.menu_abort:
	        case R.id.menu_chancel:	
	            onCancel(null);
	            return true;
	        case R.id.action_settings:
	        	//TODO do something
	        	return true;
	        case android.R.id.home:
	        	onBackPressed();
	            return true;
	        case R.id.menu_edit:
		        Intent intent = new Intent(); 
		        Controller.getInstance().setCurrentItem(this.item);
		        intent.putExtra(DetailItemActivity.PURPOSE_IS_UPDATE, true);
		        intent.setClassName(getPackageName(), DetailItemActivity.class.getName());
		        startActivity(intent);	
		        finish();
	            return true;
	        case R.id.menu_delete:
	        	onDelete();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	public void showTimePickerDialog(EditTextDatePicker editTextDatePicker) {
		DatePickerFragment newFragment = new DatePickerFragment();
		newFragment.setEditTextPicker(editTextDatePicker);
	    newFragment.show(getSupportFragmentManager(), "datePicker");
	}
	
	private void showCategoryText(){
		TextView textView = (TextView) findViewById(R.id.detail_item_category_text);
		
		ArrayList<Category> categories = Controller.getInstance().getSelectedCategoriesInItem();
		//erstelle Text fuer zugeordnete Kategorien
		StringBuilder stringBuilder = new StringBuilder();
		for (int index = 0; index < categories.size(); index++) {
			Category category = categories.get(index);
			stringBuilder.append(category);
			if(index != categories.size()-1){
				stringBuilder.append(", ");
			}
		}		

		textView.setText(stringBuilder.toString());	
	}
	
	/**
	 * 
	 * Loescht das Item
	 */	
	public void onDelete(){
		DialogDecision dd = new DialogDecision();
		String question = getResources().getText(R.string.delete_dialog_item).toString();
		String yes = getResources().getText(R.string.btn_alert_de_ok).toString();
		String no = getResources().getText(R.string.btn_alert_de_no).toString();
		dd.initDialogAttributes(question, yes, no);
        dd.show(getSupportFragmentManager(), "DeleteDialogFragment");
	}
	
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
		Controller.getInstance().deleteItem(item);
		Controller.getInstance().setCurrentItem(null);
		//zurueck zur anfragenden activity
        Intent intent = new Intent();                
        intent.setClassName(getPackageName(), ListCategoriesActivity.class.getName());
        startActivity(intent);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    	//nichts machen
    }
	
	@Override
	public void onBackPressed(){
		if(itemExits){
	        Intent intent = new Intent();                
	        intent.setClassName(getPackageName(), ListCategoriesActivity.class.getName());
	        startActivity(intent);				
		}else{
	        Intent intent = new Intent();                
	        intent.setClassName(getPackageName(), ListFormularActivity.class.getName());
	        startActivity(intent);		
		}
		
		Controller.getInstance().setSelectedCategoriesInItem(null);
		this.finish();
	}
}
