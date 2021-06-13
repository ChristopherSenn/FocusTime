package com.focustime.android.ui.calendar.create;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.focustime.android.R;
import com.focustime.android.databinding.CalendarCreateDatePickerBinding;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarDayPickDateFragment extends Activity {

    public static CalendarDayPickDateFragment newInstance() {
        return new CalendarDayPickDateFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_create_date_picker);

        final Calendar c = Calendar.getInstance();

        final DatePicker datePicker = findViewById(R.id.datePicker);
        final Button confirm = findViewById(R.id.confirmDateButton);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                Calendar startDate = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                if(startDate.before(c)){
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
