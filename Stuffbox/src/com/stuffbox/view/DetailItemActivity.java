package com.stuffbox.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Category;
import com.stuffbox.model.Feature;
import com.stuffbox.model.Formular;
import com.stuffbox.model.Icon;
import com.stuffbox.model.Item;
import com.stuffbox.view.helper.ActivityWithATimePickerEditText;
import com.stuffbox.view.helper.DatePickerFragment;
import com.stuffbox.view.helper.EditTextDatePicker;
import com.stuffbox.view.helper.Utility;

public class DetailItemActivity extends ActionBarActivity implements ActivityWithATimePickerEditText {
	
	private ListView mainListView;

	private FeatureArrayAdapterForDetailItem featureAdapter;
	private Formular formular;
	private boolean changeMode = false;
	private ImageView photoImageView;
	
	private static final String TAG = DetailItemActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_item);
		
		formular = (Formular) getIntent().getSerializableExtra(Controller.EXTRA_FORMULAR_FOR_NEW_ITEM);
		
		if (Controller.getInstance().getCurrentItem() != null){
			changeMode = false;
		}else if (formular != null) {
			changeMode = true;
		}else{ 
			//TODO
			new RuntimeException ("Something went horribly wrong :-0");
		}
		
		Item currentItem = Controller.getInstance().getCurrentItem();
		Controller.getInstance().setCurrentItem(null);
		
		//Setze Namensansicht
		if(!changeMode){
			EditText name = (EditText) findViewById(R.id.editNameFeature);
			name.setText(currentItem.getName());
			name.setEnabled(changeMode);	
		}
		
		//setze Werte fuer Kategorieanzeige
		if(changeMode){
			//initial ist aktuelle Kategorie gesetzt
			ArrayList<Category> initCategories = new ArrayList<Category>();
			initCategories.add(Controller.getInstance().getCurrentCategory());
			Controller.getInstance().setSelectedCategoriesInItem(initCategories);	
		}else{
			Controller.getInstance().setSelectedCategoriesInItem(currentItem.getCategories());		
		}
		showCategoryText();

		// Alle Eigenschaften des Formulars anzeigen
		mainListView = (ListView) findViewById( R.id.featureListViewInDetailItem);	        		

		ArrayList<Feature> features = null;
		if(changeMode){
			features = formular.getFeatures();
		}else{
			features = currentItem.getFormular().getFeatures();
		}
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
		if (!changeMode)
			actionbarmenu = R.menu.change_menu_edit_item;
		else
			actionbarmenu = R.menu.change_menu;
		getMenuInflater().inflate(actionbarmenu, menu);
		return true;
	}
	
	@Override
	public void onClickOfImageViewPhoto () {
		final CharSequence[] items = { "Foto machen", "Galerie",
        "Abbrechen" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Bild hinzufuegen");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Foto machen")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment
                            .getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
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
	
	/**
	 * Item wird gespeichert
	 *
	 * @param view
	 */
	public void onSave(View view){
		//speicher item auf der Datenbank
		String itemName = ((TextView)findViewById(R.id.editNameFeature)).getText().toString();
		ArrayList<Category> selectedCategories = Controller.getInstance().getSelectedCategoriesInItem();
		
		for(int i = 0; i < mainListView.getChildCount(); i++)
		{
			View lView = (View) mainListView.getChildAt(i);
			lView.clearFocus(); 
		} 
		
		Controller.getInstance().insertItem(itemName, formular, selectedCategories);
		Controller.getInstance().setCurrentItem(Controller.getInstance().popLastInsertedItem());
		Controller.getInstance().setSelectedCategoriesInItem(null);

		Intent intent = getIntent();
		finish();
		startActivity(intent);
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
	public void setPhotoImageView(ImageView imageView) {
		this.photoImageView = imageView;
		
	}
	
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ChooseCategoriesActivity.REQUEST_CHOOSE_CATEGORIES) {
        	//aktualisiere Kategorieanzeige
        	showCategoryText();
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == Controller.REQUEST_CAMERA) {
            	File path = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                File file = new File(path, "Test.jpg");

                try {
                    Bitmap bm;
                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
 
                    bm = BitmapFactory.decodeFile(file.getAbsolutePath(),
                            btmapOptions);
 
                    //bm = Bitmap.createScaledBitmap(bm, 100, 100, true);
                    photoImageView.setImageBitmap(bm);
                    try {                        
                        OutputStream stream = new FileOutputStream(file);
                        bm.compress(CompressFormat.JPEG, 100, stream);
                        stream.flush();
                        stream.close();
                    } catch (FileNotFoundException e) {
                    	Log.e(TAG, "Fehler beim Fotografieren file not found: ", e);
                    } catch (IOException e) {
                    	Log.e(TAG, "Fehler beim Fotografieren io Ausgabe: ", e);
                    } catch (Exception e) {
                    	Log.e(TAG, "Fehler beim Fotografieren allgemeiner fehler: ", e);
                    }
                } catch (Exception e) {
                	Log.e(TAG, "Fehler beim Fotografieren allgemeiner fehler: ", e);
                }
            } else if (requestCode == Controller.SELECT_FILE) {
            	try {
            		final Uri imageUri = data.getData();
            		final InputStream imageStream = getContentResolver().openInputStream(imageUri);
    				final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
    				photoImageView.setImageBitmap(selectedImage);
            	} catch (Exception e) {
            		Log.e(TAG, "Fehler bei der Galerie allgemeiner fehler: ", e);
				}
            }
        }
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	
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
	        case R.id.menu_edit:
	            Controller.getInstance().sayIt("Und jetzt editiere");
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
}
