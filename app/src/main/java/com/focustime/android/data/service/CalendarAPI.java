package com.focustime.android.data.service;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.util.Log;

import com.focustime.android.data.model.FocusTime;

import java.util.List;

import me.everything.providers.android.calendar.Calendar;
import me.everything.providers.android.calendar.CalendarProvider;
import me.everything.providers.android.calendar.Event;

// Fuck this piece of shit Framework and its piece of shit documentation. We are doing this synchronous now.
public class CalendarAPI {
    public static final String FOCUS_TIME_CALENDAR_NAME = "Focus Time Calendar";
    public static final String FOCUS_TIME_ACCOUNT_NAME ="focustime";

    private CalendarProvider calendarProvider;
    private Context context;

    public CalendarAPI(Context context) {
        this.context = context;
        calendarProvider = new CalendarProvider(context);
        this.createFocusTimeCalendar();
    }

    public List<Calendar> getAllCalendars() {
        List<Calendar> calendars = calendarProvider.getCalendars().getList();
        return calendars;
    }

    public List<Event> getEventsByCalendar(Calendar calendar) {
        return calendarProvider.getEvents(calendar.id).getList();
    }

    public Calendar getFocusTimeCalendar() {
        List<Calendar> calendars = this.getAllCalendars();
        long id;
        for(Calendar calendar: this.getAllCalendars()) {
            if(calendar.displayName.equals(FOCUS_TIME_CALENDAR_NAME)) {
                id = calendar.id;
                return calendar;
            }
        }
        return null;
    }

    public List<Event> getFocusTimes() {
        return calendarProvider.getEvents(getFocusTimeCalendar().id).getList();
    }

    public long createFocusTime(FocusTime focusTime) {
        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();

        values.put(Events.DTSTART, focusTime.getBeginTime().getTimeInMillis());
        values.put(Events.DTEND, focusTime.getEndTime().getTimeInMillis());
        values.put(Events.TITLE, focusTime.getTitle());
        values.put(Events.DESCRIPTION, focusTime.getDescription());
        values.put(Events.CALENDAR_ID, this.getFocusTimeCalendar().id);
        values.put(Events.EVENT_TIMEZONE, "UTC"); //TODO: Add support for different timezones
        Uri uri = cr.insert(Events.CONTENT_URI, values);

        long eventID = Long.parseLong(uri.getLastPathSegment());
        return eventID;
    }

    /**
     * Creates an Calendar where FocusTimes can be stored if it doesn't already exist.
     */
    private void createFocusTimeCalendar() {

        List<Calendar> calendars = this.getAllCalendars();

        boolean alreadyExists = false;
        for(int i = 0; i < calendars.size(); i++) {
            if(calendars.get(i).displayName.equals(FOCUS_TIME_CALENDAR_NAME)){
                alreadyExists = true;
                break;
            }
        }
        if(!alreadyExists) {
            Uri target = Uri.parse(CalendarContract.Calendars.CONTENT_URI.toString());
            target = target.buildUpon().appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                    .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, FOCUS_TIME_ACCOUNT_NAME)
                    .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, "com.google").build();

            ContentValues values = new ContentValues();
            values.put(CalendarContract.Calendars.ACCOUNT_NAME, FOCUS_TIME_ACCOUNT_NAME);
            values.put(CalendarContract.Calendars.ACCOUNT_TYPE, "com.google");
            values.put(CalendarContract.Calendars.NAME, FOCUS_TIME_CALENDAR_NAME);
            values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, FOCUS_TIME_CALENDAR_NAME);
            values.put(CalendarContract.Calendars.CALENDAR_COLOR, 0x00FF00);
            values.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_ROOT);
            values.put(CalendarContract.Calendars.OWNER_ACCOUNT, FOCUS_TIME_ACCOUNT_NAME);
            values.put(CalendarContract.Calendars.VISIBLE, 1);
            values.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
            values.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, "Europe/Rome");
            values.put(CalendarContract.Calendars.CAN_PARTIALLY_UPDATE, 1);
            values.put(CalendarContract.Calendars.CAL_SYNC1, "https://www.google.com/calendar/feeds/" + FOCUS_TIME_ACCOUNT_NAME + "/private/full");
            values.put(CalendarContract.Calendars.CAL_SYNC2, "https://www.google.com/calendar/feeds/default/allcalendars/full/" + FOCUS_TIME_ACCOUNT_NAME);
            values.put(CalendarContract.Calendars.CAL_SYNC3, "https://www.google.com/calendar/feeds/default/allcalendars/full/" + FOCUS_TIME_ACCOUNT_NAME);
            values.put(CalendarContract.Calendars.CAL_SYNC4, 1);
            values.put(CalendarContract.Calendars.CAL_SYNC5, 0);
            values.put(CalendarContract.Calendars.CAL_SYNC8, System.currentTimeMillis());

            Uri newCalendar = context.getContentResolver().insert(target, values);
        }

    }

    private void deleteCalendar() {
        Uri calUri = CalendarContract.Calendars.CONTENT_URI;
        calUri = calUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, FOCUS_TIME_ACCOUNT_NAME)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, "com.google")
                .build();

        ContentResolver cr = context.getContentResolver();
        cr.delete(calUri, null,null);

    }

    private void deleteCalendarById(String id) {
        Uri calUri = CalendarContract.Calendars.CONTENT_URI;
        calUri = calUri.buildUpon()
                .appendPath(id)
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, "com.google")
                .build();
        //calendarHandler.startDelete(0,-1,calUri,null,null);
        ContentResolver cr = context.getContentResolver();
        cr.delete(calUri, null,null);
    }

    private void updateCalendar(Calendar newCalendar) {
        calendarProvider.update(newCalendar);
    }


}
