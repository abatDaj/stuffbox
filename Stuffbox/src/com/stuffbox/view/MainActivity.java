package com.stuffbox.view;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.stuffbox.R;
import com.stuffbox.R.id;
import com.stuffbox.R.layout;
import com.stuffbox.R.menu;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.DatabaseHandler;
import com.stuffbox.model.Feature;
import com.stuffbox.model.FeatureType;
import com.stuffbox.model.Icon;

public class MainActivity extends ActionBarActivity {

	private ListView mainListView ;
	private ArrayAdapter<Feature> listAdapter ;
	
	public final static String EXTRA_KATEGORIE_NAME = "com.stuffbox.KATEGORIENAME";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);

        //Controller.initialize(this);
		ArrayList<Feature> features = Controller.getFeatures(null);
		
        // because layout.main 
        /*if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/
        
        mainListView = (ListView) findViewById( R.id.mainListView );
        
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

//	    String[] categories = new String[] { "Bücher", "Filme", "Dokus", "Lebensmittel",
//	                                      "Tee-Sorten", "Holzbretter"};  
//	    ArrayList<String> categoryList = new ArrayList<String>();
//	    categoryList.addAll( Arrays.asList(categories) );

        
        listAdapter = new ArrayAdapter<Feature>(this, R.layout.category_row, features);
	    mainListView.setAdapter( listAdapter );
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
