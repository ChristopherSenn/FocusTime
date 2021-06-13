package com.focustime.android.ui.calendar.month;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.focustime.android.R;
import com.focustime.android.databinding.FocusButtonFragmentBinding;
import com.focustime.android.databinding.MonthViewBinding;
import com.focustime.android.ui.calendar.day.CalenderDayAdapter;
import com.focustime.android.ui.calendar.focusButton.FocusButtonViewModel;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;


public class MonthViewFragment extends Fragment{

    private MonthViewBinding binding;

    private MonthViewModel mViewModel;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;



    public static MonthViewFragment newInstance() {
        return new MonthViewFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(MonthViewModel.class);
        binding = MonthViewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;



    }



}
