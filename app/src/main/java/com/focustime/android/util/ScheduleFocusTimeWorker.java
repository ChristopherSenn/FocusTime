package com.focustime.android.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.focustime.android.data.model.FocusTime;
import com.focustime.android.data.service.CalendarAPI;
import com.focustime.android.ui.calendar.day.ScheduleFocusTimeReceiver;

import java.util.Calendar;

/**
 * Worker that periodically checks what the next FocusTime is and then sets an AlarmManager to Trigger a Broadcast Receiver at that time
 */
public class ScheduleFocusTimeWorker extends Worker {

    public ScheduleFocusTimeWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }


    /**
     * Checks what the next FocusTime is and then sets an AlarmManager to Trigger a Broadcast Receiver at that time
     * @return Returns if work was done successfully or not
     */
    @Override
    public Result doWork() {
        CalendarAPI api = new CalendarAPI(getApplicationContext());

            FocusTime nextFocusTime = api.getNextFocusTime();
            if(nextFocusTime != null) {
                Calendar c = nextFocusTime.getBeginTime();

                Intent notifyIntent = new Intent(getApplicationContext(), ScheduleFocusTimeReceiver.class); // Create an Intent to the Receiver
                notifyIntent.putExtra("focusTimeId", nextFocusTime.getId());
                PendingIntent pendingIntent = PendingIntent.getBroadcast
                        (getApplicationContext(), 3, notifyIntent, PendingIntent.FLAG_CANCEL_CURRENT); // Make it a pending Intent

                AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

                alarmManager.cancel(pendingIntent); // As this worker gets called regularly, the current alarm has to be canceled to avoid duplicates
                alarmManager.set(AlarmManager.RTC_WAKEUP,  c.getTimeInMillis() , pendingIntent); // Set alarm when the next FocusTime starts with the PendingIntent to the Receiver
            }

        return Result.success();
    }
}
