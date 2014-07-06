package com.stuffbox.view;

import java.util.ArrayList;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Badge;
import com.stuffbox.model.BadgeFeed;
import com.stuffbox.model.Category;
import com.stuffbox.model.Item;
import com.stuffbox.model.UserLevel;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class BadgeActivity extends ActionBarActivity {

	private ListView mainListView ;
	private TextView lvlTextView;
	private BadgeArrayAdapter arrAdapter;
	private ArrayList<Badge> arrList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.badge);
		
		mainListView = (ListView) findViewById(R.id.badgeListView);
		//Hier drinne nur zum Levelanzeige test
		lvlTextView = (TextView) findViewById(R.id.badgeLvlTextview);
		
		/**TODO
		 * Nur Test um Umgang zu erlernen
		 */
		arrList = new ArrayList<Badge>();
		
		/**
		 * Rootkategorien fuer Badgesystem laden
		 */
		
		UserLevel level = new UserLevel();
		
		//Level anzeigen
		lvlTextView.setText("lvl " + level.getLvlCount());
		
		ArrayList<Category> allCat= Controller.getInstance().getCategories(null);
		ArrayList<Category> subCat= new ArrayList<Category>();
		ArrayList<Category> rootCat = new ArrayList<Category>();
		ArrayList<Category> interestCat = new ArrayList<Category>();
		
		//Dieser Part fuer alle Kategorien
		for(Category cat : allCat){
			interestCat.add(cat);
		}
		//Tracking und debugging infos
		System.out.println("AlleKategorien: " + allCat.size());
		System.out.println("RootKategorien: " + rootCat.size());
		System.out.println("SubKategorien: " + subCat.size());
		System.out.println("InteressierendeKategorien: " + interestCat.size());
		System.out.println("Badges: " + level.getBadgeCount());
		System.out.println("Level: " + level.getLvlCount());
		//Anzahl der Items pro Kategorie
		for(Category cat: interestCat){
			ArrayList<Item> items = Controller.getInstance().getItemsOfACategory(cat.getId());
			System.out.println(cat.getName() + "_:anhzahl:_" + items.size() + "_lvl: " + UserLevel.getBadgemark5());
			arrList.add(new Badge(cat.getName(),items.size(), cat.getIcon().getName(),level.awardedBadge1(cat.getId()),level.awardedBadge2(cat.getId()),level.awardedBadge3(cat.getId()),level.awardedBadge4(cat.getId()),level.awardedBadge5(cat.getId())));
		}
		
		
		//arrAdapter = new ArrayAdapter<String>(this, arrList);
		arrAdapter = new BadgeArrayAdapter(this, arrList);
		mainListView.setAdapter(arrAdapter);
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.badge, menu);
		getSupportActionBar().setIcon(R.drawable.icon_badge);
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
