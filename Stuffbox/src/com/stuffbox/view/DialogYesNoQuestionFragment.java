package com.stuffbox.view;

import com.stuffbox.controller.Controller;

import android.support.v4.app.DialogFragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class DialogYesNoQuestionFragment extends DialogFragment {
	
	/* Festlegung der Callback-Methoden Namen */
	public interface DeleteDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog);
		public void onDialogNegativeClick(DialogFragment dialog);
	}
	
	DeleteDialogListener	mListener;

	public DialogYesNoQuestionFragment() {}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		String question = getArguments().getString(Controller.DIALOG_ARGUMENT_YES_NO_QUESTION);
		String yesText = getArguments().getString(Controller.DIALOG_ARGUMENT_YES);
		String noText = getArguments().getString(Controller.DIALOG_ARGUMENT_NO);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(question).setPositiveButton(yesText, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				mListener.onDialogPositiveClick(DialogYesNoQuestionFragment.this);
			}
		}).setNegativeButton(noText, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// Send the negative button event back to the host activity
				mListener.onDialogNegativeClick(DialogYesNoQuestionFragment.this);
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
			mListener = (DeleteDialogListener) activity;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString() + " must implement DeleteDialogListener");
		}
	}
	
	public static DialogYesNoQuestionFragment getADeleteDialog (String yesText, String noText, String question)
	{	
		DialogYesNoQuestionFragment dialog = new DialogYesNoQuestionFragment();
		Bundle args = new Bundle();
	    args.putString(Controller.DIALOG_ARGUMENT_YES, yesText);
	    args.putString(Controller.DIALOG_ARGUMENT_NO, noText);
	    args.putString(Controller.DIALOG_ARGUMENT_YES_NO_QUESTION, question);
	    dialog.setArguments(args);
        return dialog;
	}
}
