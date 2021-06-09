package com.focustime.android.data.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.focustime.android.R;
import com.focustime.android.ui.calendar.CalendarActivity;

public class FocusTimeService extends Service {
    private static BroadcastReceiver br_ScreenOffReceiver;

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        registerScreenOffReceiver();
    }

    @Override
    public void onDestroy()
    {
        unregisterReceiver(br_ScreenOffReceiver);
        br_ScreenOffReceiver = null;
    }

    private void registerScreenOffReceiver()
    {
        br_ScreenOffReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                Log.e("whatever", "ACTION_SCREEN_OFF");
                // do something, e.g. send Intent to main app
            }
        };

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(br_ScreenOffReceiver, filter);
    }


}

/*public class FocusTimeService extends Service {
    public static final String NOTIFICATION_CHANNEL_ID = "com.example.focustime";

    public static boolean isRunning = false;
    public static FocusTimeService instance = null;




    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){

        instance = this;
        isRunning = true;


        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());

        return START_STICKY;
    }

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
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }


    @Override
    public void onDestroy(){
        isRunning = false;
        instance = null;
        //NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //manager.deleteNotificationChannel(NOTIFICATION_CHANNEL_ID);
        //notificationManager.cancel(NOTIFICATION); // Remove notification

        super.onDestroy();
    }


    public void doSomething(){
        //try {
         //   Thread.sleep(10000);
            Toast.makeText(getApplicationContext(), "Doing stuff from service...", Toast.LENGTH_SHORT).show();
        //} catch (Exception e) {}

    }
}*/