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
 * Controls the dynamic displaying of items in the timetable
 * list in the TimetableFragment
 */
public class TimetableItemAdapter extends ArrayAdapter<TimetableListItem> {

    public TimetableItemAdapter(Context c, List<TimetableListItem> items) {
        super(c, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TimetableItemView itemView = (TimetableItemView) convertView;
        if (null == itemView)
            itemView = TimetableItemView.inflate(parent);
        itemView.setItem(getItem(position));
        return itemView;
    }
}