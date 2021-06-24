package com.focustime.android.ui.calendar.day;

import java.util.Date;

public class DayElement {

    private String title;
    private Integer startHour;
    private Integer startMinute;
    private Integer duration; //in Minutes
    private String date; //DateTimeFormatter.ISO_LOCAL_DATE
    private int focusTimeLevel;
    private long dbId;


    public DayElement(String newTitle, Integer sH, Integer sM, Integer dur, String d, int focusTimeLevel, long id){
        title = newTitle;
        startHour = sH;
        startMinute = sM;
        duration = dur;
        date = d;
        this.focusTimeLevel = focusTimeLevel;
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

    public int getFocusTimeLevel() { return this.focusTimeLevel; }

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

    public void setFocusTimeLevel(int focusTimeLevel) {
        if (focusTimeLevel > 2 || focusTimeLevel < 0) this.focusTimeLevel = 0;
        else this.focusTimeLevel = focusTimeLevel;
    }

    public void setDbId(long dbId) { this.dbId = dbId; }
}
