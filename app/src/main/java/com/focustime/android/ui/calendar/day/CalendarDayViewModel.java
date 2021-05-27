package com.focustime.android.ui.calendar.day;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.focustime.android.data.model.FocusTime;
import com.focustime.android.data.service.CalendarAPI;
import com.focustime.android.data.service.CalendarService;

import java.util.List;

import me.everything.providers.android.calendar.Calendar;
import me.everything.providers.android.calendar.Event;

public class CalendarDayViewModel extends AndroidViewModel {
    private MutableLiveData<String> mText;
    private Context context;

    public CalendarDayViewModel(Application application) {
        super(application);
        this.context = application.getApplicationContext();
        mText = new MutableLiveData<>();
        mText.setValue("This is day fragment");
        test();


    }

    public void test() {
        CalendarAPI api = new CalendarAPI(this.context);
        List<Calendar> calendars = api.getAllCalendars();
        for(int i = 0; i < calendars.size(); i++) {
            Log.e("ase", calendars.get(i).toString());
            Log.e("Calendar", calendars.get(i).displayName + ", id " + calendars.get(i).id);
        }
        java.util.Calendar beginTime = java.util.Calendar.getInstance();
        beginTime.set(2021, 5, 28, 15, 00);
        java.util.Calendar endTime = java.util.Calendar.getInstance();
        endTime.set(2021, 5, 28, 15, 30);

        /*long id = api.createFocusTime(new FocusTime("Test Title3", "Test Description",
                beginTime,
                endTime));*/
        //Log.e("asljealksejas     ID", id+"");


        Log.e("Events length", api.getFocusTimes().size()+"");
        for(FocusTime ft: api.getFocusTimes()) {

            Log.e("alksje", ft.getId() + "  " + ft.getTitle());
        }


    }

    public LiveData<String> getText() {
        return mText;
    }
}