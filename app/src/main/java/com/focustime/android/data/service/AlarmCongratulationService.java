package com.focustime.android.data.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.focustime.android.R;
import com.focustime.android.ui.calendar.focusButton.CongratulationAlertReceiver;

/**
 * One Service for setting up congratulations notifications
 */
public class AlarmCongratulationService extends Service {
    public static final String NOTIFICATION_CHANNEL_ID = "com.example.focustime";
    private final static String TAG = "AlarmCService";

    private static final String CHANNEL_ID = "countdown";
    private NotificationManager manager;
    private Notification notification;

    private long mStartTimeInMills;
    private String focusTimeName;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Cancel reciever
     */
    @Override
    public void onDestroy() {
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i =  new Intent(this, CongratulationAlertReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this,0, i,0);
        manager.cancel(pi);

        super.onDestroy();
    }

    /**
     *  Get the time by intent and use it to set the alarm and receiver
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        focusTimeName = intent.getStringExtra("focusTimeName");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());


        mStartTimeInMills = intent.getLongExtra("mStartTimeInMills", 600000);


        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerAtTime = SystemClock.elapsedRealtime() + mStartTimeInMills;

        //Here set to open the service AlarmReceiver
        Intent congratulationIntent =  new Intent(this, CongratulationAlertReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this,0, congratulationIntent,0);

        //ELAPSED_REALTIME_WAKEUP means let the departure time of the timed task count
        // from the system power on and will wake up the CPU.
        manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Set up the notification channel
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground(){
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.calendar_icon)
                .setContentTitle("Ongoing FocusTime")
                .setContentText("FocusTime '" + focusTimeName + "' is currently running")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }
}
