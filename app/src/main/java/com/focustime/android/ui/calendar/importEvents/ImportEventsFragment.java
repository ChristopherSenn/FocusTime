package com.focustime.android.ui.calendar.importEvents;

import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.focustime.android.R;
import com.focustime.android.databinding.CalendarDayFragmentBinding;
import com.focustime.android.databinding.ImportEventsFragmentBinding;
import com.focustime.android.ui.calendar.day.CalendarDayViewModel;
import com.focustime.android.ui.calendar.day.CalenderDayAdapter;

public class ImportEventsFragment extends Fragment {

    private ImportEventsViewModel viewModel;
    private ImportEventsAdapter adapter;
    private RecyclerView recyclerView;
    private ImportEventsFragmentBinding binding;

    public static ImportEventsFragment newInstance() {
        return new ImportEventsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Init ViewModel and binding
        viewModel = new ViewModelProvider(this).get(ImportEventsViewModel.class);
        binding = ImportEventsFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Init the import card RecyclerView
        adapter = new ImportEventsAdapter((Activity) root.getContext(), viewModel.getEvent());
        recyclerView = binding.importRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        recyclerView.setAdapter(adapter);


        return root;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ImportEventsViewModel.class);

    }

}