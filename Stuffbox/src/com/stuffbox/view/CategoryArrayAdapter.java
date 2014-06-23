package com.stuffbox.view;

import java.lang.reflect.Field;

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

public class CategoryArrayAdapter extends ArrayAdapter<Category> {
	private final Context context;
	private final Category[] values;

	public CategoryArrayAdapter(Context context, Category[] values) {
		super(context, R.layout.category_row, values);
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
		ImageView imageView = (ImageView) rowView.findViewById(R.id.cat_row_icon);
		TextView mainText = (TextView) rowView.findViewById(R.id.cat_row_text1);

		Icon icon = values[position].getIcon();

		String iconName = icon == null ? "category_icon_default" : icon.getName();

		try {
			// category_icon_default: Der Default-Icon Name (temporär)
			Field f = R.drawable.class.getField(iconName);
			imageView.setImageResource(f.getInt(null));
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mainText.setText(values[position].getName());


		return rowView;
	}
}
