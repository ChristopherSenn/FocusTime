package com.focustime.android.ui.calendar.month;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.focustime.android.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class MonthViewHolder extends RecyclerView.ViewHolder
{

    private final ArrayList<LocalDate> daysOfMonth;
    public final View parentView;
    public final TextView dayOfMonth;
    private final MonthAdapter.OnItemListener onItemListener;
    public final ImageView dot_imageView;
    private MonthAdapter monthAdapter;



    public MonthViewHolder(@NonNull View itemView, MonthAdapter.OnItemListener onItemListener, ArrayList<LocalDate> daysOfMonth, ArrayList<Boolean> isFocusTimSet, MonthAdapter monthAdapter)
    {
        super(itemView);
        parentView = itemView.findViewById(R.id.parentView);
        dayOfMonth = itemView.findViewById(R.id.cellDayText);
        dot_imageView = itemView.findViewById(R.id.dot_focusTime);
        this.monthAdapter = monthAdapter;
        this.onItemListener = onItemListener;
        //itemView.setOnClickListener(this);
        this.daysOfMonth = daysOfMonth;

    }

}
