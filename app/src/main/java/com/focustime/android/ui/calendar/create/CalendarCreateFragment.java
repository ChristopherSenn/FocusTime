package com.focustime.android.ui.calendar.create;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.focustime.android.R;
import com.focustime.android.databinding.CalendarCreateFragmentBinding;

public class CalendarCreateFragment extends Fragment {

    private CalendarCreateViewModel mViewModel;
    private CalendarCreateFragmentBinding binding;

    public static CalendarCreateFragment newInstance() {
        return new CalendarCreateFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.calendar_create_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(CalendarCreateViewModel.class);
        binding = CalendarCreateFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.createTextView;
        mViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }

}