package com.focustime.android.ui.calendar.month;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MonthViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public MonthViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Month View Fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}