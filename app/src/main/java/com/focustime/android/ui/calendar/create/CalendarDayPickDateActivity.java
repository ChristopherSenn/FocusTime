package com.focustime.android.ui.calendar.create;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;


import com.focustime.android.R;

import java.util.Calendar;

/**
 * Date Picker For picking a Date (for creating or editing)
 */
public class CalendarDayPickDateActivity extends Activity {

    public static CalendarDayPickDateActivity newInstance() {
        return new CalendarDayPickDateActivity();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_create_date_picker);

        final Calendar c = Calendar.getInstance();

        final DatePicker datePicker = findViewById(R.id.datePicker);
        final Button confirm = findViewById(R.id.confirmDateButton);

        int[] dateint = getIntent().getIntArrayExtra("date");
        datePicker.updateDate(dateint[2], dateint[1], dateint[0]);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                Calendar startDate = Calendar.getInstance();
                startDate.set(Calendar.YEAR, datePicker.getYear());
                startDate.set(Calendar.MONTH, datePicker.getMonth());
                startDate.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                c.set(Calendar.HOUR, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);
                if(c.after(startDate)){
                    String msg = "Date has to be at least current Date";
                    Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    Intent intent = new Intent();
                    intent.putExtra("year", datePicker.getYear());
                    intent.putExtra("month", datePicker.getMonth());
                    intent.putExtra("day", datePicker.getDayOfMonth());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}
