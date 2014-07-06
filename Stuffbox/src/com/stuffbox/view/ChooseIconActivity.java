package com.stuffbox.view;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Icon;
import com.stuffbox.view.helper.PredicateLayout;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


public class ChooseIconActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.new_item_choose_icon);
		

		
		ScrollView sV = new ScrollView(this);
		
		PredicateLayout l = new PredicateLayout(this);
		sV.addView(l);
		
        for (final Icon icon : Controller.getInstance().getIcons()) {
        	
    		LinearLayout ll = new LinearLayout(this);
        	ImageView iV = new ImageView(this);
    		iV.setImageResource(icon.getDrawableId());
    		iV.setLayoutParams(new LinearLayout.LayoutParams(124,124));
    		iV.setPadding(7, 7, 7, 7);
    		iV.setOnClickListener(new OnClickListener(){
    			@Override
    			public void onClick(View v) {
    				NewCategoryActivity.setSelectedIcon(icon);
    				finish();
    			}});
    		ll.addView(iV);
    		l.addView(ll);
        }
        setContentView(sV);	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.choose_items, menu);
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
