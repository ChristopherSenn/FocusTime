package com.focustime.android.ui.calendar.focusButton;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FocusButtonViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public FocusButtonViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is focus fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}