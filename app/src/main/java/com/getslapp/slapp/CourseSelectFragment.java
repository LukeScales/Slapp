/*
 * Created by Kris Vanhoutte.
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 10/12/16 8:17 PM
 */

package com.getslapp.slapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Fragment for displaying the Course Select page.
 *
 * Display layout for this fragment is described within its XML file
 * (fragment_course_select_fragment.xml).
 *
 * Functionality behind this fragment is implemented in its XML file
 * and in SetupActivity.
 */
public class CourseSelectFragment extends Fragment {

    public CourseSelectFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course_select_fragment, container, false);

        // Force input in the course edit text to be capitalised
        EditText edittext = (EditText)view.findViewById(R.id.edit_course_text);
        edittext.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        return view;
    }
}
