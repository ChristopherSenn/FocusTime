package com.focustime.android.ui.calendar.create;

import android.app.Application;
import android.content.Context;
import androidx.lifecycle.AndroidViewModel;
import com.focustime.android.data.model.FocusTime;
import com.focustime.android.data.service.CalendarAPI;
import com.focustime.android.ui.calendar.day.DayElement;
import com.focustime.android.util.FocusTimeFactory;



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

}