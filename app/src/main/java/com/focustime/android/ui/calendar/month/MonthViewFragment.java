package com.focustime.android.ui.calendar.month;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.focustime.android.R;
import com.focustime.android.data.model.FocusTime;
import com.focustime.android.data.service.CalendarAPI;
import com.focustime.android.databinding.FocusButtonFragmentBinding;
import com.focustime.android.databinding.MonthViewBinding;
import com.focustime.android.ui.calendar.create.CalendarCreateFragment;
import com.focustime.android.ui.calendar.day.CalendarDayViewModel;
import com.focustime.android.ui.calendar.day.CalenderDayAdapter;
import com.focustime.android.ui.calendar.day.DayElement;
import com.focustime.android.ui.calendar.focusButton.FocusButtonViewModel;
import com.focustime.android.ui.calendar.importEvents.ImportEventsAdapter;
import com.focustime.android.util.ActionBarSetter;
import com.focustime.android.util.SwipeToDeleteCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MonthViewFragment extends Fragment implements MonthAdapter.OnItemListener{

    private MonthViewBinding binding;

    private MonthViewModel mViewModel;
    private TextView monthYearText;
    public static RecyclerView calendarRecyclerView;
    public static RecyclerView dailyMonthRecyclerView;
    public static LocalDate selectedDate;
    private MutableLiveData<ArrayList<DayElement>> elementList;
    private MonthAdapter adapter;
    private DailyMonthAdapater dailyMonthAdapater;
    private ArrayList<DayElement> daySchedule;



    public static MonthViewFragment newInstance() {
        return new MonthViewFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(MonthViewModel.class);
        binding = MonthViewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        selectedDate = LocalDate.now();

        calendarRecyclerView = root.findViewById(R.id.calendarRecyclerView);
        dailyMonthRecyclerView = binding.dailyMonthRV;
        monthYearText = root.findViewById(R.id.monthYearTV);
        calendarRecyclerView = binding.calendarRecyclerView;
        setMonthView();

        daySchedule = new ArrayList<>();
        elementList = new MutableLiveData<>();
        setDayViews();

        ActionBar bar = ActionBarSetter.setCustomActionBar(getActivity());
        View view = bar.getCustomView();
        ImageButton imageButton = view.findViewById(R.id.image_button_create);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*CalendarCreateFragment calendarCreateFragment = new CalendarCreateFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, calendarCreateFragment);
                transaction.addToBackStack(null);
                transaction.commit();*/
                Bundle bundle = new Bundle();
                bundle.putIntArray("currentDate", new int[] {selectedDate.getDayOfMonth(), selectedDate.getMonthValue(), selectedDate.getYear()});
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_calendar);

                navController.navigate(R.id.navigation_create, bundle);
            }
        });

        root.findViewById(R.id.arrow_left).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                previousMonthAction(root);
            }
        });

        root.findViewById(R.id.arrow_right).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                nextMonthAction(root);
            }
        });

        return root;

    }

    public void setDayViews() {
        CalendarAPI api = new CalendarAPI(getContext());
        List<FocusTime> focusTimes = api.getFocusTimes();
        for(FocusTime f: focusTimes) {
            int beginHour = f.getBeginTime().get(java.util.Calendar.HOUR_OF_DAY);
            int beginMinute = f.getBeginTime().get(java.util.Calendar.MINUTE);
            String date = f.getBeginTime().get(java.util.Calendar.DAY_OF_MONTH) + "." +
                    (f.getBeginTime().get(Calendar.MONTH)+1) + "." +
                    f.getBeginTime().get(Calendar.YEAR);
            //Log.e("date",  date);

            int duration = (int)(f.getEndTime().getTimeInMillis() - f.getBeginTime().getTimeInMillis()) / 1000 / 60;

            daySchedule.add(new DayElement(f.getTitle(),beginHour, beginMinute, duration, date,f.getFocusTimeLevel(), f.getId()));
        }
        elementList.setValue(daySchedule);


        elementList.observe(getViewLifecycleOwner(), dayElements -> {
            dailyMonthAdapater = new DailyMonthAdapater((Activity)getContext(), dayElements, this);
            dailyMonthRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            dailyMonthRecyclerView.setAdapter(dailyMonthAdapater);

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(dailyMonthAdapater));
            itemTouchHelper.attachToRecyclerView(dailyMonthRecyclerView);

        });
    }

    public void addSwipeToDelete() {

    }


    public void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(selectedDate);

        MonthAdapter calendarAdapter = new MonthAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mViewModel.getApplication(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    public static ArrayList<LocalDate> daysInMonthArray(LocalDate date)
    {
        ArrayList<LocalDate> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 1; i <= 42; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
            {
                daysInMonthArray.add(null);
            }
            else
            {
                daysInMonthArray.add(LocalDate.of(selectedDate.getYear(),selectedDate.getMonth(),i - dayOfWeek));
            }

        }
        return  daysInMonthArray;
    }

    public String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    public void previousMonthAction(View view)
    {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view)
    {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    public MonthViewModel getmViewModel() {
        return mViewModel;
    }


    @Override
    public void onItemClick(int position, LocalDate date)
    {
        if (date != null) {
            selectedDate = date;
            setMonthView();
        }
    }





}
