package com.stuffbox.view;


import java.util.ArrayList;

import com.stuffbox.R;
import com.stuffbox.model.Feature;
import com.stuffbox.model.Formular;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

public class FeatureRadioArrayAdapter extends ArrayAdapter<Feature> {
	private final Context context;
	private final ArrayList<Feature> values;
	private int selectedVariation = -1;
	private Feature currentFeature = null; 


	public FeatureRadioArrayAdapter(Context context, ArrayList<Feature> values) {
		super(context, R.layout.row_list_formular, values);
		this.context = context;
		this.values = values;
		this.selectedVariation = 0;
		currentFeature = values.get(selectedVariation);
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
		
		View rowView = inflater.inflate(R.layout.row_list_feature, parent, false);
		TextView mainText = (TextView) rowView.findViewById(R.id.feature_row_text);
		mainText.setText(values.get(position).getName());
		
		RadioButton radio = (RadioButton) rowView.findViewById(R.id.radio_feature);
		//selektierten radiobutton anzeigen
		if(position == selectedVariation){
	    	radio.setChecked(true);
	    }
	    else {
	    	radio.setChecked(false);
	    }
		
        rowView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                selectedVariation = position;
                FeatureRadioArrayAdapter.this.notifyDataSetChanged();
        		//Eigenschaft wurde ausgewaehlt
        		currentFeature = values.get(position);
            }
        });
		return rowView;
	}
	
	public Feature getCurrentFeature() {
		return currentFeature;
	}
}