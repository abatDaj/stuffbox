package com.stuffbox.view;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.stuffbox.R;
import com.stuffbox.model.DatabaseHandler;

public class SearchableActivity extends ListActivity{

	DatabaseHandler dh = new DatabaseHandler(this);

//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//	    super.onCreate(savedInstanceState);
//	    setContentView(R.layout.list);
//
//	    // Get the intent, verify the action and get the query
//	    Intent intent = getIntent();
//	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//	      String query = intent.getStringExtra(SearchManager.QUERY);
//	      doSearch(query);
//	    }
//	}
	
//	private void doSearch(String query) {
//		Cursor c = dh.getWordMatches(query, null);
//	    //process Cursor and display results
//	}
	
	   public void onCreate(Bundle savedInstanceState) { 
		      super.onCreate(savedInstanceState); 
		      handleIntent(getIntent()); 
		   } 

		   public void onNewIntent(Intent intent) { 
		      setIntent(intent); 
		      handleIntent(intent); 
		   } 

		   public void onListItemClick(ListView l, 
		      View v, int position, long id) { 
		      // call detail activity for clicked entry 
		   } 

		   private void handleIntent(Intent intent) { 
		      if (Intent.ACTION_SEARCH.equals(intent.getAction())) { 
		         String query = 
		               intent.getStringExtra(SearchManager.QUERY); 
		         doSearch(query); 
		      } 
		   }    

		   private void doSearch(String query) { 
		   // get a Cursor, prepare the ListAdapter
		   Cursor c = dh.getWordMatches(query, null);
		   // and set it
		   
		   } 
}
