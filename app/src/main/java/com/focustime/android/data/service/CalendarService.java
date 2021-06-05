package com.focustime.android.data.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.List;

import me.everything.providers.android.calendar.Calendar;
import me.everything.providers.android.calendar.CalendarProvider;

public class CalendarService extends Service {
    public static final String SERVICE_RECEIVER_ID = FocusTimeService.NOTIFICATION_CHANNEL_ID + ".customIntent";

    private Looper serviceLooper;
    private ServiceHandler serviceHandler;



    public CalendarService() {

    }


    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("CalendarServiceThread", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;

        //msg.what = serviceHandler.GET_ALL_CALENDARS;
        serviceHandler.sendMessage(msg);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {

        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onDestroy() {
        Intent intent = new Intent();
        intent.setAction(SERVICE_RECEIVER_ID);
        sendBroadcast(intent);

        //Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }



    private final class ServiceHandler extends Handler {
        public final int GET_ALL_CALENDARS = 1;

        private CalendarProvider calendarProvider;

        public ServiceHandler(Looper looper) {
            super(looper);

        }
        @Override
        public void handleMessage(Message msg) {

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            Toast.makeText(getApplicationContext(), "service done", Toast.LENGTH_SHORT).show();
            stopSelf(msg.arg1);
        }

    }

}
