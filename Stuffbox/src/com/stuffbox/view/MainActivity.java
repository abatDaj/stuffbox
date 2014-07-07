package com.stuffbox.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;


public class MainActivity extends ActionBarActivity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        Controller.getInstance(this).init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    
    public void openAbzeichenScreen(View view) {    	
        Intent intent = new Intent();        
        intent.setClassName(getPackageName(), ChooseFeaturesActivity.class.getName());
        startActivity(intent);
    }	   

    
    public void openListCategories(View view) {    	
        Intent intent = new Intent();        
        intent.setClassName(getPackageName(), ListCategoriesActivity.class.getName());
        startActivity(intent);
    }	 
}
