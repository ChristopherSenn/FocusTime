package com.focustime.android.ui.calendar.create;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.focustime.android.databinding.CalendarCreateFragmentBinding;
import com.focustime.android.ui.calendar.day.DayElement;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class CalendarCreateFragment extends Fragment {

    private CalendarCreateViewModel mViewModel;
    private CalendarCreateFragmentBinding binding;
    private Calendar inputDate;
    public static CalendarCreateFragment newInstance() {
        return new CalendarCreateFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /*ActivityResultLauncher<Intent> datePicker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            Intent data = result.getData();
                            inputDate.set(Calendar.YEAR, Integer.parseInt(data.getStringExtra("year")));
                            inputDate.set(Calendar.MONTH, Integer.parseInt(data.getStringExtra("month")));
                            inputDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(data.getStringExtra("day")));
                        }
                    }
                });*/


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
        final Button button = binding.saveFocusTime;
        final Button date = binding.dateInput;
        final TextInputLayout duration = binding.duration;

        inputDate = c;
        date.setText(getStringDateFromCalendar(inputDate));

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(root.getContext(), CalendarDayPickDateFragment.class);
                    startActivity(intent);

                    /*Intent intent = new Intent(root.getContext(), CalendarDayPickDateFragment.class);
                    datePicker.launch(intent);*/
            }
        });

        button.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                String t = "";
                t = text.getEditText().getText().toString();
                Integer sh = tp1.getHour();
                Integer sm = tp1.getMinute();
                int dur = 0; //Duration of FocusTime
                if(!duration.getEditText().getText().toString().matches("")){
                    String d = duration.getEditText().getText().toString();
                    dur = Integer.parseInt(d);
                }
                String formatDate = getStringDateFromCalendar(inputDate);
                if(!text.getEditText().getText().toString().matches("")){
                    if(dur > 0){
                        mViewModel.saveScheduleItem(new DayElement(t, sh, sm, dur, formatDate));
                        String msg = "FocusTime Saved";
                        Toast toast = Toast.makeText(root.getContext(), msg, Toast.LENGTH_LONG);
                        toast.show();
                    }
                    else{
                        mViewModel.saveScheduleItem(new DayElement(t, sh, sm, 120, formatDate));
                        String msg = "Duration set to 2 hours\n FocusTime Saved";
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

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1111 && resultCode == -1 &&  data != null && data.getData() != null){
            String d = data.getStringExtra("date");


        }
    }*/

    /**
     *
     * @param c
     * @return YYYYY-MM-DD
     *
     * Note that Calender Month starts with 0 = Jan (so Î add 1)
     */
    private String getStringDateFromCalendar(Calendar c){
        System.out.println(c.getTime());
        String returnValue = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH);
        return returnValue;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data.getData() != null) {
            inputDate.set(Calendar.YEAR, Integer.parseInt(data.getStringExtra("year")));
            inputDate.set(Calendar.MONTH, Integer.parseInt(data.getStringExtra("month")));
            inputDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(data.getStringExtra("day")));
        }
    }
}