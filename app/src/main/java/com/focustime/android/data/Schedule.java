package com.focustime.android.data;

import com.focustime.android.ui.calendar.day.DayElement;

import java.util.ArrayList;

public class Schedule {
    private ArrayList<DayElement> exampleList;

    public Schedule(){
        exampleList = new ArrayList<>();
        exampleList.add(new DayElement("blub", "blub1", "tttType"));
    }

    public int getSizeOfSchedule(){
        return exampleList.size();
    }

    public DayElement getScheduleOfDay(int position){
        //TODO
        return exampleList.get(position);
    }
}
