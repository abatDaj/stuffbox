package com.stuffbox.view;

import com.stuffbox.controller.Controller;

import android.support.v4.app.DialogFragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class DialogNoticeFragment extends DialogFragment {
	
	/* Festlegung der Callback-Methoden Namen */
	public interface ConfirmDialogListener {
		public void onDialogConirm(DialogFragment dialog);
	}
	
	ConfirmDialogListener	mListener;

	public DialogNoticeFragment() {}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		String question = getArguments().getString(Controller.DIALOG_ARGUMENT_NOTICE_TEXT);
		String yesText = getArguments().getString(Controller.DIALOG_ARGUMENT_YES);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(question).setPositiveButton(yesText, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				mListener.onDialogConirm(DialogNoticeFragment.this);
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
			mListener = (ConfirmDialogListener) activity;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString() + " must implement ConfirmDialogListener");
		}
	}
	
	public static DialogNoticeFragment getADeleteDialog (String yesText, String noticeText)
	{	
		DialogNoticeFragment dialog = new DialogNoticeFragment();
		Bundle args = new Bundle();
	    args.putString(Controller.DIALOG_ARGUMENT_YES, yesText);
	    args.putString(Controller.DIALOG_ARGUMENT_NOTICE_TEXT, noticeText);
	    dialog.setArguments(args);
        return dialog;
	}
}
