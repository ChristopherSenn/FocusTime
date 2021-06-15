package com.focustime.android.util;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.focustime.android.data.service.AlarmCongratulationService;
import com.focustime.android.ui.calendar.focusButton.FocusButtonFragment;

/**
 * Utility class used to start the AlarmCongratulation service wherever in the App it is neccessary.
 */
public class FocusTimeServiceStarter {
    private Intent notificationIntent;

    /**
     * Start the service
     * @param context Application context
     * @param startTimeInMillis Duration the service / FocusTime should last in Milliseconds
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startAlarmCongratulationService(Context context, long startTimeInMillis) {
        notificationIntent = new Intent(context, AlarmCongratulationService.class);

        if (startTimeInMillis != 0) {
            notificationIntent.putExtra("mStartTimeInMills", startTimeInMillis);
            context.startForegroundService(notificationIntent);
        }
    }

    /**
     * Manually stops the CongratulationService
     * @param context Application context
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void stopAlarmCongratulationService(Context context) {
        notificationIntent = new Intent(context, AlarmCongratulationService.class);
        context.stopService(notificationIntent);
    }

    /**
     * Activates the Phone's DND mode
     * @param context Application context
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void activateDND(Context context) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);
    }

    /**
     * Deactivates the Phone's DND mode
     * @param context Application context
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void cancelDND(Context context) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
    }
}
