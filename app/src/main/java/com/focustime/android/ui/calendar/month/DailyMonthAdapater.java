package com.focustime.android.ui.calendar.month;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.focustime.android.R;
import com.focustime.android.data.model.FocusTime;
import com.focustime.android.data.service.CalendarAPI;
import com.focustime.android.ui.calendar.day.DayElement;
import com.focustime.android.ui.calendar.edit.CalendarEditFragment;
import com.focustime.android.util.FocusTimeFactory;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class DailyMonthAdapater extends RecyclerView.Adapter<DailyMonthAdapater.RecyclerViewViewHolder> {

    Activity context;
    ArrayList<DayElement> dayElements;
    MonthViewFragment monthViewFragment;

    private DayElement mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;
    private  CalendarAPI api;

    public DailyMonthAdapater(Activity context, ArrayList<DayElement> userArrayList, MonthViewFragment cdf) {
        this.context = context;
        this.dayElements = userArrayList;
        this.monthViewFragment = cdf;
        this.api = new CalendarAPI(getContext());

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
        holder.duration.setText(dayElement.getDuration().toString());
        holder.edit.setImageResource(R.drawable.edit_icon);
        holder.delete.setImageResource(R.drawable.trash_icon);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                monthViewFragment.getmViewModel().deleteApiEntry(dayElements.get(position).getDbId(), context);
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

    public Activity getContext() {
        return context;
    }

    public void deleteItem(int position) {
        mRecentlyDeletedItem = dayElements.get(position);
        mRecentlyDeletedItemPosition = position;
        dayElements.remove(position);
        notifyItemRemoved(position);
        api.deleteFocusTime(getContext(), api.getFocusTimeById(mRecentlyDeletedItem.getDbId()));
        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        View view = context.findViewById(R.id.day_view_list_element);
        Snackbar snackbar = Snackbar.make(view, "You deleted " + mRecentlyDeletedItem.getTitle(),
                Snackbar.LENGTH_LONG);
        snackbar.setAction("Undo", v -> undoDelete());
        snackbar.show();
    }

    private void undoDelete() {
        dayElements.add(mRecentlyDeletedItemPosition,
                mRecentlyDeletedItem);
        FocusTime f = FocusTimeFactory.buildFocusTimeFromDayElement(mRecentlyDeletedItem);
        api.createFocusTime(f, getContext());
        notifyItemInserted(mRecentlyDeletedItemPosition);
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
