package com.stuffbox.view;

import java.util.ArrayList;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Category;
import com.stuffbox.model.DataSourceCategory;
import com.stuffbox.model.DatabaseHandler;
import com.stuffbox.model.Icon;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
		getSupportActionBar().setTitle(Controller.getInstance().getCurrentCategory().getName());
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_categories, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
    public void openNewCategoryScreen(View view) {    	
        Intent intent = new Intent();        
        intent.setClassName(getPackageName(), NewCategoryActivity.class.getName());
        startActivity(intent);
    }	


}
