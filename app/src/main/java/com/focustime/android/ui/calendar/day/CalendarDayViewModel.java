package com.focustime.android.ui.calendar.day;

import android.Manifest;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.focustime.android.data.model.FocusTime;
import com.focustime.android.data.service.CalendarAPI;
import com.focustime.android.data.service.CalendarService;
import com.focustime.android.data.service.FocusTimeService;
import com.focustime.android.ui.calendar.CalendarActivity;
import com.focustime.android.util.TaskRunner;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;

//import me.everything.providers.android.calendar.Calendar;
import java.util.ArrayList;



public class CalendarDayViewModel extends AndroidViewModel {
    private MutableLiveData<String> mText;
    private Context context;
    private MutableLiveData<ArrayList<DayElement>> elementList;
    private ArrayList<DayElement> daySchedule;
    private CalendarAPI api;


    public CalendarDayViewModel(Application application) {
        super(application);
        this.context = application.getApplicationContext();
        api = new CalendarAPI(this.context);

        elementList = new MutableLiveData<>();
        daySchedule = new ArrayList<>();
        init();

        Intent intent = new Intent(context, CalendarService.class);
        context.startService(intent);


        //testService();

        // TODO: Find a workaround for blocking the UI Thread
        while(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            try{Thread.sleep(10);}
            catch (Exception e){}
        }
        //testAPI();
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e("Broadcast Receiver", "Received");
                //Toast.makeText(context, "service done", Toast.LENGTH_SHORT).show();
            }
        }, new IntentFilter(CalendarService.SERVICE_RECEIVER_ID));

    }

    public void init(){
        List<FocusTime> focusTimes = api.getFocusTimes();
        for(FocusTime f: focusTimes) {
            int beginHour = f.getBeginTime().get(Calendar.HOUR_OF_DAY);
            int beginMinute = f.getBeginTime().get(Calendar.MINUTE);
            String date = f.getBeginTime().get(Calendar.YEAR) + "-" + f.getBeginTime().get(java.util.Calendar.MONTH) + "-" + f.getBeginTime().get(java.util.Calendar.DAY_OF_MONTH);
            long diffInMS = f.getEndTime().getTimeInMillis() - f.getBeginTime().getTimeInMillis();
            int diff = (int) (diffInMS / 60000);
            daySchedule.add(new DayElement(f.getTitle(),beginHour, beginMinute, diff, date, f.getId()));
        }
        //fillWithTestData();
        elementList.setValue(daySchedule);
    }

    public void fillWithTestData(){
        daySchedule.add(new DayElement("blub", 13, 14, 240, "2012-01-13", 0));
        daySchedule.add(new DayElement("blub1", 11, 24, 120, "2015-01-13", 0));
    }

    public LiveData<ArrayList<DayElement>> getToday() {
        return elementList;
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

    /*public void testAPI() {


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
                endTime));*//*
        //Log.e("asljealksejas     ID", id+"");


        Log.e("Events length", api.getFocusTimes().size()+"");
        for(FocusTime ft: api.getFocusTimes()) {

            Log.e("alksje", ft.getId() + "  " + ft.getTitle());
        }


    }
*/
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

    public void deleteApiEntry(long id, Context c){
        String stringId = "" + id;
        api.deleteCalendarById(stringId, c);
    }

}