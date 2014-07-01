package com.stuffbox.view;

import java.util.ArrayList;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Category;
import com.stuffbox.model.DataSourceCategory;
import com.stuffbox.model.DatabaseHandler;
import com.stuffbox.model.Icon;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.support.v4.app.DialogFragment; //api 8 ! 
import android.support.v4.app.FragmentManager;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class ListCategoriesActivity extends ActionBarActivity {

	private ListView mainListView ;
	private CategoryArrayAdapter categoryAdapter ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_categories);
		
        mainListView = (ListView) findViewById( R.id.categoryListView );
        mainListView.setOnItemClickListener(new OnItemClickListener()
        {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	        {		        
				Category clickedCategory = (Category) parent.getItemAtPosition(position);
		        Controller.getInstance().setCurrentCategory(clickedCategory);
		        Intent intent = new Intent();		        
		        intent.setClassName(getPackageName(), ListCategoriesActivity.class.getName());
		        startActivity(intent);
	        }
        });
        
        Category currentCategory = Controller.getInstance().getCurrentCategory();
        ArrayList<Category> mainCategories = Controller.getInstance().getSubCategories(currentCategory.getId());
		Category[] cats = new Category[mainCategories.size()];
		
		for (int i = 0 ;i < mainCategories.size();i++)
			cats[i] = mainCategories.get(i);

        categoryAdapter = new CategoryArrayAdapter (this, cats);
	    mainListView.setAdapter( categoryAdapter );	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {	
		Category currentCategory = Controller.getInstance().getCurrentCategory();
		getSupportActionBar().setTitle(currentCategory.getName());

		Icon icon = currentCategory.getIcon();
		if (icon !=null)
			getSupportActionBar().setIcon(icon.getDrawableId());

		if (currentCategory.getName().equals(DataSourceCategory.ROOT_CATEGORY))
			getMenuInflater().inflate(R.menu.list_categories_start, menu);
		else
			getMenuInflater().inflate(R.menu.list_categories, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
	    switch (itemId) {
	        case R.id.menu_new_item:
	        	onNewItem();
	            return true;
	        case R.id.menu_edit_category:
	        	onEdit();
	            return true;
	        case R.id.action_settings:
	        	//TODO do something
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }		
	}
	
	@Override
	public void onBackPressed () {
		Category currentCategory = Controller.getInstance().getCurrentCategory();
		if (!currentCategory.getName().equals(DataSourceCategory.ROOT_CATEGORY)) 
			ListCategoriesActivity.navigateBack(this);
		else {
			Intent intent = new Intent();   
	        intent.setClassName(getPackageName(), MainActivity.class.getName());
	        startActivity(intent);	
			this.finish();
		}
	}
	
	/**
	 * Löscht die Kategorie
	 */
	public void onDelete () {
		Toast.makeText(getApplicationContext(), "LÖSCHEN !!!", 7).show();
	}
	
	/**
	 * Ändert die Kategorie
	 */
	public void onEdit () {
		//ClassName details = new ClassName();
		Intent i = new Intent(this, NewCategoryActivity.class);
		i.putExtra(Controller.EXTRA_EDIT_CATEGORY, Controller.getInstance().getCurrentCategory());
		startActivity(i);		
	}
	
	/**
	 * Ändert die Kategorie
	 */
	public void onNewItem () {
        Intent intent = new Intent();        
        intent.setClassName(getPackageName(), NewItemActivity.class.getName());
        startActivity(intent);
	}	
	
    public void openNewCategoryScreen(View view) {    	
        Intent intent = new Intent();        
        intent.setClassName(getPackageName(), NewCategoryActivity.class.getName());
        startActivity(intent);
    }	
    
    public static void navigateBack(Activity a){
    	Category preCategory = Controller.getInstance().getPreCategory(Controller.getInstance().getCurrentCategory());
		Controller.getInstance().setCurrentCategory(preCategory);		
        Intent intent = new Intent();   
        intent.setClassName(a.getPackageName(), ListCategoriesActivity.class.getName());
        a.startActivity(intent);				
		a.finish();
    }
}
