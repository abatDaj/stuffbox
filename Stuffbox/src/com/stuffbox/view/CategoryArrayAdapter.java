package com.stuffbox.view;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Category;
import com.stuffbox.model.Icon;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CategoryArrayAdapter extends ArrayAdapter<Category> {
	private final Context context;
	private final ArrayList<Category> values;

	public CategoryArrayAdapter(Context context, ArrayList<Category> values) {
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
		/*LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.category_row, parent, false);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.cat_row_icon);
		TextView mainText = (TextView) rowView.findViewById(R.id.cat_row_text1);

		Icon icon = values.get(position).getIcon();

		String iconName = icon == null || !icon.getName().startsWith("category_icon_") ? "category_icon_default" : icon.getName();

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
		mainText.setText(values.get(position).getName());
		return rowView;*/
		
		
		LinearLayout rowView = new LinearLayout(context);
		rowView.setOrientation(LinearLayout.HORIZONTAL);
		rowView.setPadding(10, 10, 0, 10);
		
		ImageView imageView  = new ImageView(context);
		imageView.setLayoutParams(new LinearLayout.LayoutParams(80, 80));

		Icon icon = values.get(position).getIcon();
		String iconName = icon == null || !icon.getName().startsWith("category_icon_") ? "category_icon_default" : icon.getName();
		Controller.getInstance().setImageOnImageView(context, imageView, iconName);
		

		TextView mainText  = new TextView(context);
		mainText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT));
		mainText.setPadding(10, 0, 0, 0);
		mainText.setGravity(Gravity.CENTER_VERTICAL);
		mainText.setText(values.get(position).getName());
		rowView.addView(imageView);
		mainText.setTextColor(Color.WHITE);
		rowView.addView(mainText);

		return rowView;
	}
}
