package com.stuffbox.view;

import java.io.FileOutputStream;
import java.io.OutputStream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;


public class MainActivity extends ActionBarActivity {

	private ImageButton btn ;
	
	private static final String TAG = MainActivity.class.getSimpleName();
	
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
    
    public void openFormularScreen(View view) {    	
        Intent intent = new Intent();        
        intent.setClassName(getPackageName(), FormularActivity.class.getName());
        startActivity(intent);
    }	
    
    public void openFormularNewScreen(View view){    	
        Intent intent = new Intent();        
        intent.setClassName(getPackageName(), NewFormularActivity.class.getName());
        startActivity(intent);
    }		
    
    public void openAbzeichenScreen(View view) {    	
        Intent intent = new Intent();        
        intent.setClassName(getPackageName(), BadgeActivity.class.getName());
        startActivity(intent);
    }	
    
    public void openArtenScreen(View view) {    	
        Intent intent = new Intent();        
        intent.setClassName(getPackageName(), FeatureActivity.class.getName());
        startActivity(intent);
    }	    

    
    public void openListCategories(View view) {    	
        Intent intent = new Intent();        
        intent.setClassName(getPackageName(), ListCategoriesActivity.class.getName());
        startActivity(intent);
    }	 
    
    public void openFotoScreen(View view) {    	
    	Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
    	startActivityForResult(cameraIntent, 42);
    }	
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	try {
        if (requestCode == 42) {
            Bitmap thumb = (Bitmap) data.getExtras().get("data"); 
            btn = (ImageButton)findViewById(R.id.imageButtonFoto);
            btn.setImageBitmap(thumb);
            OutputStream stream = new FileOutputStream("/sdcard/test.jpg");
            thumb.compress(CompressFormat.JPEG, 100, stream);
        }
    	}catch (Exception e) {
    		Log.e(TAG, "Fehler beim Fotografieren: ", e);
    	}
    }	    	
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
