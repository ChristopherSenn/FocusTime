package com.focustime.android.data.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class FocusTime {
    public static long UNDEFINED_ID = -1;

    public static final List<String> FOCUS_TIME_LEVELS = Arrays.asList("Priority only", "Alarms only", "Total Silence");

    private Calendar beginTime, endTime;
    private String title;
    private int focusTimeLevel;
    private long id;

    /**
     * The default Constructor for creating custom FocusTimes
     *
     * @param title Title of the Focus Time
     * @param beginTime BeginTime of the FocusTime
     * @param endTime EndTime of the FocusTime
     */
    public FocusTime(String title, Calendar beginTime, Calendar endTime, int focusTimeLevel) {
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.title = title;
        this.focusTimeLevel = focusTimeLevel;
        this.id = UNDEFINED_ID;
    }

    /**
     * This should only ever be used when working with a FocusTime that already exists.
     * Never just select a random ID!
     *
     * @param title Title of the Focus Time
     * @param beginTime BeginTime of the FocusTime
     * @param endTime EndTime of the FocusTime
     * @param id Internal ID of the FocusTime
     */
    public FocusTime(String title, Calendar beginTime, Calendar endTime, int focusTimeLevel, long id) {
        this(title, beginTime, endTime, focusTimeLevel);
        this.id = id;
    }

    public Calendar getBeginTime() {
        return this.beginTime;
    }

    public void setBeginTime(Calendar beginTime) {
        this.beginTime = beginTime;
    }

    public Calendar getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getFocusTimeLevel() { return this.focusTimeLevel; }

    public void setFocusTimeLevel(int focusTimeLevel) {
        if(focusTimeLevel > 2 || focusTimeLevel < 0) this.focusTimeLevel = 0;
        else this.focusTimeLevel = focusTimeLevel;
    }


    public long getId() {
        return this.id;
    }
}
