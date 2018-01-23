/*
 * Created by Kris Vanhoutte.
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 10/12/16 8:17 PM
 */

package com.getslapp.slapp;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Controls the dynamic displaying of items in the event
 * list in the EventsFragment
 */
public class EventItemAdapter extends ArrayAdapter<EventListItem> {

    public EventItemAdapter(Context c, List<EventListItem> items) {
        super(c, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EventItemView itemView = (EventItemView)convertView;
        if (null == itemView)
            itemView = EventItemView.inflate(parent);
        itemView.setItem(getItem(position));
        return itemView;
    }

}