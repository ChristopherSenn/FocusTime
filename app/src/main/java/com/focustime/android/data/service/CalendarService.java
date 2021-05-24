package com.focustime.android.data.service;

/*import android.app.Service;
import android.content.Context;
import android.content.Intent;
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
    private Looper serviceLooper;
    private ServiceHandler serviceHandler;



    public CalendarService() {

    }

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("CalendarServiceThread", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper, this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        Log.e("asekj", startId+"");
        msg.what = serviceHandler.GET_ALL_CALENDARS;
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
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }



    private final class ServiceHandler extends Handler {
        public final int GET_ALL_CALENDARS = 1;

        private CalendarProvider calendarProvider;
        private Context context;

        public ServiceHandler(Looper looper, Context context) {
            super(looper);
            this.context =  context;
            this.calendarProvider = new CalendarProvider(this.context);
        }
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_ALL_CALENDARS:
                    List<Calendar> result = this.getAllCalendars();
                    Log.e("lakhjekla", "lkajseklajse");
                    Intent in = new Intent(GET_ALL_CALENDARS);
                    in.putExtra(GET_ALL_CALENDARS, result);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(in);
                    //sendMessage(obtainMessage(GET_ALL_CALENDARS, "IDK"));
                    break;
            }

            stopSelf(msg.arg1);
        }

        private List<Calendar> getAllCalendars() {
            List<Calendar> calendars = calendarProvider.getCalendars().getList();
            return calendars;
        }
    }

}*/
public class CalendarService {

}