package com.focustime.android.ui.calendar.focusButton;

import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;

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

import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;

import com.focustime.android.R;
import com.focustime.android.databinding.FocusButtonFragmentBinding;

import java.util.Locale;


public class FocusButtonFragment extends Fragment {

    private FocusButtonViewModel mViewModel;
    private FocusButtonFragmentBinding binding;
    private NotificationManager mNotificationManager;

    private TextView mTextViewCountdown;
    private Button mButtonStartStop;
    private ImageView mImageView;

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;

    private int mHour = 0, mMinute = 10;

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
        mButtonStartStop = binding.buttonStartStop;
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
                                if (hourOfDay == 0 && minute < 10) {
                                    mHour = hourOfDay;
                                    mMinute = minute;
                                    mTextViewCountdown.setText("0" + minute + ":" + "00");
                                } else {
                                    mHour = hourOfDay;
                                    mMinute = minute;
                                    mTextViewCountdown.setText(hourOfDay * 60 + minute + ":" + "00");
                                }
                            }
                        }, mHour, mMinute, true);
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
            String[] time = mTextViewCountdown.getText().toString().split(":");
            mMinute = Integer.parseInt(time[0]);
            mTimeLeftInMillis = mMinute * 60 * 1000;

            if (mTimeLeftInMillis == 0) {
                Toast.makeText(FocusButtonFragment.this.getContext(), "please set a focustime", Toast.LENGTH_SHORT).show();
            } else {
                mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);

                mImageView.setImageResource(0);

                mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        mTimeLeftInMillis = millisUntilFinished;
                        updateCountDownText();
                    }

                    @Override
                    public void onFinish() {
                        mCountDownTimer.cancel();
                        mTimerRunning = false;
                        mButtonStartStop.setText("start");

                        mTextViewCountdown.setClickable(true);
                        cancelDND();
                    }
                }.start();

                mTimerRunning = true;
                mButtonStartStop.setText("give up");

                mTextViewCountdown.setClickable(false);
            }

        }
    }

    private void updateCountDownText() {
        int minutes = (int) mTimeLeftInMillis / 1000 / 60;
        int seconds = (int) mTimeLeftInMillis / 1000 % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountdown.setText(timeLeftFormatted);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void stopTimer() {
        mTimeLeftInMillis = mMinute * 60 * 1000;
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