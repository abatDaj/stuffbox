package com.stuffbox.view;

import com.stuffbox.R;
import com.stuffbox.R.id;
import com.stuffbox.R.layout;
import com.stuffbox.R.menu;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Badge;
import com.stuffbox.model.Category;
import com.stuffbox.view.helper.Utility;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Build;

public class BadgeDetailActivity extends ActionBarActivity {

	Badge badge;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.badge_detail);
		badge = (Badge) getIntent().getSerializableExtra(Controller.EXTRA_BADGE_SHOW_DETAIL);
		
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setPadding(0, 20, 0, 20);
		ll.setLayoutParams(new ListView.LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		TextView textView = new TextView(this);
		textView.setLayoutParams(new LayoutParams(150,LinearLayout.LayoutParams.FILL_PARENT));
		textView.setGravity(Gravity.CENTER_VERTICAL);
		textView.setText("Anzahl Einträge: " + badge.getItemcount());
		textView.setPadding(20, 0, 0, 0);
		ll.addView(textView);
		
		LinearLayout rowView = new LinearLayout(this);
		rowView.setOrientation(LinearLayout.HORIZONTAL);
		rowView.setPadding(0, 20, 0, 20);
		rowView.setLayoutParams(new ListView.LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		int drawableId = badge.getCategory().getIcon().getDrawableId();

		ImageView greyPics[] = new ImageView[5];
		
		for (int i = 0; i < greyPics.length; i++) {
			greyPics[i] = new ImageView(this);
			greyPics[i].setImageResource(drawableId);
			greyPics[i].setLayoutParams(new LinearLayout.LayoutParams(Controller.BADGE_ICON_SIZE, Controller.BADGE_ICON_SIZE));
			if (badge.getHighestBadge() > 1) {
				ImageView star1 = Utility.stuffBoxStarIconCloner(this,drawableId, badge.getHighestBadge());
				rowView.addView(star1);
			} else {
				Utility.makeImageViewGrey(greyPics[i]);
				rowView.addView(greyPics[i]);
			}
		}
		ll.addView(rowView);
		
		FrameLayout rootLayout = (FrameLayout)findViewById(android.R.id.content);
		rootLayout.addView(ll);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getSupportActionBar().setTitle(badge.getCategory().getName() + " ("+ badge.getItemcount() + ") ");
		getMenuInflater().inflate(R.menu.edit, menu);
		getMenuInflater().inflate(R.menu.badge_detail, menu);
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
