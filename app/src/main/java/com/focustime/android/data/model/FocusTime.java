package com.focustime.android.data.model;

import java.util.Calendar;

public class FocusTime {
    private Calendar beginTime, endTime;
    private String title, description;
    private long id;

    /**
     * The default Constructor for creating custom FocusTimes
     *
     * @param title Title of the Focus Time
     * @param description Description of the FocusTime
     * @param beginTime BeginTime of the FocusTime
     * @param endTime EndTime of the FocusTime
     */
    public FocusTime(String title, String description, Calendar beginTime, Calendar endTime) {
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.title = title;
        this.description = description;
    }

    /**
     * This should only ever be used when working with a FocusTime that already exists.
     * Never just select a random ID!
     *
     * @param title Title of the Focus Time
     * @param description Description of the FocusTime
     * @param beginTime BeginTime of the FocusTime
     * @param endTime EndTime of the FocusTime
     * @param id Internal ID of the FocusTime
     */
    public FocusTime(String title, String description, Calendar beginTime, Calendar endTime, long id) {
        this(title, description, beginTime, endTime);
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

    public String getDescription() {
        return this.description;
    }

    public long getId() {
        return this.id;
    }
}