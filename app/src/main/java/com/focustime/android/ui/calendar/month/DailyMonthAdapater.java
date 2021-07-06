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
import com.focustime.android.util.TimeFormatter;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Time;
import java.util.ArrayList;

public class DailyMonthAdapater extends RecyclerView.Adapter<DailyMonthAdapater.RecyclerViewViewHolder> {

    Activity context;
    ArrayList<DayElement> dayElements;
    MonthViewFragment monthViewFragment;

    private DayElement recentlyDeletedItem;
    private int recentlyDeletedItemPosition;
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
        String t = TimeFormatter.formatHourMinute(dayElement.getStartHour()) + ":" + TimeFormatter.formatHourMinute(dayElement.getStartMinute());
        holder.time.setText(t);
        holder.duration.setText(TimeFormatter.formatDuration(dayElement.getDuration()));
        holder.edit.setImageResource(R.drawable.edit_icon);
        holder.delete.setImageResource(R.drawable.trash_icon);



        holder.focusTimeLevel.setText(FocusTime.FOCUS_TIME_LEVELS.get(dayElement.getFocusTimeLevel()));
        int[] colors = {context.getColor(R.color.colorLevel0), context.getColor(R.color.colorLevel1), context.getColor(R.color.colorLevel2),};
        holder.focusTimeLevel.setTextColor(colors[dayElement.getFocusTimeLevel()]);

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
                intent.putExtra("focusTimeLevel", dayElement.getFocusTimeLevel());
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

    /**
     * Deletes an item at a given position
     * @param position
     */
    public void deleteItem(int position) {
        recentlyDeletedItem = dayElements.get(position);
        recentlyDeletedItemPosition = position;
        dayElements.remove(position);

        notifyItemRemoved(position);

        api.deleteFocusTime(getContext(), api.getFocusTimeById(recentlyDeletedItem.getDbId())); //Delete item from db
        showUndoSnackbar(); // Show a snackbar to undo the delete
    }

    /**
     * Creates a Snackbar that can undo the last delete of an item
     */
    private void showUndoSnackbar() {
        View view = context.findViewById(R.id.day_view_list_element);
        Snackbar snackbar = Snackbar.make(view, "You deleted " + recentlyDeletedItem.getTitle(),
                Snackbar.LENGTH_LONG);
        snackbar.setAction("Undo", v -> undoDelete()); // Map the actual undo function
        snackbar.show();
    }

    /**
     * Readd the deleted item to the ArrayList and the Database
     */
    private void undoDelete() {


        FocusTime f = FocusTimeFactory.buildFocusTimeFromDayElement(recentlyDeletedItem);
        long newId = api.createFocusTime(f, getContext()); //Add item back to database
        recentlyDeletedItem.setDbId(newId);

        dayElements.add(recentlyDeletedItemPosition, recentlyDeletedItem); //Add item back to List

        notifyItemInserted(recentlyDeletedItemPosition);
    }

    class RecyclerViewViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView time;
        TextView duration;
        TextView date;
        ImageButton edit;
        ImageButton delete;
        TextView focusTimeLevel;

        public RecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title1);
            time = itemView.findViewById(R.id.time1);
            duration = itemView.findViewById(R.id.focusDuration);
            date = itemView.findViewById(R.id.date1);
            edit = itemView.findViewById(R.id.editButton);
            delete = itemView.findViewById(R.id.deleteButton);
            focusTimeLevel = itemView.findViewById(R.id.textViewfocustimelevel);
        }
    }


}
