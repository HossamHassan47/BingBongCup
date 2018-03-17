package com.wordpress.hossamhassan47.bingbongcup.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import com.wordpress.hossamhassan47.bingbongcup.dao.AppDatabase;
import com.wordpress.hossamhassan47.bingbongcup.entities.Player;
import com.wordpress.hossamhassan47.bingbongcup.entities.RoundMatch;
import com.wordpress.hossamhassan47.bingbongcup.entities.TimestampConverter;

import java.util.Calendar;
import java.util.List;

public class SetMatchTimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    NoticeDialogListener mListener;

    int roundMatchId;
    String selectedDate;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        roundMatchId = getArguments().getInt("roundMatchId");
        selectedDate = getArguments().getString("selectedDate");

        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        String matchDate = selectedDate + " " + hourOfDay + ":" + minute +":00";

        AppDatabase db = AppDatabase.getAppDatabase(getActivity());
        RoundMatch roundMatch = db.roundMatchDao().loadRoundMatchById(roundMatchId);
        roundMatch.matchDate = TimestampConverter.fromTimestamp(matchDate);

        if(db.roundMatchDao().updateRoundMatch(roundMatch)>0){
            // Send the positive button event back to the host activity
            mListener.onDialogPositiveClick(SetMatchTimeFragment.this);
        }
    }
}
