package com.focustime.android.ui.calendar.month;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.focustime.android.R;

import java.time.LocalDate;
import java.util.ArrayList;


public class MonthViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    private final ArrayList<LocalDate> daysOfMonth;
    public final View parentView;
    public final TextView dayOfMonth;
    private final MonthAdapter.OnItemListener onItemListener;
    public final ImageView dot_imageView;
    public MonthViewHolder(@NonNull View itemView, MonthAdapter.OnItemListener onItemListener, ArrayList<LocalDate> daysOfMonth)
    {
        super(itemView);
        parentView = itemView.findViewById(R.id.parentView);
        dayOfMonth = itemView.findViewById(R.id.cellDayText);
        dot_imageView = itemView.findViewById(R.id.dot_focusTime);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
        this.daysOfMonth = daysOfMonth;
    }

    @Override
    public void onClick(View view)
    {
        onItemListener.onItemClick(getAdapterPosition(), daysOfMonth.get(getAdapterPosition()));
    }
}
