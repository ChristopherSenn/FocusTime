package com.focustime.android.ui.calendar.month;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.focustime.android.R;

import java.time.LocalDate;
import java.util.ArrayList;

/*
Adapter class for displaying months
*/
public class MonthAdapter extends RecyclerView.Adapter<MonthViewHolder>
{
    private final ArrayList<LocalDate> daysOfMonth;
    private final OnItemListener onItemListener;

    public MonthAdapter(ArrayList<LocalDate> daysOfMonth, OnItemListener onItemListener)
    {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public MonthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new MonthViewHolder(view, onItemListener, daysOfMonth);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthViewHolder holder, int position)
    {
        final LocalDate date = daysOfMonth.get(position);
        if (date == null){
            holder.dayOfMonth.setText("");
        }else{
            holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));
            if (date.equals(MonthViewFragment.selectedDate))
                holder.parentView.setBackgroundColor(Color.LTGRAY);

        }

    }

    @Override
    public int getItemCount()
    {
        return daysOfMonth.size();
    }

    public interface  OnItemListener
    {
        void onItemClick(int position, LocalDate date);
    }
}