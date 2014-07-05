package com.stuffbox.view;

import java.util.ArrayList;

import com.stuffbox.R;
import com.stuffbox.model.Item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.row_item_list_in_a_category, parent, false);
		
		
		TextView itemNameTextView = (TextView) rowView.findViewById(R.id.nameOfItemInACategory);
		itemNameTextView.setText(items.get(position).getName());

		return rowView;
	}
}
