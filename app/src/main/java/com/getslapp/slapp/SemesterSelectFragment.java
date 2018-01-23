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
import android.widget.Button;

/**
 * Fragment for displaying the Semester Select page.
 *
 * Display layout for this fragment is described within its XML file
 * (fragment_semester_select_fragment.xml).
 *
 * Note: Depending on the university/institution which SLAPP is being
 *       expanded to, it might be necessary to add more than two
 *       buttons/implement an edittext as in the YearSelectFragment.
 */
public class SemesterSelectFragment extends Fragment {

    public SemesterSelectFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_semester_select_fragment, container, false);

        Button semester_one_button = (Button)view.findViewById(R.id.semester_1_button);
        semester_one_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SetupActivity)getActivity()).selectSemesterDone(v, 1);
            }
        });

        Button semester_two_button = (Button)view.findViewById(R.id.semester_2_button);
        semester_two_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SetupActivity)getActivity()).selectSemesterDone(v, 2);
            }
        });

        return view;
    }
}
