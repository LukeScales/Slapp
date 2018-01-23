/*
 * Created by Kris Vanhoutte.
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 10/12/16 8:17 PM
 */

package com.getslapp.slapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Fragment for displaying the Calendar.
 *
 * Display layout for this fragment is described within its XML file
 * (fragment_calendar_fragment.xml).
 *
 * When a date is selected in the calendar, the shared preferences
 * for the calendar are accessed and the events for that particular
 * date are displayed in a list view below the calendar.
 *
 * The Calendar used here is the CalendarView described in the
 * CalendarView.java class
 *
 * The format of the calendar shared preferences is:
 *
 * {
 *     "Date 1":[
 *          {
 *              "starttime" : "time1",
 *              "endtime" : "time2",
 *              "name" : "name1",
 *              "description" : "description1",
 *              "location" : "location1",
 *              "colour" : "colour1"
 *          },
 *          {
 *              ...
 *          }
 *     ],
 *     "Date 2":[
 *          ...
 *     ],
 *     ...
 * }
 */
public class CalendarFragment extends ListFragment {

    ArrayList<Date> events;
    ArrayList<CalendarListItem> items;
    String selected_date = null;
    CalendarItemAdapter calendar_item_adapter;

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar_fragment, container, false);
        events = new ArrayList<>();
        items = new ArrayList<>();

        initialiseCalendar(view);

        DateFormat df = SimpleDateFormat.getDateInstance();
        Date date = new Date();
        selected_date = df.format(date);

        updateList(selected_date);

        FloatingActionButton button = (FloatingActionButton)view.findViewById(R.id.calendar_action_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).openCalendarEvent(selected_date);
            }
        });

        calendar_item_adapter = new CalendarItemAdapter(getActivity(), items);
        setListAdapter(calendar_item_adapter);

        return view;
    }

    public void initialiseCalendar(View view)
    {
        String string_calendar = ((MainActivity)getActivity()).sharedPref.getString(getString(R.string.shared_pref_calendar), "");
        if(!string_calendar.equals(""))
        {
            try {
                JSONObject json_calendar = new JSONObject(string_calendar);
                JSONArray dates = json_calendar.names();
                DateFormat df = SimpleDateFormat.getDateInstance();

                for(int i = 0; i < dates.length(); i++)
                {
                    String event_date = dates.getString(i);
                    try{
                        Date date = df.parse(event_date);
                        events.add(date);
                    }
                    catch (ParseException e)
                    {
                        Log.e("Calendar Fragment", "ParseException" + e);
                    }
                }
            } catch (JSONException e) {
                Log.e("Calendar Fragment", "JSONException " + e);
            }
        }

        CalendarView calendar = (CalendarView)view.findViewById(R.id.calendar_view);
        calendar.updateCalendar(events);

        // assign event handler
        calendar.setEventHandler(new CalendarView.EventHandler()
        {
            @Override
            public void onDayPress(Date date)
            {
                // show returned day
                DateFormat df = SimpleDateFormat.getDateInstance();
                String date_string = df.format(date);
                updateList(date_string);
                calendar_item_adapter.notifyDataSetChanged(); //Causes the array of events to update its display
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Do something when a list item is clicked
        ((MainActivity)getActivity()).openCalendarEvent(items.get(position));
    }

    public void updateList(String date_string)
    {
        selected_date = date_string;
        items.clear();

        String string_calendar = ((MainActivity)getActivity()).sharedPref.getString(getString(R.string.shared_pref_calendar), "");
        if(!string_calendar.equals(""))
        {
            try {
                JSONObject json_calendar = new JSONObject(string_calendar);

                if(json_calendar.has(date_string))
                {
                    JSONArray today_array = json_calendar.getJSONArray(date_string);

                    for(int i = 0; i < today_array.length(); i++)
                    {
                        JSONObject json_event = today_array.getJSONObject(i);
                        CalendarListItem item = new CalendarListItem(date_string,
                                json_event.getString(getString(R.string.calendar_event_starttime)),
                                json_event.getString(getString(R.string.calendar_event_endtime)),
                                json_event.getString(getString(R.string.calendar_event_name)),
                                json_event.getString(getString(R.string.calendar_event_description)),
                                json_event.getString(getString(R.string.calendar_event_location)),
                                json_event.getInt(getString(R.string.calendar_event_colour)));
                        items.add(item);
                    }
                }

            } catch (JSONException e) {
                Log.e("Calendar Fragment", "JSONException " + e);
            }
        }
    }
}
