/*
 * Created by Kris Vanhoutte.
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 10/12/16 8:17 PM
 */

package com.getslapp.slapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Fragment for displaying the Timetable.
 *
 * Display layout for this fragment is described within its XML file
 * (fragment_timetable_fragment.xml).
 *
 * The timetable fragment contains 7 lists, one for each day of the
 * week which are dynamically set from the timetable shared preferences.
 *
 * To allow the use of multiple listviews within a scroll view, the
 * following workaround was used: http://stackoverflow.com/questions/
 * 3495890/how-can-i-put-a-listview-into-a-scrollview-without-it-collapsing
 *
 * The format of the timetable shared preferences is:
 *
 * {
 *     "Monday":[
 *          {
 *              "starttime" : "time1",
 *              "endtime" : "time2",
 *              "module_name" : "name1",
 *              "module_code" : "code1",
 *              "location" : "location1",
 *              "lecturer" : "lecturer1",
 *              "weeks" : "week1 - week6",
 *              "type" : "type1",
 *              "colour" : "colour1"
 *          },
 *          {
 *              ...
 *          }
 *     ],
 *     "Tuesday":[
 *          ...
 *     ],
 *     ...
 * }
 */
public class TimetableFragment extends Fragment {

    ArrayList<TimetableListItem> monday_items;
    ArrayList<TimetableListItem> tuesday_items;
    ArrayList<TimetableListItem> wednesday_items;
    ArrayList<TimetableListItem> thursday_items;
    ArrayList<TimetableListItem> friday_items;
    ArrayList<TimetableListItem> saturday_items;
    ArrayList<TimetableListItem> sunday_items;

    public TimetableFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timetable_fragment, container, false);

        monday_items = new ArrayList<>();
        tuesday_items = new ArrayList<>();
        wednesday_items = new ArrayList<>();
        thursday_items = new ArrayList<>();
        friday_items = new ArrayList<>();
        saturday_items = new ArrayList<>();
        sunday_items = new ArrayList<>();

        initialiseItems(); //Fills the ArrayLists

        final ListView monday_list = (ListView)view.findViewById(R.id.monday_list);
        monday_list.setAdapter(new TimetableItemAdapter(getActivity(), monday_items));
        monday_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               ((MainActivity)getActivity()).openTimetableEvent(monday_items.get(position));
           }
        });

        ListView tuesday_list = (ListView) view.findViewById(R.id.tuesday_list);
        tuesday_list.setAdapter(new TimetableItemAdapter(getActivity(), tuesday_items));
        tuesday_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity)getActivity()).openTimetableEvent(tuesday_items.get(position));
            }
        });

        final ListView wednesday_list = (ListView)view.findViewById(R.id.wednesday_list);
        wednesday_list.setAdapter(new TimetableItemAdapter(getActivity(), wednesday_items));
        wednesday_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity)getActivity()).openTimetableEvent(wednesday_items.get(position));
            }
        });

        final ListView thursday_list = (ListView)view.findViewById(R.id.thursday_list);
        thursday_list.setAdapter(new TimetableItemAdapter(getActivity(), thursday_items));
        thursday_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity)getActivity()).openTimetableEvent(thursday_items.get(position));
            }
        });

        ListView friday_list = (ListView)view.findViewById(R.id.friday_list);
        friday_list.setAdapter(new TimetableItemAdapter(getActivity(), friday_items));
        friday_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity)getActivity()).openTimetableEvent(friday_items.get(position));
            }
        });

        ListView saturday_list = (ListView)view.findViewById(R.id.saturday_list);
        saturday_list.setAdapter(new TimetableItemAdapter(getActivity(), saturday_items));
        saturday_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity)getActivity()).openTimetableEvent(saturday_items.get(position));
            }
        });

        ListView sunday_list = (ListView)view.findViewById(R.id.sunday_list);
        sunday_list.setAdapter(new TimetableItemAdapter(getActivity(), sunday_items));
        sunday_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity)getActivity()).openTimetableEvent(sunday_items.get(position));
            }
        });

        setListViewHeightBasedOnChildren(monday_list);
        setListViewHeightBasedOnChildren(tuesday_list);
        setListViewHeightBasedOnChildren(wednesday_list);
        setListViewHeightBasedOnChildren(thursday_list);
        setListViewHeightBasedOnChildren(friday_list);
        setListViewHeightBasedOnChildren(saturday_list);
        setListViewHeightBasedOnChildren(sunday_list);

        return view;
    }

    public void initialiseItems()
    {
        monday_items.clear();
        tuesday_items.clear();
        wednesday_items.clear();
        thursday_items.clear();
        friday_items.clear();
        saturday_items.clear();
        sunday_items.clear();

        String string_timetable = ((MainActivity)getActivity()).sharedPref.getString(getString(R.string.shared_pref_timetable), "");
        Log.i("Timetable", string_timetable);
        if(!string_timetable.equals(""))
        {
            try{
                JSONObject json_timetable = new JSONObject(string_timetable);
                if(json_timetable.has(getString(R.string.timetable_event_monday)))
                {
                    JSONArray monday_array = json_timetable.getJSONArray(getString(R.string.timetable_event_monday));
                    addItem(getString(R.string.timetable_event_monday), monday_array, monday_items);
                }
                if(json_timetable.has(getString(R.string.timetable_event_tuesday)))
                {
                    JSONArray tuesday_array = json_timetable.getJSONArray(getString(R.string.timetable_event_tuesday));
                    addItem(getString(R.string.timetable_event_tuesday), tuesday_array, tuesday_items);
                }
                if(json_timetable.has(getString(R.string.timetable_event_wednesday)))
                {
                    JSONArray wednesday_array = json_timetable.getJSONArray(getString(R.string.timetable_event_wednesday));
                    addItem(getString(R.string.timetable_event_wednesday), wednesday_array, wednesday_items);
                }
                if(json_timetable.has(getString(R.string.timetable_event_thursday)))
                {
                    JSONArray thursday_array = json_timetable.getJSONArray(getString(R.string.timetable_event_thursday));
                    addItem(getString(R.string.timetable_event_thursday), thursday_array, thursday_items);
                }
                if(json_timetable.has(getString(R.string.timetable_event_friday)))
                {
                    JSONArray friday_array = json_timetable.getJSONArray(getString(R.string.timetable_event_friday));
                    addItem(getString(R.string.timetable_event_friday), friday_array, friday_items);
                }
                if(json_timetable.has(getString(R.string.timetable_event_saturday)))
                {
                    JSONArray saturday_array = json_timetable.getJSONArray(getString(R.string.timetable_event_saturday));
                    addItem(getString(R.string.timetable_event_saturday), saturday_array, saturday_items);
                }
                if(json_timetable.has(getString(R.string.timetable_event_sunday)))
                {
                    JSONArray sunday_array = json_timetable.getJSONArray(getString(R.string.timetable_event_sunday));
                    addItem(getString(R.string.timetable_event_sunday), sunday_array, sunday_items);
                }
            }
            catch (JSONException e)
            {
                Log.e("Timetable Fragment", "A - JSONException" + e);
            }
        }
    }

    public void addItem(String day, JSONArray array, ArrayList<TimetableListItem> array_list)
    {
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject json_module = array.getJSONObject(i);
                TimetableListItem item = new TimetableListItem(day,
                        json_module.getString(getString(R.string.timetable_event_starttime)),
                        json_module.getString(getString(R.string.timetable_event_endtime)),
                        json_module.getString(getString(R.string.timetable_event_module_name)),
                        json_module.getString(getString(R.string.timetable_event_module_code)),
                        json_module.getString(getString(R.string.timetable_event_location)),
                        json_module.getString(getString(R.string.timetable_event_lecturer)),
                        json_module.getString(getString(R.string.timetable_event_weeks)),
                        json_module.getString(getString(R.string.timetable_event_type)),
                        json_module.getInt(getString(R.string.timetable_event_colour)));
                array_list.add(item);
            }
        }
        catch (JSONException e)
        {
            Log.e("Timetable Fragment", "B - JSONException" + e);
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
