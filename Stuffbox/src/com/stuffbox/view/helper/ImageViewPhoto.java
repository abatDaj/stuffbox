package com.stuffbox.view.helper;

import com.stuffbox.controller.Controller;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;


public class ImageViewPhoto extends ImageView {

	private final ActivityWithATimePickerEditText activityWithATimePickerEditText2;
	private boolean callOnCklick = true;

	public boolean isCallOnCklick() {
		return callOnCklick;
	}

	public void setCallOnCklick(boolean callOnCklick) {
		this.callOnCklick = callOnCklick;
	}

	public ImageViewPhoto(Context context, ActivityWithATimePickerEditText activityWithATimePickerEditText) {
		super(context);
		activityWithATimePickerEditText2 = activityWithATimePickerEditText;
		
//		setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				activityWithATimePickerEditText2.onClickOfImageViewPhoto();
//			}
//		});
	}
}
