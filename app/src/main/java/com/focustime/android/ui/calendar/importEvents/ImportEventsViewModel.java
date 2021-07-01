package com.focustime.android.ui.calendar.importEvents;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.focustime.android.data.service.CalendarAPI;
import com.focustime.android.ui.calendar.day.DayElement;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import me.everything.providers.android.calendar.Event;

public class ImportEventsViewModel extends AndroidViewModel {
    Context context;

    public ImportEventsViewModel(@NonNull @NotNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
    }

    /**
     * Gets the List of Events to Import from the Calendar API
     * @return List of Events to import
     */
    public List<Event> getEvent() {
        CalendarAPI api = new CalendarAPI(context);


        return api.getFocusEventsForImport();
    }
}