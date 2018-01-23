/*
 * Created by Kris Vanhoutte.
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 10/12/16 8:17 PM
 */

package com.getslapp.slapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Fragment for displaying the Events page.
 *
 * Display layout for this fragment is described within its XML file
 * (fragment_events_fragment.xml).
 *
 * This fragment uses a listview similar to that in the CalendarFragment
 * which is dynamically populated with the list of events.
 */
public class EventsFragment extends ListFragment {


    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events_fragment, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            setEvents();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return view;
    }

    public void setEvents()throws IOException
    {
        String[] event_ids = loadArray(getString(R.string.shared_pref_events_id_array));
        String[] event_urls = loadArray(getString(R.string.shared_pref_events_url_array));
        String[] event_short_desc = loadArray(getString(R.string.shared_pref_events_short_desc_array));
        String[] event_long_desc = loadArray(getString(R.string.shared_pref_events_long_desc_array));
        String[] event_address = loadArray(getString(R.string.shared_pref_events_address_array));
        String[] event_time = loadArray(getString(R.string.shared_pref_events_time_array));
        String[] event_requester = loadArray(getString(R.string.shared_pref_events_requester_array));
        String[] event_contact_info = loadArray(getString(R.string.shared_pref_events_contact_info_array));

        ArrayList<EventListItem> items = new ArrayList<EventListItem>();

        for(int i = 0; i < event_ids.length; i++)
        {
            EventListItem item = new EventListItem(event_ids[i], event_urls[i], event_short_desc[i], event_long_desc[i], event_address[i], event_time[i], event_requester[i], event_contact_info[i]);
            items.add(item);
        }

        setListAdapter(new EventItemAdapter(getActivity(), items));
    }

    public String[] loadArray(String arrayName)
    {
        Log.i("LoadArray", arrayName);
        SharedPreferences prefs = ((MainActivity)getActivity()).sharedPref;
        int size = prefs.getInt(arrayName + "_size", 0);
        String array[] = new String[size];
        for(int i=0;i<size;i++)
            array[i] = prefs.getString(arrayName + "_" + i, null);
        return array;
    }
}
