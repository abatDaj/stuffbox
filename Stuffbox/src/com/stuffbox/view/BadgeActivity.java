package com.stuffbox.view;

import java.util.ArrayList;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Badge;
import com.stuffbox.model.BadgeFeed;
import com.stuffbox.model.Category;
import com.stuffbox.model.Item;
import com.stuffbox.model.Level;

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
		//Beispiel
		//arrList.add(new Badge("Tolle Musik, wie Metal",6,"category_icon_musi",true,true,true,true,true));
		//arrList.add(new Badge("KleidungKleidungKleidungKleidungKleidungKleidungKleidungKleidung",3,"category_icon_clothes",true, true, false,false,false));
		
		/**
		 * Rootkategorien fuer Badgesystem laden
		 */
		
		Level level = new Level();
		ArrayList<Category> allCat= Controller.getInstance().getCategories(null);
		ArrayList<Category> subCat= new ArrayList<Category>();
		ArrayList<Category> rootCat = new ArrayList<Category>();
		ArrayList<Category> interestCat = new ArrayList<Category>();
		//Dieser Teil wenn man nur Subkategorien haben moechte
		/*for(Category cat: allCat){
			if(cat.getPreCategoryId() == -1){
				//Pruefen ob bereits vorhanden
				boolean doubledCat = false;
				for(Category checkCat: rootCat){
					if(checkCat.equals(cat)){
						doubledCat = true;
					}
				}
				if(!doubledCat){
					rootCat.add(cat);
				}
			}
			
		}
		
		for(Category cat: rootCat){
			ArrayList<Category> subCatCopy= Controller.getInstance().getSubCategories(cat.getId());
			//Subkategories adden
			for(Category catc: subCatCopy){
				boolean doubledCat = false;
				for(Category checkCat: subCat){
					if(checkCat.equals(catc)){
						doubledCat = true;
					}
				}
				if(!doubledCat){
					subCat.add(catc);
				}
			}
			
		}*/
		
		//Dieser Part fuer alle Kategorien
		for(Category cat : allCat){
			interestCat.add(cat);
		}
		//Tracking und debugging infos
		System.out.println("AlleKategorien: " + allCat.size());
		System.out.println("RootKategorien: " + rootCat.size());
		System.out.println("SubKategorien: " + subCat.size());
		System.out.println("InteressierendeKategorien: " + interestCat.size());
		
		//Anzahl der Items pro Kategorie
		for(Category cat: interestCat){
			ArrayList<Item> items = Controller.getInstance().getItemsOfACategory(cat.getId());
			System.out.println(cat.getName() + "_:anhzahl:_" + items.size() + "_lvl: " + Level.getBadgemark5());
			if(items.size() >= Level.getBadgemark5()){
				System.out.println(items.size() + "_lvl_" + Level.getBadgemark5());
				arrList.add(new Badge(cat.getName(),items.size(), cat.getIcon().getName(),true,true,true,true,true));
			}else if(items.size() >= Level.getBadgemark4()){
				arrList.add(new Badge(cat.getName(),items.size(), cat.getIcon().getName(),true,true,true,true,false));
			}else if(items.size() >= Level.getBadgemark3()){
				arrList.add(new Badge(cat.getName(),items.size(), cat.getIcon().getName(),true,true,true,false,false));
			}else if(items.size() >= Level.getBadgemark2()){
				arrList.add(new Badge(cat.getName(),items.size(), cat.getIcon().getName(),true,true,false,false,false));
			}else if(items.size() >= Level.getBadgemark1()){
				arrList.add(new Badge(cat.getName(),items.size(), cat.getIcon().getName(),true,false,false,false,false));
			}else{
				arrList.add(new Badge(cat.getName(),items.size(), cat.getIcon().getName(),false,false,false,false,false));
			}
			
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
