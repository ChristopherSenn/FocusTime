package com.focustime.android.ui.calendar.focusButton;

import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
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
import com.focustime.android.data.service.AlarmCongratulationService;
import com.focustime.android.databinding.FocusButtonFragmentBinding;
import com.focustime.android.util.FocusTimeServiceStarter;


import java.util.Locale;


public class FocusButtonFragment extends Fragment {

    private FocusButtonViewModel mViewModel;
    private FocusButtonFragmentBinding binding;
    private NotificationManager mNotificationManager;

    private TextView mTextViewCountdown;
    private Button mButtonStartStop;
    private Button mTestButton;
    private ImageView mImageView;

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mStartTimeInMills;
    private long mTimeLeftInMillis;
    private long mEndTime;

    private int mHour, mMinute;

    private Intent notificationIntent;
    private FocusTimeServiceStarter focusTimeServiceStarter;

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

        focusTimeServiceStarter = new FocusTimeServiceStarter();
        mTextViewCountdown = binding.textViewCountdown;
        mButtonStartStop = binding.buttonStartStop;
        mImageView = binding.imageView;
        mTestButton = binding.buttonTest;

        mButtonStartStop.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    stopTimer();
                    //stopAlarmCongratulationService();
                    focusTimeServiceStarter.stopAlarmCongratulationService(getContext());
                } else {
                    startTimer();
                    //startAlarmCongratulationService();
                    focusTimeServiceStarter.startAlarmCongratulationService(getContext(), mStartTimeInMills);
                }
            }
        });

        mTestButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    stopTimer();
                    //stopAlarmCongratulationService();
                    focusTimeServiceStarter.stopAlarmCongratulationService(getContext());
                } else {
                    mStartTimeInMills = 10000;
                    mTimeLeftInMillis = 10000;
                    startTimer();
                    //startAlarmCongratulationService();
                    focusTimeServiceStarter.startAlarmCongratulationService(getContext(), mStartTimeInMills);
                }
            }
        });

        mTextViewCountdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStartTimeInMills != 0) {
                    mHour = (int) (mStartTimeInMills / 1000) / 3600;
                    mMinute = (int) mStartTimeInMills / 1000 / 60 - mHour * 60;
                }

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
                                mStartTimeInMills = (mHour * 60 + mMinute) * 60 * 1000;
                                mTimeLeftInMillis = mStartTimeInMills;
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
            if (mStartTimeInMills == 0) {
                Toast.makeText(FocusButtonFragment.this.getContext(), "please set a focustime", Toast.LENGTH_SHORT).show();
            } else {
                focusTimeServiceStarter.activateDND(getContext());
                mImageView.setImageResource(0);

                mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
                mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        mTimeLeftInMillis = millisUntilFinished;
                        updateCountDownText();
                    }

                    @Override
                    public void onFinish() {
                        focusTimeServiceStarter.cancelDND(getContext());

                        mCountDownTimer.cancel();
                        mTimerRunning = false;
                        mImageView.setImageResource(R.drawable.congratulation);

                        updateCountDownText(mStartTimeInMills);
                        updateComponents();
                    }
                }.start();

                mTimerRunning = true;
                updateComponents();
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void stopTimer() {
        mTimerRunning = false;
        mCountDownTimer.cancel();

        mImageView.setImageResource(R.drawable.tryharder);

        mCountDownTimer.cancel();
        updateCountDownText(mStartTimeInMills);
        updateComponents();
        focusTimeServiceStarter.cancelDND(getContext());
    }

    private void updateCountDownText() {
        int minutes = (int) mTimeLeftInMillis / 1000 / 60;
        int seconds = (int) mTimeLeftInMillis / 1000 % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountdown.setText(timeLeftFormatted);
    }

    private void updateCountDownText(long mStartTimeInMills) {
        int minutes = (int) mStartTimeInMills / 1000 / 60;
        int seconds = (int) mStartTimeInMills / 1000 % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountdown.setText(timeLeftFormatted);

        mTimeLeftInMillis = mStartTimeInMills;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void activateDND() {
        mNotificationManager = (NotificationManager) FocusButtonFragment.this.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void cancelDND() {
        mNotificationManager = (NotificationManager) FocusButtonFragment.this.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
    }

    private void updateComponents() {
        if (mTimerRunning) {
            mButtonStartStop.setText("give up");
            mTextViewCountdown.setClickable(false);
        } else {
            mButtonStartStop.setText("start");
            mTextViewCountdown.setClickable(true);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences preferences = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);

        mStartTimeInMills = preferences.getLong("startTimeInMillis", 600000);
        mTimeLeftInMillis = preferences.getLong("millisLeft", mStartTimeInMills);
        mTimerRunning = preferences.getBoolean("timeRunning", false);

        updateCountDownText();
        updateComponents();

        if (mTimerRunning) {
            mEndTime = preferences.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;

                updateCountDownText(mStartTimeInMills);
                updateComponents();
                focusTimeServiceStarter.cancelDND(getContext());

                mImageView.setImageResource(R.drawable.congratulation);
            } else {
                startTimer();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        SharedPreferences preferences = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putLong("startTimeInMillis",mStartTimeInMills);
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timeRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);

        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }
}