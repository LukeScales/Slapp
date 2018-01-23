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
 * Fragment for editing a timetable event.
 *
 * Display layout for this fragment is described within its XML file
 * (fragment_timetable_event_fragment.xml).
 *
 * This fragment uses the VintageChroma colour picker, which can
 * be found here: https://android-arsenal.com/details/1/3352
 */
public class TimetableEventFragment extends Fragment {
    private static final String ARG_DAY = "day";
    private static final String ARG_START_TIME = "starttime";
    private static final String ARG_END_TIME = "endtime";
    private static final String ARG_MODULE_CODE = "code";
    private static final String ARG_MODULE_NAME = "name";
    private static final String ARG_TYPE = "type";
    private static final String ARG_LECTURER = "lecturer";
    private static final String ARG_LOCATION = "location";
    private static final String ARG_WEEKS = "weeks";
    private static final String ARG_COLOUR = "colour";

    private String event_day = null;
    private String start_time = null;
    private String previous_start_time = null;
    private String end_time = null;
    private String module_code = null;
    private String previous_module_code = null;
    private String module_name = null;
    private String type = null;
    private String lecturer = null;
    private String location = null;
    private String weeks = null;
    private int colour = Color.GREEN;

    public TimetableEventFragment() {
        // Required empty public constructor
    }

    public static TimetableEventFragment newInstance(TimetableListItem item) {
        TimetableEventFragment fragment = new TimetableEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DAY, item.getDay());
        args.putString(ARG_START_TIME, item.getStartTime());
        args.putString(ARG_END_TIME, item.getEndTime());
        args.putString(ARG_MODULE_CODE, item.getModuleCode());
        args.putString(ARG_MODULE_NAME, item.getModuleName());
        args.putString(ARG_TYPE, item.getType());
        args.putString(ARG_LECTURER, item.getLecturer());
        args.putString(ARG_LOCATION, item.getLocation());
        args.putString(ARG_WEEKS, item.getWeeks());
        args.putInt(ARG_COLOUR, item.getColour());

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            event_day = getArguments().getString(ARG_DAY);
            start_time = getArguments().getString(ARG_START_TIME);
            previous_start_time = start_time;
            end_time = getArguments().getString(ARG_END_TIME);
            module_code = getArguments().getString(ARG_MODULE_CODE);
            previous_module_code = module_code;
            module_name = getArguments().getString(ARG_MODULE_NAME);
            type = getArguments().getString(ARG_TYPE);
            lecturer = getArguments().getString(ARG_LECTURER);
            location = getArguments().getString(ARG_LOCATION);
            weeks = getArguments().getString(ARG_WEEKS);
            colour = getArguments().getInt(ARG_COLOUR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timetable_event_fragment, container, false);

        if(module_code != null)
        {
            ((TextView)view.findViewById(R.id.timetable_event_day_textpicker)).setText(event_day);
            ((EditText)view.findViewById(R.id.timetable_event_module_code_edittext)).setText(module_code);
            ((EditText)view.findViewById(R.id.timetable_event_module_name_edittext)).setText(module_name);
            ((EditText)view.findViewById(R.id.timetable_event_weeks_edittext)).setText(weeks);

            Spinner start_spinner = ((Spinner)view.findViewById(R.id.timetable_event_starttime_spinner));
            start_spinner.setSelection(((ArrayAdapter)start_spinner.getAdapter()).getPosition(start_time));
            Spinner end_spinner = ((Spinner)view.findViewById(R.id.timetable_event_endtime_spinner));
            end_spinner.setSelection(((ArrayAdapter)end_spinner.getAdapter()).getPosition(end_time));

            view.findViewById(R.id.timetable_event_colour_button).setBackgroundColor(colour);
            ((EditText)view.findViewById(R.id.timetable_event_type_edittext)).setText(type);
            ((EditText)view.findViewById(R.id.timetable_event_lecturer_edittext)).setText(lecturer);
            ((EditText)view.findViewById(R.id.timetable_event_location_edittext)).setText(location);
        }

        ImageButton colour_button = (ImageButton)view.findViewById(R.id.timetable_event_colour_button);
        colour_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new ChromaDialog.Builder()
                        .initialColor(getResources().getColor(R.color.colorPrimary))
                        .colorMode(ColorMode.ARGB) // RGB, ARGB, HVS, CMYK, CMYK255, HSL
                        .indicatorMode(IndicatorMode.HEX) //HEX or DECIMAL; Note that (HSV || HSL || CMYK) && IndicatorMode.HEX is a bad idea
                        .onColorSelected(new OnColorSelectedListener() {
                            @Override public void onColorSelected(int newColor) {
                                v.setBackgroundColor(newColor);
                                updateColour(newColor);
                            }
                        })
                        .create()
                        .show(((MainActivity)getActivity()).getSupportFragmentManager(), "ChromaDialog");
            }
        });

        ImageButton cancel_button = (ImageButton)view.findViewById(R.id.timetable_event_cancel_button);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ((MainActivity)getActivity()).backToTimetable();
            }
        });

        ImageButton delete_button = (ImageButton)view.findViewById(R.id.timetable_event_delete_button);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.i("Timetable Fragment", "Delete Button");
                new AlertDialog.Builder(getActivity())
                        .setTitle("Delete")
                        .setMessage("Are you sure you wish to delete this event?")
                        .setCancelable(true)
                        .setPositiveButton("Delete",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        deleteEvent();
                                        ((MainActivity)getActivity()).backToTimetable();
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

        ImageButton save_button = (ImageButton)view.findViewById(R.id.timetable_event_save_button);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.i("Timetable Fragment", "Save Button");

                new AlertDialog.Builder(getActivity())
                        .setTitle("Save")
                        .setMessage("Do you want to save this event?")
                        .setCancelable(true)
                        .setPositiveButton("Save",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        if(saveEvent())
                                            ((MainActivity)getActivity()).backToTimetable();
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
        EditText module_code_edittext = (EditText)(((MainActivity)getActivity()).findViewById(R.id.timetable_event_module_code_edittext));
        module_code = module_code_edittext.getText().toString();
        Spinner starttime_spinner = (Spinner)(((MainActivity)getActivity()).findViewById(R.id.timetable_event_starttime_spinner));
        start_time = starttime_spinner.getSelectedItem().toString();

        String string_timetable = ((MainActivity)getActivity()).sharedPref.getString(getString(R.string.shared_pref_timetable), "");
        if(!string_timetable.equals(""))
        {
            try {
                JSONObject json_timetable = new JSONObject(string_timetable);

                if(json_timetable.has(event_day))
                {
                    JSONArray day_array = json_timetable.getJSONArray(event_day);

                    for(int i = 0; i < day_array.length(); i++)
                    {
                        JSONObject json_event = day_array.getJSONObject(i);
                        String json_code = json_event.getString(getString(R.string.timetable_event_module_code));
                        String json_starttime = json_event.getString(getString(R.string.timetable_event_starttime));

                        if((json_code.equals(previous_module_code)) && (json_starttime.equals(previous_start_time)))
                        {
                            day_array.remove(i);
                            if(day_array.length() != 0)
                            {
                                json_timetable.remove(event_day);
                                json_timetable.put(event_day, day_array);
                            }
                            SharedPreferences.Editor editor = ((MainActivity)getActivity()).sharedPref.edit();
                            if(json_timetable.length() == 0)
                                editor.putString(getString(R.string.shared_pref_timetable), "");
                            else
                                editor.putString(getString(R.string.shared_pref_timetable), json_timetable.toString());
                            editor.commit();
                        }
                    }
                }
            } catch (JSONException e) {
                Log.e("Timetable Fragment", "JSONException " + e);
            }
        }
    }

    public Boolean saveEvent() {
        EditText module_code_edittext = (EditText) (((MainActivity) getActivity()).findViewById(R.id.timetable_event_module_code_edittext));
        module_code = module_code_edittext.getText().toString();
        EditText module_name_edittext = (EditText) (((MainActivity) getActivity()).findViewById(R.id.timetable_event_module_name_edittext));
        module_name = module_name_edittext.getText().toString();
        Spinner starttime_spinner = (Spinner) (((MainActivity) getActivity()).findViewById(R.id.timetable_event_starttime_spinner));
        start_time = starttime_spinner.getSelectedItem().toString();
        Spinner endtime_spinner = (Spinner) (((MainActivity) getActivity()).findViewById(R.id.timetable_event_endtime_spinner));
        end_time = endtime_spinner.getSelectedItem().toString();
        EditText type_edittext = (EditText) (((MainActivity) getActivity()).findViewById(R.id.timetable_event_type_edittext));
        type = type_edittext.getText().toString();
        EditText lecturer_edittext = (EditText) (((MainActivity) getActivity()).findViewById(R.id.timetable_event_lecturer_edittext));
        lecturer = lecturer_edittext.getText().toString();
        EditText location_edittext = (EditText) (((MainActivity) getActivity()).findViewById(R.id.timetable_event_location_edittext));
        location = location_edittext.getText().toString();
        EditText weeks_edittext = (EditText) (((MainActivity) getActivity()).findViewById(R.id.timetable_event_weeks_edittext));
        weeks = weeks_edittext.getText().toString();

        if(module_code.length() > 0 && module_name.length() > 0 && location.length() > 0 && (start_time.compareTo(end_time) <= 0))
        {
            JSONObject json_timetable = null;
            JSONObject json_new_event = new JSONObject();
            try {
                json_new_event.put(getString(R.string.timetable_event_module_code), module_code);
                json_new_event.put(getString(R.string.timetable_event_module_name), module_name);
                json_new_event.put(getString(R.string.timetable_event_starttime), start_time);
                json_new_event.put(getString(R.string.timetable_event_endtime), end_time);
                json_new_event.put(getString(R.string.timetable_event_type), type);
                json_new_event.put(getString(R.string.timetable_event_lecturer), lecturer);
                json_new_event.put(getString(R.string.timetable_event_location), location);
                json_new_event.put(getString(R.string.timetable_event_weeks), weeks);
                json_new_event.put(getString(R.string.timetable_event_colour), colour);
            } catch (JSONException e) {
                Log.e("Timetable Fragment", "1 - JSONException " + e);
            }

            String string_timetable = ((MainActivity) getActivity()).sharedPref.getString(getString(R.string.shared_pref_timetable), "");
            if (!string_timetable.equals("")) {
                try {
                    json_timetable = new JSONObject(string_timetable);

                    if (json_timetable.has(event_day)) {
                        JSONArray day_array = json_timetable.getJSONArray(event_day);
                        JSONArray new_day_array = new JSONArray();
                        Boolean event_exists = false;

                        for (int i = 0; i < day_array.length(); i++) {
                            JSONObject json_event = day_array.getJSONObject(i);
                            String json_name = json_event.getString(getString(R.string.timetable_event_module_code));
                            String json_starttime = json_event.getString(getString(R.string.timetable_event_starttime));

                            if ((json_name.equals(previous_module_code)) && (json_starttime.equals(previous_start_time)))
                            {
                                event_exists = true;
                                new_day_array.put(json_new_event);
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
                        if (!event_exists) {
                            day_array.put(json_new_event);
                        }

                        json_timetable.remove(event_day);
                        json_timetable.put(event_day, day_array);
                    } else {
                        JSONArray day_array = new JSONArray();
                        day_array.put(json_new_event);
                        json_timetable.remove(event_day);
                        json_timetable.put(event_day, day_array);
                    }
                } catch (JSONException e) {
                    Log.e("Timetable Fragment", "2 - JSONException " + e);
                }
            } else {
                json_timetable = new JSONObject();
                JSONArray day_array = new JSONArray();
                day_array.put(json_new_event);
                try {
                    json_timetable.put(event_day, day_array);
                } catch (JSONException e) {
                    Log.e("Timetable Fragment", "3 - JSONException " + e);
                }
            }

            SharedPreferences.Editor editor = ((MainActivity) getActivity()).sharedPref.edit();
            editor.putString(getString(R.string.shared_pref_timetable), json_timetable.toString());
            editor.commit();
            return true;
        }
        else
        {
            if(module_code.length() == 0)
            {
                Toast.makeText(getContext(), "Module code required", Toast.LENGTH_SHORT).show();
            }
            else if(module_name.length() == 0)
            {
                Toast.makeText(getContext(), "Module name required", Toast.LENGTH_SHORT).show();
            }
            else if(location.length() == 0)
            {
                Toast.makeText(getContext(), "Location required", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getContext(), "End Time cannot be before Start Time", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }
}
