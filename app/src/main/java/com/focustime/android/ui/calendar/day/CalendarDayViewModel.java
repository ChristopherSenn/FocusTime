package com.focustime.android.ui.calendar.day;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class CalendarDayViewModel extends ViewModel {
    private MutableLiveData<ArrayList<DayElement>> elementList;
    private ArrayList<DayElement> daySchedule;

    public CalendarDayViewModel() {
        elementList = new MutableLiveData<>();
        daySchedule = new ArrayList<>();
        init();
    }

    public void init(){
        //TODO getData daySchedule = data;
        fillWithTestData();
        System.out.println("fill it");
        elementList.setValue(daySchedule);
    }

    public void fillWithTestData(){
        daySchedule.add(new DayElement("blub", 13, 14, 240, "2012-01-13"));
        daySchedule.add(new DayElement("blub1", 11, 24, 120, "2015-01-13"));
        System.out.println(daySchedule.size());
    }

    public LiveData<ArrayList<DayElement>> getToday() {
        return elementList;
    }
}