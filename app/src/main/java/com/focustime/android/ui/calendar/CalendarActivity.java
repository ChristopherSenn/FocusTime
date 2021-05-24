package com.focustime.android.ui.calendar;

//import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.focustime.android.R;

import com.focustime.android.databinding.ActivityCalendarBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;




public class CalendarActivity extends AppCompatActivity {

    private ActivityCalendarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_calendar);
        binding = ActivityCalendarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_day, R.id.navigation_create, R.id.navigation_focusButton).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_calendar);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);



    }
}