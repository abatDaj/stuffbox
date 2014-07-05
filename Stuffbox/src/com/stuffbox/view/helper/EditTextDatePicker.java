package com.stuffbox.view.helper;


import com.stuffbox.controller.Controller;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;;

public class EditTextDatePicker extends EditText {

	private ActivityWithATimePickerEditText activityWithATimePickerEditText;	
	
	public EditTextDatePicker(Context context, ActivityWithATimePickerEditText activityWithATimePickerEditText) {
		super(context);
		this.activityWithATimePickerEditText = activityWithATimePickerEditText;
		this.setHint("TT.MM.JJJJ");
		this.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		setEms(Controller.NUMBER_CHARS_OF_MOST_EDIT_TEXTS_IN_ICON_SCREEN);
		
		setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
		        InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		        if (imm != null) {
		        	imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		        }
		        showTimePickerDialog();
			}
		});
		setOnTouchListener(new View.OnTouchListener() {

			@Override
		    public boolean onTouch(View v, MotionEvent event) {
				v.onTouchEvent(event);
		        InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		        if (imm != null) {
		        	imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		        }
		        //showTimePickerDialog();
		        return true;
		    }
		});
	}
		
	public void showTimePickerDialog () {
		activityWithATimePickerEditText.showTimePickerDialog(this);
	}


}
