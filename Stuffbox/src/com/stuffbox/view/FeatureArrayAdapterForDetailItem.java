package com.stuffbox.view;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.stuffbox.R;
import com.stuffbox.model.Category;
import com.stuffbox.model.Feature;
import com.stuffbox.model.Icon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FeatureArrayAdapterForDetailItem extends ArrayAdapter<Feature> {
	private final Context context;
	private final ArrayList<Feature> features;

	public FeatureArrayAdapterForDetailItem(Context context, ArrayList<Feature> features) {
		super(context, R.layout.row_detail_item_feature, features);
		this.context = context;
		this.features = features;
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
		View rowView = inflater.inflate(R.layout.row_detail_item_feature, parent, false);
		TextView mainText = (TextView) rowView.findViewById(R.id.featureName);
		mainText.setText(features.get(position).getName());
		//View editFuture = (View) rowView.findViewById(R.id.editFeature);
		return rowView;
	}
}
