package com.focustime.android.ui.calendar.importEvents;

import android.app.Activity;
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
        holder.date.setText(event.title);
        String t = event.title + ":" + event.title;
        holder.time.setText(t);
        holder.description.setText(event.description);
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