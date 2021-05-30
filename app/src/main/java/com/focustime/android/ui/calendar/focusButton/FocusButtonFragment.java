package com.focustime.android.ui.calendar.focusButton;

import android.Manifest;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.focustime.android.R;
import com.focustime.android.databinding.CalendarDayFragmentBinding;
import com.focustime.android.databinding.FocusButtonFragmentBinding;
import com.focustime.android.ui.calendar.day.CalendarDayViewModel;

import java.io.File;
import java.util.Locale;
import java.util.Objects;


public class FocusButtonFragment extends Fragment {
    private static final int REQUEST_CODE_NOTIFICATION_POLICY_PERMISSION = 1;

    private FocusButtonViewModel mViewModel;
    private FocusButtonFragmentBinding binding;
    private NotificationManager mNotificationManager;

    private TextView mTextViewCountdown;
    private Button mButtonStartStop;
    private ImageView mImageView;

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;

    private int mHour, mMinute;

    public static FocusButtonFragment newInstance() {
        return new FocusButtonFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.calendar_day_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(FocusButtonViewModel.class);
        binding = FocusButtonFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mTextViewCountdown = binding.textViewCountdown;
        mButtonStartStop  = binding.buttonStartStop;
        mImageView = binding.imageView;

        mButtonStartStop.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    stopTimer();
                } else {
                    startTimer();
                }
            }
        });

        mTextViewCountdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomTimePickerDialog customTimePickerDialog = new CustomTimePickerDialog(FocusButtonFragment.this.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Display Selected time in textbox
                                if (hourOfDay < 10 && minute < 10) {
                                    mTextViewCountdown.setText("0" + hourOfDay + ":" + "0" + minute );
                                } else if (minute < 10) {
                                    mTextViewCountdown.setText(hourOfDay + ":" + "0" + minute);
                                } else {
                                    mTextViewCountdown.setText(hourOfDay + ":" + minute);
                                }
                            }
                        }, mHour, mMinute, true);
                //customTimePickerDialog.updateTime(5, 5);
                customTimePickerDialog.show();
            }
        });

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startTimer() {
        mNotificationManager = (NotificationManager) FocusButtonFragment.this.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // Check if the notification policy access has been granted for the app.
        if (!mNotificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        } else {
            mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);

            mImageView.setImageResource(0);

            String[] time = mTextViewCountdown.getText().toString().split(":");
            mHour = Integer.parseInt(time[0]);
            mMinute = Integer.parseInt(time[1]);
            mTimeLeftInMillis = (mHour * 60 + mMinute) * 60 * 1000;

            mTimerRunning = true;
            mButtonStartStop.setText("give up");

            mTextViewCountdown.setClickable(false);

            mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mTimeLeftInMillis = millisUntilFinished;
                    updateCountDownText();
                }

                @Override
                public void onFinish() {
                    if(mCountDownTimer != null) {
                        mCountDownTimer.cancel();
                    }

                    mTimerRunning = false;
                    mButtonStartStop.setText("start");

                    mTextViewCountdown.setClickable(true);
                    cancelDND();
                }
            }.start();


        }
    }

    private void updateCountDownText() {
        int minutes = (int) mTimeLeftInMillis / 1000 / 60;
        int seconds = (int) mTimeLeftInMillis / 1000 % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        mTextViewCountdown.setText(timeLeftFormatted);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void stopTimer() {
        mTimeLeftInMillis = 0;
        updateCountDownText();
        mTimerRunning = false;
        mCountDownTimer.cancel();
        mImageView.setImageResource(R.drawable.tryharder);
        mButtonStartStop.setText("start");
        mTextViewCountdown.setClickable(true);
        cancelDND();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void cancelDND() {
        mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
    }
}
