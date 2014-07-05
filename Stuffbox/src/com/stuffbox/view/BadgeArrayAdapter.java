package com.stuffbox.view;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.stuffbox.R;
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
		

		/*
		 * Select badges
		 */
		if(values.get(position).isBadge1()){
			imageView1.setImageResource(R.drawable.category_icon_music_badge1);
		}else{
			imageView1.setImageResource(R.drawable.category_icon_music_badge0);
		}
		
		if(values.get(position).isBadge2()){
			imageView2.setImageResource(R.drawable.category_icon_music_badge2);
		}else{
			imageView2.setImageResource(R.drawable.category_icon_music_badge0);
		}
		
		if(values.get(position).isBadge3()){
			imageView3.setImageResource(R.drawable.category_icon_music_badge3);
		}else{
			imageView3.setImageResource(R.drawable.category_icon_music_badge0);
		}
		
		if(values.get(position).isBadge4()){
			imageView4.setImageResource(R.drawable.category_icon_music_badge4);
		}else{
			imageView4.setImageResource(R.drawable.category_icon_music_badge0);
		}
		
		if(values.get(position).isBadge5()){
			imageView5.setImageResource(R.drawable.category_icon_music_badge5);
		}else{
			imageView5.setImageResource(R.drawable.category_icon_music_badge0);
		}
		//Icon icon1 = values.get(position).getIcon();
		

		//String iconName = icon == null || !icon.getName().startsWith("category_icon_") ? "category_icon_default" : icon.getName();

		/*try {
			// category_icon_default: Der Default-Icon Name (temporär)
			Field f = R.drawable.class.getField(iconName);
			imageView.setImageResource(f.getInt(null));
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		badgeCatText.setText(values.get(position).getCatName());


		return rowView;
	}

}
