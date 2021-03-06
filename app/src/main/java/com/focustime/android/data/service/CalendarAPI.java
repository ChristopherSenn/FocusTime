package com.focustime.android.data.service;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.util.Log;

import com.focustime.android.data.model.FocusTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.everything.providers.android.calendar.Calendar;
import me.everything.providers.android.calendar.CalendarProvider;
import me.everything.providers.android.calendar.Event;

import static java.lang.Math.abs;


public class CalendarAPI {
    public static final String FOCUS_TIME_CALENDAR_NAME = "Focus Time Calendar";
    public static final String FOCUS_TIME_ACCOUNT_NAME ="focustime";


    private final CalendarProvider calendarProvider;

    public CalendarAPI(Context context) {
        calendarProvider = new CalendarProvider(context);
        //this.deleteFocusTimeCalendar(context); //Use this do autodelete Calendar if neccessary
        this.createFocusTimeCalendar(context);


    }

    /**
     * Returns a list of all of the Users Calendars on their phone
     * @return List of all Calendars on the users phone
     */
    public List<Calendar> getAllCalendars() {
        return calendarProvider.getCalendars().getList();
    }

    /**
     * Returns the Events of a certain calendar.
     * Note that this returns Events, NOT FocusTimes!
     * Use getFocusTimes() if you need FocusTimes specifically!
     *
     * @param calendar The calendar whose Events should be returned
     * @return List of all Events in a certain calendar
     */
    public List<Event> getEventsByCalendar(Calendar calendar) {
        return calendarProvider.getEvents(calendar.id).getList();
    }

    /**
     * Returns the Calendar that contains the FocusTimes.
     * This should just be used if information from the Calendar is needed!
     * If you want to access the FocusTimes, use getFocusTimes() instead!
     *
     * @return Calendar Object of the FocusTime Calendar
     */
    public Calendar getFocusTimeCalendar() {
        for(Calendar calendar: this.getAllCalendars()) {
            if(calendar.displayName.equals(FOCUS_TIME_CALENDAR_NAME)) {
                return calendar;
            }
        }
        return null;
    }

    /**
     * Returns a List of every FocusTime Event that is happening from today on
     *
     * @return List of every FocusTime Event.
     */
    public List<FocusTime> getFocusTimes() {
        Calendar c = getFocusTimeCalendar();
        List<Event> e = calendarProvider.getEvents(c.id).getList();


        ArrayList<FocusTime> focusTimes = new ArrayList<>();

        for(Event event: e) {


            FocusTime f = getFocusTimeById(event.id);
            focusTimes.add(f);

        }
        Collections.sort(focusTimes, ((o1, o2) -> o1.getBeginTime().compareTo(o2.getBeginTime())));

        return focusTimes;
    }

    /**
     * Returns the next FocusTime that is going to happen-
     *
     * @return List of every FocusTime Event.
     */
    public FocusTime getNextFocusTime() {
        Calendar c = getFocusTimeCalendar();
        List<Event> e = calendarProvider.getEvents(c.id).getList();

        ArrayList<FocusTime> focusTimes = new ArrayList<>();

        for(Event event: e) {
            FocusTime f = getFocusTimeById(event.id);
            if(!f.getBeginTime().before(java.util.Calendar.getInstance())){
                focusTimes.add(f);
            }
        }
        Collections.sort(focusTimes, ((o1, o2) -> o1.getBeginTime().compareTo(o2.getBeginTime())));

        if(getFocusTimes().size() > 0) { // If size <= 0 there are no FocusTimes to schedule
            return focusTimes.get(0);
        }
        else {
            return null;
        }
    }

    /**
     *
     * @param day The day that events should be gotten from
     * @return List of FocusTimes from that day
     */
    public List<FocusTime> getFocusTimesByDay(java.util.Calendar day) {
        Calendar c = getFocusTimeCalendar();
        List<Event> e = calendarProvider.getEvents(c.id).getList();
        ArrayList<FocusTime> focusTimes = new ArrayList<>();

        java.util.Calendar start = (java.util.Calendar) day.clone();
        start.set(java.util.Calendar.HOUR_OF_DAY, 0);
        start.set(java.util.Calendar.MINUTE, 0);
        start.set(java.util.Calendar.SECOND, 0);
        start.set(java.util.Calendar.MILLISECOND, 0);

        java.util.Calendar end = (java.util.Calendar) day.clone();
        end.set(java.util.Calendar.HOUR_OF_DAY, 23);
        end.set(java.util.Calendar.MINUTE, 59);
        end.set(java.util.Calendar.SECOND, 59);
        end.set(java.util.Calendar.MILLISECOND, 0);


        for(Event event: e) {
            FocusTime f = getFocusTimeById(event.id);

            if(!f.getBeginTime().before(start) && !f.getBeginTime().after(end)){
                focusTimes.add(f);
            }
        }
        Collections.sort(focusTimes, ((o1, o2) -> o1.getBeginTime().compareTo(o2.getBeginTime())));

        return focusTimes;
    }

    /**
     * Returns a specific FocusTime with a certain ID
     * Returns null if the ID doesn't exist or is from an event in a different Calendar.
     *
     * @param id ID of the desired FocusTime
     * @return FocusTime with the corresponding ID
     */
    public FocusTime getFocusTimeById(long id) {
        Event event = calendarProvider.getEvent(id);
        if(event.calendarId != getFocusTimeCalendar().id) {
            //The selected Event doesn't belong to the Focus Time Calendar!
            return null;
        }

        java.util.Calendar beginTime = java.util.Calendar.getInstance();
        beginTime.setTimeInMillis(event.dTStart);
        java.util.Calendar endTime = java.util.Calendar.getInstance();
        endTime.setTimeInMillis(event.dTend);

        int focusTimeLevel = 0;
        String title = event.title;
        try {
            String[] split = event.title.split("#");
            focusTimeLevel = Integer.parseInt(split[split.length-1]);

            title = "";
            for (int i = 0; i < split.length-1; i++) {
                title += split[i];
            }
        }catch (NumberFormatException e) {
            // No level encoded!
            // Level is set to default (0).
        }

        return new FocusTime(title, beginTime, endTime, focusTimeLevel,  event.id);
    }


    /**
     * Save a FocusTime to the phone's database
     *
     * @param focusTime FocusTime Object that should be saved/created
     * @param context application context
     * @return ID of the created Calendar Event
     */
    public long createFocusTime(FocusTime focusTime, Context context) {
        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();


        values.put(Events.DTSTART, focusTime.getBeginTime().getTimeInMillis());
        values.put(Events.DTEND, focusTime.getEndTime().getTimeInMillis());
        values.put(Events.TITLE, focusTime.getTitle() + "#" + focusTime.getFocusTimeLevel());
        values.put(Events.ACCESS_LEVEL, Events.ACCESS_PUBLIC);
        values.put(Events.CALENDAR_ID, this.getFocusTimeCalendar().id);
        values.put(Events.EVENT_TIMEZONE, "UTC");
        Uri uri = cr.insert(Events.CONTENT_URI, values);

        long eventID = Long.parseLong(uri.getLastPathSegment());
        return eventID;
    }

    /**
     * Creates an Calendar where FocusTimes can be stored if it doesn't already exist.
     *
     * @param context application context
     */
    private void createFocusTimeCalendar(Context context) {

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
            values.put(CalendarContract.Calendars.ACCOUNT_TYPE,CalendarContract.ACCOUNT_TYPE_LOCAL);
            values.put(CalendarContract.Calendars.NAME, FOCUS_TIME_CALENDAR_NAME);
            values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, FOCUS_TIME_CALENDAR_NAME);
            values.put(CalendarContract.Calendars.CALENDAR_COLOR, 0x00FF00);
            values.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_EDITOR);
            values.put(CalendarContract.Calendars.OWNER_ACCOUNT, FOCUS_TIME_ACCOUNT_NAME);
            values.put(CalendarContract.Calendars.VISIBLE, 0);
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

    /**
     * Deletes a given FocusTime from the database.
     * @param context Application Context
     * @param focusTime Focus Thime that should be deleted
     * @return True if FocusTime was deleted successfully, False if the FocusTime doesn't have an ID, or the ID is of a different Event than a FocusTime
     */
    public boolean deleteFocusTime(Context context, FocusTime focusTime) {
        if(focusTime.getId() == FocusTime.UNDEFINED_ID) {
            //Given FocusTime doesn't have a valid ID.
            return false;
        } else {
            if(getFocusTimeById(focusTime.getId()) == null) {
                //The given Event is not a FocusTime!
                return false;
            } else {
                ContentResolver cr = context.getContentResolver();
                Uri deleteUri = null;
                deleteUri = ContentUris.withAppendedId(Events.CONTENT_URI, focusTime.getId());
                cr.delete(deleteUri, null, null);
                return true;
            }

        }

    }

    /**
     * Updates the stored FocusTime with the values of the given FocusTime
     * @param context Application context
     * @param focusTime FocusTime that should be updated
     * @return True if FocusTime was updated successfully, False if the FocusTime doesn't have an ID, or the ID is of a different Event than a FocusTime
     */
    public boolean updateFocusTime(Context context, FocusTime focusTime) {
        if(focusTime.getId() == FocusTime.UNDEFINED_ID) {
           //Given FocusTime doesn't have a valid ID.
            return false;
        } else {
            if(getFocusTimeById(focusTime.getId()) == null) {
                //The given Event is not a FocusTime!
                return false;
            } else {
                ContentResolver cr = context.getContentResolver();
                ContentValues values = new ContentValues();
                Uri updateUri = null;

                values.put(Events.TITLE, focusTime.getTitle() + "#" + focusTime.getFocusTimeLevel());
                values.put(Events.DTSTART, focusTime.getBeginTime().getTimeInMillis());
                values.put(Events.DTEND, focusTime.getEndTime().getTimeInMillis());

                updateUri = ContentUris.withAppendedId(Events.CONTENT_URI, focusTime.getId());
                cr.update(updateUri, values, null, null);
                return true;
            }

        }

    }



    /**
     * Deletes the focus time calendar and every FocusTime.
     * Don't use this unless you know exactly what you are doing!
     *
     * @param context application context
     */
    public void deleteFocusTimeCalendar(Context context) {
        Uri calUri = CalendarContract.Calendars.CONTENT_URI;
        calUri = calUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, FOCUS_TIME_ACCOUNT_NAME)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, "LOCAL")
                .build();

        ContentResolver cr = context.getContentResolver();
        cr.delete(calUri, null,null);

    }

    /**
     * Deletes a calendar with the corresponding ID and all of it's events if the calendar was created by the user.
     * Just removes the calendar from the phone if it was a system calendar (Like Holidays etc.)
     * NEVER use this unless you fucked up really bad an know exactly what you are doing!
     * You can delete all of the users Events and Calendars using this!
     *
     * @param id ID of the calendar that should be deleted
     * @param context application context
     */
    private void deleteCalendarById(String id, Context context) {

        Uri calUri = CalendarContract.Calendars.CONTENT_URI;
        calUri = calUri.buildUpon()
                .appendPath(id)
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, "com.google")
                .build();
        ContentResolver cr = context.getContentResolver();
        cr.delete(calUri, null,null);
    }

    /**
     * Updates a certain Calendar if any of its Parametes changed.
     *
     * @param newCalendar Calendar that should be updated
     */
    private void updateCalendar(Calendar newCalendar) {
        calendarProvider.update(newCalendar);
    }

    /**
     * Looks through the Users Calendars, finds his personal one and returns every upcoming Event from it, that is not already imported.
     * @return List of Events the User potentially wants to import. Returns null in case the User has no personal Calendar
     */
    public List<Event> getFocusEventsForImport() {
        List<Calendar> calendars = getAllCalendars();
        List<Event> events, upcomingEvents = new ArrayList<Event>();
        List<FocusTime> focusTimes = getFocusTimes();



        for(Calendar c: calendars) {


            if(c.accountName.equals(c.ownerAccount) && !c.accountName.equals(FOCUS_TIME_ACCOUNT_NAME)) { // Looks for the users personal calendar
                events = getEventsByCalendar(c);

                for(Event event: events) {

                    if(event.dTStart > java.util.Calendar.getInstance().getTimeInMillis()) { // Filters for only upcoming events
                        boolean isAlreadyImported = false;

                        for(FocusTime ft: focusTimes) { // Check if the Event has already been imported

                            if(ft.getTitle().equals(event.title) && abs(ft.getBeginTime().getTimeInMillis() -event.dTStart) < 80000) {
                                isAlreadyImported = true;
                                break;
                            }
                        }

                        if(!isAlreadyImported) upcomingEvents.add(event); // Add to the return List if it fits all of the criteria

                    }


                }
                Collections.sort(upcomingEvents, ((o1, o2) -> Long.compare(o1.dTStart, o2.dTStart))); // Sort by start date/time
                return upcomingEvents;
            }

        }

        return null;
    }


}
