package com.stuffbox.view;

import java.util.ArrayList;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Category;
import com.stuffbox.model.Icon;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
		return getCustomView(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	public View getCustomView(int position, View convertView, ViewGroup parent) {
		LinearLayout rowView = new LinearLayout(context);
		rowView.setOrientation(LinearLayout.HORIZONTAL);
		rowView.setPadding(10, 10, 0, 10);
		
		ImageView imageView  = new ImageView(context);
		//Listengröße entsprechend Display einstellen
		//TODO move to static method
	    int param_in_dp = 80;
	    final float scale = context.getResources().getDisplayMetrics().density;
	    int param_in_px = (int) (param_in_dp * scale + 0.5f);
		imageView.setLayoutParams(new LinearLayout.LayoutParams(param_in_px, param_in_px));

		Icon icon = values.get(position).getIcon();
		String iconName = icon == null || !icon.getName().startsWith("category_icon_") ? "category_icon_default" : icon.getName();
		Controller.getInstance().setImageOnImageView(context, imageView, iconName);
		
		TextView mainText  = new TextView(context);
		mainText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT));
		//TODO check device size
		mainText.setPadding(50, 0, 0, 0);
		mainText.setGravity(Gravity.CENTER_VERTICAL);
		mainText.setText(values.get(position).getName());
		//TODO hübschen font auswählen
		Typeface textfont = Typeface.createFromAsset(context.getAssets(), "fonts/dk_crayon_crumble.ttf");
		mainText.setTypeface(textfont);
		
		//Textgröße entsprechend Display einstellen
	    int textSize_in_dp = 10;  // 6 dps
	    int textSize_in_px = (int) (textSize_in_dp * scale + 0.5f);
	    mainText.setTextSize(textSize_in_px);
		
		rowView.addView(imageView);
		mainText.setTextColor(Color.WHITE);
		rowView.addView(mainText);

		return rowView;
	}
}
