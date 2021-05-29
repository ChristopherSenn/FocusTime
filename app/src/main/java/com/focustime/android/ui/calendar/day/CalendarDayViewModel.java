package com.focustime.android.ui.calendar.day;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.focustime.android.data.Schedule;

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

        elementList.setValue(daySchedule);
    }

    public void fillWithTestData(){
        daySchedule.add(new DayElement("a", "b", "c"));
        daySchedule.add(new DayElement("abb", "asdfb", "cafgds"));
        daySchedule.add(new DayElement("adfgbnbtdgaha", "babfgdagbfdr", "cfgbdabagfd"));
        daySchedule.add(new DayElement("abfgadabfg", "bafbdgafbgd", "afgbdabfgdc"));
        daySchedule.add(new DayElement("a", "b", "afgbdabgfdc"));
        daySchedule.add(new DayElement("a", "bfabgdabfgd", "c"));
        daySchedule.add(new DayElement("aabfgdabfgd", "b", "c"));
        daySchedule.add(new DayElement("abfagdbafgd", "b", "cabdfg"));
        daySchedule.add(new DayElement("afgba", "b", "cadfgb"));
        System.out.println(daySchedule.size());
    }

    public LiveData<ArrayList<DayElement>> getToday() {
        return elementList;
    }
}