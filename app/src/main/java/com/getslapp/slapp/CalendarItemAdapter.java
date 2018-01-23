/*
 * Created by Kris Vanhoutte.
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 10/12/16 8:17 PM
 */

package com.getslapp.slapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Controls the dynamic displaying of items in the event
 * list in the CalendarFragment
 */
public class CalendarItemAdapter extends ArrayAdapter<CalendarListItem>
{
    public CalendarItemAdapter(Context c, List<CalendarListItem> items) {
        super(c, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CalendarItemView itemView = (CalendarItemView) convertView;
        if (null == itemView)
            itemView = CalendarItemView.inflate(parent);
        itemView.setItem(getItem(position));
        return itemView;
    }
}