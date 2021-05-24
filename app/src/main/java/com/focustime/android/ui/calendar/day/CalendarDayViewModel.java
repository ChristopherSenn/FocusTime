package com.focustime.android.ui.calendar.day;

import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.focustime.android.data.service.CalendarService;

public class CalendarDayViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public CalendarDayViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is day fragment");


    }

    public LiveData<String> getText() {
        return mText;
    }
}