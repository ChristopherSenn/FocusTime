package com.focustime.android.ui.calendar.edit;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;

import com.focustime.android.data.model.FocusTime;
import com.focustime.android.data.service.CalendarAPI;
import com.focustime.android.ui.calendar.day.DayElement;

import java.util.Calendar;

public class CalendarEditViewModel extends AndroidViewModel {
    private Context context;

    public CalendarEditViewModel(Application application) {
        super(application);
        this.context = application.getApplicationContext();
    }

    public void editItem(DayElement d){
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
        FocusTime f = new FocusTime(d.getTitle(), beginTime, endTime, d.getDbId());
        api.updateFocusTime(f);
    }
}
