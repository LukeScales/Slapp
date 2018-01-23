/*
 * Created by Kris Vanhoutte.
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 10/12/16 8:17 PM
 */

package com.getslapp.slapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * MainActivity
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public String college;
    public SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = this.getSharedPreferences("AppPrefs", 0);
        Log.i("MainActivity", "OnCreate");
        Boolean timetable_set = sharedPref.getBoolean(getString(R.string.shared_pref_timetable_set), false);

        if(!timetable_set) //Switches to the SetupActivity
        {
            Log.i("MainActivity", "Timetable not set");
            Intent setup_intent = new Intent(getApplicationContext(), SetupActivity.class);
            startActivity(setup_intent);
        }
        else
        {
            setContentView(R.layout.activity_nav_drawer);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            new downloadPromotionsAndEvents().execute(); // Runs an Asynctask to download the latest promotions and events

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            Fragment fragment = null;
            Class fragmentClass;
            SharedPreferences.Editor editor = sharedPref.edit();
            int app_opens = sharedPref.getInt(getString(R.string.shared_pref_app_opens), 0);

            // Every 15 times the app is opened, go straight to the promotions page instead of
            // the timetable.
            if(app_opens < 15)
            {
                fragmentClass = TimetableFragment.class;
                editor.putInt(getString(R.string.shared_pref_app_opens), app_opens + 1);
                editor.commit();
                setTitle("Timetable");
            }
            else
            {
                fragmentClass = PromotionsFragment.class;
                editor.putInt(getString(R.string.shared_pref_app_opens), 0);
                editor.commit();
                setTitle("Promotions");
            }

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

            String defaultCollege = getString(R.string.college);
            college = sharedPref.getString(getString(R.string.shared_pref_college), defaultCollege);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //Do nothing
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = TimetableFragment.class;
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_timetable) {
            fragmentClass = TimetableFragment.class;
        } else if (id == R.id.nav_calendar) {
            fragmentClass = CalendarFragment.class;
        } else if (id == R.id.nav_links) {
            fragmentClass = LinksFragment.class;
        } else if (id == R.id.nav_events) {
            fragmentClass = EventsFragment.class;
        } else if (id == R.id.nav_promotions) {
            fragmentClass = PromotionsFragment.class;
        } else if (id == R.id.nav_settings) {
            fragmentClass = SettingsFragment.class;
        } else if (id == R.id.nav_about) {
            fragmentClass = AboutFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        item.setChecked(true);
        setTitle(item.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * On Click Functions for the About Fragment
     */
    public void promotionAlert(View view)
    {
        DialogFragment newFragment = AlertFragment.newInstance(getString(R.string.promotion_title), getString(R.string.promotion_message), getString(R.string.copy_email), getString(R.string.email_us));
        newFragment.show(getSupportFragmentManager(), "dialog");
    }
    public void eventAlert(View view)
    {
        DialogFragment newFragment = AlertFragment.newInstance(getString(R.string.events_title), getString(R.string.events_message), getString(R.string.copy_email), getString(R.string.email_us));
        newFragment.show(getSupportFragmentManager(), "dialog");
    }
    public void bugAlert(View view)
    {
        DialogFragment newFragment = AlertFragment.newInstance(getString(R.string.bug_title), getString(R.string.bug_message), getString(R.string.copy_email), getString(R.string.email_us));
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    public void doButtonClick(String choice, String title)
    {
        String subject, text;
        String[] email = {getString(R.string.email)};

        switch(choice)
        {
            case "Copy Email":
                ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setPrimaryClip(ClipData.newPlainText("Email", getString(R.string.email)));
                Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                break;
            case "Email us":
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
                switch(title)
                {
                    case "How to Promote":
                        subject = getString(R.string.promotion_email_subject);
                        text = getString(R.string.promotion_email_text);
                        break;
                    case "Add an Event":
                        subject = getString(R.string.events_email_subject);
                        text = getString(R.string.events_email_text);
                        break;
                    case "Report a Bug":
                        subject = getString(R.string.bug_email_subject);
                        text = getString(R.string.bug_email_text);
                        break;
                    default:
                        subject = getString(R.string.default_email_subject);
                        text = getString(R.string.default_email_text);
                        break;
                }
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                emailIntent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(emailIntent);
                break;
        }
    }

    //OnClick functions from the AboutFragment
    public void goToLinkedIn(View view)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://ie.linkedin.com/in/kris-vanhoutte-761432a9"));
        startActivity(intent);
    }
    public void goToTwitter(View view)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/_Kris_VH"));
        startActivity(intent);
    }
    public void goToIcons8(View view)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://icons8.com/"));
        startActivity(intent);
    }
    public void goToPromotion(View view)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharedPref.getString(getString(R.string.shared_pref_promo_url), "https://getslapp.com/")));
        startActivity(intent);
    }

    // Create a new Calendar Event
    public void openCalendarEvent(String date)
    {
        Fragment fragment = CalendarEventFragment.newInstance(date);
        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    // Edit an existing Calendar Event
    public void openCalendarEvent(CalendarListItem item)
    {
        Fragment fragment = CalendarEventFragment.newInstance(item);
        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    // Return to the CalendarFragment from the CalendarEventFragment
    public void backToCalendar()
    {
        Fragment fragment = new CalendarFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    // Edit an existing Timetable Event
    public void openTimetableEvent(TimetableListItem item)
    {
        Fragment fragment = TimetableEventFragment.newInstance(item);
        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    // Return to the TimetableFragment from the TimetableEventFragment
    public void backToTimetable()
    {
        Fragment fragment = new TimetableFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, fragment).commit();
    }


    public void refreshTimetableDialog(View view)
    {
        new AlertDialog.Builder(this)
                .setTitle("Refresh Timetable")
                .setMessage("Are you sure you want to refresh your timetable? You will lose any changes which you have made to the current one.")
                .setCancelable(true)
                .setPositiveButton("I'm sure.",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                refreshTimetable();
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }
                )
                .create()
                .show();
    }

    public void refreshTimetable()
    {
        Fragment fragment = new DCUDownloadFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, fragment).commit();

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
                    SharedPreferences.Editor editor = sharedPref.edit();
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

            Fragment settings_fragment = new SettingsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.flContent, settings_fragment).commit();
        }
        else
        {
            Toast.makeText(this, "Error in downloading timetable.", Toast.LENGTH_LONG).show();
        }
    }

    public void downloadNewTimetable(View view)
    {
        new AlertDialog.Builder(this)
                .setTitle("New Timetable")
                .setMessage("Are you sure you want to download a new timetable? Doing so will delete your current one and any changes which you have made to it.")
                .setCancelable(true)
                .setPositiveButton("I'm sure.",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putBoolean(getString(R.string.shared_pref_timetable_set), false);
                                editor.commit();
                                Intent setup_intent = new Intent(getApplicationContext(), SetupActivity.class);
                                startActivity(setup_intent);
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }
                )
                .create()
                .show();
    }

    private class downloadPromotionsAndEvents extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            try{
                if(isPromoUpdateNeeded())
                {
                    getPromotion();
                }

                String[] event_ids = loadArray(getString(R.string.shared_pref_events_id_array));
                if(isEventsUpdateNeeded(event_ids))
                {
                    getEvents();
                }

                return "All Done!";
            }
            catch(Exception e)
            {
                Log.e( "Promo", "Exception " + e);
                return null;
            }
        }

        private Boolean isPromoUpdateNeeded()
        {
            String url = getString(R.string.promotion_update_needed_url);
            url += "?promotionId=" + sharedPref.getString(getString(R.string.shared_pref_promo_id), "");

            try{
                String input_string = getStringFromInputStream(fetchStream(url));
                Log.i("input_string", input_string);
                try{
                    JSONObject reader = new JSONObject(input_string);
                    if(reader.getString("updateNeeded") == "true")
                        return true;
                    else
                        return false;
                }
                catch (JSONException e)
                {
                    Log.e( "Promo", "JSONException " + e);
                    return false;
                }
            }
            catch (IOException e)
            {
                Log.e("Promo", "IOException " + e);
                return false;
            }
        }

        private void getPromotion()
        {
            String url = getString(R.string.get_promotion_url);
            try{
                String input_string = getStringFromInputStream(fetchStream(url));
                Log.i("input_string 2", input_string);
                try{
                    JSONObject reader = new JSONObject(input_string);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.shared_pref_promo_id), reader.getString("id"));
                    editor.putString(getString(R.string.shared_pref_promo_url), reader.getString("url"));
                    editor.putString(getString(R.string.shared_pref_promo_image_link), getString(R.string.promotion_image_url) + reader.getString("imageName"));
                    editor.commit();
                    return;
                }
                catch (JSONException e)
                {
                    Log.e( "Promo", "JSONException " + e);
                    return;
                }
            }
            catch (IOException e)
            {
                Log.e("Promo", "IOException " + e);
                return;
            }
        }

        private InputStream fetchStream(String url_string)
        {
            try
            {
                URL url = new URL(url_string);
                HttpURLConnection c = ( HttpURLConnection ) url.openConnection();
                c.setDoInput( true );
                c.connect();
                InputStream input_stream = c.getInputStream();
                return input_stream;
            }
            catch ( MalformedURLException e )
            {
                Log.d( "Promo - fetchStream", "Passed invalid URL: " + url_string);
            }
            catch ( IOException e )
            {
                Log.d( "Promo - fetchStream", "IO exception: " + e );
            }
            return null;
        }

        public String getStringFromInputStream(InputStream stream) throws IOException
        {
            int n = 0;
            char[] buffer = new char[1024 * 4];
            InputStreamReader reader = new InputStreamReader(stream);
            StringWriter writer = new StringWriter();
            while (-1 != (n = reader.read(buffer))) writer.write(buffer, 0, n);
            return writer.toString();
        }

        private Boolean isEventsUpdateNeeded(String[] event_ids)
        {
            String url = getString(R.string.events_update_needed_url);

            for(int i = 0; i < event_ids.length; i++)
            {
                if(i == 0)
                    url += "?id";
                else
                    url += "&id";

                url += Integer.toString(i) + "=" + event_ids[i];
            }

            try{
                String input_string = getStringFromInputStream(fetchStream(url));
                try{
                    JSONObject reader = new JSONObject(input_string);
                    Log.i("input_string 1", input_string);
                    if(reader.getString("updateNeeded") == "true")
                        return true;
                    else
                        return false;
                }
                catch (JSONException e)
                {
                    Log.e( "Events", "JSONException " + e);
                    return false;
                }
            }
            catch (IOException e)
            {
                Log.e("Events", "IOException " + e);
                return false;
            }
        }

        private void getEvents()
        {
            String url = getString(R.string.get_events_url);
            try{
                String input_string = getStringFromInputStream(fetchStream(url));
                Log.i("input_string 2", input_string);
                try{
                    JSONArray json_array = new JSONArray(input_string);

                    String[] event_ids = new String[json_array.length()];
                    String[] event_urls = new String[json_array.length()];
                    String[] event_short_desc = new String[json_array.length()];
                    String[] event_long_desc = new String[json_array.length()];
                    String[] event_address = new String[json_array.length()];
                    String[] event_time = new String[json_array.length()];
                    String[] event_requester = new String[json_array.length()];
                    String[] event_contact_info = new String[json_array.length()];

                    for(int i = 0; i < json_array.length(); i++)
                    {
                        JSONObject json_object = json_array.getJSONObject(i);

                        // TODO: Add a catch to prevent adding events that don't have the required parameters
                        event_ids[i] = json_object.getString("id");
                        event_urls[i] = json_object.getString("url");
                        event_short_desc[i] = json_object.getString("shortDescription");
                        event_long_desc[i] = json_object.getString("longDescription");
                        event_address[i] = json_object.getString("address");
                        event_time[i] = json_object.getString("time");
                        event_requester[i] = json_object.getString("requester");
                        event_contact_info[i] = json_object.getString("contactInfo");
                    }

                    saveArray(event_ids, getString(R.string.shared_pref_events_id_array));
                    saveArray(event_urls, getString(R.string.shared_pref_events_url_array));
                    saveArray(event_short_desc, getString(R.string.shared_pref_events_short_desc_array));
                    saveArray(event_long_desc, getString(R.string.shared_pref_events_long_desc_array));
                    saveArray(event_address, getString(R.string.shared_pref_events_address_array));
                    saveArray(event_time, getString(R.string.shared_pref_events_time_array));
                    saveArray(event_requester, getString(R.string.shared_pref_events_requester_array));
                    saveArray(event_contact_info, getString(R.string.shared_pref_events_contact_info_array));

                    return;
                }
                catch (JSONException e)
                {
                    Log.e( "events", "JSONException " + e);
                    return;
                }
            }
            catch (IOException e)
            {
                Log.e("Events", "IOException " + e);
                return;
            }
        }

        public boolean saveArray(String[] array, String arrayName)
        {
            Log.i("SaveArray", arrayName);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(arrayName +"_size", array.length);
            for(int i=0;i<array.length;i++)
                editor.putString(arrayName + "_" + i, array[i]);
            return editor.commit();
        }

        public String[] loadArray(String arrayName)
        {
            Log.i("LoadArray", arrayName);
            int size = sharedPref.getInt(arrayName + "_size", 0);
            String array[] = new String[size];
            for(int i=0;i<size;i++)
                array[i] = sharedPref.getString(arrayName + "_" + i, null);
            return array;
        }
    }
}
