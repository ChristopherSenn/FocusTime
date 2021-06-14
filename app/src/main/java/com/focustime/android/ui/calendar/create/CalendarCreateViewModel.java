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
        beginTime.set(Calendar.YEAR, Integer.parseInt(splitDate[0]));
        beginTime.set(Calendar.MONTH, Integer.parseInt(splitDate[1]));
        beginTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splitDate[2]));
        beginTime.set(Calendar.HOUR, d.getStartHour());
        beginTime.set(Calendar.MINUTE, d.getStartMinute());
        beginTime.set(Calendar.SECOND, 0);
        beginTime.set(Calendar.MILLISECOND, 0);

        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.YEAR, Integer.parseInt(splitDate[0]));
        endTime.set(Calendar.MONTH, Integer.parseInt(splitDate[1]));
        endTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splitDate[2]));
        endTime.set(Calendar.HOUR, d.getStartHour());
        endTime.set(Calendar.MINUTE, d.getStartMinute());
        endTime.add(Calendar.MINUTE, d.getDuration());
        endTime.set(Calendar.SECOND, 0);
        endTime.set(Calendar.MILLISECOND, 0);
        System.out.println(d.getDuration());
        System.out.println("Inserting in API (Begin End Diff): " + beginTime.getTimeInMillis() + " " + endTime.getTimeInMillis() + " Diff:" + (endTime.getTimeInMillis()-beginTime.getTimeInMillis()));
        FocusTime f = new FocusTime(d.getTitle(), beginTime, endTime);
        api.createFocusTime(f, context);
    }
}