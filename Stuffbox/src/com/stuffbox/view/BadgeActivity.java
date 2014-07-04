package com.stuffbox.view;

import java.util.ArrayList;

import com.stuffbox.R;
import com.stuffbox.model.Badge;
import com.stuffbox.model.BadgeFeed;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BadgeActivity extends ActionBarActivity {

	private ListView mainListView ;
	private BadgeArrayAdapter arrAdapter;
	private ArrayList<Badge> arrList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.badge);
		
		mainListView = (ListView) findViewById(R.id.badgeListView);
		/**TODO
		 * Nur Test um Umgang zu erlernen
		 */
		arrList = new ArrayList<Badge>();
		arrList.add(new Badge("Buecher"));
		arrList.add(new Badge("Kleidung"));
		
		//arrAdapter = new ArrayAdapter<String>(this, arrList);
		arrAdapter = new BadgeArrayAdapter(this, arrList);
		mainListView.setAdapter(arrAdapter);
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.badge, menu);
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
