/*
 * Created by Kris Vanhoutte.
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 10/12/16 8:17 PM
 */

package com.getslapp.slapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment for displaying the DCU Download page. This is the page
 * which displays while the timetable is being fetched from the DCU
 * website.
 *
 * Display layout for this fragment is described within its XML file
 * (fragment_download_dcu_fragment.xml).
 *
 * Functionality behind this fragment is implemented in its XML file
 * and in SetupActivity.
 */
public class DCUDownloadFragment extends Fragment {

    public DCUDownloadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_download_dcu_fragment, container, false);
    }
}
