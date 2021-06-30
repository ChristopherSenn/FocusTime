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
import com.focustime.android.util.FocusTimeFactory;

import java.util.Calendar;
import java.util.TimeZone;



public class CalendarCreateViewModel extends AndroidViewModel {
    private Context context;


    public CalendarCreateViewModel(Application application) {
        super(application);
        this.context = application.getApplicationContext();
    }

    public void saveScheduleItem(DayElement d){

        CalendarAPI api = new CalendarAPI(context);
        FocusTime f = FocusTimeFactory.buildFocusTimeFromDayElement(d);
        api.createFocusTime(f, context);
    }

    /**
     * Compute the current Time Zone the user is in
     * @return
     */
    public String getTimeZone() {
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