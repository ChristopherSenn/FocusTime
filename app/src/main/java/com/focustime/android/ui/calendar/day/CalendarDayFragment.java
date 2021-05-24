package com.focustime.android.ui.calendar.day;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.focustime.android.R;
import com.focustime.android.data.model.FocusTime;
import com.focustime.android.data.service.CalendarAPI;
import com.focustime.android.data.service.CalendarService;
import com.focustime.android.databinding.CalendarDayFragmentBinding;

import java.util.List;

import me.everything.providers.android.calendar.Calendar;
import me.everything.providers.android.calendar.Event;

public class CalendarDayFragment extends Fragment {

    private CalendarDayViewModel mViewModel;
    private CalendarDayFragmentBinding binding;

    public static CalendarDayFragment newInstance() {
        return new CalendarDayFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.calendar_day_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(CalendarDayViewModel.class);
        binding = CalendarDayFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.dayTextview;
        mViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        test();

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void test() {
        CalendarAPI api = new CalendarAPI(this.getContext());
        List<Calendar> calendars = api.getAllCalendars();
        for(int i = 0; i < calendars.size(); i++) {
            Log.e("Calendar", calendars.get(i).displayName + ", id " + calendars.get(i).id);
        }
        java.util.Calendar beginTime = java.util.Calendar.getInstance();
        beginTime.set(2021, 5, 24, 15, 00);
        java.util.Calendar endTime = java.util.Calendar.getInstance();
        endTime.set(2021, 5, 24, 15, 30);

        /*long id = api.createFocusTime(new FocusTime("Test Title", "Test Description",
                beginTime,
                endTime));*/
        //Log.e("asljealksejas     ID", id+"");

        for(Event e: api.getFocusTimes()) {
            Log.e("alksje", e.title);
        }


    }

}