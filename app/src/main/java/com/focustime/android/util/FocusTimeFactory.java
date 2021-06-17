package com.focustime.android.util;

import com.focustime.android.data.model.FocusTime;

import java.util.Calendar;

import me.everything.providers.android.calendar.Event;

public class FocusTimeFactory {
    public static FocusTime buildFocusTime(Event event) {
        Calendar beginTime = java.util.Calendar.getInstance();
        beginTime.setTimeInMillis(event.dTStart);
        Calendar endTime = java.util.Calendar.getInstance();
        endTime.setTimeInMillis(event.dTend);

        return new FocusTime(event.title, beginTime, endTime, event.id);
    }

}
