/*
 * Created by Kris Vanhoutte.
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 10/12/16 8:17 PM
 */

package com.getslapp.slapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Fragment for displaying the Quick Links page.
 *
 * Display layout for this fragment is described within its XML file
 * (fragment_links_fragment.xml).
 *
 * A list is dynamically generated depending on the college that the
 * user is attending. The text and urls used are stored in strings.xml.
 *
 * Note: New strings will need to be created and further else if
 *       statements (or a case statement) will be required when the
 *       app is expanding to further institutes.
 */
public class LinksFragment extends ListFragment {

    ArrayList<LinksListItem> items;

    public LinksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View links_view = inflater.inflate(R.layout.fragment_links_fragment, container, false);
        items = new ArrayList<LinksListItem>();

        String[] texts;
        String[] urls;

        if(((MainActivity)getActivity()).college.equals("DCU"))
        {
            texts = getResources().getStringArray(R.array.dcu_link_texts);
            urls = getResources().getStringArray(R.array.dcu_link_urls);
            urls[urls.length - 1] = ((MainActivity)getActivity()).sharedPref.getString(getString(R.string.shared_pref_timetable_link), urls[0]);
        }
        else if (((MainActivity)getActivity()).college.equals("TCD"))
        {
            texts = getResources().getStringArray(R.array.tcd_link_texts);
            urls = getResources().getStringArray(R.array.tcd_link_urls);
        }
        else
        {
            texts = null;
            urls = null;
        }

        for(int i = 0; i < texts.length; i++)
        {
            LinksListItem item = new LinksListItem(texts[i], urls[i]);
            items.add(item);
        }

        setListAdapter(new LinksItemAdapter(getActivity(), items));

        return links_view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(items.get(position).getUrl()));
        startActivity(intent);
    }
}
