package com.focustime.android.util;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

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
    public void startAlarmCongratulationService(Context context, long startTimeInMillis, String focusTimeName) {
        notificationIntent = new Intent(context, AlarmCongratulationService.class);

        if (startTimeInMillis != 0) {
            notificationIntent.putExtra("mStartTimeInMills", startTimeInMillis);
            notificationIntent.putExtra("focusTimeName", focusTimeName);
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
     * Activates the Phone's total silence DND mode
     * @param context Application context
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void activateTotalSilenceDND(Context context) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);
    }


    /**
     * Activates the Phone's priority only DND mode
     * @param context Application context
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void activatePriorityOnlyDND(Context context) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY);
    }

    /**
     * Activates the Phone's alarms only DND mode
     * @param context Application context
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void activateAlarmsOnlyDND(Context context) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALARMS);
    }

    /**
     * Activates the Phone's DND mode with dnd level
     * 0: Priority only
     * 1: Alarms only
     * 2: Total Silence
     * @param context Application context
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void activateDNDWithLevel(Context context, int level) {

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Check if the notification policy access has been granted for the app.
        if (!mNotificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            if (level == 0)
                activatePriorityOnlyDND(context);
            else if (level == 1)
                activateAlarmsOnlyDND(context);
            else if (level == 2)
                activateTotalSilenceDND(context);

        }

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
