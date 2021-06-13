package com.focustime.android.util;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.focustime.android.data.service.AlarmCongratulationService;
import com.focustime.android.ui.calendar.focusButton.FocusButtonFragment;

public class FocusTimeServiceStarter {
    private Intent notificationIntent;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startAlarmCongratulationService(Context context, long startTimeInMillis) {
        notificationIntent = new Intent(context, AlarmCongratulationService.class);

        if (startTimeInMillis != 0) {
            notificationIntent.putExtra("mStartTimeInMills", startTimeInMillis);
            context.startForegroundService(notificationIntent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void stopAlarmCongratulationService(Context context) {
        notificationIntent = new Intent(context, AlarmCongratulationService.class);
        context.stopService(notificationIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void activateDND(Context context) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void cancelDND(Context context) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
    }
}
