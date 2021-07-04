package com.focustime.android.ui.calendar.day;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.focustime.android.R;
import com.focustime.android.ui.calendar.edit.CalendarEditFragment;

import java.util.ArrayList;

public class CalenderDayAdapter extends RecyclerView.Adapter <CalenderDayAdapter.RecyclerViewViewHolder> {

    Activity context;
    ArrayList<DayElement> dayElements;
    CalendarDayFragment calendarDayFragment;

    public CalenderDayAdapter(Activity context, ArrayList<DayElement> userArrayList, CalendarDayFragment cdf) {
        this.context = context;
        this.dayElements = userArrayList;
        this.calendarDayFragment = cdf;
    }


    @NonNull
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.day_view_list_element, parent,false);
        RecyclerViewViewHolder rvh = new RecyclerViewViewHolder(rootView);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewViewHolder holder, int position) {
        DayElement dayElement = dayElements.get(position);

        holder.title.setText(dayElement.getTitle());
        holder.date.setText(dayElement.getDate());
        String t = dayElement.getStartHour() + ":" + dayElement.getStartMinute();
        holder.time.setText(t);
        int d = dayElement.getDuration();
        int h = d/60; //division rounded down
        int m = d%60; //modulo
        String dur = h+":"+m;
        holder.duration.setText(dur);
        holder.edit.setImageResource(R.drawable.edit_icon);
        holder.delete.setImageResource(R.drawable.trash_icon);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                calendarDayFragment.getmViewModel().deleteApiEntry(dayElements.get(position).getDbId(), context);
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(context, CalendarEditFragment.class);
                intent.putExtra("id", dayElement.getDbId());
                intent.putExtra("hour", dayElement.getStartHour());
                intent.putExtra("minute", dayElement.getStartMinute());
                intent.putExtra("duration", dayElement.getDuration());
                intent.putExtra("title", dayElement.getTitle());
                intent.putExtra("date", dayElement.getDate());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return dayElements.size();
    }

    class RecyclerViewViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView time;
        TextView duration;
        TextView date;
        ImageButton edit;
        ImageButton delete;

        public RecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title1);
            time = itemView.findViewById(R.id.time1);
            duration = itemView.findViewById(R.id.focusDuration);
            date = itemView.findViewById(R.id.date1);
            edit = itemView.findViewById(R.id.editButton);
            delete = itemView.findViewById(R.id.deleteButton);

        }
    }
}
