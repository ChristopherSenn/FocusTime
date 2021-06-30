package com.focustime.android.util;

import com.focustime.android.data.model.FocusTime;
import com.focustime.android.ui.calendar.day.DayElement;

import java.util.Calendar;
import java.util.TimeZone;

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

    public static FocusTime buildFocusTimeFromDayElement(DayElement dayElement) {
        Calendar beginTime = java.util.Calendar.getInstance(TimeZone.getTimeZone(getTimeZone()));

        String[] splitDate =  dayElement.getDate().split("-");
        beginTime.set(Integer.parseInt(splitDate[0]), Integer.parseInt(splitDate[1])-1,
                Integer.parseInt(splitDate[2]),
                dayElement.getStartHour(),
                dayElement.getStartMinute());
        beginTime.set(Calendar.SECOND, 0);
        beginTime.set(Calendar.MILLISECOND, 0);

        Calendar endTime = Calendar.getInstance(TimeZone.getTimeZone(getTimeZone()));
        endTime.set(Integer.parseInt(splitDate[0]),
                Integer.parseInt(splitDate[1])-1,
                Integer.parseInt(splitDate[2]), dayElement.getStartHour(),
                dayElement.getStartMinute()+dayElement.getDuration());
        endTime.set(Calendar.SECOND, 0);
        endTime.set(Calendar.MILLISECOND, 0);
        FocusTime f = new FocusTime(dayElement.getTitle(), beginTime, endTime, dayElement.getFocusTimeLevel());

        return f;
    }

    /**
     * Compute the current Time Zone the user is in
     * @return
     */
    private static String getTimeZone() {
        Calendar cal = Calendar.getInstance();
        long milliDiff = cal.get(Calendar.ZONE_OFFSET);
        String [] ids = TimeZone.getAvailableIDs();
        String name = null;
        for (String id : ids) {
            TimeZone tz = TimeZone.getTimeZone(id);
            if (tz.getRawOffset() == milliDiff) {
                // Found a match.
                name = id;
                break;
            }
        }
        return name;
    }

}
