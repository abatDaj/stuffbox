package com.stuffbox.view;

import java.util.ArrayList;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Category;
import com.stuffbox.model.Icon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoryChooseArrayAdapter extends ArrayAdapter<Category> {
	private final Context context;
	private final ArrayList<Category> values;
	private ArrayList<Category> selectedvalues = new ArrayList<Category>();

	public CategoryChooseArrayAdapter(Context context, ArrayList<Category> values) {
		super(context, R.layout.category_choose_row, values);
		this.context = context;
		this.values = values;		
		
		
		ArrayList<Category> tmp = Controller.getInstance().getSelectedCategoriesInItem();
		if (tmp != null && Controller.getInstance().getSelectedCategoriesInItem().size() > 0)
			selectedvalues = Controller.getInstance().getSelectedCategoriesInItem();
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
				
		//zeige Name der Kategorie
		TextView mainText = (TextView) rowView.findViewById(R.id.cat_row_text1);
		mainText.setText(values.get(position).getName());
		
		//zeige Icon der Kategorie an
		ImageView imageView = (ImageView) rowView.findViewById(R.id.cat_row_icon);
		
		Icon icon = values.get(position).getIcon();
		
		imageView.setImageResource(values.get(position).getIcon().getDrawableId());
		CheckBox checkbox = (CheckBox) rowView.findViewById(R.id.cat_row_checkbox7);
		
		// Kategorie, von der man das Item erstellte, sollte gesetzt sein.
		if (Controller.getInstance().getSelectedCategoriesInItem() == null) {
			if (values.get(position).getId() == Controller.getInstance().getCurrentCategory().getId()){ 
				checkbox.setChecked(true); 
				selectedvalues.add(Controller.getInstance().getCurrentCategory());
			}
		}
		else 
		{
			for (Category c : Controller.getInstance().getSelectedCategoriesInItem()){
				if (c.getId() == values.get(position).getId()){
					checkbox.setChecked(true);	
				}
			}
		}
		
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener(){		
    		public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if(!isChecked){
                	selectedvalues.remove(values.get(position));
                }else{
                	selectedvalues.add(values.get(position));
                }
			}});
		
		return rowView;
	}
	
	/**
	 * Gibt die selektierten Kategorien zurueck
	 * @return
	 */
	public ArrayList<Category> getSelectedCategories(){
		return selectedvalues;
	}

	public void setSelectedgetSelectedCategories(ArrayList<Category> selectedCategories) {
		this.selectedvalues = selectedCategories;
	}
}
