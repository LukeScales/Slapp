/*
 * Created by Kris Vanhoutte.
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 10/12/16 8:17 PM
 */

package com.getslapp.slapp;

/**
 * Class for storing data about the items displayed
 * in the calendar event list in the CalendarFragment
 */
public class CalendarListItem {
    private String calendar_date;
    private String calendar_starttime;
    private String calendar_endtime;
    private String calendar_name;
    private String calendar_description;
    private String calendar_location;
    private int calendar_colour;

    public CalendarListItem(String date, String starttime, String endtime, String name, String description, String location, int colour)
    {
        super();
        calendar_date = date;
        calendar_starttime = starttime;
        calendar_endtime = endtime;
        calendar_name = name;
        calendar_description = description;
        calendar_location = location;
        calendar_colour = colour;
    }

    public String getDate() { return calendar_date; }
    public void setDate(String date) { calendar_date = date; }

    public String getTime() { return calendar_starttime + " - " + calendar_endtime; }
    public String getStartTime() { return calendar_starttime; }
    public String getEndTime() { return calendar_endtime; }
    public void setTime(String starttime, String endtime) {calendar_starttime = starttime; calendar_endtime = endtime;}

    public String getName() { return calendar_name; }
    public void setName(String name) {calendar_name = name;}

    public String getDescription() { return calendar_description; }
    public void setDescription(String description) {calendar_description = description;}

    public String getLocation() { return calendar_location; }
    public void setLocation(String location) {calendar_location = location;}

    public int getColour() { return calendar_colour; }
    public void setColour(int colour) {calendar_colour = colour;}
}