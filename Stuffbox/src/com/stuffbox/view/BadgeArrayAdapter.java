package com.stuffbox.view;

import java.util.ArrayList;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Badge;
import com.stuffbox.view.helper.Utility;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class BadgeArrayAdapter extends ArrayAdapter<Badge> {
	private final Context context;
	private final ArrayList<Badge> values;

	public BadgeArrayAdapter(Context context, ArrayList<Badge> values) {
		super(context, R.layout.badge_row, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	public View getCustomView(int position, View convertView, ViewGroup parent) {

		LinearLayout rowView = new LinearLayout(context);
		rowView.setOrientation(LinearLayout.HORIZONTAL);
		rowView.setPadding(0, 20, 0, 20);
		rowView.setLayoutParams(new ListView.LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		rowView.setOnClickListener(null);
		rowView.setOnTouchListener(null);

		TextView textView = new TextView(context);
		textView.setLayoutParams(new LayoutParams(150,LinearLayout.LayoutParams.FILL_PARENT));
		textView.setGravity(Gravity.CENTER_VERTICAL);
		textView.setText(values.get(position).getCategory().getName() + " ("+ values.get(position).getItemcount() + ") ");
		textView.setPadding(20, 0, 0, 0);

		rowView.addView(textView);

		int drawableId = values.get(position).getCategory().getIcon().getDrawableId();

		ImageView greyPics[] = new ImageView[5];
		for (int i = 0; i < greyPics.length; i++) {
			greyPics[i] = new ImageView(context);
			greyPics[i].setImageResource(drawableId);
			greyPics[i].setLayoutParams(new LinearLayout.LayoutParams(Controller.BADGE_ICON_SIZE, Controller.BADGE_ICON_SIZE));
		}

		if (values.get(position).isBadge1()) {
			ImageView star1 = Utility.stuffBoxStarIconCloner(context,drawableId, 1);
			rowView.addView(star1);
		} else {
			rowView.addView(greyPics[0]);
			Utility.makeImageViewGrey(greyPics[0]);
		}
		if (values.get(position).isBadge2()) {
			ImageView star2 = Utility.stuffBoxStarIconCloner(context,drawableId, 2);
			rowView.addView(star2);
		} else {
			rowView.addView(greyPics[1]);
			Utility.makeImageViewGrey(greyPics[1]);
		}

		if (values.get(position).isBadge3()) {
			ImageView star3 = Utility.stuffBoxStarIconCloner(context,drawableId, 3);
			rowView.addView(star3);
		} else {
			rowView.addView(greyPics[2]);
			Utility.makeImageViewGrey(greyPics[2]);
		}

		if (values.get(position).isBadge4()) {
			ImageView star4 = Utility.stuffBoxStarIconCloner(context,drawableId, 4);
			rowView.addView(star4);
		} else {
			rowView.addView(greyPics[3]);
			Utility.makeImageViewGrey(greyPics[3]);
		}

		if (values.get(position).isBadge5()) {
			ImageView star5 = Utility.stuffBoxStarIconCloner(context,drawableId, 5);
			rowView.addView(star5);
		} else {
			rowView.addView(greyPics[4]);
			Utility.makeImageViewGrey(greyPics[4]);
		}
		return rowView;
	}
}