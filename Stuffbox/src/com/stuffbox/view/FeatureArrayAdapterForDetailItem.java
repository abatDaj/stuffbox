package com.stuffbox.view;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Feature;
import com.stuffbox.view.helper.ActivityWithATimePickerEditText;
import com.stuffbox.view.helper.EditTextDatePicker;

public class FeatureArrayAdapterForDetailItem extends ArrayAdapter<Feature> {
	
	private final Context context;
	private final ArrayList<Feature> features;
	private ActivityWithATimePickerEditText activityWithATimePickerEditText;
	private boolean editable = false;
	
	public FeatureArrayAdapterForDetailItem(Context context, ArrayList<Feature> features, ActivityWithATimePickerEditText activityWithATimePickerEditText) {
		super(context, R.layout.row_detail_item_feature, features);
		this.context = context;
		this.features = features;
		this.activityWithATimePickerEditText = activityWithATimePickerEditText;
		
	}
	/**
	 * Setzt ob die Elemente eingabebereit sind
	 * @param editable
	 */
	public void setEditable(boolean editable){
		this.editable = editable;
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
		
		
		switch (feature.getType()) {
		//Eigenschaft hat Typ Text
		case Text:	
			rowView = buildTextEdit(rowView, mainText, feature);
			break;
		//Eigenschaft hat Typ Foto
		case Foto:
			editText.setVisibility(LinearLayout.GONE);
			buildImageEdit(editImage, feature);
			break;
		case Dezimalzahl:
			rowView = buildDecimalEdit(rowView, mainText, feature);
			break;
		case Ganzzahl:
			rowView = buildIntegerEdit(rowView, mainText, feature);
			break;
		//Eigenschaft hat Typ Datum
		case Datum:
			rowView = buildDateEdit(rowView, mainText, feature);
			break;
		case Wahrheitswert:
			rowView = buildBooleanEdit(rowView, mainText, feature);
			break;
		case Ranking:
			rowView = buildRankingEdit(rowView, mainText, feature);
			break;
		default:
			rowView = buildTextEdit(rowView, mainText, feature);
			break;
		}
		return rowView;
	}
	
	/**
	 * Erstellt den Eingabebereich fuer Eigenschaften mit der Art Text
	 * @param editText
	 * @param feature
	 */
	private LinearLayout buildTextEdit(View rowView, TextView mainText, final Feature feature){
		LinearLayout rowViewText= new LinearLayout(context);
		rowViewText.setOrientation(LinearLayout.VERTICAL);
		((ViewGroup)rowView).removeView(mainText);
		rowViewText.addView(mainText);
		final EditText editNormalText= new EditText(context);
		editNormalText.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		editNormalText.setEms(Controller.NUMBER_CHARS_OF_MOST_EDIT_TEXTS_IN_ICON_SCREEN * 2);
		rowViewText.addView(editNormalText);
		
		//setze Wert wenn moeglich
		if(feature.getValue() != null){
			feature.setValue("item_photo");
			editNormalText.setText(feature.getValue().toString());
		}
		
		//setze Listener um Werte zu speichern
		editNormalText.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				feature.setValue(editNormalText.getText().toString());
			}
		});
		
		editNormalText.setEnabled(editable);
		
		return rowViewText;
	}
	/**
	 * Erstellt den Eingabebereich fuer Eigenschaften mit der Art Bild
	 * @param editImage
	 * @param feature
	 */
	private void buildImageEdit(final ImageButton editImage, final Feature feature){
		
		//setze Wert wenn moeglich sonst Defaultwert
		String pictureName;
		if(feature.getValue() == null){
			pictureName = "item_photo";
			feature.setValue(pictureName);
		}else{
			pictureName = feature.getValue().toString();
		}
		Controller.getInstance().setImageOnImageView(context, editImage, pictureName);
		editImage.setTag(pictureName);
		
		//setze Listener um Werte zu speichern
		editImage.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				feature.setValue(((ImageButton) v).getTag());
			}
		});
		
		editImage.setEnabled(editable);
	}
	/**
	 * Erstellt den Eingabebereich fuer Eigenschaften mit der Art Datum
	 * @param rowView
	 * @param mainText
	 * @param feature
	 * @return
	 */
	private LinearLayout buildDateEdit(View rowView, TextView mainText, final Feature feature){	
		final EditTextDatePicker editTimePicker = new EditTextDatePicker(context, activityWithATimePickerEditText);
		LinearLayout rowViewDate = new LinearLayout(context);
		rowViewDate.setOrientation(LinearLayout.VERTICAL);
		// mainText darf kein parent haben
	    ((ViewGroup)rowView).removeView(mainText);
	    rowViewDate.addView(mainText);
	    rowViewDate.addView(editTimePicker);
		
		//setze Wert wenn moeglich sonst Defaultwert
		if (feature.getValue() == null) {		
			//initial value
			Time now = new Time();
			now.setToNow();
			feature.setValue(now.toString());
		}
		
		editTimePicker.setText(feature.getValue().toString());
	    
	 	//setze Listener um Werte zu speichern
	    editTimePicker.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				feature.setValue(editTimePicker.getText().toString());
			}
		});
	    
	    editTimePicker.setEnabled(editable);
	    
		return rowViewDate;
	}
	/**
	 * Erstellt den Eingabebereich fuer Eigenschaften mit der Art Wahrheitswert
	 * @param rowView
	 * @param mainText
	 * @param feature
	 * @return
	 */
	private LinearLayout buildBooleanEdit(View rowView, TextView mainText, final Feature feature){

		LinearLayout rowViewBoolean = new LinearLayout(context);
		rowViewBoolean.setOrientation(LinearLayout.VERTICAL);
		((ViewGroup)rowView).removeView(mainText);
		rowViewBoolean.addView(mainText);

		final CheckBox checkBox = new CheckBox(context);
		rowViewBoolean.addView(checkBox);
		
		//setze Wert wenn moeglich sonst Defaultwert
		if (feature.getValue() == null) {		
			//initial value
			feature.setValue(false);
		}
		checkBox.setSelected((Boolean) feature.getValue());
		
		//setze Listener um Werte zu speichern
		checkBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				feature.setValue(checkBox.isSelected());
			}
		});
		
		checkBox.setEnabled(editable);
		
		return rowViewBoolean;
	}
	/**
	 * Erstellt den Eingabebereich fuer Eigenschaften mit der Art Bewertung
	 * @param rowView
	 * @param mainText
	 * @param feature
	 * @return
	 */
	private LinearLayout buildDecimalEdit(View rowView, TextView mainText, final Feature feature){
		LinearLayout rowViewDecimalzahl= new LinearLayout(context);
		rowViewDecimalzahl.setOrientation(LinearLayout.VERTICAL);
		((ViewGroup)rowView).removeView(mainText);
		rowViewDecimalzahl.addView(mainText);
		
		final EditText editDezimalzahl= new EditText(context);
		// InputType = Zahlen + Dezimalzahlen + Minuswerte moeglich
		editDezimalzahl.setInputType(InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL + InputType.TYPE_NUMBER_FLAG_SIGNED);
		editDezimalzahl.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		editDezimalzahl.setEms(Controller.NUMBER_CHARS_OF_MOST_EDIT_TEXTS_IN_ICON_SCREEN);
		rowViewDecimalzahl.addView(editDezimalzahl);
		
		//setze Wert wenn moeglich sonst Defaultwert
		if (feature.getValue() == null) {		
			//initial value
			feature.setValue(0);
		}
		editDezimalzahl.setText(feature.getValue().toString());
		
		//setze Listener um Werte zu speichern
		editDezimalzahl.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				feature.setValue(editDezimalzahl.getText().toString());
			}
		});
		
		editDezimalzahl.setEnabled(editable);
		
		return rowViewDecimalzahl;
	}
	/**
	 * Erstellt den Eingabebereich fuer Eigenschaften mit der Art Bewertung
	 * @param rowView
	 * @param mainText
	 * @param feature
	 * @return
	 */
	private LinearLayout buildIntegerEdit(View rowView, TextView mainText, final Feature feature){	
		LinearLayout rowViewInteger= new LinearLayout(context);
		rowViewInteger.setOrientation(LinearLayout.VERTICAL);
		((ViewGroup)rowView).removeView(mainText);
		rowViewInteger.addView(mainText);
		
		final EditText editGanzahl= new EditText(context);
		// InputType = Zahlen + Minuswerte möglich
		editGanzahl.setInputType(InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_SIGNED);
		editGanzahl.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		editGanzahl.setEms(Controller.NUMBER_CHARS_OF_MOST_EDIT_TEXTS_IN_ICON_SCREEN);
		rowViewInteger.addView(editGanzahl);
		
		//setze Wert wenn moeglich sonst Defaultwert
		if (feature.getValue() == null) {		
			//initial value
			feature.setValue(0);
		}
		editGanzahl.setText(feature.getValue().toString());
		
		//setze Listener um Werte zu speichern
		editGanzahl.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				feature.setValue(editGanzahl.getText().toString());
			}
		});
		
		editGanzahl.setEnabled(editable);
		
		return rowViewInteger;
	}
	/**
	 * Erstellt den Eingabebereich fuer Eigenschaften mit der Art Bewertung
	 * @param rowView
	 * @param mainText
	 * @param feature
	 * @return
	 */
	private LinearLayout buildRankingEdit(View rowView, TextView mainText, final Feature feature){

		LinearLayout rowViewRanking= new LinearLayout(context);
		rowViewRanking.setOrientation(LinearLayout.VERTICAL);
		((ViewGroup)rowView).removeView(mainText);
		rowViewRanking.addView(mainText);
		LinearLayout rowViewStars= new LinearLayout(context);
		rowViewStars.setOrientation(LinearLayout.HORIZONTAL);
		
		if (feature.getValue() == null){
			feature.setValue(Controller.DEFAULT_RANKING_VALUE);
		}
		
		/*
		 * 9 Sterne werden in den horizontalen Layout hinzugefügt und jedem
		 * wird ein onCklickListener gegeben, der je nach angeklickter Position
		 * die Sterne farbig oder eingegraut anzeigt.
		 */
		
		// TODO Aeussere Vorschleife dazu bringen Initial-Value darzustellen
		for (int i = 0 ; i < 9; i++) {
			ImageView imageview = new ImageView(context);
			imageview.setOnTouchListener(new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					LinearLayout rowViewStarsAgain = (LinearLayout)v.getParent();
					int indexInLayout = rowViewStarsAgain.indexOfChild(v);
					setStarsForRanking(indexInLayout, rowViewStarsAgain);
					//save value
					feature.setValue(indexInLayout + 1);	
					return false;
				}
			});
			imageview.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					LinearLayout rowViewStarsAgain = (LinearLayout)v.getParent();
					int indexInLayout = rowViewStarsAgain.indexOfChild(v);
					setStarsForRanking(indexInLayout, rowViewStarsAgain);
					//save value
					feature.setValue(indexInLayout + 1);
				}
			});
			imageview.setImageResource(R.drawable.ranking_star_3);
			imageview.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			
			imageview.setEnabled(editable);
			
			rowViewStars.addView(imageview);
		}
		rowViewRanking.addView(rowViewStars);
		rowViewRanking.setClickable(false);
		rowViewRanking.setOnClickListener(null);
		
		//setze Wert wenn moeglich sonst Defaultwert
		if (feature.getValue() == null) {		
			//initial value
			feature.setValue(Controller.DEFAULT_RANKING_VALUE);
		}
		setStarsForRanking((Integer) feature.getValue(), rowViewStars);
		
		return rowViewRanking;		
	}
	
	private void setStarsForRanking(int indexInLayout, LinearLayout rowViewStars){
		for (int i = 0; i < Controller.NUMBER_STARS_OF_RANKING; i++) {
			ImageView star = ((ImageView)rowViewStars.getChildAt(i));
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
	}
}
