/*
 * Quelle:
 * http://developer.android.com/guide/topics/ui/controls/pickers.html
 */

package com.stuffbox.view.helper;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	
	private EditTextDatePicker editTextPicker = null;
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	    	
        final Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

	@Override
	public void onDateSet(DatePicker datePicker, int year, int month, int day) {
		if (editTextPicker!= null)
			editTextPicker.setText(day + "." + (month + 1) + "." + year);
		this.dismiss();
	}

	public EditTextDatePicker getEditTextPicker() {
		return editTextPicker;
	}

	public void setEditTextPicker(EditTextDatePicker editTextPicker) {
		this.editTextPicker = editTextPicker;
	}
}