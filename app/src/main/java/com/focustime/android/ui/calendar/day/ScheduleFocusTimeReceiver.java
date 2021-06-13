package com.focustime.android.ui.calendar.day;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.focustime.android.data.model.FocusTime;
import com.focustime.android.data.service.CalendarAPI;
import com.focustime.android.data.service.FocusTimeService;
import com.focustime.android.util.FocusTimeServiceStarter;

public class ScheduleFocusTimeReceiver extends BroadcastReceiver {

    public ScheduleFocusTimeReceiver() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.e("lakje", "j");
        CalendarAPI calendarAPI = new CalendarAPI(context);
        if(calendarAPI.getFocusTimes().size() > 0) {
            FocusTime next = calendarAPI.getFocusTimes().get(0);
            long duration = next.getEndTime().getTimeInMillis() - next.getBeginTime().getTimeInMillis();
            Log.e("Time Duration", duration+"");
            Log.e("Start", next.getBeginTime().getTimeInMillis()+"");
            Log.e("End", next.getEndTime().getTimeInMillis()+"");

            FocusTimeServiceStarter starter = new FocusTimeServiceStarter();
            starter.activateDND(context);
            starter.startAlarmCongratulationService(context, duration);
        }

        Toast.makeText(context, "Worker is Working", Toast.LENGTH_LONG).show();
        //Intent intent1 = new Intent(context, FocusTimeService.class);
        //context.startService(intent1);
    }
}
