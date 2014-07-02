package com.stuffbox.view;


import com.stuffbox.R;
import com.stuffbox.model.Formular;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

public class FormularArrayAdapter extends ArrayAdapter<Formular> {
	private final Context context;
	private final Formular[] values;
	private int mSelectedVariation = -1;
	private Formular currentFormular = null; 


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
private void b(int p){mSelectedVariation = p;};

	public View getCustomView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.row_list_formular, parent, false);
		TextView mainText = (TextView) rowView.findViewById(R.id.cat_row_text1);
		mainText.setText(values[position].getName());
		
		RadioButton radio = (RadioButton) rowView.findViewById(R.id.radio_formular);
	
	    if(position==mSelectedVariation) 
	    	radio.setChecked(true);
	    else 
	    	radio.setChecked(false);
		
        rowView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mSelectedVariation = position;
                FormularArrayAdapter.this.notifyDataSetChanged();
                currentFormular = values[position];
            }
        });
		
		
		
		
		
		return rowView;
	}

	public Formular getCurrentFormular() {
		return currentFormular;
	}
}