/*
 * Created by Kris Vanhoutte.
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 10/12/16 8:17 PM
 */

package com.getslapp.slapp;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pavelsikun.vintagechroma.ChromaDialog;
import com.pavelsikun.vintagechroma.IndicatorMode;
import com.pavelsikun.vintagechroma.OnColorSelectedListener;
import com.pavelsikun.vintagechroma.colormode.ColorMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Fragment for editing or creating a new calendar event.
 *
 * Display layout for this fragment is described within its XML file
 * (fragment_calendar_event_fragment.xml).
 *
 * If an event is being edited, the current details for the event
 * will be displayed. Otherwise the details will be blank.
 *
 * This fragment uses the VintageChroma colour picker, which can
 * be found here: https://android-arsenal.com/details/1/3352
 *
 * Note: Currently the date isn't editable from within this
 *       fragment, this was the intended funtionality, but
 *       can be changed for ease of use.
 */
public class CalendarEventFragment extends Fragment {
    private static final String ARG_DATE = "date";
    private static final String ARG_START_TIME = "starttime";
    private static final String ARG_END_TIME = "endtime";
    private static final String ARG_NAME = "name";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_LOCATION = "location";
    private static final String ARG_COLOUR = "colour";

    private String event_date = null;
    private String start_time = null;
    private String original_start_time = null; //Original name and start time are tracked in case of saving/deletion after editing.
    private String end_time = null;
    private String name = null;
    private String original_name = null;
    private String description = null;
    private String location = null;
    private int colour = Color.GREEN;

    public CalendarEventFragment() {
        // Required empty public constructor
    }

    public static CalendarEventFragment newInstance(String selected_date) {
        CalendarEventFragment fragment = new CalendarEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DATE, selected_date);

        fragment.setArguments(args);
        return fragment;
    }

    public static CalendarEventFragment newInstance(CalendarListItem item) {
        CalendarEventFragment fragment = new CalendarEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DATE, item.getDate());
        args.putString(ARG_START_TIME, item.getStartTime());
        args.putString(ARG_END_TIME, item.getEndTime());
        args.putString(ARG_NAME, item.getName());
        args.putString(ARG_DESCRIPTION, item.getDescription());
        args.putString(ARG_LOCATION, item.getLocation());
        args.putInt(ARG_COLOUR, item.getColour());

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            event_date = getArguments().getString(ARG_DATE); //event_date will always be present as an argument passed in. See note in starting comments.
            if(getArguments().size() > 1)
            {
                start_time = getArguments().getString(ARG_START_TIME);
                original_start_time = start_time;
                end_time = getArguments().getString(ARG_END_TIME);
                name = getArguments().getString(ARG_NAME);
                original_name = name;
                description = getArguments().getString(ARG_DESCRIPTION);
                location = getArguments().getString(ARG_LOCATION);
                colour = getArguments().getInt(ARG_COLOUR);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar_event_fragment, container, false);

        if(event_date!= null)
            ((TextView)view.findViewById(R.id.calendar_event_date_textpicker)).setText(event_date);

        if(name != null)
        {
            ((EditText)view.findViewById(R.id.calendar_event_name_edittext)).setText(name);

            Spinner start_spinner = ((Spinner)view.findViewById(R.id.calendar_event_starttime_spinner));
            start_spinner.setSelection(((ArrayAdapter)start_spinner.getAdapter()).getPosition(start_time));
            Spinner end_spinner = ((Spinner)view.findViewById(R.id.calendar_event_endtime_spinner));
            end_spinner.setSelection(((ArrayAdapter)end_spinner.getAdapter()).getPosition(end_time));

            view.findViewById(R.id.calendar_event_colour_button).setBackgroundColor(colour);
            ((EditText)view.findViewById(R.id.calendar_event_description_edittext)).setText(description);
            ((EditText)view.findViewById(R.id.calendar_event_location_edittext)).setText(location);
        }

        ImageButton colour_button = (ImageButton)view.findViewById(R.id.calendar_event_colour_button);
        colour_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new ChromaDialog.Builder()
                        .initialColor(Color.parseColor("#48e651"))
                        .colorMode(ColorMode.ARGB) // RGB, ARGB, HVS, CMYK, CMYK255, HSL
                        .indicatorMode(IndicatorMode.HEX) //HEX or DECIMAL; Note that (HSV || HSL || CMYK) && IndicatorMode.HEX is a bad idea
                        .onColorSelected(new OnColorSelectedListener() {
                            @Override public void onColorSelected(int newColor) {
                                v.setBackgroundColor(newColor);
                                updateColour(newColor);
                            }
                        })
                        .create()
                        .show((getActivity()).getSupportFragmentManager(), "ChromaDialog");
            }
        });

        ImageButton cancel_button = (ImageButton)view.findViewById(R.id.calendar_event_cancel_button);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ((MainActivity)getActivity()).backToCalendar();
            }
        });

        ImageButton delete_button = (ImageButton)view.findViewById(R.id.calendar_event_delete_button);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.i("Calendar Fragment", "Delete Button");
                new AlertDialog.Builder(getActivity())
                        .setTitle("Delete")
                        .setMessage("Are you sure you wish to delete this event?")
                        .setCancelable(true)
                        .setPositiveButton("Delete",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        deleteEvent();
                                        ((MainActivity)getActivity()).backToCalendar();
                                    }
                                }
                        )
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                    }
                                }
                        )
                        .create()
                        .show();
            }
        });

        ImageButton save_button = (ImageButton)view.findViewById(R.id.calendar_event_save_button);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.i("Calendar Fragment", "Save Button");

                new AlertDialog.Builder(getActivity())
                        .setTitle("Save")
                        .setMessage("Do you want to save this event?")
                        .setCancelable(true)
                        .setPositiveButton("Save",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        if(saveEvent())
                                            ((MainActivity)getActivity()).backToCalendar();
                                    }
                                }
                        )
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                    }
                                }
                        )
                        .create()
                        .show();
            }
        });

        return view;
    }

    public void updateColour(int color)
    {
        colour = color;
    }

    public void deleteEvent()
    {
        EditText name_edittext = (EditText)(((MainActivity)getActivity()).findViewById(R.id.calendar_event_name_edittext));
        name = name_edittext.getText().toString();
        Spinner starttime_spinner = (Spinner)(((MainActivity)getActivity()).findViewById(R.id.calendar_event_starttime_spinner));
        start_time = starttime_spinner.getSelectedItem().toString();

        String string_calendar = ((MainActivity)getActivity()).sharedPref.getString(getString(R.string.shared_pref_calendar), "");
        if(!string_calendar.equals(""))
        {
            try {
                JSONObject json_calendar = new JSONObject(string_calendar);

                if(json_calendar.has(event_date))
                {
                    JSONArray day_array = json_calendar.getJSONArray(event_date);

                    for(int i = 0; i < day_array.length(); i++)
                    {
                        JSONObject json_event = day_array.getJSONObject(i);
                        String json_name = json_event.getString(getString(R.string.calendar_event_name));
                        String json_starttime = json_event.getString(getString(R.string.calendar_event_starttime));

                        if((json_name.equals(original_name)) && (json_starttime.equals(original_start_time)))
                        {
                            day_array.remove(i);
                            if(day_array.length() == 0)
                            {
                                json_calendar.remove(event_date);
                            }
                            else
                            {
                                json_calendar.remove(event_date);
                                json_calendar.put(event_date, day_array);
                            }
                            SharedPreferences.Editor editor = ((MainActivity)getActivity()).sharedPref.edit();
                            if(json_calendar.length() == 0)
                                editor.putString(getString(R.string.shared_pref_calendar), "");
                            else
                                editor.putString(getString(R.string.shared_pref_calendar), json_calendar.toString());
                            editor.commit();
                        }
                    }
                }
            } catch (JSONException e) {
                Log.e("Calendar Fragment", "JSONException " + e);
            }
        }
    }

    public Boolean saveEvent() {
        EditText name_edittext = (EditText) (((MainActivity) getActivity()).findViewById(R.id.calendar_event_name_edittext));
        name = name_edittext.getText().toString();
        Spinner starttime_spinner = (Spinner) (((MainActivity) getActivity()).findViewById(R.id.calendar_event_starttime_spinner));
        start_time = starttime_spinner.getSelectedItem().toString();
        Spinner endtime_spinner = (Spinner) (((MainActivity) getActivity()).findViewById(R.id.calendar_event_endtime_spinner));
        end_time = endtime_spinner.getSelectedItem().toString();
        EditText description_edittext = (EditText) (((MainActivity) getActivity()).findViewById(R.id.calendar_event_description_edittext));
        description = description_edittext.getText().toString();
        EditText location_edittext = (EditText) (((MainActivity) getActivity()).findViewById(R.id.calendar_event_location_edittext));
        location = location_edittext.getText().toString();

        if(name.length() > 0 && (start_time.compareTo(end_time) <= 0))
        {
            JSONObject json_calendar = null;
            JSONObject json_new_event = new JSONObject();
            try {
                json_new_event.put(getString(R.string.calendar_event_name), name);
                json_new_event.put(getString(R.string.calendar_event_starttime), start_time);
                json_new_event.put(getString(R.string.calendar_event_endtime), end_time);
                json_new_event.put(getString(R.string.calendar_event_description), description);
                json_new_event.put(getString(R.string.calendar_event_location), location);
                json_new_event.put(getString(R.string.calendar_event_colour), colour);
            } catch (JSONException e) {
                Log.e("Calendar Fragment", "JSONException " + e);
            }

            String string_calendar = ((MainActivity) getActivity()).sharedPref.getString(getString(R.string.shared_pref_calendar), "");
            if (!string_calendar.equals("")) {
                try {
                    json_calendar = new JSONObject(string_calendar);

                    if (json_calendar.has(event_date)) {
                        JSONArray day_array = json_calendar.getJSONArray(event_date);
                        JSONArray new_day_array = new JSONArray();
                        Boolean event_exists = false;

                        for (int i = 0; i < day_array.length(); i++) {
                            JSONObject json_event = day_array.getJSONObject(i);
                            String json_name = json_event.getString(getString(R.string.calendar_event_name));
                            String json_starttime = json_event.getString(getString(R.string.calendar_event_starttime));

                            //Compare to the original name and start time, which overrides the current event if it exists
                            if ((json_name.equals(original_name)) && (json_starttime.equals(original_start_time))) {
                                event_exists = true;

                                new_day_array.put(json_new_event);
                                //day_array.put(i, json_new_event);
                            }
                            else if((json_starttime.compareTo(start_time) > 0) && !event_exists)
                            {
                                event_exists = true;
                                new_day_array.put(json_new_event);
                                new_day_array.put(json_event);
                            }
                            else
                            {
                                new_day_array.put(json_event);
                            }
                        }
                        if(!event_exists)
                        {
                            new_day_array.put(json_new_event);
                        }

                        json_calendar.remove(event_date);
                        json_calendar.put(event_date, new_day_array);
                    } else {
                        JSONArray day_array = new JSONArray();
                        day_array.put(json_new_event);
                        json_calendar.remove(event_date);
                        json_calendar.put(event_date, day_array);
                    }
                } catch (JSONException e) {
                    Log.e("Calendar Fragment", "JSONException " + e);
                }
            } else {
                json_calendar = new JSONObject();
                JSONArray day_array = new JSONArray();
                day_array.put(json_new_event);
                try {
                    json_calendar.put(event_date, day_array);
                } catch (JSONException e) {
                    Log.e("Calendar Fragment", "JSONException " + e);
                }
            }

            SharedPreferences.Editor editor = ((MainActivity) getActivity()).sharedPref.edit();
            editor.putString(getString(R.string.shared_pref_calendar), json_calendar.toString());
            editor.commit();
            return true;
        }
        else
        {
            if(name.length() == 0)
            {
                Toast.makeText(getContext(), "Event Name required", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getContext(), "End Time cannot be before Start Time", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }
}
