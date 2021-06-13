package com.focustime.android.ui.calendar.create;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.focustime.android.data.model.FocusTime;
import com.focustime.android.data.service.CalendarAPI;
import com.focustime.android.ui.calendar.day.DayElement;

import java.util.Calendar;

public class CalendarCreateViewModel extends AndroidViewModel {
    private Context context;


    public CalendarCreateViewModel(Application application) {
        super(application);
        this.context = application.getApplicationContext();
    }

    public void saveScheduleItem(DayElement d){
        CalendarAPI api = new CalendarAPI(context);
        Calendar beginTime = java.util.Calendar.getInstance();
        String[] splitDate =  d.getDate().split("-");
        beginTime.set(Integer.parseInt(splitDate[0]), Integer.parseInt(splitDate[1]), Integer.parseInt(splitDate[2]), d.getStartHour(), d.getStartMinute());
        Calendar endTime = Calendar.getInstance();
        endTime.set(Integer.parseInt(splitDate[0]), Integer.parseInt(splitDate[1]), Integer.parseInt(splitDate[2]), d.getStartHour(), d.getStartMinute()+d.getDuration());
        FocusTime f = new FocusTime(d.getTitle(), beginTime, endTime);
        api.createFocusTime(f, context);
    }
}