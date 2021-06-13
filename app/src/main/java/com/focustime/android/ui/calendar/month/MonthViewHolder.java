package com.focustime.android.ui.calendar.month;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.focustime.android.R;

import java.time.LocalDate;
import java.util.ArrayList;

public class MonthViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    //public final View parentView;
    public final TextView dayOfMonth;
    private final MonthAdapter.OnItemListener onItemListener;
    public MonthViewHolder(@NonNull View itemView, MonthAdapter.OnItemListener onItemListener)
    {
        super(itemView);
        //parentView = itemView.findViewById(R.id.parentView);
        dayOfMonth = itemView.findViewById(R.id.cellDayText);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        onItemListener.onItemClick(getAdapterPosition(), (String) dayOfMonth.getText());
    }
}
