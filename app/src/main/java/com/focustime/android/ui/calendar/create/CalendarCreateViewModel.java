package com.focustime.android.ui.calendar.create;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.focustime.android.data.model.FocusTime;
import com.focustime.android.data.service.CalendarAPI;
import com.focustime.android.ui.calendar.day.DayElement;

import java.util.Calendar;
import java.util.TimeZone;

public class CalendarCreateViewModel extends AndroidViewModel {
    private Context context;


    public CalendarCreateViewModel(Application application) {
        super(application);
        this.context = application.getApplicationContext();
    }

    public void saveScheduleItem(DayElement d){
        //Log.e("create", "date: " + d.getDate() + "  hour: " + d.getStartHour()  + "   min " + d.getStartMinute());
        CalendarAPI api = new CalendarAPI(context);
        Calendar beginTime = java.util.Calendar.getInstance(TimeZone.getTimeZone(this.getTimeZone()));

        String[] splitDate =  d.getDate().split("-");
        beginTime.set(Integer.parseInt(splitDate[0]), Integer.parseInt(splitDate[1]), Integer.parseInt(splitDate[2]), d.getStartHour(), d.getStartMinute());
        beginTime.set(Calendar.SECOND, 0);
        //Log.e("create", beginTime.toString());
        Calendar endTime = Calendar.getInstance(TimeZone.getTimeZone(this.getTimeZone()));
        endTime.set(Integer.parseInt(splitDate[0]), Integer.parseInt(splitDate[1]), Integer.parseInt(splitDate[2]), d.getStartHour(), d.getStartMinute()+d.getDuration());
        endTime.set(Calendar.SECOND, 0);
        FocusTime f = new FocusTime(d.getTitle(), beginTime, endTime);
        api.createFocusTime(f, context);
    }

    public String getTimeZone() {
        Calendar cal = Calendar.getInstance();
        long milliDiff = cal.get(Calendar.ZONE_OFFSET);
        // Got local offset, now loop through available timezone id(s).
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
        Log.e("name", name);
        return name;
    }
}