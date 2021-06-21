package com.focustime.android.ui.calendar;

//import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.focustime.android.R;

import com.focustime.android.data.service.FocusTimeService;
import com.focustime.android.databinding.ActivityCalendarBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Arrays;


public class CalendarActivity extends AppCompatActivity {
    private final int CALLBACK_ID = 42;

    private ActivityCalendarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_calendar);




        // Check if we have permission to access the users phone calendar database
        checkPermission(CALLBACK_ID, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR);

        binding = ActivityCalendarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_day, R.id.navigation_create, R.id.navigation_focusButton).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_calendar);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


    }

    /**
     * Checks if the user has given their permission for our app to use the specified "features" of their phone and asks them to give their permission if not
     *
     * @param callbackId Callback ID to identify the permission check later on
     * @param permissionsId IDs of the permissions that are checked
     */
    private void checkPermission(int callbackId, String... permissionsId) {
        boolean permissions = true;
        for (String p : permissionsId) {
            permissions = permissions && ContextCompat.checkSelfPermission(this, p) == PackageManager.PERMISSION_GRANTED;
        }

        if (!permissions)
            ActivityCompat.requestPermissions(this, permissionsId, callbackId);
    }


}