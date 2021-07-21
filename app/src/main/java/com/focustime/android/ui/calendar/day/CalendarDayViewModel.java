package com.focustime.android.ui.calendar.day;

import android.Manifest;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.focustime.android.data.model.FocusTime;
import com.focustime.android.data.service.CalendarAPI;
import com.focustime.android.util.ScheduleFocusTimeWorker;

import java.util.Calendar;
import java.util.List;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * This Class handles the Backend Connection to the Day View and the Update of the Schedule List in the DayView
 */
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


        scheduleFocusTimeWorker();

        while(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            try{Thread.sleep(10);}
            catch (Exception e){}
        }


    }


/**
 * gets the whole Schedule from the Backend API and stores it in daySchedule
 */
    public void init(){
        List<FocusTime> focusTimes = api.getFocusTimes();
        for(FocusTime f: focusTimes) {
            int beginHour = f.getBeginTime().get(java.util.Calendar.HOUR_OF_DAY);
            int beginMinute = f.getBeginTime().get(java.util.Calendar.MINUTE);
            String date = f.getBeginTime().get(java.util.Calendar.YEAR) + "-" + (f.getBeginTime().get(java.util.Calendar.MONTH) + 1)
                    + "-" + f.getBeginTime().get(java.util.Calendar.DAY_OF_MONTH);


            int duration = (int)(f.getEndTime().getTimeInMillis() - f.getBeginTime().getTimeInMillis()) / 1000 / 60;


            daySchedule.add(new DayElement(f.getTitle(),beginHour, beginMinute, duration, date, f.getFocusTimeLevel(), FocusTime.UNDEFINED_ID));
        }
        elementList.setValue(daySchedule);
    }

    /**
     * Schedules a Worker to check every 15 Minutes what the next upcoming FocusTime is and to set an alarm at it's start Time
     */
    private void scheduleFocusTimeWorker() {
        PeriodicWorkRequest periodicWork = new PeriodicWorkRequest.Builder(ScheduleFocusTimeWorker.class, 15, TimeUnit.MINUTES).build();
        WorkManager.getInstance().enqueueUniquePeriodicWork("NextFocusTime", ExistingPeriodicWorkPolicy.REPLACE ,periodicWork);

    }

    public LiveData<ArrayList<DayElement>> getToday() {
        return elementList;
    }


    public LiveData<String> getText() {
        return mText;
    }


    /**
     *
     * Deletes a Focus Time Entry in the Database by it's ID
     *
     * @param id FocusTime id (assigned when the Focus time gets created)
     * @param c Context
     */
    public void deleteApiEntry(long id, Context c){
        String stringId = "" + id;
        api.deleteFocusTime(c, api.getFocusTimeById(id));
    }

}