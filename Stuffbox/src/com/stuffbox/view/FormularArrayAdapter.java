package com.stuffbox.view;


import com.stuffbox.R;
import com.stuffbox.model.Formular;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FormularArrayAdapter extends ArrayAdapter<Formular> {
	private final Context context;
	private final Formular[] values;

	public FormularArrayAdapter(Context context, Formular[] values) {
		super(context, R.layout.row_list_formular, values);
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
		View rowView = inflater.inflate(R.layout.category_row, parent, false);
		TextView mainText = (TextView) rowView.findViewById(R.id.cat_row_text1);
		mainText.setText(values[position].getName());
		return rowView;
	}
}