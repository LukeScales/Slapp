/*
 * Created by Kris Vanhoutte.
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 10/12/16 8:17 PM
 */

package com.getslapp.slapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Activity which contains the functionality behind all of the
 * fragments associated with the initial setup of the app.
 * (CollegeSelectFragment, CourseSelectFragment,
 * YearSelectFragment, SemesterSelectFragment, DCUDownloadFragment)
 *
 * This activity is called from the MainActivity upon initial
 * use of the app or when a used wishes to download a new timetable.
 */
public class SetupActivity extends AppCompatActivity {

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_activity);

        sharedPref = this.getSharedPreferences("AppPrefs", 0);

        changeFragment(CollegeSelectFragment.class);
    }


    @Override
    public void onBackPressed() {
        // Prevents the user from pressing back and exiting the app accidentally
    }

    private void changeFragment(Class fragmentClass)
    {
        Fragment fragment = null;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_content, fragment).commit();
    }

    /**
     * Called when "next" is pressed in CollegeSelectFragment
     *
     * Note: When adding further colleges, will need to add further
     *       conditions to the if(college.equals...) statement
     */
    public void selectCollegeDone(View view)
    {
        EditText editText = (EditText)findViewById(R.id.college_email_text);
        if(editText.getText().length() != 0)
        {
            String college_email = String.valueOf(editText.getText());
            if(college_email.contains("@"))
            {
                String email_parts[] = college_email.split("@");
                String domain = email_parts[1];

                if(domain.contains("."))
                {
                    String domain_parts[] = domain.split("\\.");
                    String college = domain_parts[0];
                    if(college.equals("dcu") || college.equals("DCU"))
                    {
                        //Store the college and email in the shared preferences
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.shared_pref_college), college.toUpperCase()); // toUpperCase for consistency and usage in 'if' statements elsewhere
                        editor.putString(getString(R.string.shared_pref_college_email), college_email);
                        editor.commit();

                        changeFragment(CourseSelectFragment.class);
                    }
                    else {
                        Toast.makeText(this, "Unfortunately your college is not supported yet, we hope to add it soon.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Please enter your college email", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called when "back" is pressed in CourseSelectFragment
     */
    public void backToCollegeFrag(View view)
    {
        changeFragment(CollegeSelectFragment.class);
    }

    /**
     * Called when "next" is pressed in CourseSelectFragment
     */
    public void selectCourseDone(View view)
    {
        EditText editText = (EditText)findViewById(R.id.edit_course_text);
        if(editText.getText().length() != 0)
        {
            String course = String.valueOf(editText.getText());
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.shared_pref_course), course);
            editor.commit();

            changeFragment(YearSelectFragment.class);
        }
        else
        {
            Toast.makeText(this, "Please enter a course code", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called when "back" is pressed in YearSelectFragment
     */
    public void backToCourseFrag(View view)
    {
        changeFragment(CourseSelectFragment.class);
    }

    /**
     * Called when "next" is pressed in YearSelectFragment
     *
     * Note: There are no checks here about the range of possible
     *       years which users can input, although they will need
     *       to be valid in later functions.
     */
    public void selectYearDone(View view)
    {
        EditText editText = (EditText)findViewById(R.id.edit_year_text);
        if(editText.getText().length() != 0)
        {
            String year = String.valueOf(editText.getText());
            final SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.shared_pref_year), year);
            editor.commit();

            changeFragment(SemesterSelectFragment.class);
        }
        else
        {
            Toast.makeText(this, "Please enter a year of study", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called when "back" is pressed in SemesterSelectFragment
     */
    public void backToYearFrag(View view) { changeFragment(YearSelectFragment.class); }

    /**
     * Called when "next" is pressed in SemesterSelectFragment
     */
    public void selectSemesterDone(View view, int semester)
    {
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.shared_pref_semester), semester);
        editor.commit();

        // Displays DCUDownloadFragment which is a loading screen
        changeFragment(DCUDownloadFragment.class);

        // Create a thread to download & save the timetable
        Thread timetable_thread = new Thread()
        {
            public void run()
            {
                final DCUDownloader downloader = new DCUDownloader();
                String timetable_link = downloader.getTimetableLink(sharedPref.getString(getString(R.string.shared_pref_course), ""), sharedPref.getString(getString(R.string.shared_pref_year), ""), sharedPref.getInt(getString(R.string.shared_pref_semester), 1));

                String input_stream = downloader.downloadTimetable(timetable_link);
                String timetable = downloader.extractTimetable(input_stream);
                if(timetable != null)
                {
                    editor.putString(getString(R.string.shared_pref_timetable_link), timetable_link);
                    editor.putString(getString(R.string.shared_pref_timetable), timetable);
                    editor.putBoolean(getString(R.string.shared_pref_timetable_set), true);
                    editor.commit();
                }
            }
        };
        timetable_thread.start();

        try {
            timetable_thread.join();
        }
        catch(InterruptedException e){
            Log.e("Thread", "InterruptedException " + e);
        }

        String timetable = sharedPref.getString(getString(R.string.shared_pref_timetable), "");

        if(timetable != "")
        {
            Toast.makeText(this, "Timetable downloaded.", Toast.LENGTH_SHORT).show();

            Intent nav_drawer_intent = new Intent(this, MainActivity.class);
            startActivity(nav_drawer_intent);
        }
        else
        {
            Toast.makeText(this, "Error in downloading timetable.", Toast.LENGTH_LONG).show();
        }
    }
}
