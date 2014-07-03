package com.stuffbox.view;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Category;
import com.stuffbox.model.Feature;
import com.stuffbox.model.Icon;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
	
	public View getCustomView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.row_detail_item_feature, parent, false);
		
		//setze Name
		TextView mainText = (TextView) rowView.findViewById(R.id.featureName);
		mainText.setText(features.get(position).getName());
		
		//erstelle Inputfeld entsprechend der Art
		final Feature feature = features.get(position);
		
		final TextView editText = (TextView) rowView.findViewById(R.id.editItemText);
		ImageButton editImage = (ImageButton) rowView.findViewById(R.id.editItemImage);
		//TODO restliche Eigeschaften
		
		
		switch (feature.getType()) {
		//Eigenschaft hat Typ Text
		case Text:
			editImage.setVisibility(LinearLayout.GONE);
			editText.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					feature.setValue(editText.getText().toString());
				}
			});
			break;
		//Eigenschaft hat Typ Foto
		case Foto:
			editImage.setImageDrawable(context.getResources().getDrawable( R.drawable.item_photo ));
			editText.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					feature.setValue("Image");
				}
			});
			editText.setVisibility(LinearLayout.GONE);
			break;
		//TODO restliche Typen
		default:
			//editText.setText(features.get(position).getValue().toString());
			//feature.setValue(editText.getText().toString());
			editImage.setVisibility(LinearLayout.GONE);
			editText.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					feature.setValue(editText.getText().toString());
				}
			});
			break;
		}
		//View editFuture = (View) rowView.findViewById(R.id.editFeature);
		return rowView;
	}
}
