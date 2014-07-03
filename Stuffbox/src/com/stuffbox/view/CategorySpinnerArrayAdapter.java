package com.stuffbox.view;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.stuffbox.R;
import com.stuffbox.model.Category;
import com.stuffbox.model.Icon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * Dem Objekt wird bei der Instanzierung ein Category-ArrayList übergeben und das Objekt selber
 * wird beispielsweise einem "Spinner" übergeben. Die Methode getCustomView
 * wird bei jeder Iteration aufgerufen und dort kann jede einzelne Zeile (Spinner-Reihe z.B.)
 * modifziert werden.
 *  
 * */

public class CategorySpinnerArrayAdapter extends ArrayAdapter<Category> {
	private final Context context;
	private final ArrayList<Category> categories;

	public CategorySpinnerArrayAdapter(Context context, ArrayList<Category> categories) {
	    super(context, R.layout.row_spinner_categories_in_item, categories);
	    this.context = context;
	    this.categories = categories;
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
		View rowView = inflater.inflate(R.layout.row_spinner_categories_in_item, parent, false);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.cat_row_icon_in_itemspinner);
		int resId = categories.get(position).getIcon().getDrawableId();
		imageView.setImageResource(resId);
		TextView textView = (TextView) rowView.findViewById(R.id.cat_row_text_in_itemspinner);
		textView.setText(categories.get(position).getName());
		return rowView;
	}
}
