package com.stuffbox.view;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Badge;
import com.stuffbox.model.Category;
import com.stuffbox.model.Icon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BadgeArrayAdapter extends ArrayAdapter<Badge>{
	private final Context context;
	private final ArrayList<Badge> values;
	
	public BadgeArrayAdapter(Context context, ArrayList<Badge> values){
		super(context, R.layout.badge_row, values);
		this.context = context;
		this.values = values;
		
		
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}
	
	public View getCustomView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.badge_row, parent, false);
		TextView badgeCatText = (TextView) rowView.findViewById(R.id.badge_row_text1);
		ImageView imageView1 = (ImageView) rowView.findViewById(R.id.badge_row_icon1);
		ImageView imageView2 = (ImageView) rowView.findViewById(R.id.badge_row_icon2);
		ImageView imageView3 = (ImageView) rowView.findViewById(R.id.badge_row_icon3);
		ImageView imageView4 = (ImageView) rowView.findViewById(R.id.badge_row_icon4);
		ImageView imageView5 = (ImageView) rowView.findViewById(R.id.badge_row_icon5);

		//Set Text
		badgeCatText.setText(values.get(position).getCatName());
		/*
		 * Select badges
		 */
		if(values.get(position).isBadge1()){
			Controller.getInstance().setImageOnImageView(this.context, imageView1, "badge_" + values.get(position).getBadgeIconSet()+"_1");
		}else{
			Controller.getInstance().setImageOnImageView(this.context, imageView1, "badge_" + values.get(position).getBadgeIconSet()+"_0");
		}
		
		if(values.get(position).isBadge2()){
			Controller.getInstance().setImageOnImageView(this.context, imageView2, "badge_" + values.get(position).getBadgeIconSet()+"_2");
		}else{
			Controller.getInstance().setImageOnImageView(this.context, imageView2, "badge_" + values.get(position).getBadgeIconSet()+"_0");
		}
		
		if(values.get(position).isBadge3()){
			Controller.getInstance().setImageOnImageView(this.context, imageView3, "badge_" + values.get(position).getBadgeIconSet()+"_3");
		}else{
			Controller.getInstance().setImageOnImageView(this.context, imageView3, "badge_" + values.get(position).getBadgeIconSet()+"_0");
		}
		
		if(values.get(position).isBadge4()){
			Controller.getInstance().setImageOnImageView(this.context, imageView4, "badge_" + values.get(position).getBadgeIconSet()+"_4");
		}else{
			Controller.getInstance().setImageOnImageView(this.context, imageView4, "badge_" + values.get(position).getBadgeIconSet()+"_0");
		}
		
		if(values.get(position).isBadge5()){
			Controller.getInstance().setImageOnImageView(this.context, imageView5, "badge_" + values.get(position).getBadgeIconSet()+"_5");
		}else{
			Controller.getInstance().setImageOnImageView(this.context, imageView5, "badge_" + values.get(position).getBadgeIconSet()+"_0");
		}

		return rowView;
	}

}
