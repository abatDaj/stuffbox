package com.stuffbox.view;

import java.util.ArrayList;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Badge;
import com.stuffbox.view.helper.Utility;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.badge_row, parent, false);
		TextView badgeCatText = (TextView) rowView.findViewById(R.id.badge_row_text1);
		ImageView imageView1 = (ImageView) rowView.findViewById(R.id.badge_row_icon1);
		ImageView imageView2 = (ImageView) rowView.findViewById(R.id.badge_row_icon2);
		ImageView imageView3 = (ImageView) rowView.findViewById(R.id.badge_row_icon3);
		ImageView imageView4 = (ImageView) rowView.findViewById(R.id.badge_row_icon4);
		ImageView imageView5 = (ImageView) rowView.findViewById(R.id.badge_row_icon5);
		
		
		((LinearLayout)rowView).removeView(imageView1);
		((LinearLayout)rowView).removeView(imageView2);
		((LinearLayout)rowView).removeView(imageView3);
		((LinearLayout)rowView).removeView(imageView4);
		((LinearLayout)rowView).removeView(imageView5);
		
		LinearLayout ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setPadding(10, 10, 0, 10);
		ll.setLayoutParams(new ListView.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 100));
		
		TextView textView = new TextView (context);
		textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT));
		textView.setGravity(Gravity.CENTER_VERTICAL);
		textView.setText(values.get(position).getCategory().getName() + " (" + values.get(position).getItemcount() + ") ");
		textView.setPadding(20, 0, 0, 0);
		
		ll.addView(textView);

		int drawableId =  values.get(position).getCategory().getIcon().getDrawableId();
		
		ImageView greyPics[] = new ImageView[5];
		
		for (int i = 0; i< greyPics.length; i++)
		{
			greyPics[i] = new ImageView(context);
			greyPics[i].setImageResource(drawableId);
			greyPics[i].setLayoutParams(new LinearLayout.LayoutParams(60,60));
		}
		
		//Set Text
		badgeCatText.setText(values.get(position).getCategory().getName() + " (" + values.get(position).getItemcount() + ") ");
		/*
		 * Select badges
		 */
		try{
			if(values.get(position).isBadge1()){				
				//Controller.getInstance().setImageOnImageView(this.context, imageView1, "badge_" + values.get(position).getBadgeIconSet()+"_1");
				ImageView star1 = Utility.stuffBoxStarIconCloner(context, imageView1,drawableId, 1);
				//((LinearLayout)rowView).addView(star1);
				ll.addView(star1);
				
			}else{
				ll.addView(greyPics[0]);
				Utility.makeImageViewGrey(greyPics[0]);
				//Controller.getInstance().setImageOnImageView(this.context, imageView1, "badge_" + values.get(position).getBadgeIconSet()+"_0");
			}
			}catch(Exception e){
				//Laden des Bildes scheiterte, versuche Default Bild
				try{
					if(values.get(position).isBadge1()){
						Controller.getInstance().setImageOnImageView(this.context, imageView1, "badge_category_icon_default_1");
					}else{
						Utility.makeImageViewGrey(imageView1);
						//Controller.getInstance().setImageOnImageView(this.context, imageView1, "badge_category_icon_default_0");
					}
				}catch(Exception eS){
					//Laden selbst des Defaults scheiterte
				}
			}
		
		try{
			if(values.get(position).isBadge2()){
				//Controller.getInstance().setImageOnImageView(this.context, imageView2, "badge_" + values.get(position).getBadgeIconSet()+"_2");
				ImageView star2 = Utility.stuffBoxStarIconCloner(context, imageView1,drawableId, 2);
				
				
				
				//((LinearLayout)rowView).addView(star2);
				ll.addView(star2);
			}else{
				ll.addView(greyPics[1]);
				Utility.makeImageViewGrey(greyPics[1]);
				//Controller.getInstance().setImageOnImageView(this.context, imageView2, "badge_" + values.get(position).getBadgeIconSet()+"_0");
			}
			}catch(Exception e){
				//Laden des Bildes scheiterte, versuche Default Bild
				try{
					if(values.get(position).isBadge2()){
						Controller.getInstance().setImageOnImageView(this.context, imageView2, "badge_category_icon_default_2");
					}else{
						Utility.makeImageViewGrey(imageView2);
						//Controller.getInstance().setImageOnImageView(this.context, imageView2, "badge_category_icon_default_0");
					}
				}catch(Exception eS){
					//Laden selbst des Defaults scheiterte
				}
			}
		
		try{
			if(values.get(position).isBadge3()){
				//Controller.getInstance().setImageOnImageView(this.context, imageView3, "badge_" + values.get(position).getBadgeIconSet()+"_3");
				ImageView star3 = Utility.stuffBoxStarIconCloner(context, imageView1,drawableId, 3);
				
				//((LinearLayout)rowView).addView(star3);
				ll.addView(star3);
			}else{
				ll.addView(greyPics[2]);
				Utility.makeImageViewGrey(greyPics[2]);
				//Controller.getInstance().setImageOnImageView(this.context, imageView3, "badge_" + values.get(position).getBadgeIconSet()+"_0");
			}
			}catch(Exception e){
				//Laden des Bildes scheiterte, versuche Default Bild
				try{
					if(values.get(position).isBadge3()){
						Controller.getInstance().setImageOnImageView(this.context, imageView3, "badge_category_icon_default_3");
					}else{
						Utility.makeImageViewGrey(imageView3);
						//Controller.getInstance().setImageOnImageView(this.context, imageView3, "badge_category_icon_default_0");
					}
				}catch(Exception eS){
					//Laden selbst des Defaults scheiterte
				}
			}
		
		try{
			if(values.get(position).isBadge4()){
				//Controller.getInstance().setImageOnImageView(this.context, imageView4, "badge_" + values.get(position).getBadgeIconSet()+"_4");
				ImageView star4 = Utility.stuffBoxStarIconCloner(context, imageView1,drawableId, 4);
				
				//((LinearLayout)rowView).addView(star4);
				ll.addView(star4);
			}else{
				ll.addView(greyPics[3]);
				Utility.makeImageViewGrey(greyPics[3]);
				//Controller.getInstance().setImageOnImageView(this.context, imageView4, "badge_" + values.get(position).getBadgeIconSet()+"_0");
			}
			}catch(Exception e){
				//Laden des Bildes scheiterte, versuche Default Bild
				try{
					if(values.get(position).isBadge4()){
						Controller.getInstance().setImageOnImageView(this.context, imageView4, "badge_category_icon_default_4");
					}else{
						Utility.makeImageViewGrey(imageView4);
						//Controller.getInstance().setImageOnImageView(this.context, imageView4, "badge_category_icon_default_0");
					}
				}catch(Exception eS){
					//Laden selbst des Defaults scheiterte
				}
			}
		
		try{
			if(values.get(position).isBadge5()){
				//Controller.getInstance().setImageOnImageView(this.context, imageView5, "badge_" + values.get(position).getBadgeIconSet()+"_5");
				ImageView star5 = Utility.stuffBoxStarIconCloner(context, imageView1,drawableId, 5);
				//((LinearLayout)rowView).addView(star5);
				ll.addView(star5);
			}else{
				ll.addView(greyPics[4]);
				Utility.makeImageViewGrey(greyPics[4]);
				//Controller.getInstance().setImageOnImageView(this.context, imageView5, "badge_" + values.get(position).getBadgeIconSet()+"_0");
			}
			}catch(Exception e){
				//Laden des Bildes scheiterte, versuche Default Bild
				try{
					if(values.get(position).isBadge5()){
						Controller.getInstance().setImageOnImageView(this.context, imageView5, "badge_category_icon_default_5");
					}else{
						Utility.makeImageViewGrey(imageView5);
						//Controller.getInstance().setImageOnImageView(this.context, imageView5, "badge_category_icon_default_0");
					}
				}catch(Exception eS){
					//Laden selbst des Defaults scheiterte
				}
			}
		
		return ll;
	}
}
