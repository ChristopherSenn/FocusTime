package com.focustime.android.ui.calendar.day;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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