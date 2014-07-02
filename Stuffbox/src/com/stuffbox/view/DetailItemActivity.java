package com.stuffbox.view;

import java.util.ArrayList;

import com.stuffbox.R;
import com.stuffbox.R.id;
import com.stuffbox.R.layout;
import com.stuffbox.R.menu;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Category;
import com.stuffbox.model.Feature;
import com.stuffbox.model.Formular;
import com.stuffbox.model.Icon;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Build;

public class DetailItemActivity extends ActionBarActivity {
	
	private ListView mainListView ;
	private FeatureArrayAdapterForDetailItem formularAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_item);
		
		Formular serializedCategory = (Formular) getIntent().getSerializableExtra(Controller.EXTRA_FORMULAR_FOR_NEW_ITEM);
		if (serializedCategory != null) {

			mainListView = (ListView) findViewById( R.id.featureListViewInDetailItem);

			ArrayList<Feature> g = Controller.getInstance().getFormulars(null).get(0).getFeatures();
	        		
	        ArrayList<Feature> features = serializedCategory.getFeatures();
	        
	        Feature[] feas = new Feature[g.size()];
			Controller.getInstance().sayIt("ZAHL: " + g.size());
			for (int i = 0 ;i < g.size();i++)
				feas[i] = g.get(i);

			formularAdapter = new FeatureArrayAdapterForDetailItem (this, feas);
		    mainListView.setAdapter( formularAdapter );			
		}
		else // TODO
			 new RuntimeException ("Something went horribly wrong :-0");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		Category currentCategory = Controller.getInstance().getCurrentCategory();
		Icon icon = currentCategory.getIcon();
		if (icon !=null)
			getSupportActionBar().setIcon(icon.getDrawableId());
		
		getMenuInflater().inflate(R.menu.detail_item, menu);
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
}
