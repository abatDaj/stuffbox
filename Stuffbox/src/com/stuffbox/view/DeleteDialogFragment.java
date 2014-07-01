package com.stuffbox.view;

import com.stuffbox.R;

import android.support.v4.app.DialogFragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class DeleteDialogFragment extends DialogFragment {
	
	/* Festlegung der Callback-Methoden Namen */
	public interface DeleteDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog);
		public void onDialogNegativeClick(DialogFragment dialog);
	}

	DeleteDialogListener	mListener;

	public DeleteDialogFragment() {}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.delete_dialog_category).setPositiveButton(R.string.delete_dialog_yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				mListener.onDialogPositiveClick(DeleteDialogFragment.this);
			}
		}).setNegativeButton(R.string.delete_dialog_no, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// Send the negative button event back to the host activity
				mListener.onDialogNegativeClick(DeleteDialogFragment.this);
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
}
