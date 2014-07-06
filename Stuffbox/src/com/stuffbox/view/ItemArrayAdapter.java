package com.stuffbox.view;

import java.util.ArrayList;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Category;
import com.stuffbox.model.Feature;
import com.stuffbox.model.FeatureType;
import com.stuffbox.model.Item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ItemArrayAdapter extends ArrayAdapter<Item> {
	private final Context context;
	private final ArrayList<Item> items;

	public ItemArrayAdapter(Context context, ArrayList<Item> items) {
	    super(context, R.layout.row_item_list_in_a_category, items);
	    this.context = context;
	    this.items = items;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return getCustomView(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return getCustomView(position, convertView, parent);
	}

	public View getCustomView(int position, View convertView, ViewGroup parent) {
		
		LinearLayout rowView = new LinearLayout(context);
		rowView.setOrientation(LinearLayout.HORIZONTAL);
		rowView.setPadding(10, 10, 0, 10);

	
		ArrayList<Feature> features  = items.get(position).getFormular().getFeatures();
		
		// Falls Bild gespeichert und vorhanden darstellen, andernfalls Icon von Kategorie
		// aber eingegraut in der Icon-Liste darstellen.
		
		ArrayList<Feature> featuresImages = new ArrayList<Feature>();
		for (Feature f : features) 
		{
			if(f.getType() == FeatureType.Foto)
			{
				featuresImages.add(f);
				break;
			}
		}
		ImageView imageOfIconInRow = new ImageView(context);
		imageOfIconInRow.setLayoutParams(new LinearLayout.LayoutParams(70, 70));

		
		if (featuresImages.size() == 1) 
		{
			Controller.getInstance().setImageOnImageView(context, imageOfIconInRow, featuresImages.get(0).getValue().toString());
		}
		else
		{
			int drawId = Controller.getInstance().getCurrentCategory().getIcon().getDrawableId();
			imageOfIconInRow.setImageResource(drawId);
		}
		rowView.addView(imageOfIconInRow);
		
		// NAch dem Bild folgt der Name
		TextView iconNameInRow = new TextView(context);
		iconNameInRow.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		iconNameInRow.setText(items.get(position).getName());
		iconNameInRow.setPadding(10, 0, 0, 0);
	
		rowView.addView(iconNameInRow);

		return rowView;
	}
}