package com.focustime.android.ui.calendar.create;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CalendarCreateViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public CalendarCreateViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is create fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}