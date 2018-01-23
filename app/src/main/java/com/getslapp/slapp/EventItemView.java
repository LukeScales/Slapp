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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Custom view for displaying items in the events list in the
 * EventsFragment.
 *
 * Display layout for this view is described within the XML
 * files event_item_view.xml and event_item_view_children.xml
 *
 * TODO: Redo Class and XML files.
 * Note: This class and it's associated XML files need to be
 *       changed to reflect how events should correctly be
 *       displayed. This could not be done in the original
 *       version as no events were available for testing.
 *       Some work may be required if downloading and storing
 *       of an image is required.
 */

public class EventItemView extends RelativeLayout {
    private TextView mTitleTextView;
    private TextView mDescriptionTextView;
    private ImageView mImageView;

    public static EventItemView inflate(ViewGroup parent) {
        EventItemView itemView = (EventItemView)LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item_view, parent, false);
        return itemView;
    }

    public EventItemView(Context c) {
        this(c, null);
    }

    public EventItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EventItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.event_item_view_children, this, true);
        setupChildren();
    }

    private void setupChildren() {
        mTitleTextView = (TextView) findViewById(R.id.item_titleTextView);
        mDescriptionTextView = (TextView) findViewById(R.id.item_descriptionTextView);
        mImageView = (ImageView) findViewById(R.id.item_imageView);
    }

    public void setItem(EventListItem item) {
        mTitleTextView.setText(item.getShortDesc());
        mDescriptionTextView.setText(item.getLongDesc());
        // TODO: set up image URL
    }

    public ImageView getImageView () {
        return mImageView;
    }

    public TextView getTitleTextView() {
        return mTitleTextView;
    }

    public TextView getDescriptionTextView() {
        return mDescriptionTextView;
    }
}