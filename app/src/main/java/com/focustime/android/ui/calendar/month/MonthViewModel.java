package com.focustime.android.ui.calendar.month;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.focustime.android.R;
import com.focustime.android.data.service.CalendarAPI;
import com.focustime.android.util.ScheduleFocusTimeWorker;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MonthViewModel extends AndroidViewModel {

    Context context;


    private MutableLiveData<String> mText;

    public MonthViewModel(@NonNull @NotNull Application application){
        super(application);
        this.context = application.getApplicationContext();

        scheduleFocusTimeWorker();

        // TODO: Find a workaround for blocking the UI Thread
        while(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            try{Thread.sleep(10);}
            catch (Exception e){}
        }
    }

    /**
     * Schedules a Worker to check every 15 Minutes what the next upcoming FocusTime is and to set an alarm at it's start Time
     */
    private void scheduleFocusTimeWorker() {
        PeriodicWorkRequest periodicWork = new PeriodicWorkRequest.Builder(ScheduleFocusTimeWorker.class, 15, TimeUnit.MINUTES).build();
        WorkManager.getInstance().enqueueUniquePeriodicWork("NextFocusTime", ExistingPeriodicWorkPolicy.REPLACE ,periodicWork);

    }

    public void deleteApiEntry(long id, Context c){
        CalendarAPI api = new CalendarAPI(c);
        String stringId = "" + id;
        api.deleteFocusTime(c, api.getFocusTimeById(id));
    }




}