/*
 * Created by Kris Vanhoutte.
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 10/12/16 8:17 PM
 */

package com.getslapp.slapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Fragment for displaying alert dialogs for emailing SLAPP.
 *
 * These alerts are displayed in three cases, for promotions,
 * events and bug reports.
 */
public class AlertFragment extends DialogFragment {

    // The fragment initialization parameters
    private static final String ARG_TITLE = "title";
    private static final String ARG_MESSAGE = "message";
    private static final String ARG_POS_CHOICE = "posChoice";
    private static final String ARG_NEG_CHOICE = "negChoice";

    public AlertFragment() {
        // Required empty public constructor
    }

    public static AlertFragment newInstance(String alertTitle, String alertMessage, String positiveChoice, String negativeChoice) {
        AlertFragment fragment = new AlertFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, alertTitle);
        args.putString(ARG_MESSAGE, alertMessage);
        args.putString(ARG_POS_CHOICE, positiveChoice);
        args.putString(ARG_NEG_CHOICE, negativeChoice);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String title = getArguments().getString(ARG_TITLE);
        final String message = getArguments().getString(ARG_MESSAGE);
        final String posChoice = getArguments().getString(ARG_POS_CHOICE);
        final String negChoice = getArguments().getString(ARG_NEG_CHOICE);

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton(posChoice,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((MainActivity)getActivity()).doButtonClick(posChoice, title);
                            }
                        }
                )
                .setNegativeButton(negChoice,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((MainActivity)getActivity()).doButtonClick(negChoice, title);
                            }
                        }
                )
                .create();
    }
}
