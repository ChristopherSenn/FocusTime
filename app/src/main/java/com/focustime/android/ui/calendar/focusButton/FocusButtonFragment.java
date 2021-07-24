package com.focustime.android.ui.calendar.focusButton;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.focustime.android.databinding.FocusButtonFragmentBinding;
import com.focustime.android.util.ActionBarSetter;
import com.focustime.android.util.FocusTimeServiceStarter;


import java.util.Locale;

/**
 * Fragment for the display of view about setting focus time manually
 */
public class FocusButtonFragment extends Fragment {

    private FocusButtonViewModel mViewModel;
    private FocusButtonFragmentBinding binding;
    private NotificationManager mNotificationManager;

    private TextView mTextViewCountdown;
    private Button mButtonStartStop;
    private ImageView mImageView;

    private TimerCircle timerCircle;

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mStartTimeInMills;
    private long mTimeLeftInMillis;
    private long mEndTime;
    private int dndLevel = 2;
    private int mHour, mMinute;

    private Intent notificationIntent;
    private FocusTimeServiceStarter focusTimeServiceStarter;

    public static FocusButtonFragment newInstance() {
        return new FocusButtonFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(FocusButtonViewModel.class);
        binding = FocusButtonFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ActionBarSetter.setDefaultActionBar(getActivity());

        focusTimeServiceStarter = new FocusTimeServiceStarter();
        mTextViewCountdown = binding.textViewCountdown;
        mButtonStartStop = binding.buttonStartStop;
        mImageView = binding.imageView;

        timerCircle = binding.timer;

        /**
         * set start and stop button
         */
        mButtonStartStop.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    stopTimer();

                    focusTimeServiceStarter.stopAlarmCongratulationService(getContext());
                } else {
                    showDNDTypesDialog();
                }
            }
        });


        /**
         * set CustomTimePickerDialog on a Button
         */
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
                customTimePickerDialog.setTitle("Set Time in Hours : Minutes");
                customTimePickerDialog.show();
            }
        });

        timerCircle.setFinishListenter(new TimerCircle.onFinishListener() {
            @Override
            public void onFinish() {
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
                focusTimeServiceStarter.activateDNDWithLevel(getContext(), dndLevel);
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

                        updateCountDownText(mStartTimeInMills);
                        updateComponents();
                    }
                }.start();

                mTimerRunning = true;

                int mDuration = Long.valueOf(mTimeLeftInMillis).intValue();
                int mMaxTime = Long.valueOf(mStartTimeInMills).intValue();
                int currentTime = Long.valueOf(mStartTimeInMills - mTimeLeftInMillis).intValue();
                timerCircle.setDuration(mDuration, mMaxTime, currentTime);
                timerCircle.setVisibility(View.VISIBLE);

                updateComponents();
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void stopTimer() {
        mTimerRunning = false;
        mTimeLeftInMillis = mStartTimeInMills;

        mCountDownTimer.cancel();

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
    private void cancelDND() {
        mNotificationManager = (NotificationManager) FocusButtonFragment.this.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
    }

    private void updateComponents() {
        if (mTimerRunning) {
            mButtonStartStop.setText("Give Up");
            mTextViewCountdown.setClickable(false);
        } else {
            mButtonStartStop.setText("Start FocusTime");
            mTextViewCountdown.setClickable(true);
        }
    }

    /**
     * Read the necessary information from SharedPreferences
     */
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

            } else {
                startTimer();
            }
        }
    }


    /**
     * save the necessary information from SharedPreferences
     */
    @Override
    public void onStop() {
        super.onStop();

        SharedPreferences preferences = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();


        if(!mTimerRunning == false) {
            editor.putLong("startTimeInMillis",mStartTimeInMills);
            editor.putLong("millisLeft", mTimeLeftInMillis);
            editor.putBoolean("timeRunning", mTimerRunning);
            editor.putLong("endTime", mEndTime);
        }


        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    /**
     * When user click the start button, a dialog pops up to set the dnd level
     */
    private void showDNDTypesDialog(){
        final String[] items = { "Priority only", "Alarms only", "Total Silence" };
        dndLevel = 0;

        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(this.getContext());
        singleChoiceDialog.setTitle("Differnt DND type");
        // The second parameter is the default option and is set to 0 here
        singleChoiceDialog.setSingleChoiceItems(items, 2,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dndLevel = which;
                    }
                });
        singleChoiceDialog.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startTimer();
                        focusTimeServiceStarter.startAlarmCongratulationService(getContext(), mStartTimeInMills, "Custom FocusTime");
                    }
                });
        singleChoiceDialog.show();
    }
}