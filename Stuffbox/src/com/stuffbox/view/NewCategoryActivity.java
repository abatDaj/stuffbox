package com.stuffbox.view;

import java.util.ArrayList;
import java.util.LinkedList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Category;
import com.stuffbox.model.DataSourceCategory;
import com.stuffbox.model.Icon;
import com.stuffbox.view.DialogDecision.DialogDecisionListener;

public class NewCategoryActivity extends ActionBarActivity implements DialogDecisionListener {
	
	Category categoryToEdit = null;
	private static Icon selectedIcon2 = Controller.getInstance().getCurrentCategory().getIcon();
	ImageView iV = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_category);

		//Spinner spinner = (Spinner) findViewById(R.id.spinner_new_category_icon);
		//Controller.fillIconTableWithSomeIcons(this);
		ArrayList<Icon> allicons = Controller.getInstance().getIcons();
		LinkedList<Icon> list = new LinkedList<Icon>();

		/*for (Icon i : allicons)
			if (i.getName().contains(getResources().getText(R.string.prefix_icon_category))) 
				list.add(i);*/
		
		Icon[] icons = new Icon[list.size()];
		
		for (int i = 0 ;i < list.size();i++)
			icons[i] = list.get(i);
		
		//IconArrayAdapter adapter = new IconArrayAdapter(this, icons);
		//spinner.setAdapter(adapter);
		
		
	// Icon-Auswahl
		
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setPadding(10, 30, 0, 0);
		TextView tV = new TextView(this);
		tV.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		tV.setText("Wähle ein Icon aus");
		tV.setTextSize(Controller.CSS_TEXT_SIZE_LABELS);
		
		int  did = selectedIcon2.getDrawableId();
		iV = new ImageView(this);
		iV.setImageResource(did);
		iV.setLayoutParams(new LinearLayout.LayoutParams(150,150));
		iV.setPadding(0, 20, 0, 0);
		
		iV.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();        
			    intent.setClassName(getPackageName(), ChooseIconActivity.class.getName());
			    startActivityForResult(intent, Controller.REQUEST_CODE_CHOOSE_ICON) ;
			}});
		
		ll.addView(tV);
		ll.addView(iV);
		
		LinearLayout view = (LinearLayout)findViewById(R.id.new_category_screen);
		view.addView(ll);
		
		// Icon Auswahl fertig
		
		
		Category serializedCategory = (Category) getIntent().getSerializableExtra(Controller.EXTRA_EDIT_CATEGORY);
		if (serializedCategory != null) 
		{
			if (!serializedCategory.getName().equals(DataSourceCategory.ROOT_CATEGORY)) 
			{
				categoryToEdit = serializedCategory;
				// setzt den aktuellen Icon
				for (int i = 0; i < icons.length; i++)
				{
					if (icons[i].getId() == serializedCategory.getIcon().getId()) 	
					{
						//spinner.setSelection(i);
						selectedIcon2 = icons[i];
						Controller.getInstance().setImageOnImageView(this, iV, selectedIcon2.getName());
						break;
					}
				}
				EditText editTextName = (EditText) findViewById(R.id.edit_category_name);
				editTextName.setText(categoryToEdit.getName());
			}
		}
		else
			Controller.getInstance().setImageOnImageView(this, iV, Controller.getInstance().getCurrentCategory().getIcon().getName());
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // Check which request we're responding to
	    if (requestCode == Controller.REQUEST_CODE_CHOOSE_ICON) 
	        Controller.getInstance().setImageOnImageView(this, iV, selectedIcon2.getName());
  
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (categoryToEdit != null) {
			getSupportActionBar().setTitle(this.getResources().getString(R.string.actionbartitle_edit_category));
			getMenuInflater().inflate(R.menu.edit_category, menu);
		} else {
			getMenuInflater().inflate(R.menu.new_category, menu);
		}

		Icon icon = Controller.getInstance().getCurrentCategory().getIcon();
		if (icon !=null){
			getSupportActionBar().setIcon(icon.getDrawableId());
		}
		
		return true;
	}
	/**
	 * Eine neue Kategorie wird angelegt.
	 */
	public void onSaveCategory(){
		Category newCategory = saveCategory(false);
		Controller.getInstance().setCurrentCategory(newCategory);
		//TODO ordnetlichen dialog anzeigen - Erfolg/Misserfolg
		goToNewCurrentCategory();
	}
	/**
	 * Die Aenderungen an der aktuellen Kategorie werden auf die Datenbank gespeichert
	 */
	public void onUpdate(){
		Category updatedCategory = saveCategory(true);
		//TODO ordnetlichen dialog anzeigen - Erfolg/Misserfolg
		Controller.getInstance().setCurrentCategory(updatedCategory);
		goToNewCurrentCategory();
	}
	/**
	 * Speichert oder aendert die akutelle Kategorie
	 * TODO evntuell statt boolean ein enum anlegen
	 * @param isUpdate gibt an, ob insert oder update ausgefuehrt werden soll
	 * @return
	 */
	private Category saveCategory(boolean isUpdate){
		//Spinner spinner = (Spinner) findViewById(R.id.spinner_new_category_icon);
		//Icon selectedIcon = (Icon) spinner.getSelectedItem();
		String categoryName = ((TextView)findViewById(R.id.edit_category_name)).getText().toString();
		Category category = null;
		if(!isUpdate){
			//neue Kategorie einfuegen
			category = Controller.getInstance().insertCategory(
					categoryName, 
					selectedIcon2, 
					Controller.getInstance().getCurrentCategory().getId()); // Die aktuelle Kategorie wird die Oberkategorie
		}else{
			//ausgewaehlte Kategorie aktualisieren
			Category updatecategory = Controller.getInstance().getCurrentCategory();
			updatecategory.setName(categoryName);
			updatecategory.setIcon(selectedIcon2);
			category = Controller.getInstance().updateCategory(updatecategory);
		}
		return category;
	}
	
	private void goToNewCurrentCategory(){
        Intent intent = new Intent();   
        intent.setClassName(getPackageName(), ListCategoriesActivity.class.getName());
        startActivity(intent);
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
	
	/**
	 * 
	 * Loescht die Kategorie. Fragt aber vorher sicherheitshalber nochmal noch.
	 */	
	public void onDelete(){
		DialogDecision dd = new DialogDecision();
		String question = getResources().getText(R.string.delete_dialog_category).toString();
		String yes = getResources().getText(R.string.btn_alert_de_ok).toString();
		String no = getResources().getText(R.string.btn_alert_de_no).toString();
		dd.initDialogAttributes(question, yes, no);
        dd.show(getSupportFragmentManager(), "DeleteDialogFragment");
	}
	
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
    	Controller.getInstance().deleteCategory(categoryToEdit);
    	//Controller.getInstance().deleteCategoryRecursively(categoryToEdit); //TODO funktioniert noch nicht.
    	onBackPressed(); 
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    	// Nirwana
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	
			int itemId = item.getItemId();
		    switch (itemId) {
		        case R.id.menu_save_new_category:
		        	onSaveCategory();
		            return true;
		        case R.id.menu_update_category:
		        	onUpdate();
		            return true;
		        case R.id.menu_delete_category:
		            onDelete();
		            return true;
		        case R.id.menu_chancel_category:
		            onChancel();
		            return true;
		        case R.id.action_settings:
		        	//TODO do something
		        	return true;
		        default:
		            return super.onOptionsItemSelected(item);
		    }		
	}

	public static Icon getSelectedIcon() {
		return selectedIcon2;
	}

	public static void setSelectedIcon(Icon selectedIconFrom) {
		selectedIcon2 = selectedIconFrom;
	}
	
    @Override
    public void onBackPressed(){	
        Intent intent = new Intent();   
        intent.setClassName(getPackageName(), ListCategoriesActivity.class.getName());
        startActivity(intent);				
		finish();
    }
}
