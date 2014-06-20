package com.stuffbox.view;

import com.stuffbox.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class IconArrayAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] values;

	public IconArrayAdapter(Context context, String[] values) {
	    super(context, R.layout.new_category_spinner_row, values);
	    this.context = context;
	    this.values = values;
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
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.new_category_spinner_row, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.new_category_spinner_label);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.new_category_spinner_icon);
		imageView.setImageResource(Integer.parseInt(values[position]));
		textView.setText(values[position]);
		return rowView;
	}
}
