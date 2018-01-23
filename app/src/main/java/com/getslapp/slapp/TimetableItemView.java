/*
 * Created by Kris Vanhoutte.
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 10/12/16 8:17 PM
 */

package com.getslapp.slapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Custom view for displaying items in the timetable events
 * list in the TimetableFragment.
 *
 * Display layout for this view is described within the XML
 * files timetable_item_view.xml and timetable_item_view_children.xml
 */
public class TimetableItemView extends RelativeLayout {
    private View timetable_colour_view;
    private TextView timetable_type_textview;
    private TextView timetable_time_textview;
    private TextView timetable_location_textview;
    private TextView timetable_module_textview;

    public static TimetableItemView inflate(ViewGroup parent) {
        TimetableItemView itemView = (TimetableItemView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timetable_item_view, parent, false);
        return itemView;
    }

    public TimetableItemView(Context c) {
        this(c, null);
    }

    public TimetableItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimetableItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.timetable_item_view_children, this, true);
        setupChildren();
    }

    private void setupChildren() {
        timetable_colour_view = findViewById(R.id.timetable_colour_view);
        timetable_type_textview = (TextView)findViewById(R.id.timetable_type_textview);
        timetable_time_textview = (TextView)findViewById(R.id.timetable_time_textview);
        timetable_location_textview = (TextView)findViewById(R.id.timetable_location_textview);
        timetable_module_textview = (TextView)findViewById(R.id.timetable_module_textview);
    }

    public void setItem(TimetableListItem item) {
        timetable_colour_view.setBackgroundColor(item.getColour());
        timetable_type_textview.setText(item.getType());
        timetable_time_textview.setText(item.getTime());
        timetable_location_textview.setText(item.getLocation());
        timetable_module_textview.setText(item.getModule());
    }

    public View getColourView() { return timetable_colour_view; }
    public TextView getTypeTextView() { return timetable_type_textview; }
    public TextView getTimeTextView() { return timetable_time_textview; }
    public TextView getLocationTextView() { return timetable_location_textview; }
    public TextView getModuleTextView() { return timetable_module_textview; }
}