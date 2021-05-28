package com.focustime.android.ui.calendar.day;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.focustime.android.data.model.FocusTime;
import com.focustime.android.data.service.CalendarAPI;
import com.focustime.android.data.service.CalendarService;
import com.focustime.android.data.service.FocusTimeService;
import com.focustime.android.util.TaskRunner;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
        testService();
        testAPI();



    }

    public void testService() {
        TaskRunner taskRunner = new TaskRunner();
        taskRunner.executeAsync(new StartServiceTask(), (result) -> {
            makeServiceDoSomething();
        });
    }





    public void makeServiceDoSomething(){
        if( FocusTimeService.isRunning )
            FocusTimeService.instance.doSomething();
    }

    public void testAPI() {
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

    class StartServiceTask implements Callable<Void> {

        @Override
        public Void call() {
            if( FocusTimeService.isRunning ){
                // Stop service
                Intent intent = new Intent(context, FocusTimeService.class);
                context.stopService(intent);
            }
            else {
                // Start service
                Intent intent = new Intent(context, FocusTimeService.class);
                context.startService(intent);
                Log.e("Service TEst", "Service Started");

            }
            return null;
        }
    }

}