package com.focustime.android.ui.calendar.importEvents;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.focustime.android.data.service.CalendarAPI;

import org.jetbrains.annotations.NotNull;
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