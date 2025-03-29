package com.example.utu;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EventCalendarFragment extends Fragment {
    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<EventItem> allEvents; // Static list of all events

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d("EventCalendar", "onAttach called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("EventCalendar", "onCreateView called");
        View view = inflater.inflate(R.layout.fragment_event_calendar, container, false);

        // Verify the view is not null
        if (view == null) {
            Log.e("EventCalendar", "Inflated view is null");
            return null;
        }

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.eventsRecyclerView);
        if (recyclerView == null) {
            Log.e("EventCalendar", "RecyclerView not found in layout");
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new EventAdapter(new ArrayList<>());
            recyclerView.setAdapter(adapter);
            Log.d("EventCalendar", "RecyclerView initialized");
        }

        // Initialize static event data
        allEvents = new ArrayList<>();
        allEvents.add(new EventItem("TATTVAHACK '25",
                "TattvaHack is being organized by August Infotech in collaboration with CGPIT",
                "Mar 08, 2025", "9:00 AM"));
        allEvents.add(new EventItem("UTU General Meet",
                "Classes for all departments after 1.30 PM are suspended",
                "Mar 10, 2025", "01:30 PM"));
        allEvents.add(new EventItem("Sports Day 2025",
                "Annual Sports Day with exciting competitions!",
                "Mar 10, 2025", "8:00 AM"));
        allEvents.add(new EventItem("Cultural Night 2025",
                "Cultural Night featuring student performances.",
                "Mar 15, 2025", "6:00 PM"));
        Log.d("EventCalendar", "Static events initialized: " + allEvents.size() + " events");

        // Set up CalendarView
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        if (calendarView == null) {
            Log.e("EventCalendar", "CalendarView not found in layout");
        } else {
            calendarView.setClickable(true);
            calendarView.setFocusable(true);
            calendarView.setFocusableInTouchMode(true);
            Log.d("EventCalendar", "Setting OnDateChangeListener for CalendarView");
            calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
                // Convert selected date to formatted string (MMM dd, yyyy)
                String selectedDate = String.format(Locale.getDefault(), "Mar %02d, %04d", dayOfMonth, year);
                Log.d("EventCalendar", "Selected date: " + selectedDate);

                // Filter events for the selected date
                List<EventItem> filteredEvents = new ArrayList<>();
                for (EventItem event : allEvents) {
                    if (event.getDate().equals(selectedDate)) {
                        filteredEvents.add(event);
                    }
                }
                Log.d("EventCalendar", "Filtered events for " + selectedDate + ": " + filteredEvents.size());

                adapter.updateEvents(filteredEvents);
                if (filteredEvents.isEmpty()) {
                    Toast.makeText(getContext(), "No events on " + selectedDate, Toast.LENGTH_SHORT).show();
                }
            });
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("EventCalendar", "onViewCreated called");
    }
}

// EventItem class to hold event data
class EventItem {
    private String title;
    private String description;
    private String date;
    private String time;

    public EventItem(String title, String description, String date, String time) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getTime() { return time; }
}

// EventAdapter for RecyclerView
class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private List<EventItem> eventList;

    public EventAdapter(List<EventItem> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventItem event = eventList.get(position);
        holder.titleTextView.setText(event.getTitle() != null ? event.getTitle() : "No Title");
        holder.descriptionTextView.setText(event.getDescription() != null ? event.getDescription() : "No Description");
        holder.dateTextView.setText(event.getDate() != null ? event.getDate() : "No Date");
        holder.timeTextView.setText(event.getTime() != null ? event.getTime() : "No Time");
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void updateEvents(List<EventItem> newEventList) {
        this.eventList = newEventList != null ? newEventList : new ArrayList<>();
        Log.d("EventCalendar", "Updating adapter with " + this.eventList.size() + " events");
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView, dateTextView, timeTextView;

        ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.eventTitle);
            descriptionTextView = itemView.findViewById(R.id.eventDescription);
            dateTextView = itemView.findViewById(R.id.eventDate);
            timeTextView = itemView.findViewById(R.id.eventTime);
        }
    }
}