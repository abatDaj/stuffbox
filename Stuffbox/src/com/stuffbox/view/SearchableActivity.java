package com.stuffbox.view;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Item;
import com.stuffbox.view.helper.Utility;

public class SearchableActivity extends ActionBarActivity{
	private ListView itemListView ;
	private ItemArrayAdapter itemAdapter ;

	
	@Override
   public void onCreate(Bundle savedInstanceState) { 
      super.onCreate(savedInstanceState); 
      setContentView(R.layout.list);
      handleIntent(getIntent()); 
      
      // Anzeigen der Items:
      itemListView = (ListView) findViewById( R.id.listView );
      ArrayList<Item> allItems = new ArrayList<Item>();
      itemAdapter = new ItemArrayAdapter (this, allItems);
      itemListView.setAdapter( itemAdapter );	
      
	    // Groesse der Listen anhand der Anzahl der Eigenschaften neu setzen.
      Utility.setListViewHeightBasedOnChildren(itemListView, 0);
   } 

   public void onNewIntent(Intent intent) { 
      setIntent(intent); 
      handleIntent(intent); 
   } 

   private void handleIntent(Intent intent) { 
      if (Intent.ACTION_SEARCH.equals(intent.getAction())) { 
         String query = intent.getStringExtra(SearchManager.QUERY); 
         doSearch(query); 
      } 
   }    

   private void doSearch(String query) { 
   // Items holen
	   ArrayList<Item> items = Controller.getInstance().getItemsFromWordMatches(query, null);
	   itemAdapter = new ItemArrayAdapter (this, items);
        itemListView.setAdapter( itemAdapter );	
        
	    // Groesse der Listen anhand der Anzahl der Eigenschaften neu setzen.
        Utility.setListViewHeightBasedOnChildren(itemListView, 0);
        
//        // Anzeigen der Items:
//        itemListView = (ListView) findViewById( R.id.itemListView );
//        itemListView.setOnItemClickListener(new OnItemClickListener()
//        {
//	        @Override
//	        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//	        {		        
//				Item clickedItem = (Item) parent.getItemAtPosition(position);
//				Controller.getInstance().setCurrentItem(clickedItem);
//		        Intent intent = new Intent();        
//		        intent.setClassName(getPackageName(), DetailItemActivity.class.getName());
//		        startActivity(intent);
//	        }
//        });
//
//        ArrayList<Item> allItems = Controller.getInstance().getItemsOfACategory(Controller.getInstance().getCurrentCategory().getId());
//        itemAdapter = new ItemArrayAdapter (this, allItems);
//        itemListView.setAdapter( itemAdapter );	
//        
//	    // Groesse der Listen anhand der Anzahl der Eigenschaften neu setzen.
//        Utility.setListViewHeightBasedOnChildren(itemListView, 0);
   }
}
