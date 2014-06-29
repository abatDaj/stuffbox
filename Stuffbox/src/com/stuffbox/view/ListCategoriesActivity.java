package com.stuffbox.view;

import java.util.ArrayList;

import com.stuffbox.R;
import com.stuffbox.R.id;
import com.stuffbox.R.layout;
import com.stuffbox.R.menu;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Category;
import com.stuffbox.model.DatabaseHandler;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Build;

public class ListCategoriesActivity extends ActionBarActivity {

	private ListView mainListView ;
	private CategoryArrayAdapter categoryAdapter ;
	public final static String EXTRA_KATEGORIE_NAME = "com.stuffbox.KATEGORIENAME";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_categories);
			       
        ArrayList<Category> mainCategories = Controller.getInstance().getSubCategories(DatabaseHandler.INDEX_OF_ROOT_CATEGORY);
        
        mainListView = (ListView) findViewById( R.id.categoryListView );
        
        mainListView.setOnItemClickListener(new OnItemClickListener()
        {
	        @Override
	        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	        {
		        Intent intent = new Intent();		        
		        intent.setClassName(getPackageName(), CategoryActivity.class.getName());
		        intent.putExtra(EXTRA_KATEGORIE_NAME, mainListView.getAdapter().getItem(arg2).toString());
		        startActivity(intent);
	        }
        });
        
		Category[] cats = new Category[mainCategories.size()];
		
		for (int i = 0 ;i < mainCategories.size();i++)
			cats[i] = mainCategories.get(i);

        categoryAdapter = new CategoryArrayAdapter (this, cats);
	    mainListView.setAdapter( categoryAdapter );

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

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
