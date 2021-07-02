package com.focustime.android.util;

public class TimeFormatter {
    private static final String MIN = "m";
    private static final String HOUR = "h";

    /**
     * Adds a 0 to an Integer if its smaller than 10
     * @param i Input integer
     * @return Formatted String
     */
    public static String formatHourMinute(int i) {
        if(i < 10) return "0"+i;
        return i+"";
    }

    public static String formatDuration (int durationInMinutes) {
        if(durationInMinutes < 60) {
            return durationInMinutes + MIN;
        } else {
            int minutes = durationInMinutes%60;
            String hoursString = durationInMinutes/60 + HOUR;
            if(minutes != 0)
                return hoursString + " " + minutes + MIN;
            else
                return hoursString;
        }
    }
}
