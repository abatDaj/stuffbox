package com.stuffbox.view.helper;

import android.content.Context;
import android.widget.ImageView;


public class ImageViewPhoto extends ImageView {

	private boolean callOnCklick = true;

	public boolean isCallOnCklick() {
		return callOnCklick;
	}

	public void setCallOnCklick(boolean callOnCklick) {
		this.callOnCklick = callOnCklick;
	}

	public ImageViewPhoto(Context context) {
		super(context);
	}
}
