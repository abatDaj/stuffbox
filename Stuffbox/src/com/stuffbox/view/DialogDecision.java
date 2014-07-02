package com.stuffbox.view;

import com.stuffbox.view.DeleteDialogFragment.DeleteDialogListener;

import android.app.AlertDialog;
import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.content.DialogInterface;

public class DialogDecision extends DialogFragment {

	//private AlertDialog.Builder builder;
	private CharSequence message;
	private CharSequence btn_text_pos; // Text positive Button
	private CharSequence btn_text_neg; // Text negative Button

	public interface DialogDecisionListener {
		public void onDialogPositiveClick(DialogFragment dialog);
		public void onDialogNegativeClick(DialogFragment dialog);
	}

	DialogDecisionListener	mListener;
	
	public DialogDecision() {
		this.message = "";
		this.btn_text_pos = "";
		this.btn_text_neg = "";

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder
				.setMessage(message)
				.setNegativeButton(btn_text_neg,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								mListener.onDialogNegativeClick(DialogDecision.this);
							}

						})
				.setPositiveButton(btn_text_pos,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								mListener.onDialogPositiveClick(DialogDecision.this);
							}

						});

		return builder.create();

	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Sicherstellung, dass die Callback-Methode implementiert ist
		try {
			// Instantiate the NoticeDialogListener so we can send events to the
			// host
			mListener = (DialogDecisionListener) activity;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString() + " must implement DialogDecisionListener");
		}
	}

	public void initDialogAttributes(CharSequence message,
			CharSequence btn_text_pos, CharSequence btn_text_neg) {
		this.message = message;
		this.btn_text_pos = btn_text_pos;
		this.btn_text_neg = btn_text_neg;

	}
}
