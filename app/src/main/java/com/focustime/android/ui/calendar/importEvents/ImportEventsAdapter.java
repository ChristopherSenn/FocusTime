package com.focustime.android.ui.calendar.importEvents;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.focustime.android.data.service.CalendarAPI;
import com.focustime.android.ui.calendar.create.CalendarCreateFragment;
import com.focustime.android.ui.calendar.day.DayElement;
import com.focustime.android.util.FocusTimeFactory;
import com.focustime.android.util.TimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.everything.providers.android.calendar.Event;

public class ImportEventsAdapter extends RecyclerView.Adapter <ImportEventsAdapter.RecyclerViewViewHolder> {

    private Activity context;
    private List<Event> events;
    private CalendarAPI api;


    public ImportEventsAdapter(Activity context, List<Event> userArrayList) {
        this.context = context;
        this.events = userArrayList;
        api = new CalendarAPI(context);
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

        /**
         * Set the Text fields according to the event information
         */
        holder.title.setText(event.title);

        Calendar beginDate = Calendar.getInstance();
        beginDate.setTimeInMillis(event.dTStart);
        String stringDate = beginDate.get(Calendar.DAY_OF_MONTH) + "." + (beginDate.get(Calendar.MONTH) + 1) + "." + beginDate.get(Calendar.YEAR);

        String[] days = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
        String dayOfWeek = days[beginDate.get(Calendar.DAY_OF_WEEK) - 1];
        stringDate = dayOfWeek + ", " + stringDate;

        holder.date.setText(stringDate);

        String beginHour = TimeFormatter.formatHourMinute(beginDate.get(Calendar.HOUR_OF_DAY));
        String beginMinute = TimeFormatter.formatHourMinute(beginDate.get(Calendar.MINUTE));
        String eventCode = null;

        String endHour, endMinute;
        Calendar endDate = Calendar.getInstance();
        if (event.dTend != 0) {

            endDate.setTimeInMillis(event.dTend);

        } else { //This happens if an event is marked as repeating
            if(event.duration == null && event.dTend == 0) { // Error handling for faulty db entries
                endDate.setTimeInMillis(beginDate.getTimeInMillis());
            } else {
                String duration = event.duration;
                duration = duration.substring(0, duration.length() - 1);
                duration = duration.substring(1);


                String[] codes =  {"DAILY", "WEEKLY", "MONTHLY"};
                eventCode = event.rRule.split(";")[0].substring(5);


                long deltaMs = Long.parseLong(duration) * 1000;
                endDate.setTimeInMillis(beginDate.getTimeInMillis() + deltaMs);
            }


        }

        endHour = TimeFormatter.formatHourMinute(endDate.get(Calendar.HOUR_OF_DAY));
        endMinute = TimeFormatter.formatHourMinute(endDate.get(Calendar.MINUTE));

        String stringTime = beginHour + ":" + beginMinute + " - " + endHour + ":" + endMinute;
        if(eventCode != null) stringDate = stringDate + " - " + eventCode.substring(0, 1) + eventCode.substring(1).toLowerCase();
        holder.date.setText(stringDate);

        holder.time.setText(stringTime);

        if(!event.description.equals("")) {
            holder.description.setText(event.description);
        } else {
            holder.description.setVisibility(View.GONE);
        }


        //Remove the Event from the list
        holder.dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                events.remove(position);
                notifyDataSetChanged();
            }
        });

        // Add the Event as a FocusTime and remove it from the list
        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Select Focus Time Level");
                String[] options = FocusTime.FOCUS_TIME_LEVELS.toArray(new String[0]);
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        event.title = event.title + "#" + which;

                        if(event.rRule != null) {
                            String eventCode = event.rRule.split(";")[0].substring(5);
                            long oneDayMs = 86400000;
                            event.dTend = endDate.getTimeInMillis();

                            switch (eventCode) {
                                case "DAILY":
                                    for(int i = 0; i < 30; i++) {
                                        FocusTime importFocusTime = FocusTimeFactory.buildFocusTime(event);
                                        api.createFocusTime(importFocusTime, context);

                                        event.dTStart += oneDayMs;
                                        event.dTend += oneDayMs;
                                    }
                                    break;
                                case "WEEKLY":
                                    for(int i = 0; i < 10; i++) {
                                        FocusTime importFocusTime = FocusTimeFactory.buildFocusTime(event);
                                        api.createFocusTime(importFocusTime, context);

                                        event.dTStart += oneDayMs * 7;
                                        event.dTend += oneDayMs * 7;
                                    }
                                    break;
                                case "MONTHLY":
                                    for(int i = 0; i < 3; i++) {
                                        FocusTime importFocusTime = FocusTimeFactory.buildFocusTime(event);
                                        api.createFocusTime(importFocusTime, context);

                                        beginDate.add(Calendar.MONTH, 1);
                                        endDate.add(Calendar.MONTH, 1);
                                        event.dTStart = beginDate.getTimeInMillis();
                                        event.dTend = endDate.getTimeInMillis();
                                    }
                            }
                        } else {

                                FocusTime importFocusTime = FocusTimeFactory.buildFocusTime(event);

                                api.createFocusTime(importFocusTime, context);

                        }

                        events.remove(position);
                        notifyDataSetChanged();




                    }
                });
                builder.show();


            }
        });
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