package com.stuffbox.view;

import com.stuffbox.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * 
 * Dem Objekt wird bei der Instanzierung ein String-Array übergeben und das Objekt selber
 * wird beispielsweise einem "Spinner" übergeben. Die Methode getCustomView
 * wird bei jeder Iteration aufgerufen und dort kann jede einzelne Zeile (Spinner-Reihe z.B.)
 * modifziert werden. Hier wird dem View-Objekt ein Icon zugewiesen. Das Icon entspricht
 * dem String Namen (R.drawable."XYZ") der jeweiigen Iterationvon "values".
 *  
 * */

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
		ImageView imageView = (ImageView) rowView.findViewById(R.id.new_category_spinner_icon);
		
		// Falls keine Icons gefunden worden sind
		//imageView.setImageResource(R.drawable.category_icon_pets);
		try {
			imageView.setImageResource(Integer.parseInt(values[position]));
		} catch (NumberFormatException e) {
			System.err.println("No category Icons where found");
			e.printStackTrace();
		}
		return rowView;
	}
}
