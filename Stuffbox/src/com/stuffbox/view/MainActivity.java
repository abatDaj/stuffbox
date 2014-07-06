package com.stuffbox.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.provider.MediaStore; 
import android.provider.MediaStore.MediaColumns;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;


public class MainActivity extends ActionBarActivity {

	private ImageButton btn ;
	
	private static final String TAG = MainActivity.class.getSimpleName();
	private static final int REQUEST_CAMERA = 42;
	private static final int SELECT_FILE = 1;
	
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
        intent.setClassName(getPackageName(), BadgeActivity.class.getName());
        startActivity(intent);
    }	   

    
    public void openListCategories(View view) {    	
        Intent intent = new Intent();        
        intent.setClassName(getPackageName(), ListCategoriesActivity.class.getName());
        startActivity(intent);
    }	 
}
