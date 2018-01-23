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
 * Controls the dynamic displaying of items in the links
 * list in the LinksFragment
 */
public class LinksItemAdapter extends ArrayAdapter<LinksListItem> {

    public LinksItemAdapter(Context c, List<LinksListItem> items) {
        super(c, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinksItemView itemView = (LinksItemView)convertView;
        if (null == itemView)
            itemView = LinksItemView.inflate(parent);
        itemView.setItem(getItem(position));
        return itemView;
    }
}
