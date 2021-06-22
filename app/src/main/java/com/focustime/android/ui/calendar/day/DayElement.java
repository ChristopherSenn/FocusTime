package com.focustime.android.ui.calendar.day;

import java.util.Date;

public class DayElement {

    private String title;
    private Integer startHour;
    private Integer startMinute;
    private Integer duration; //in Minutes
    private String date; //DateTimeFormatter.ISO_LOCAL_DATE
    private long dbId;


    public DayElement(String newTitle, Integer sH, Integer sM, Integer dur, String d, long id){
        title = newTitle;
        startHour = sH;
        startMinute = sM;
        duration = dur;
        date = d;
        dbId = id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getStartHour() {
        return startHour;
    }

    public Integer getStartMinute() {
        return startMinute;
    }

    public Integer getDuration() {
        return duration;
    }

    public String getDate() {
        return date;
    }

    public long getDbId() {
        return dbId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStartHour(Integer startHour) {
        this.startHour = startHour;
    }

    public void setStartMinute(Integer startMinute) {
        this.startMinute = startMinute;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDbId(long dbId) { this.dbId = dbId; }
}
