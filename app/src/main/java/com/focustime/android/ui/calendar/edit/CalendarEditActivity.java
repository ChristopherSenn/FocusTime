package com.focustime.android.ui.calendar.edit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.focustime.android.R;
import com.focustime.android.data.model.FocusTime;
import com.focustime.android.data.service.CalendarAPI;
import com.focustime.android.ui.calendar.create.CalendarDayPickDateActivity;
import com.focustime.android.ui.calendar.day.DayElement;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Calendar;
import java.util.List;

/**
 * Activity for editing a FocusTime
 */
public class CalendarEditActivity extends Activity {

    private Calendar inputDate;
    private int hour, minute;
    private Button button;
    private Button date;
    private long id;
    private Spinner spinner;

    private List<String> spinnerOptions;


    public static CalendarEditActivity newInstance() {
        return new CalendarEditActivity();
    }

    @SuppressLint("SetTextI18n")
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        Bundle args = getIntent().getExtras();
        setContentView(R.layout.calendar_edit_fragment);
        id = args.getLong("id");


        hour = args.getInt("hour");
        minute = args.getInt("minute");

        TimePicker tp1 = findViewById(R.id.start);
        tp1.setIs24HourView(true);
        tp1.setHour(hour);
        tp1.setMinute(minute);


        TextInputLayout title = findViewById(R.id.textInput);
        title.getEditText().setText(args.getString("title"));

        TextInputLayout duration = findViewById(R.id.duration);
        duration.getEditText().setText("" + args.getInt("duration"));

        button = findViewById(R.id.saveFocusTime);
        date = findViewById(R.id.dateInput);

        inputDate = Calendar.getInstance();
        String[] splitDate =  args.getString("date").split("\\.");
        inputDate.set(Calendar.YEAR, Integer.parseInt(splitDate[2]));
        inputDate.set(Calendar.MONTH, Integer.parseInt(splitDate[1])-1);
        inputDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splitDate[0]));
        inputDate.set(Calendar.HOUR, 0);
        inputDate.set(Calendar.MINUTE, 0);
        inputDate.set(Calendar.SECOND, 0);
        inputDate.set(Calendar.MILLISECOND, 0);

        date.setText(getStringDateFromCalendar(inputDate));


        /**
         * Button to Open the Date Picker
         */
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CalendarDayPickDateActivity.class);
                int[] dateInt = new int[] {inputDate.get(Calendar.DAY_OF_MONTH), inputDate.get(Calendar.MONTH), inputDate.get(Calendar.YEAR)};
                intent.putExtra("date", dateInt);
                startActivityForResult(intent, 100);

            }
        });

        /**
         * Button to Save the Set Data
         */
        button.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                String t = "";
                t = title.getEditText().getText().toString();

                Integer sh = tp1.getHour();
                Integer sm = tp1.getMinute();

                int dur = 0; //Duration of FocusTime
                if(!duration.getEditText().getText().toString().matches("")){
                    String d = duration.getEditText().getText().toString();
                    dur = Integer.parseInt(d);
                }

                String formatDate = getStringDateFromCalendar(inputDate);

                if(!title.getEditText().getText().toString().matches("")){

                    if(dur > 0){
                        editItem(new DayElement(t, sh, sm, dur, formatDate, spinner.getSelectedItemPosition(), id));
                    }
                    else{
                        editItem(new DayElement(t, sh, sm, 120, formatDate, spinner.getSelectedItemPosition(), id));
                    }
                    onBackPressed();


                }
                else{
                    String msg = "Name Field must contain at least one Character";
                    Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        spinnerOptions = FocusTime.FOCUS_TIME_LEVELS;


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerOptions);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(args.getInt("focusTimeLevel"));

    }

    /**
     *
     * @param c Calendar
     * @return YYYYY-MM-DD
     *
     * Note that Calender Month starts with 0 = Jan (so Î add 1)
     */
    private String getStringDateFromCalendar(Calendar c){
        String returnValue =c.get(Calendar.DAY_OF_MONTH) + "." + (c.get(Calendar.MONTH) + 1) + "." + c.get(Calendar.YEAR) ;
        return returnValue;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data.getExtras() != null) {
            inputDate.set(Calendar.YEAR, data.getIntExtra("year", 0));
            inputDate.set(Calendar.MONTH, data.getIntExtra("month", 0));
            inputDate.set(Calendar.DAY_OF_MONTH, data.getIntExtra("day", 0));
            date.setText(getStringDateFromCalendar(inputDate));
        }
    }

    /**
     *  Saves an Edited FocusTime in the Backend
     * @param d
     */
    public void editItem(DayElement d){

        CalendarAPI api = new CalendarAPI(getApplicationContext());
        Calendar beginTime = java.util.Calendar.getInstance();
        String[] splitDate =  d.getDate().split("\\.");
        beginTime.set(Calendar.YEAR, Integer.parseInt(splitDate[2]));
        beginTime.set(Calendar.MONTH, Integer.parseInt(splitDate[1])-1);
        beginTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splitDate[0]));
        beginTime.set(Calendar.HOUR_OF_DAY, d.getStartHour());
        beginTime.set(Calendar.MINUTE, d.getStartMinute());
        beginTime.set(Calendar.SECOND, 0);
        beginTime.set(Calendar.MILLISECOND, 0);


        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.YEAR, Integer.parseInt(splitDate[2]));
        endTime.set(Calendar.MONTH, Integer.parseInt(splitDate[1])-1);
        endTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splitDate[0]));
        endTime.set(Calendar.HOUR_OF_DAY, d.getStartHour());
        endTime.set(Calendar.MINUTE, d.getStartMinute());
        endTime.add(Calendar.MINUTE, d.getDuration());
        endTime.set(Calendar.SECOND, 0);
        endTime.set(Calendar.MILLISECOND, 0);
        FocusTime f = new FocusTime(d.getTitle(), beginTime, endTime, d.getFocusTimeLevel(), d.getDbId());
        api.updateFocusTime(getApplicationContext(), f);
    }
}