package com.stuffbox.view;

import com.stuffbox.R;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class CategoryActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_detail);
		Intent intent = getIntent();
		//((TextView)(findViewById(R.id.categoryText))).setText("Es wurde "+intent.getStringExtra(ListCategoriesActivity.EXTRA_KATEGORIE_NAME)+ " gewählt!");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		Intent intent = getIntent();
		//String message = intent.getStringExtra(ListCategoriesActivity.EXTRA_KATEGORIE_NAME);
		//getSupportActionBar().setTitle(message);

		//Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.category, menu);
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
	
	
    public void openArtenScreen(View view) {    	
        Intent intent = new Intent();        
        intent.setClassName(getPackageName(), NewFeatureActivity.class.getName());
        startActivity(intent);
    }	
}
