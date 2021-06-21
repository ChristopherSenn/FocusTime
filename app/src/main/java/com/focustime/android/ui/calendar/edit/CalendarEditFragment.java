package com.focustime.android.ui.calendar.edit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.focustime.android.R;
import com.focustime.android.data.model.FocusTime;
import com.focustime.android.data.service.CalendarAPI;
import com.focustime.android.databinding.CalendarCreateFragmentBinding;
import com.focustime.android.databinding.CalendarEditFragmentBinding;
import com.focustime.android.ui.calendar.create.CalendarCreateViewModel;
import com.focustime.android.ui.calendar.create.CalendarDayPickDateFragment;
import com.focustime.android.ui.calendar.day.DayElement;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class CalendarEditFragment extends Activity {

    private Calendar inputDate;
    private Button button;
    private Button date;
    private long id;
    public static CalendarEditFragment newInstance() {
        return new CalendarEditFragment();
    }

    @SuppressLint("SetTextI18n")
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        Bundle args = getIntent().getExtras();
        setContentView(R.layout.calendar_edit_fragment);
        id = args.getLong("id");

        //return inflater.inflate(R.layout.calendar_create_fragment, container, false);


        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePicker tp1 = findViewById(R.id.start);
        tp1.setIs24HourView(true);
        tp1.setHour(args.getInt("hour"));
        tp1.setMinute(args.getInt("minute"));
        TextInputLayout text = findViewById(R.id.textInput);
        button = findViewById(R.id.saveFocusTime);
        date = findViewById(R.id.dateInput);
        TextInputLayout duration = findViewById(R.id.duration);
        text.getEditText().setText(args.getString("title"));
        duration.getEditText().setText("" + args.getInt("duration"));


        inputDate = Calendar.getInstance();
        String[] splitDate =  args.getString("date").split("-");
        inputDate.set(Calendar.YEAR, Integer.parseInt(splitDate[0]));
        inputDate.set(Calendar.MONTH, Integer.parseInt(splitDate[1]));
        inputDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splitDate[2]));
        inputDate.set(Calendar.HOUR, args.getInt("hour"));
        inputDate.set(Calendar.MINUTE, args.getInt("minute"));
        inputDate.set(Calendar.SECOND, 0);
        inputDate.set(Calendar.MILLISECOND, 0);
        date.setText(getStringDateFromCalendar(inputDate));

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CalendarDayPickDateFragment.class);
                    startActivityForResult(intent, 100);

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
                        editItem(new DayElement(t, sh, sm, dur, formatDate, id));
                        String msg = "FocusTime Saved";
                        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
                        toast.show();
                    }
                    else{
                        editItem(new DayElement(t, sh, sm, 120, formatDate, id));
                        String msg = "Duration set to 2 hours\n FocusTime Saved";
                        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                else{
                    String msg = "Aktivity Name Field must contain at least one Character";
                    Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data.getExtras() != null) {
            System.out.println(data.getIntExtra("year", 0));
            inputDate.set(Calendar.YEAR, data.getIntExtra("year", 0));
            inputDate.set(Calendar.MONTH, data.getIntExtra("month", 0));
            inputDate.set(Calendar.DAY_OF_MONTH, data.getIntExtra("day", 0));
            date.setText(getStringDateFromCalendar(inputDate));
        }
    }

    public void editItem(DayElement d){
        CalendarAPI api = new CalendarAPI(getApplicationContext());
        Calendar beginTime = java.util.Calendar.getInstance();
        String[] splitDate =  d.getDate().split("-");
        beginTime.set(Calendar.YEAR, Integer.parseInt(splitDate[0]));
        beginTime.set(Calendar.MONTH, Integer.parseInt(splitDate[1]));
        beginTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splitDate[2]));
        beginTime.set(Calendar.HOUR, d.getStartHour());
        beginTime.set(Calendar.MINUTE, d.getStartMinute());
        beginTime.set(Calendar.SECOND, 0);
        beginTime.set(Calendar.MILLISECOND, 0);

        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.YEAR, Integer.parseInt(splitDate[0]));
        endTime.set(Calendar.MONTH, Integer.parseInt(splitDate[1]));
        endTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splitDate[2]));
        endTime.set(Calendar.HOUR, d.getStartHour());
        endTime.set(Calendar.MINUTE, d.getStartMinute());
        endTime.add(Calendar.MINUTE, d.getDuration());
        endTime.set(Calendar.SECOND, 0);
        endTime.set(Calendar.MILLISECOND, 0);
        FocusTime f = new FocusTime(d.getTitle(), beginTime, endTime, d.getDbId());
        api.updateFocusTime(f);
    }
}