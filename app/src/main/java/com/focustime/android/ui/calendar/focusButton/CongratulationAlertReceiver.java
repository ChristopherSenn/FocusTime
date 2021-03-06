package com.focustime.android.ui.calendar.focusButton;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.focustime.android.data.service.AlarmCongratulationService;

/**
 * A receiver to cancel DND mode and trigger congratulation notifications when the timer is over.
 */
public class CongratulationAlertReceiver extends BroadcastReceiver {
    private NotificationManager mNotificationManager;
    private Intent notificationIntent;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        cancelDND(context);

        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(1, nb.build());

        notificationIntent = new Intent(context, AlarmCongratulationService.class);
        context.stopService(notificationIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void cancelDND(Context context) {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
    }
}
