package com.wordpress.hossamhassan47.bingbongcup.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Hossam on 3/17/2018.
 */

public class SetMatchDateFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    int roundMatchId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        roundMatchId = getArguments().getInt("roundMatchId");

        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {

        // Create and show the dialog.
        Bundle bundle = new Bundle();
        bundle.putInt("roundMatchId", roundMatchId);

        bundle.putString("selectedDate", year + "-" + (month + 1) + "-" + day);

        SetMatchTimeFragment setMatchTimeFragment = new SetMatchTimeFragment();
        setMatchTimeFragment.setArguments(bundle);

        setMatchTimeFragment.show(getFragmentManager(), "dialog_SetRoundMatchTime");
    }
}
