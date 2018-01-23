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
 * Custom view for displaying items in the calendar events
 * list in the CalendarFragment.
 *
 * Display layout for this view is described within the XML
 * files calendar_item_view.xml and calendar_item_view_children.xml
 */
public class CalendarItemView extends RelativeLayout {
    private View calendar_colour_view;
    private TextView calendar_time_textview;
    private TextView calendar_name_textview;

    public static CalendarItemView inflate(ViewGroup parent) {
        CalendarItemView itemView = (CalendarItemView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_item_view, parent, false);
        return itemView;
    }

    public CalendarItemView(Context c) {
        this(c, null);
    }

    public CalendarItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.calendar_item_view_children, this, true);
        setupChildren();
    }

    private void setupChildren() {
        calendar_colour_view = findViewById(R.id.calendar_colour_view);
        calendar_time_textview = (TextView)findViewById(R.id.calendar_time_textview);
        calendar_name_textview = (TextView)findViewById(R.id.calendar_name_textview);
    }

    public void setItem(CalendarListItem item) {
        calendar_colour_view.setBackgroundColor(item.getColour());
        calendar_time_textview.setText(item.getTime());
        calendar_name_textview.setText(item.getName());
    }

    public View getColourView() { return calendar_colour_view; }
    public TextView getTimeTextView() { return calendar_time_textview; }
    public TextView getNameTextView() { return calendar_name_textview; }
}