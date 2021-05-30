package com.focustime.android.ui.calendar.day;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.focustime.android.R;
import com.focustime.android.data.model.FocusTime;
import com.focustime.android.data.service.CalendarAPI;
import com.focustime.android.data.service.CalendarService;
import com.focustime.android.databinding.CalendarDayFragmentBinding;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

import me.everything.providers.android.calendar.Calendar;
import me.everything.providers.android.calendar.Event;

public class CalendarDayFragment extends Fragment {


    private CalendarDayViewModel mViewModel;
    private CalendarDayFragmentBinding binding;
    private CalenderDayAdapter adapter;
    private RecyclerView resview;
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

        resview = binding.dayView;

        mViewModel.getToday().observe(getViewLifecycleOwner(), d -> {
            adapter = new CalenderDayAdapter((Activity) root.getContext(), d);
            resview.setLayoutManager(new LinearLayoutManager(root.getContext()));
            resview.setAdapter(adapter);
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
        mViewModel.getToday();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}