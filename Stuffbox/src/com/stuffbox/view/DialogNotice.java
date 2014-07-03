package com.stuffbox.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.content.DialogInterface;

public class DialogNotice extends DialogFragment {

	private CharSequence message;
	private CharSequence btn_text;
	private AlertDialog.Builder builder;

	public DialogNotice() {
		this.message = "";
		this.btn_text = "";
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		this.builder = new AlertDialog.Builder(getActivity());
		this.builder.setMessage(message).setPositiveButton(btn_text,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

					}

				});

		return this.builder.create();

	}

	public void setMessage(CharSequence message) {
		this.message = message;
	}

	public void setButtonText(CharSequence btn_text) {
		this.btn_text = btn_text;
	}
	
	public void initDialogAttributes(CharSequence message, CharSequence btn_text){
		this.message = message;
		this.btn_text = btn_text;
	}
}
