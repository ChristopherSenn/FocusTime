package com.focustime.android.util;

import com.focustime.android.data.model.FocusTime;

import java.util.Calendar;

import me.everything.providers.android.calendar.Event;

public class FocusTimeFactory {
    /**
     * Builds a FocusTime from an Event
     * @param event The given Event
     * @return A Focus Time
     */
    public static FocusTime buildFocusTime(Event event) {
        Calendar beginTime = java.util.Calendar.getInstance();
        beginTime.setTimeInMillis(event.dTStart);
        Calendar endTime = java.util.Calendar.getInstance();
        endTime.setTimeInMillis(event.dTend);

        String[] split = event.title.split("#");
        int focusTimeLevel = Integer.parseInt(split[split.length-1]);

        String title = "";
        for (int i = 0; i < split.length-1; i++) {
            title += split[i];
        }

        return new FocusTime(title, beginTime, endTime, focusTimeLevel, event.id);
    }

}
