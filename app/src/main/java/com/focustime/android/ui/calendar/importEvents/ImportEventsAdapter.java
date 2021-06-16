package com.focustime.android.ui.calendar.importEvents;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.focustime.android.R;
import com.focustime.android.data.model.FocusTime;
import com.focustime.android.ui.calendar.day.DayElement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.everything.providers.android.calendar.Event;

public class ImportEventsAdapter extends RecyclerView.Adapter <ImportEventsAdapter.RecyclerViewViewHolder> {

    Activity context;
    List<Event> events;

    public ImportEventsAdapter(Activity context, List<Event> userArrayList) {
        this.context = context;
        this.events = userArrayList;
    }


    @NonNull
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.import_card, parent,false);
        RecyclerViewViewHolder rvh = new RecyclerViewViewHolder(rootView);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewViewHolder holder, int position) {
        Event event = events.get(position);

        holder.title.setText(event.title);

        Calendar beginDate = Calendar.getInstance();
        beginDate.setTimeInMillis(event.dTStart);
        String stringDate = beginDate.get(Calendar.DAY_OF_MONTH) + "." + (beginDate.get(Calendar.MONTH) + 1) + "." + beginDate.get(Calendar.YEAR);
        Log.e("asdlkj", event.dTStart+"");
        holder.date.setText(stringDate);

        Calendar endDate = Calendar.getInstance();
        endDate.setTimeInMillis(event.dTend);
        String beginHour = formatHourMinute(beginDate.get(Calendar.HOUR_OF_DAY));
        String beginMinute = formatHourMinute(beginDate.get(Calendar.MINUTE));
        String endHour = formatHourMinute(endDate.get(Calendar.HOUR_OF_DAY));
        String endMinute = formatHourMinute(endDate.get(Calendar.MINUTE));
        String stringTime = beginHour + ":" + beginMinute + " - " + endHour + ":" + endMinute;
        holder.time.setText(stringTime);

        holder.description.setText(event.description);
    }

    private String formatHourMinute(int i) {
        if(i < 10) return "0"+i;
        return i+"";
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class RecyclerViewViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView date;
        TextView time;
        TextView description;

        Button dismiss, save;

        public RecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textView_importCard_title);
            date = itemView.findViewById(R.id.textView_importCard_date);
            time = itemView.findViewById(R.id.textView_importCard_time);
            description = itemView.findViewById(R.id.textView_importCard_description);
            dismiss = itemView.findViewById(R.id.button_importCard_dismiss);
            save = itemView.findViewById(R.id.button_importCard_import);
        }
    }
}