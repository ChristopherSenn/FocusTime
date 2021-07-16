package com.focustime.android.util;

import android.app.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ActionBarSetter {
    // Used for the Plus Icon on the Calendar Actionbar
    public static ActionBar setCustomActionBar(Activity activity) {
        ActionBar bar = ((AppCompatActivity)activity).getSupportActionBar();
        bar.setDisplayShowCustomEnabled(true);
        return bar;
    }

    public static void setDefaultActionBar(Activity activity) {
        ActionBar bar = ((AppCompatActivity)activity).getSupportActionBar();
        bar.setDisplayShowCustomEnabled(false);

    }
}
