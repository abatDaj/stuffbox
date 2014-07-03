package com.stuffbox.view;


import java.util.ArrayList;

import com.stuffbox.R;
import com.stuffbox.model.Formular;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

public class FormularArrayAdapter extends ArrayAdapter<Formular> {
	private final Context context;
	private final ArrayList<Formular> values;
	private int selectedVariation = -1;
	private Formular currentFormular = null; 


	public FormularArrayAdapter(Context context, ArrayList<Formular> values) {
		super(context, R.layout.row_list_formular, values);
		this.context = context;
		this.values = values;
		if(values.size() > 0){
			//wenn mind. ein Formular exisitert, setze default das erste als ausgewaehltes
			this.selectedVariation = 1;
			currentFormular = values.get(selectedVariation);
		}
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
	
	//TODO was soll diese Methode machen? und wenn sie was macht, ist b eventuell nicht der beste Name ;)
	private void b(int p){
		selectedVariation = p;
	};

	public View getCustomView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rowView = inflater.inflate(R.layout.row_list_formular, parent, false);
		TextView mainText = (TextView) rowView.findViewById(R.id.formular_row_text);
		mainText.setText(values.get(position).getName());
		
		RadioButton radio = (RadioButton) rowView.findViewById(R.id.radio_formular);
		TextView presymbol = (TextView) rowView.findViewById(R.id.presymbol_formular);
		
		if(values.get(position).getId() == ListFormularActivity.idNewFormularEntry){
			//zeige fuer Eintrag neues Formular erstellen plus statt button an
			radio.setVisibility(LinearLayout.GONE);
		}else{ 
			//zeige fuer alle Formulareintraege button statt plus
			presymbol.setVisibility(LinearLayout.GONE);
			//selektierten radiobutton anzeigen
			if(position == selectedVariation){
		    	radio.setChecked(true);
		    }
		    else {
		    	radio.setChecked(false);
		    }
		}

        rowView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                selectedVariation = position;
                FormularArrayAdapter.this.notifyDataSetChanged();
                
            	if(values.get(position).getId() == ListFormularActivity.idNewFormularEntry){
            		//neues Formular soll angelegt werden
            		
	        		//TODO bisherige Eingabe speichern
	                Intent intent = new Intent();        
	                intent.setClassName(context.getPackageName(), NewFormularActivity.class.getName());
	                ((Activity) context).startActivityForResult(intent, NewFormularActivity.REQUEST_NEW_FORMULAR);
            	}else{
            		//Formular wurde ausgewaehlt
            		currentFormular = values.get(position);
            	}
            }
        });
		return rowView;
	}

	@Override
	public void add(Formular formular) {
		selectedVariation = values.size();
        FormularArrayAdapter.this.notifyDataSetChanged();
        currentFormular = formular;
		super.add(formular);
	}
	
	public Formular getCurrentFormular() {
		return currentFormular;
	}
}