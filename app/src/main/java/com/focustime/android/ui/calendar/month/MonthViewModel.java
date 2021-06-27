package com.focustime.android.ui.calendar.month;

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.focustime.android.R;
import com.focustime.android.data.service.CalendarAPI;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MonthViewModel extends AndroidViewModel {

    Context context;


    private MutableLiveData<String> mText;

    public MonthViewModel(@NonNull @NotNull Application application){
        super(application);
        this.context = application.getApplicationContext();
    }

    public void deleteApiEntry(long id, Context c){
        CalendarAPI api = new CalendarAPI(c);
        String stringId = "" + id;
        api.deleteFocusTime(c, api.getFocusTimeById(id));
    }




}