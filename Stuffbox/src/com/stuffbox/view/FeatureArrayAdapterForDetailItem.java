package com.stuffbox.view;

import java.util.ArrayList;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Feature;
import com.stuffbox.view.helper.ActivityWithATimePickerEditText;
import com.stuffbox.view.helper.EditTextDatePicker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.text.InputType;
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
			LinearLayout rowViewText= new LinearLayout(context);
			rowViewText.setOrientation(LinearLayout.VERTICAL);
			((ViewGroup)rowView).removeView(mainText);
			rowViewText.addView(mainText);
			EditText editNormalText= new EditText(context);
			// InputType = Zahlen + Dezimalzahlen + Minuswerte möglich
			editNormalText.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			editNormalText.setEms(Controller.NUMBER_CHARS_OF_MOST_EDIT_TEXTS_IN_ICON_SCREEN * 2);
			rowViewText.addView(editNormalText);
			return rowViewText;
		case Foto:
			editImage.setImageDrawable(context.getResources().getDrawable( R.drawable.item_photo ));
			editText.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					feature.setValue("Image");
				}
			});
			editText.setVisibility(LinearLayout.GONE);
			break;
		case Dezimalzahl:
			LinearLayout rowViewDezimalzahl= new LinearLayout(context);
			rowViewDezimalzahl.setOrientation(LinearLayout.VERTICAL);
			((ViewGroup)rowView).removeView(mainText);
			rowViewDezimalzahl.addView(mainText);
			EditText editDezimalzahl= new EditText(context);
			// InputType = Zahlen + Dezimalzahlen + Minuswerte möglich
			editDezimalzahl.setInputType(InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL + InputType.TYPE_NUMBER_FLAG_SIGNED);
			editDezimalzahl.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			editDezimalzahl.setEms(Controller.NUMBER_CHARS_OF_MOST_EDIT_TEXTS_IN_ICON_SCREEN);
			rowViewDezimalzahl.addView(editDezimalzahl);
			return rowViewDezimalzahl;
		case Ganzzahl:
			LinearLayout rowViewGanzzahl= new LinearLayout(context);
			rowViewGanzzahl.setOrientation(LinearLayout.VERTICAL);
			((ViewGroup)rowView).removeView(mainText);
			rowViewGanzzahl.addView(mainText);
			EditText editGanzahl= new EditText(context);
			// InputType = Zahlen + Minuswerte möglich
			editGanzahl.setInputType(InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_SIGNED);
			editGanzahl.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			editGanzahl.setEms(Controller.NUMBER_CHARS_OF_MOST_EDIT_TEXTS_IN_ICON_SCREEN);
			rowViewGanzzahl.addView(editGanzahl);
			return rowViewGanzzahl;
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
			/*
			 * 9 Sterne werden in den horizontalen Layout hinzugefügt und jedem
			 * wird ein onCklickListener gegeben, der je nach angeklickter Position
			 * die Sterne farbig oder eingegraut anzeigt.
			 */
			for (int i = 0 ; i < 9; i++) {
				ImageView iV = new ImageView(context);
				iV.setOnTouchListener(new View.OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						LinearLayout rowViewStarsAgain = (LinearLayout)v.getParent();
						int indexInLayout = rowViewStarsAgain.indexOfChild(v);
						for (int i = 0; i < Controller.NUMBER_STARS_OF_RANKING; i++) {
							ImageView star = ((ImageView)rowViewStarsAgain.getChildAt(i));
							if (i <= indexInLayout) 
							{
								star.setImageResource(R.drawable.ranking_star_3);
								// TODO setColorFilter vermutlich besser
								//star.setColorFilter(Color.rgb(Color.BLACK, Color.BLACK, Color.BLACK), android.graphics.PorterDuff.Mode.MULTIPLY);
							}
							else 
							{
								star.setImageResource(R.drawable.ranking_star_4);
							}
						}						return false;
					}
				});
				iV.setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View v) {
						LinearLayout rowViewStarsAgain = (LinearLayout)v.getParent();
						int indexInLayout = rowViewStarsAgain.indexOfChild(v);
						for (int i = 0; i < Controller.NUMBER_STARS_OF_RANKING; i++) {
							ImageView star = ((ImageView)rowViewStarsAgain.getChildAt(i));
							if (i <= indexInLayout) 
							{
								star.setImageResource(R.drawable.ranking_star_3);
								// TODO setColorFilter vermutlich besser
								//star.setColorFilter(Color.rgb(Color.BLACK, Color.BLACK, Color.BLACK), android.graphics.PorterDuff.Mode.MULTIPLY);
							}
							else 
							{
								star.setImageResource(R.drawable.ranking_star_4);
							}
						}
						//((ImageView)v).setImageResource(R.drawable.ranking_star_4);
					}});
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
