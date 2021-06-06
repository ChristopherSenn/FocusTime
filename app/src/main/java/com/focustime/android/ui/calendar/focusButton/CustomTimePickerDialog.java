package com.focustime.android.ui.calendar.focusButton;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.widget.NumberPicker;


public class CustomTimePickerDialog extends TimePickerDialog {

    private final static int TIME_PICKER_INTERVAL = 5;
    private TimePicker mTimePicker;
    private final OnTimeSetListener mTimeSetListener;

    public CustomTimePickerDialog(Context context, OnTimeSetListener listener,
                                  int hourOfDay, int minute, boolean is24HourView) {
        super(context, TimePickerDialog.THEME_HOLO_LIGHT, null, hourOfDay,
                minute / TIME_PICKER_INTERVAL, is24HourView);
        mTimeSetListener = listener;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                if (mTimeSetListener != null) {
                    mTimeSetListener.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(),
                            mTimePicker.getCurrentMinute() * TIME_PICKER_INTERVAL);
                }
                break;
            case BUTTON_NEGATIVE:
                cancel();
                break;
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            mTimePicker  = findViewById(Resources.getSystem().getIdentifier(
                    "timePicker",
                    "id",
                    "android"
            ));

            NumberPicker minutePicker = mTimePicker.findViewById(Resources.getSystem().getIdentifier(
                    "minute",
                    "id",
                    "android"
            ));

            NumberPicker hourPicker = mTimePicker.findViewById(Resources.getSystem().getIdentifier(
                    "hour",
                    "id",
                    "android"
            ));

            mTimePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);

            minutePicker.setMinValue(0);
            minutePicker.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
            List<String> displayedValues = new ArrayList<>();
            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                displayedValues.add(String.format("%02d", i));
            }
            minutePicker.setDisplayedValues(displayedValues
                    .toArray(new String[displayedValues.size()]));

            hourPicker.setMinValue(0);
            hourPicker.setMaxValue(5);
            List<String> displayedValues2 = new ArrayList<>();
            for (int i = 0; i < 6; i ++) {
                displayedValues2.add(String.format("%02d", i));
            }
            hourPicker.setDisplayedValues(displayedValues2
                    .toArray(new String[displayedValues2.size()]));

            //The following code is probably too old so that it can't get the picker
//            Class<?> classForid = Class.forName("com.android.internal.R$id");
//            Field timePickerField = classForid.getField("timePicker");
//            mTimePicker = (TimePicker) findViewById(timePickerField.getInt(null));
//            Field field = classForid.getField("minute");
//
//            NumberPicker minuteSpinner = (NumberPicker) mTimePicker
//                    .findViewById(field.getInt(null));
//            minuteSpinner.setMinValue(0);
//            minuteSpinner.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
//            List<String> displayedValues = new ArrayList<>();
//            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
//                displayedValues.add(String.format("%02d", i));
//            }
//            minuteSpinner.setDisplayedValues(displayedValues
//                    .toArray(new String[displayedValues.size()]));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}