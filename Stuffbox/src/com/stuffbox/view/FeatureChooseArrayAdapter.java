package com.stuffbox.view;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stuffbox.R;
import com.stuffbox.model.Feature;

public class FeatureChooseArrayAdapter extends ArrayAdapter<Feature> {
	private final Context context;
	private final ArrayList<Feature> values;
	private ArrayList<Feature> selectedvalues = new ArrayList<Feature>();

	public FeatureChooseArrayAdapter(Context context, ArrayList<Feature> values) {
		super(context, R.layout.category_choose_row, values);
		this.context = context;
		this.values = values;		
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	public View getCustomView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View rowView = inflater.inflate(R.layout.category_choose_row, parent, false);
				
		//zeige Name der Eigenschaft
		TextView mainText = (TextView) rowView.findViewById(R.id.cat_row_text1);
		mainText.setText(values.get(position).getName());
		
		//zeige Icon der Eigenschaft an
		ImageView imageView = (ImageView) rowView.findViewById(R.id.cat_row_icon);
		imageView.setVisibility(LinearLayout.GONE);
		
		CheckBox checkbox = (CheckBox) rowView.findViewById(R.id.cat_row_checkbox7);
		
		// Kategorie, von der man das Item erstellte, sollte gesetzt sein.
		for (Feature feature : selectedvalues){
			if (feature.getId() == values.get(position).getId()){
				checkbox.setChecked(true);	
			}
		}
		
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener(){		
    		public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
    			if(!isChecked){
    				//entferne Eigenschaft aus ausgewaehlten
	    			for(int index = 0; index < selectedvalues.size(); index++){
	    				if (selectedvalues.get(index).getId() == values.get(position).getId()){
		                    selectedvalues.remove(index);
	    				}
	    			}
                }else{
                	//fuege Eigenschaft zu ausgewaehlten hinzu
                	selectedvalues.add(values.get(position));
                }

			}});
		
		return rowView;
	}
	
	/**
	 * Gibt die selektierten Eigenschaft zurueck
	 * @return
	 */
	public ArrayList<Feature> getSelectedFeatures(){
		return selectedvalues;
	}
}
