package com.focustime.android.ui.calendar.day;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    /**
     * Starts the next FocusTime
     * That FocusTime should start right when the Receiver is called, as it gets set by the Worker to that specific time
     * @param context Application context
     * @param intent Start Intent
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        CalendarAPI calendarAPI = new CalendarAPI(context);

        //if(calendarAPI.getFocusTimes().size() > 0) { // Check if there are any FocusTime to be started
            //FocusTime next = calendarAPI.getFocusTimes().get(0);
            FocusTime next = calendarAPI.getFocusTimeById(intent.getLongExtra("focusTimeId", -1));
            long duration = next.getEndTime().getTimeInMillis() - next.getBeginTime().getTimeInMillis(); // Calculate duration of the FocusTime

            SharedPreferences preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();


            editor.putLong("startTimeInMillis", next.getEndTime().getTimeInMillis() - next.getBeginTime().getTimeInMillis());
            editor.putLong("millisLeft", next.getEndTime().getTimeInMillis() - next.getBeginTime().getTimeInMillis());
            editor.putBoolean("timeRunning", true);
            editor.putLong("endTime", next.getEndTime().getTimeInMillis());

            editor.apply();

            FocusTimeServiceStarter starter = new FocusTimeServiceStarter();
            starter.activateDNDWithLevel(context, next.getFocusTimeLevel());
            starter.startAlarmCongratulationService(context, duration); // Start the FocusTimeService with the calculated duration
        }

        //Toast.makeText(context, "Worker is Working", Toast.LENGTH_LONG).show();
    //}
}
