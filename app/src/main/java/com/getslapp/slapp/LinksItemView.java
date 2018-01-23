/*
 * Created by Kris Vanhoutte.
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 10/12/16 8:17 PM
 */

package com.getslapp.slapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Custom view for displaying items in the links list in the LinksFragment.
 *
 * Display layout for this view is described within the XML files
 * links_item_view.xml and links_item_view_children.xml
 */
public class LinksItemView extends RelativeLayout {
    private Button link_button;
    private String link_url;

    public static LinksItemView inflate(ViewGroup parent) {
        LinksItemView itemView = (LinksItemView)LayoutInflater.from(parent.getContext())
                .inflate(R.layout.links_item_view, parent, false);
        return itemView;
    }

    public LinksItemView(Context c) {
        this(c, null);
    }

    public LinksItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinksItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.links_item_view_children, this, true);
        setupChildren();
    }

    private void setupChildren() {
        link_button = (Button) findViewById(R.id.links_button);
        link_button.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link_url));
                getContext().startActivity(intent);
            }
        });
    }

    public void setItem(LinksListItem item) {
        link_button.setText(item.getText());
        link_url = item.getUrl();
    }

    public Button getLinkButton() {
        return link_button;
    }
}