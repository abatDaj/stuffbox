package com.stuffbox.view;

import java.util.ArrayList;

import android.content.Context;
import android.text.InputType;
import android.text.format.Time;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Feature;
import com.stuffbox.view.helper.ActivityWithATimePickerEditText;
import com.stuffbox.view.helper.EditTextDatePicker;
import com.stuffbox.view.helper.ImageViewPhoto;

public class FeatureArrayAdapterForDetailItem extends ArrayAdapter<Feature> {
	
	private final Context context;
	private final ArrayList<Feature> features;
	private ActivityWithATimePickerEditText activityWithATimePickerEditText;
	private boolean editable = false;
	
	//private static final String TAG = FeatureArrayAdapterForDetailItem.class.getSimpleName();
	
	public FeatureArrayAdapterForDetailItem(Context context, ArrayList<Feature> features, ActivityWithATimePickerEditText activityWithATimePickerEditText) {
		super(context, R.layout.row_empty, features);
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
		
		//erstelle Inputfeld entsprechend der Art
		final Feature feature = features.get(position);
		
		LinearLayout rowView= new LinearLayout(context);
		rowView.setOrientation(LinearLayout.VERTICAL);
		rowView.setPadding(15, 15, 0, 15);
		
		// Der Eigenschaftsname (über dem Eingabefeld)
		TextView featureNameTextView = new TextView (context);
		featureNameTextView.setText(features.get(position).getName());
		rowView.addView(featureNameTextView);

		/*
		 *  Je nachdem um welche Eigenschaft es sich handelt (FeatureType) wird
		 *  ein individuelles Input-Feld, Picker, Check-Liste, etc. erstellt.
		 */ 
		switch (feature.getType()) {
		//Eigenschaft hat Typ Text
		case Text:	
			buildTextEdit(rowView, feature);
			break;
		case Ganzzahl:
			buildIntegerEdit(rowView, feature);
			break;
		case Dezimalzahl:
			buildDecimalEdit(rowView, feature);
			break;
		//Eigenschaft hat Typ Foto
		case Foto:
			buildImageEdit(rowView, feature);
			break;
		//Eigenschaft hat Typ Datum
		case Datum:
			buildDateEdit(rowView, feature);
			break;
		case Wahrheitswert:
			buildBooleanEdit(rowView, feature);
			break;
		case Ranking:
			buildRankingEdit(rowView, feature);
			break;
		default:
			buildTextEdit(rowView, feature);
			break;
		}
		return rowView;
	}
	
	/**
	 * Erstellt den Eingabebereich fuer Eigenschaften mit der Art Text
	 * @param editText
	 * @param feature
	 */
	private void buildTextEdit(LinearLayout rowView, final Feature feature){
		final EditText editNormalText= new EditText(context);
		editNormalText.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		editNormalText.setEms(Controller.NUMBER_CHARS_OF_LARGER_EDIT_TEXTS_IN_ICON_SCREEN);

		//Textfarbe setzen, damit Texte auch im Aenderungsmodus lesbar sind
		editNormalText.setTextColor(context.getResources().getColor(R.drawable.selector_item_fields));		
		
		//setze Wert wenn moeglich
		if(feature.getValue() == null){
			feature.setValue("");
		}
		editNormalText.setText(feature.getValue().toString());
		
		//setze Listener um Werte zu speichern
		editNormalText.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				feature.setValue(editNormalText.getText().toString());
			}
		});
		
		editNormalText.setEnabled(editable);
		rowView.addView(editNormalText);
	}
	
	/**
	 * Erstellt den Eingabebereich fuer Eigenschaften mit der Art Bewertung
	 * @param rowView
	 * @param mainText
	 * @param feature
	 * @return
	 */
	private void buildIntegerEdit(LinearLayout rowView, final Feature feature){	
		
		final EditText editGanzahl= new EditText(context);
		// InputType = Zahlen + Minuswerte möglich
		editGanzahl.setInputType(InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_SIGNED);
		editGanzahl.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		editGanzahl.setEms(Controller.NUMBER_CHARS_OF_MOST_EDIT_TEXTS_IN_ICON_SCREEN);
		
		//Textfarbe setzen, damit Texte auch im Aenderungsmodus lesbar sind
	    editGanzahl.setTextColor(context.getResources().getColor(R.drawable.selector_item_fields));
		
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
		rowView.addView(editGanzahl);
	}	
	
	/**
	 * Erstellt den Eingabebereich fuer Eigenschaften mit der Art Bewertung
	 * @param rowView
	 * @param mainText
	 * @param feature
	 * @return
	 */
	private void buildDecimalEdit(LinearLayout rowView, final Feature feature){
		
		final EditText editDezimalzahl= new EditText(context);
		// InputType = Zahlen + Dezimalzahlen + Minuswerte moeglich
		editDezimalzahl.setInputType(InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL + InputType.TYPE_NUMBER_FLAG_SIGNED);
		editDezimalzahl.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		editDezimalzahl.setEms(Controller.NUMBER_CHARS_OF_MOST_EDIT_TEXTS_IN_ICON_SCREEN);
		
		//Textfarbe setzen, damit Texte auch im Aenderungsmodus lesbar sind
	    editDezimalzahl.setTextColor(context.getResources().getColor(R.drawable.selector_item_fields));
		
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
		
		rowView.addView(editDezimalzahl);
	}
	
	/**
	 * Erstellt den Eingabebereich fuer Eigenschaften mit der Art Bild
	 * @param editImage
	 * @param feature
	 */
	private void buildImageEdit(LinearLayout rowView, final Feature feature){
		
		final ImageViewPhoto imageViewPhoto = new ImageViewPhoto(context, activityWithATimePickerEditText);
		this.activityWithATimePickerEditText.setPhotoImageView(imageViewPhoto);
		
		String pictureName;
		// Das oder ist notwendig weil über die Feature-Liste anscheinend zweimal iteriert wird (
		// (warum auch immer).
		if(feature.getValue() == null || feature.getValue().equals(Controller.DEFAULT_ICON_VALUE_FOR_PICTURE))
		{	
			feature.setValue(Controller.DEFAULT_ICON_VALUE_FOR_PICTURE);
			pictureName = Controller.getInstance().getCurrentCategory().getIcon().getName();
			Controller.getInstance().setImageOnImageView(context, imageViewPhoto, pictureName);
			imageViewPhoto.setTag(Controller.DEFAULT_ICON_VALUE_FOR_PICTURE); //TODO Anweisung vll. notwendig ?!?
		 }
		else if (feature.getValue().toString().equals(Controller.DEFAULT_ICON_VALUE_FOR_PICTURE) == false)
		{
			// Foto wird angezeigt, falls vorhanden.
			activityWithATimePickerEditText.showFinallyRealCapturedPhoto( feature.getValue().toString());
		}
		
		// Entweder wird ein Foto geschossen (if-Block) oder
		// der User hat auf "Iem-Speichern" geklickt (else-Block)
		imageViewPhoto.setOnClickListener(new OnClickListener() {
		@Override
			public void onClick(View v) {
				if (imageViewPhoto.isCallOnCklick()) // Foto schießen
				{
					String randomNumber = String.valueOf(Math.random()).substring(2, 6);
					String fileNameOfPhoto = Controller.getInstance().getCurrentCategory().getName() + "_" + randomNumber;
					activityWithATimePickerEditText.onClickOfImageViewPhoto(fileNameOfPhoto);
				}
				else // speichern 
				{
					feature.setValue(((String)imageViewPhoto.getTag())); // der TAG ist der Datei-Name
				}
			}
		});		
		imageViewPhoto.setEnabled(editable);
		rowView.addView(imageViewPhoto);
	}
	
	/**
	 * Erstellt den Eingabebereich fuer Eigenschaften mit der Art Datum
	 * @param rowView
	 * @param mainText
	 * @param feature
	 * @return
	 */
	private void buildDateEdit(LinearLayout rowView, final Feature feature){	
		final EditTextDatePicker editTimePicker = new EditTextDatePicker(context, activityWithATimePickerEditText);
		
		//Textfarbe setzen, damit Texte auch im Aenderungsmodus lesbar sind
	    editTimePicker.setTextColor(context.getResources().getColor(R.drawable.selector_item_fields));
	    
		//setze Wert wenn moeglich sonst Defaultwert
		if (feature.getValue() == null) {		
			//initial value
			Time now = new Time();
			now.setToNow();
			feature.setValue(now.format("%d.%m.%Y"));
			
		}
		
		editTimePicker.setText(feature.getValue().toString());
		
	 	//setze Listener um Werte zu speichern
	    editTimePicker.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				feature.setValue(editTimePicker.getText().toString());
			}
		});
	    editTimePicker.setEnabled(editable);
	    rowView.addView(editTimePicker);
	}
	
	/**
	 * Erstellt den Eingabebereich fuer Eigenschaften mit der Art Wahrheitswert
	 * 
	 * @param rowView
	 * @param mainText
	 * @param feature
	 * @return
	 */
	private void buildBooleanEdit(LinearLayout rowView, final Feature feature){

		final CheckBox checkBox = new CheckBox(context);
		
		//Textfarbe setzen, damit Texte auch im Aenderungsmodus lesbar sind
	    checkBox.setButtonDrawable(context.getResources().getDrawable(R.drawable.selector_item_checkbox));
		
		//setze Wert wenn moeglich sonst Defaultwert
		if (feature.getValue() == null) {		
			//initial value
			feature.setValue(false);
		}
		checkBox.setChecked((Boolean) feature.getValue());
		
		//setze Listener um Werte zu speichern
		checkBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				feature.setValue(checkBox.isChecked());
			}
		});
		
		checkBox.setEnabled(editable);
		rowView.addView(checkBox);
	}
	
	/**
	 * Erstellt den Eingabebereich fuer Eigenschaften mit der Art Bewertung
	 * @param rowView
	 * @param feature
	 */
	private void buildRankingEdit(LinearLayout rowView, final Feature feature){

		LinearLayout rowViewStars= new LinearLayout(context);
		rowViewStars.setOrientation(LinearLayout.HORIZONTAL);
		rowView.setPadding(15, 30, 0, 30);

		
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
		
		//setze Wert wenn moeglich sonst Defaultwert
		if (feature.getValue() == null) {		
			//initial value
			feature.setValue(Controller.DEFAULT_RANKING_VALUE);
		}
		setStarsForRanking((Integer) feature.getValue() - 1, rowViewStars);
		
		rowView.addView(rowViewStars);
		rowView.setClickable(false);
		rowView.setOnClickListener(null);	
	}
	
	private void setStarsForRanking(int indexInLayout, LinearLayout rowViewStars){
		for (int i = 0; i < Controller.NUMBER_STARS_OF_RANKING; i++) {
			ImageView star = ((ImageView)rowViewStars.getChildAt(i));
			if (i <= indexInLayout) 
			{
				star.setImageResource(R.drawable.ranking_star_3);
			}
			else 
			{
				star.setImageResource(R.drawable.ranking_star_4);
			}
		}
	}
}