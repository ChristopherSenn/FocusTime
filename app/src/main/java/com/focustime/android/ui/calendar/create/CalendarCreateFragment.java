package com.focustime.android.ui.calendar.create;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.focustime.android.R;
import com.focustime.android.databinding.CalendarCreateFragmentBinding;
import com.focustime.android.ui.calendar.day.DayElement;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

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

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        final TimePicker tp1 = binding.start;
        tp1.setIs24HourView(true);
        tp1.setCurrentHour(hour);
        tp1.setCurrentMinute(minute);
        final TextInputLayout text = binding.textInput;
        final Button button = binding.button1;
        final TextInputLayout date = binding.dateInput;
        final TextInputLayout duration = binding.duration;

        button.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                String t = "";
                t = text.getEditText().getText().toString();
                Integer sh = tp1.getHour();
                Integer sm = tp1.getMinute();
                Integer dur = 0; //Duration of FocusTime
                if(!duration.getEditText().getText().toString().matches("")){
                    String d = duration.getEditText().getText().toString();
                    dur = Integer.parseInt(d);
                }

                LocalDateTime dateNow = LocalDateTime.now();
                DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_DATE;
                String formatDate = dateNow.format(df);
                if(!date.getEditText().getText().toString().matches("") && date.getEditText().getText().toString().length() == 8){
                    String d = "" + date.getEditText().getText().toString();
                    formatDate = d.substring(0,4) + "-" + d.substring(4,6) + "-" + d.substring(6,8);
                    System.out.println(formatDate);
                }
                System.out.println(formatDate);
                if(!text.getEditText().getText().toString().matches("")){
                    if(dur > 0){
                        mViewModel.saveScheduleItem(new DayElement(t, sh, sm, dur, formatDate));
                        String msg = "FocusTime Saved";
                        Toast toast = Toast.makeText(root.getContext(), msg, Toast.LENGTH_LONG);
                        toast.show();
                    }
                    else{
                        mViewModel.saveScheduleItem(new DayElement(t, sh, sm, 120, formatDate));
                        String msg = "Duration has to be greater 0\n If it is empty it is set to 2 hours\n FocusTime Saved";
                        Toast toast = Toast.makeText(root.getContext(), msg, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                else{
                    String msg = "Aktivity Name Field must contain at least one Character";
                    Toast toast = Toast.makeText(root.getContext(), msg, Toast.LENGTH_SHORT);
                    toast.show();
                }
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