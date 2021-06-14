package com.focustime.android.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.focustime.android.data.model.FocusTime;
import com.focustime.android.data.service.CalendarAPI;
import com.focustime.android.ui.calendar.day.ScheduleFocusTimeReceiver;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TestWorker extends Worker {
    public TestWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }



    @Override
    public Result doWork() {
        //TODO: FIx Months

        // Do the work here--in this case, upload the images.
        Log.e("WORKER", "DOING SOMETHING");
        CalendarAPI api = new CalendarAPI(getApplicationContext());
        if(api.getFocusTimes().size() > 0) {
            FocusTime nextFocusTime = api.getFocusTimes().get(0);
            Calendar c = nextFocusTime.getBeginTime();
            //c.set(Calendar.MONTH, c.get(Calendar.MONTH)-1);

            Calendar c2 = nextFocusTime.getEndTime();
            //c2.set(Calendar.MONTH, c2.get(Calendar.MONTH)-1);
            //Log.e("Schedule Time", c.getTimeInMillis()+"");
            //Log.e("Schedule Time Offset", c.getTimeZone().getDisplayName()+"");
            Intent notifyIntent = new Intent(getApplicationContext(), ScheduleFocusTimeReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast
                    (getApplicationContext(), 3, notifyIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

            alarmManager.cancel(pendingIntent);
            alarmManager.set(AlarmManager.RTC_WAKEUP,  c.getTimeInMillis() , pendingIntent);
        }





        // Indicate whether the work finished successfully with the Result
        return Result.success();
    }
}
