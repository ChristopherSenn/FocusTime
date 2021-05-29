package com.focustime.android.ui.calendar.day;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.focustime.android.R;

import java.util.ArrayList;

public class CalenderDayAdapter extends RecyclerView.Adapter <CalenderDayAdapter.RecyclerViewViewHolder> {

    Activity context;
    ArrayList<DayElement> dayElements;

    public CalenderDayAdapter(Activity context, ArrayList<DayElement> userArrayList) {
        this.context = context;
        this.dayElements = userArrayList;
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
        holder.comment.setText(dayElement.getComment());
        holder.type.setText(dayElement.getType());
    }

    @Override
    public int getItemCount() {
        return dayElements.size();
    }

    class RecyclerViewViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView comment;
        TextView type;

        public RecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textView1);
            comment = itemView.findViewById(R.id.textView2);
            type = itemView.findViewById(R.id.type);

        }
    }
}
