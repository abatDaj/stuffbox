package com.stuffbox.view;

import java.util.ArrayList;

import com.stuffbox.R;
import com.stuffbox.model.Feature;
import com.stuffbox.view.helper.ActivityWithATimePickerEditText;
import com.stuffbox.view.helper.EditTextDatePicker;

import android.content.Context;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class FeatureArrayAdapterForDetailItem extends ArrayAdapter<Feature> {
	private final Context context;
	private final ArrayList<Feature> features;
	ActivityWithATimePickerEditText activityWithATimePickerEditText;

	public FeatureArrayAdapterForDetailItem(Context context, ArrayList<Feature> features, ActivityWithATimePickerEditText activityWithATimePickerEditText) {
		super(context, R.layout.row_detail_item_feature, features);
		this.context = context;
		this.features = features;
		this.activityWithATimePickerEditText = activityWithATimePickerEditText;
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
		View rowView = inflater.inflate(R.layout.row_detail_item_feature, parent, false);
		
		//setze Name
		TextView mainText = (TextView) rowView.findViewById(R.id.featureName);
		mainText.setText(features.get(position).getName());
		
		//erstelle Inputfeld entsprechend der Art
		final Feature feature = features.get(position);
		
		final TextView editText = (TextView) rowView.findViewById(R.id.editItemText);
		ImageButton editImage = (ImageButton) rowView.findViewById(R.id.editItemImage);
		//TODO restliche Eigeschaften
		
		
		switch (feature.getType()) {
		//Eigenschaft hat Typ Text
		case Text:
			editImage.setVisibility(LinearLayout.GONE);
			editText.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					feature.setValue(editText.getText().toString());
				}
			});
			break;
		//Eigenschaft hat Typ Foto
		case Foto:
			editImage.setImageDrawable(context.getResources().getDrawable( R.drawable.item_photo ));
			editText.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					feature.setValue("Image");
				}
			});
			editText.setVisibility(LinearLayout.GONE);
			break;
		case Datum:
			EditTextDatePicker editTimePicker = new EditTextDatePicker(context, activityWithATimePickerEditText);
			LinearLayout rowViewDate = new LinearLayout(context);
			rowViewDate.setOrientation(LinearLayout.VERTICAL);
			// mainText darf kein parent haben
		    ((ViewGroup)rowView).removeView(mainText);
		    rowViewDate.addView(mainText);
		    rowViewDate.addView(editTimePicker);
			return rowViewDate;
		case Wahrheitswert:
			LinearLayout rowViewBoolean = new LinearLayout(context);
			rowViewBoolean.setOrientation(LinearLayout.VERTICAL);
			((ViewGroup)rowView).removeView(mainText);
			rowViewBoolean.addView(mainText);
			RadioButton rB1 = new RadioButton(context);
			rB1.setText(context.getResources().getString(R.string.delete_dialog_yes));
			RadioButton rB2 = new RadioButton(context);
			rB2.setText(context.getResources().getString(R.string.delete_dialog_no));
			RadioGroup rG = new RadioGroup (context);
			rG.addView(rB1);
			rG.addView(rB2);
			rG.check(rB1.getId());
			rowViewBoolean.addView(rG);
			return rowViewBoolean;
		case Ranking:
			LinearLayout rowViewRanking= new LinearLayout(context);
			rowViewRanking.setOrientation(LinearLayout.VERTICAL);
			((ViewGroup)rowView).removeView(mainText);
			rowViewRanking.addView(mainText);
			LinearLayout rowViewStars= new LinearLayout(context);
			rowViewStars.setOrientation(LinearLayout.HORIZONTAL);
	
			//rowViewRanking.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			for (int i = 0 ; i < 9; i++) {
				ImageView iV = new ImageView(context);
				iV.setImageResource(R.drawable.ranking_star_3);
				iV.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				rowViewStars.addView(iV);
			}
			rowViewRanking.addView(rowViewStars);
			rowViewRanking.setClickable(false);
			rowViewRanking.setOnClickListener(null);
			return rowViewRanking;
		default:
			//editText.setText(features.get(position).getValue().toString());
			//feature.setValue(editText.getText().toString());
			editImage.setVisibility(LinearLayout.GONE);
			editText.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					feature.setValue(editText.getText().toString());
				}
			});
			break;
		}
		//View editFuture = (View) rowView.findViewById(R.id.editFeature);
		return rowView;
	}
}
