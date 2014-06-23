package com.stuffbox.view;

import java.util.ArrayList;

import android.content.Intent;
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
import android.widget.ListView;
import android.widget.Toast;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Category;
import com.stuffbox.model.Feature;
import com.stuffbox.model.Icon;

public class MainActivity extends ActionBarActivity {

	private ListView mainListView ;
	private CategoryArrayAdapter categoryAdapter ;
	
	public final static String EXTRA_KATEGORIE_NAME = "com.stuffbox.KATEGORIENAME";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        Controller.initialize(this);
        ArrayList<Category> categories = Controller.getCategories(null);
	        
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
        
        //insertIcon(database,drawableFields[i].getName(),"egal");
        
		Category[] cats = new Category[categories.size()];
		
		for (int i = 0 ;i < categories.size();i++)
			cats[i] = categories.get(i);

        categoryAdapter = new CategoryArrayAdapter (this, cats);
	    mainListView.setAdapter( categoryAdapter );
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
    
    public void openNewCategoryScreen(View view) {    	
        Intent intent = new Intent();        
        intent.setClassName(getPackageName(), NewCategoryActivity.class.getName());
        startActivity(intent);
    }	

    public void openCategoryScreen(View view) {    	
        Intent intent = new Intent();        
        intent.setClassName(getPackageName(), CategoryActivity.class.getName());
        startActivity(intent);
    }	
    
    public void openFormularScreen(View view) {    	
        Intent intent = new Intent();        
        intent.setClassName(getPackageName(), FormularActivity.class.getName());
        startActivity(intent);
    }	
    

    public void openDetailScreen(View view) {    	
        Intent intent = new Intent();        
        intent.setClassName(getPackageName(), DetailActivity.class.getName());
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
